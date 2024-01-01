package kukekyakya.kukemarket.config.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//인증은 되었더라도 사용자가 요청에 대한 접근 권하이 없을때에 작동하는 핸들러
//컨트롤러 계층 도달전에 수행하기 때문에 exceptionAdvice 는 잡아낼수 없다.
//우리는 response 클래스로 일관되게 작성하고 있으니깐 리다이렉트를 해준다.
//그리고 거기서 이에 대한 예외를 발생시켜서 exceptionAdvice 에 대한 예외를 다루도록 한다.
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendRedirect("/exception/access-denied");
    }
}
