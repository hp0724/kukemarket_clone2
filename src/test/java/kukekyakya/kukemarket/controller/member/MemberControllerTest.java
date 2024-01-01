package kukekyakya.kukemarket.controller.member;

import kukekyakya.kukemarket.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {
    @InjectMocks MemberController memberController;
    @Mock
    MemberService memberService;
    MockMvc mockMvc;

    //memberController 테스트 하기 위해
    //standaloneSetup controller를 테스트 하는 환경 제공
    @BeforeEach
    void beforeEach (){
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();

    }
    @Test
    void readTest() throws Exception {
        Long id = 1L ;

        mockMvc.perform(
                        get("/api/members/{id}",id))
                .andExpect(status().isOk());
        verify(memberService).read(id);
    }

    @Test
    void deleteTest() throws Exception {
        Long id = 1L ;

        mockMvc.perform(
                        delete("/api/members/{id}",id))
                .andExpect(status().isOk());
        verify(memberService).delete(id);

    }
}