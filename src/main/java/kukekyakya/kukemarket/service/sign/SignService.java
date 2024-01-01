package kukekyakya.kukemarket.service.sign;

import kukekyakya.kukemarket.config.token.TokenHelper;
import kukekyakya.kukemarket.dto.sign.RefreshTokenResponse;
import kukekyakya.kukemarket.dto.sign.SignInResponse;
import kukekyakya.kukemarket.dto.sign.SignUpRequest;
import kukekyakya.kukemarket.dto.sign.SignInRequest;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.exception.*;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
// 하나의 메소드를 하나의 트랜잭션으로 묶어주기위해 선언
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenHelper accessTokenHelper;
    private final TokenHelper refreshTokenHelper;

    @Transactional
    public void signUp(SignUpRequest req){
        validateSignUpInfo(req);
        //entity 변환과정에서 권한 설정과 비밀번호 암호화 등록
        memberRepository.save(SignUpRequest.toEntity(req,
                roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                passwordEncoder));
    }
    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest req){
        Member member=memberRepository.findByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        validatePassword(req,member);
        String subject = createSubject(member);
        String accessToken = accessTokenHelper.createToken(subject);
        String refreshToken = refreshTokenHelper.createToken(subject);
        return new SignInResponse(accessToken,refreshToken);
    }

    //refresh token 에서 subject 추출해서 새로운 엑세스 토큰 발급
    public RefreshTokenResponse refreshToken(String rToken){
        validateRefreshToken(rToken);
        String subject = refreshTokenHelper.extractSubject(rToken);
        String accessToken = accessTokenHelper.createToken(subject);
        return new RefreshTokenResponse(accessToken);
    }
    private void validateRefreshToken(String rToken){
        //refresh token 유효하지 않은 경우 예외 처리
        if(!refreshTokenHelper.validate(rToken)){
            throw new AuthenticationEntryPointException();
        }
    }

    //이메일과 닉네임의 중복섬 검사
    private void validateSignUpInfo(SignUpRequest req){
        if(memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        if(memberRepository.existsByNickname(req.getNickname()))
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
    }
    private void validatePassword(SignInRequest req,Member member){
        if(!passwordEncoder.matches(req.getPassword(),member.getPassword())){
            throw new LoginFailureException();
        }
    }

    //jwt 에 들어갈 subject memberId로 생성
    private String createSubject(Member member){
        return String.valueOf(member.getId());
    }

}
