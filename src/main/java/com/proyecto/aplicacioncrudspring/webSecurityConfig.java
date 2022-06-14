package com.proyecto.aplicacioncrudspring;

import com.proyecto.aplicacioncrudspring.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class webSecurityConfig extends WebSecurityConfigurerAdapter {

    String[] resources = new String[]{
            "/include/**","/css/**","/icons/**","/img/**","/js/**","/layer/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
        .antMatchers(resources).permitAll()
        .antMatchers("/","/index").permitAll()
                //Request a otros aparte de estos necesita authenticacion
                .anyRequest().authenticated()
                .and()
        .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/userform")
                .failureUrl("/login/?error=true")
                .usernameParameter("username") //estos valores pertenecen al input del formulario
                .passwordParameter("password")
                .and()
                .csrf().disable()
        .logout()
                .permitAll()
                .logoutSuccessUrl("/login?logout");


    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(4);
    }

    @Autowired
    UserDetailsService userDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //customizamos el inicio de sesion
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());

    }




}
