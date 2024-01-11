package kukekyakya.kukemarket.config.token;

import io.jsonwebtoken.Claims;
import kukekyakya.kukemarket.handler.JwtHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//@Bean 통해 직접적으로 spring context 에 등록하기에
//component scan이 되지 않도록 @Component 같은 annoation을 붙이지 않음
@RequiredArgsConstructor
public class TokenHelper {
    private final JwtHandler jwtHandler;
    private final String key;
    private final long maxAgeSeconds;

    private static final String SEP=",";
    private static final String ROLE_TYPES = "ROLE_TYPES";
    private static final String MEMBER_ID ="MEMBER_ID";


    public String createToken(PrivateClaims privateClaims){
        return jwtHandler.createToken(
                key,
                Map.of(MEMBER_ID,privateClaims.getMemberId(),ROLE_TYPES,privateClaims.getRoleTypes().stream().collect(Collectors.joining(SEP))),
                maxAgeSeconds
        );
    }

    public Optional<PrivateClaims> parse (String token){
        return jwtHandler.parse(key,token).map(this::convert);
    }
    private PrivateClaims convert(Claims claims) {
        return new PrivateClaims(
                claims.get(MEMBER_ID, String.class),
                Arrays.asList(claims.get(ROLE_TYPES, String.class).split(SEP))
        );
    }

    @Getter
    @AllArgsConstructor
    public static class PrivateClaims { // 3
        private String memberId;
        private List<String> roleTypes;
    }
}
