package kukekyakya.kukemarket.controller.post;

import kukekyakya.kukemarket.advice.ExceptionAdvice;
import kukekyakya.kukemarket.controller.post.PostController;
import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.exception.CategoryNotFoundException;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.exception.PostNotFoundException;
import kukekyakya.kukemarket.exception.UnsupportedImageFormatException;
import kukekyakya.kukemarket.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static kukekyakya.kukemarket.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostControllerAdviceTest {
    @InjectMocks
    PostController postController;
    @Mock
    PostService postService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(postController).setControllerAdvice(new ExceptionAdvice()).build();

    }
    @Test
    void createExceptionByMemberNotFoundException()throws Exception{
        given(postService.create(any())).willThrow(MemberNotFoundException.class);

        performCreate()
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1007));

    }

    @Test
    void createExceptionByCategoryNotFoundException() throws Exception {
        given(postService.create(any())).willThrow(CategoryNotFoundException.class);
        performCreate()
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1010));
    }

    @Test
    void createExceptionByUnsupportedImageFormatException() throws Exception{
        // given
        given(postService.create(any())).willThrow(UnsupportedImageFormatException.class);

        // when, then
        performCreate()
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1013));
    }

    @Test
    void readExceptionByPostNotFoundTest() throws Exception {
        given(postService.read(anyLong())).willThrow(PostNotFoundException.class);

        mockMvc.perform(
                        get("/api/posts/{id}",1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1012)
                );
    }

    @Test
    void deleteExceptionByPostNotFoundTest() throws Exception{
        doThrow(PostNotFoundException.class).when(postService).delete(anyLong());

        mockMvc.perform(
                        delete("/api/posts/{id}",1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1012));
    }
//
//    @Test
//    void updateExceptionByPostNotFoundTest() throws Exception{
//        given(postService.update(anyLong(),any())).willThrow(PostNotFoundException.class);
//
//        mockMvc.perform(
//                        multipart("/api/posts/{id}",1L)
//                                .param("title","title")
//                                .param("content","content")
//                                .param("price","1234")
//                                .with(requestPostProcessor->{
//                                    requestPostProcessor.setMethod("PUT");
//                                    return requestPostProcessor;
//                                })
//                                .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(-1012));
//    }
    private ResultActions performCreate()throws Exception{
        PostCreateRequest req = createPostCreateRequest();
        return mockMvc.perform(
                multipart("/api/posts")
                        .param("title",req.getTitle())
                        .param("content",req.getContent())
                        .param("price",String.valueOf(req.getPrice()))
                        .param("categoryId",String.valueOf(req.getCategoryId()))
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );
    }
}