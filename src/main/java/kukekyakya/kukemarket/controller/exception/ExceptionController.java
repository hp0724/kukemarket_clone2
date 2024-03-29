package kukekyakya.kukemarket.controller.exception;

import kukekyakya.kukemarket.exception.AccessDeniedException;
import kukekyakya.kukemarket.exception.AuthenticationEntryPointException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

//api 문서를 만들지 않도록
@ApiIgnore
@RestController
public class ExceptionController {
    @GetMapping("/exception/entry-point")
    public void entryPoint() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/exception/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException();
    }
}