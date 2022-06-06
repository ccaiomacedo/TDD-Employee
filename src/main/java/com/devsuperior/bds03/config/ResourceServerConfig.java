package com.devsuperior.bds03.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private JwtTokenStore tokenStore;

    private static final String[] PUBLIC = {"/oauth/token","/h2-console/**"};

    private static final String[] OPERATOR_OR_ADMIN = {"/departments/**"};

    private static final String[] ADMIN = {"/employees/**"};

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore); //com isso o resourcer server é capaz de decodificar o token e analisar se o token está batendo, verifica se o token está expirado ou n, etc
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //h2
        if (Arrays.asList(env.getActiveProfiles()).contains("test")){ // aqui está liberando acesso aos frames do banco qye tem o profile de test
            http.headers().frameOptions().disable();
        }

        http.authorizeRequests()
                .antMatchers(PUBLIC).permitAll()//permite as autorizações com essas rotas
                .antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")//só quem pode acessar é quem tiver essas roles
                .antMatchers(HttpMethod.POST,ADMIN).hasRole("ADMIN")
                .anyRequest().authenticated();//qualquer outra rota peça autenticação
    }
}
