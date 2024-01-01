package kukekyakya.kukemarket.config.token;

import kukekyakya.kukemarket.handler.JwtHandler;
import lombok.RequiredArgsConstructor;

//@Bean 통해 직접적으로 spring context 에 등록하기에
//component scan이 되지 않도록 @Component 같은 annoation을 붙이지 않음
@RequiredArgsConstructor
public class TokenHelper {
    private final JwtHandler jwtHandler;
    private final String key;
    private final long maxAgeSeconds;

    public String createToken(String subject){
        return jwtHandler.createToken(key,subject,maxAgeSeconds);
    }
    public boolean validate(String token){
        return jwtHandler.validate(key,token);
    }
    public String extractSubject(String token){
        return jwtHandler.extractSubject(key,token);
    }
}
