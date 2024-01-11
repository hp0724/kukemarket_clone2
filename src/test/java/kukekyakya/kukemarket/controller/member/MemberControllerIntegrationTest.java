package kukekyakya.kukemarket.controller.member;

import kukekyakya.kukemarket.dto.sign.SignInRequest;
import kukekyakya.kukemarket.dto.sign.SignInResponse;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.init.TestInitDB;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static kukekyakya.kukemarket.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
class MemberControllerIntegrationTest {
    @Autowired WebApplicationContext context;
    @Autowired MockMvc mockMvc;

    @Autowired TestInitDB initDB;
    @Autowired SignService signService;
    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        initDB.initDB();
    }

    @Test
    void readTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                        get("/api/members/{id}", member.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse signInRes = signService.signIn(createSignInRequest(initDB.getMember1Email(), initDB.getPassword()));

        // when, then
        //로그인하여 발급받은 토큰을 Authorization 헤더에 포함해서 요청
        mockMvc.perform(
                        delete("/api/members/{id}", member.getId()).header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByAdminTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse adminSignInRes = signService.signIn(createSignInRequest(initDB.getAdminEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/members/{id}", member.getId()).header("Authorization", adminSignInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);

        // when, then
        //인증되지 않은 사용자가 요청 전송하는 경우
        //3xx 상태 코드 응답
        mockMvc.perform(
                        delete("/api/members/{id}", member.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse attackerSignInRes = signService.signIn(createSignInRequest(initDB.getMember2Email(), initDB.getPassword()));

        // when, then
        //권한 없는  사용자가 요청 전송하는 경우
        //3xx 상태 코드 응답
        mockMvc.perform(
                        delete("/api/members/{id}", member.getId()).header("Authorization", attackerSignInRes.getAccessToken()))
                .andExpect(status().isInternalServerError());
    }

    //이전에는 refreshtoken을 api요청했을때 customAccessDeniedHandelr 가 작동하는데 정상이였지만
    // 수정된 방식에서는 refreshtoken 을 요청하더라도 인증 할수 있는 사용자로 판단하지 않는다.
    //CustomAuthenticationEntryPointHandler 동작해야함
    @Test
    void deleteUnauthorizedByRefreshTokenTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse signInRes = signService.signIn(createSignInRequest(initDB.getMember1Email(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                        delete("/api/members/{id}", member.getId()).header("Authorization", signInRes.getRefreshToken()))
                .andExpect(status().isUnauthorized());
    }

}