package br.com.bcfinances.shared.infrastructure.caching;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.support.NullValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;

@Configuration
public class RedisCacheConfig {

    private static final String NULL_VALUE_CLASS_ID = "@class";
    private static final byte[] EMPTY_ARRAY = new byte[0];

    private RedisSerializer<Object> jsonSerializer() {
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.setDefaultTyping(new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL, ptv) {
            @Override
            public boolean useForType(JavaType t) {
                return true;
            }
        }.init(JsonTypeInfo.Id.CLASS, null).inclusion(JsonTypeInfo.As.WRAPPER_ARRAY));

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        registerNullValueSerializer(mapper);
        return new ObjectMapperRedisSerializer(mapper);
    }

    // Base configuration to be reused by modules via RedisCacheManagerBuilderCustomizer
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        RedisSerializer<Object> valueSerializer = jsonSerializer();
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(valueSerializer));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        RedisSerializer<Object> valueSerializer = jsonSerializer();

        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();

        return template;
    }

    private static void registerNullValueSerializer(ObjectMapper mapper) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(new NullValueSerializer(NULL_VALUE_CLASS_ID));
        mapper.registerModule(module);
    }

    private record ObjectMapperRedisSerializer(ObjectMapper mapper) implements RedisSerializer<Object> {

        @Override
        public byte[] serialize(Object source) {
            if (source == null) {
                return EMPTY_ARRAY;
            }

            try {
                return mapper.writeValueAsBytes(source);
            } catch (Exception ex) {
                throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
            }
        }

        @Override
        public Object deserialize(byte[] source) {
            if (source == null || source.length == 0) {
                return null;
            }

            try {
                return mapper.readValue(source, Object.class);
            } catch (Exception ex) {
                throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
            }
        }
    }

    private static class NullValueSerializer extends StdSerializer<NullValue> {
        private final String classIdentifier;

        private NullValueSerializer(String classIdentifier) {
            super(NullValue.class);
            this.classIdentifier = classIdentifier;
        }

        @Override
        public void serialize(NullValue value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField(classIdentifier, NullValue.class.getName());
            gen.writeEndObject();
        }

        @Override
        public void serializeWithType(NullValue value, JsonGenerator gen, SerializerProvider provider,
                                      TypeSerializer typeSer) throws IOException {
            serialize(value, gen, provider);
        }
    }
}
