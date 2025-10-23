package br.com.bcfinances.auth.infrastructure.caching;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@Service
public class RedisRsaKeyStore {

    private static final String KEY = "bcf:v2:auth:keys:rsa";

    private final ValueOperations<String, Object> valueOperations;

    public RedisRsaKeyStore(RedisTemplate<String, Object> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    public KeyPair getOrCreateKeyPair() {
        Object cached = valueOperations.get(KEY);
        StoredRsaKey stored = parseStoredKey(cached);
        if (stored != null) {
            try {
                return toKeyPair(stored);
            } catch (Exception ignored) {
                // fallthrough to regenerate keys when deserialization fails
            }
        }

        StoredRsaKey generated = generateAndPersist();
        return toKeyPairUnchecked(generated);
    }

    private StoredRsaKey generateAndPersist() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();

            StoredRsaKey stored = new StoredRsaKey(
                    Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()),
                    Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded())
            );

            valueOperations.set(KEY, stored);
            return stored;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to generate RSA key pair", ex);
        }
    }

    private StoredRsaKey parseStoredKey(Object cached) {
        if (cached instanceof StoredRsaKey stored) {
            return stored;
        }

        if (cached instanceof Map<?, ?> map) {
            Object publicValue = map.get("publicKeyBase64");
            Object privateValue = map.get("privateKeyBase64");
            if (publicValue instanceof String publicKey && privateValue instanceof String privateKey) {
                return new StoredRsaKey(publicKey, privateKey);
            }
        }

        return null;
    }

    private KeyPair toKeyPair(StoredRsaKey stored) throws Exception {
        byte[] publicBytes = Base64.getDecoder().decode(stored.publicKeyBase64());
        byte[] privateBytes = Base64.getDecoder().decode(stored.privateKeyBase64());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));

        return new KeyPair(publicKey, privateKey);
    }

    private KeyPair toKeyPairUnchecked(StoredRsaKey stored) {
        try {
            return toKeyPair(stored);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to recreate RSA key pair from Redis", ex);
        }
    }

    private record StoredRsaKey(String publicKeyBase64, String privateKeyBase64) {
    }
}
