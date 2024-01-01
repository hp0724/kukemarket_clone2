package kukekyakya.kukemarket.config.security;

import kukekyakya.kukemarket.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //토큰을 통해 사용자 인증을 위한 의존성
    private final TokenService tokenService;
    //토큰을 통해 사용자 인증을 위한 의존성 토큰에 저장된 subject (id) 로 사용자의 정보를 조회하는데 사용
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        //security 무시할 url
        web.ignoring().mvcMatchers("/exception/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests() // 4
                        .antMatchers(HttpMethod.POST, "/api/sign-in", "/api/sign-up").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .antMatchers(HttpMethod.DELETE, "/api/members/{id}/**").access("@memberGuard.check(#id)")
                        .anyRequest().hasAnyRole("ADMIN")
                .and()
                    //인증된 사용자가 권한 부족 등의 사유로 인해 접근이 거부되었을 때 작동할 핸들러를 지정해줍니다.
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()) // 5
                .and()
                    // 인증되지 않은 사용자의 접근이 거부되었을 때 작동할 핸들러를 지정해줍니다.
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 6
                .and() // 7
                    //토큰으로 사용자를 인증하기 위해 직접 정의한 JwtAuthenticationFilter를
                    // UsernamePasswordAuthenticationFilter의 이전 위치에 등록해줍니다.
                    // JwtAuthenticationFilter는 필요한 의존성인 TokenService와 CustomUserDetailsService를 주입받습니다.
                    .addFilterBefore(new JwtAuthenticationFilter(tokenService, userDetailsService), UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
