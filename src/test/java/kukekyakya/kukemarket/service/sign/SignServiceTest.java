package kukekyakya.kukemarket.service.sign;

import kukekyakya.kukemarket.config.token.TokenHelper;
import kukekyakya.kukemarket.dto.sign.RefreshTokenResponse;
import kukekyakya.kukemarket.dto.sign.SignInRequest;
import kukekyakya.kukemarket.dto.sign.SignInResponse;
import kukekyakya.kukemarket.dto.sign.SignUpRequest;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.member.Role;
import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.exception.*;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.role.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static kukekyakya.kukemarket.factory.entity.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SignServiceTest {
    SignService signService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenHelper accessTokenHelper;
    @Mock
    TokenHelper refreshTokenHelper;

    //통일한 타입의 @Mock 에 대해서 제대로 인식하지 못하기에 InjectMock 대신 @Mock으로 만들어진
    //의존성을 직접 지정해준다.
    @BeforeEach
    void beforeEach() {
        signService = new SignService(memberRepository, roleRepository, passwordEncoder, accessTokenHelper, refreshTokenHelper);
    }


    @Test
    void signUpTest() {
        // given
        SignUpRequest req = createSignUpRequest();
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.of(new Role(RoleType.ROLE_NORMAL)));

        // when
        signService.signUp(req);

        // then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }
    @Test
    void validateSignUpByDuplicateEmailTest() {
        // given
        given(memberRepository.existsByEmail(anyString())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberEmailAlreadyExistsException.class);
    }

    @Test
    void validateSignUpByDuplicateNicknameTest() {
        // given
        given(memberRepository.existsByNickname(anyString())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberNicknameAlreadyExistsException.class);
    }

    @Test
    void signUpRoleNotFoundTest() {
        // given
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    void signInTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(accessTokenHelper.createToken(any())).willReturn("access");
        given(refreshTokenHelper.createToken(any())).willReturn("refresh");

        // when
        SignInResponse res = signService.signIn(new SignInRequest("email", "password"));

        // then
        assertThat(res.getAccessToken()).isEqualTo("access");
        assertThat(res.getRefreshToken()).isEqualTo("refresh");
    }

    @Test
    void signInExceptionByNoneMemberTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    void signInExceptionByInvalidPasswordTest() {
        // given
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @Test
    void refreshTokenTest() {
        // given
        String refreshToken = "refreshToken";
        String subject = "subject";
        String accessToken = "accessToken";
        given(refreshTokenHelper.parse(refreshToken)).willReturn(Optional.of(new TokenHelper.PrivateClaims("memberId", List.of("ROLE_NORMAL"))));
        given(accessTokenHelper.createToken(any())).willReturn(accessToken);

        // when
        RefreshTokenResponse res = signService.refreshToken(refreshToken);

        // then
        assertThat(res.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    void refreshTokenExceptionByInvalidTokenTest() {
        // given
        String refreshToken = "refreshToken";
        given(refreshTokenHelper.parse(refreshToken)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.refreshToken(refreshToken))
                .isInstanceOf(RefreshTokenFailureException.class);
    }


    private SignUpRequest createSignUpRequest() {
        return new SignUpRequest("email", "password", "username", "nickname");
    }

}
