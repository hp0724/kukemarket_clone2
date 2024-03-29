package kukekyakya.kukemarket.config.security;

import kukekyakya.kukemarket.config.token.TokenHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
//security config 에 직접 순서를 넣어주었기 때문에 자동으로 chain에 등록해주는 component 선언을 안함
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final CustomUserDetailsService userDetailsService;


//    //refresh
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        //토큰값을 꺼내오고
//        String token = extractToken(request);
//
//        if(validateToken(token)) {
//            setAuthentication(token);
//        }
//        chain.doFilter(request, response);
//    }
//
//    private String extractToken(ServletRequest request) {
//        return ((HttpServletRequest)request).getHeader("Authorization");
//    }
//
//    private boolean validateToken(String token) {
//        return token != null && accessTokenHelper.validate(token);
//    }
//
//
//
//    //컨텍스트에 토큰에서 꺼내온 사용자 id를 이용한 사용자 정보를 넣어준다.
//    private void setAuthentication(String token) {
//        //토큰에서 사용자의 id를 추출하고
//        String userId = accessTokenHelper.extractSubject(token);
//        //UserDetailsService.loadUserByUsername을 호출하고 있습니다.
//        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);
//        //토큰 type , 사용자 정보, 권한 넣어줌
//        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
//    }

    @Override
    public void doFilter (ServletRequest request,ServletResponse response , FilterChain chain) throws IOException,ServletException {
        extractToken(request).map(userDetailsService::loadUserByUsername).ifPresent(this::setAuthentication);
        chain.doFilter(request,response);
    }

    private void setAuthentication(CustomUserDetails userDetails){
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(userDetails,userDetails.getAuthorities()));
    }

    private Optional<String> extractToken(ServletRequest request){
        return Optional.ofNullable(((HttpServletRequest) request).getHeader("Authorization"));
    }


}