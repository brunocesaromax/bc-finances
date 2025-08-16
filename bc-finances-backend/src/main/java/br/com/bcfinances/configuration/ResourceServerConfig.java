package br.com.bcfinances.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Profile("oauth-security")
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/categories").permitAll() //Categorias todos podem acessar o restante tem que estar autenticado
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Não manter state de seção
                .and().httpBasic()
                .and().csrf().disable(); // Configuração para evitar problemas como um javascriptInjection na aplicação

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.stateless(true); // Não guardar seção
    }

    @Bean
    public MethodSecurityExpressionHandler createExpressionHandler(){
        return new OAuth2MethodSecurityExpressionHandler();
    }
}
