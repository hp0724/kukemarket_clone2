package kukekyakya.kukemarket.controller.sign;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kukekyakya.kukemarket.dto.response.Response;
import kukekyakya.kukemarket.dto.sign.SignInRequest;
import kukekyakya.kukemarket.dto.sign.SignUpRequest;
import kukekyakya.kukemarket.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static kukekyakya.kukemarket.dto.response.Response.success;

//JSON 으로 응답하기 위해 @RestController 선언
@RestController
@Api(value = "Sign Controller", tags = "Sign") // 1
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @ApiOperation(value = "회원가입", notes = "회원가입을 한다.") // 2
    @PostMapping("/api/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    //@Valid 통해서 request 객체의 필드 값 검증
    //@RequestBody 통해서 JSON 바디를 객체로 변환
    public Response signUp(@Valid @RequestBody SignUpRequest req){
        signService.signUp(req);
        return success();
    }
    @ApiOperation(value = "로그인", notes = "로그인을 한다.") // 2
    @PostMapping("/api/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn (@Valid @RequestBody SignInRequest req){
        //성공시 token 전달
        return success(signService.signIn(req));

    }
    //파라미터에 설정된 @RequestHeader는 required 옵션의 기본 설정 값이 true이기 때문에
    // , 이 헤더 값이 전달되지 않았을 때 예외가 발생하게 됩니다.
    //이 때 발생하는 예외가 MissingRequestHeaderException 입니다.
    @ApiOperation(value = "토큰 재발급", notes = "리프레시 토큰으로 새로운 액세스 토큰을 발급 받는다.") // 2
    @PostMapping("/api/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public Response refreshToken(@ApiIgnore @RequestHeader( value = "Authorization")String refreshToken){
        return success(signService.refreshToken(refreshToken));
    }
}
