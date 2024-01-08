package kukekyakya.kukemarket.controller.post;

import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

import static kukekyakya.kukemarket.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @InjectMocks PostController postController;
    @Mock
    PostService postService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();

    }
    @Test
    void createTest()throws Exception{
        //요청을 통해 컨트롤러에서 @ModelAttribute로 전달받을 PostCreateRequest를 캡쳐할 수 있도록 선언해둡니다.
        ArgumentCaptor<PostCreateRequest> postCreateRequestArgumentCaptor = ArgumentCaptor.forClass(PostCreateRequest.class);
        //multipart()를 이용하여 mutlipart/form-data 요청을 보내기 위한 데이터들을 지정해줍니다.
        List<MultipartFile> imageFiles = List.of(
                new MockMultipartFile("test1","test1.PNG", MediaType.IMAGE_PNG_VALUE,"test1".getBytes()),
                new MockMultipartFile("test2","test2.PNG", MediaType.IMAGE_PNG_VALUE,"test2".getBytes())
        );
        PostCreateRequest req = createPostCreateRequestWithImages(imageFiles);

        mockMvc.perform(
                        multipart("/api/posts")
                                .file("images",imageFiles.get(0).getBytes())
                                .file("images",imageFiles.get(1).getBytes())
                                .param("title",req.getTitle())
                                .param("content",req.getContent())
                                .param("price",String.valueOf(req.getPrice()))
                                .param("categoryId",String.valueOf(req.getCategoryId()))
                                //해당 요청은 POST 메소드임을 지정해줍니다.
                                .with(requestPostProcessor -> {
                                    requestPostProcessor.setMethod("POST");
                                    return requestPostProcessor;
                                })
                                //content type을 지정해줍니다.
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
        //Mock으로 만들어둔 PostService.create가 호출되는지 확인하고, 전달되는 인자를 캡쳐하였습니다.
        verify(postService).create(postCreateRequestArgumentCaptor.capture());

        PostCreateRequest capturedRequest = postCreateRequestArgumentCaptor.getValue();
        assertThat(capturedRequest.getImages().size()).isEqualTo(2);
    }

    @Test
    void readTest()throws Exception{
        Long id =1L;

        mockMvc.perform(
                        get("/api/posts/{id}",id))
                .andExpect(status().isOk());
        verify(postService).read(id);
    }

    @Test
    void deleteTest() throws Exception {
        Long id =1L ;
        mockMvc.perform(
                        delete("/api/posts/{id}",id))
                .andExpect(status().isOk());
        verify(postService).delete(id);
    }
//
//    @Test
//    void updateTest() throws Exception{
//        // given
//        //메서드에 전달된 PostUpdateRequest 캡처 한다.
//        ArgumentCaptor<PostUpdateRequest> postUpdateRequestArgumentCaptor = ArgumentCaptor.forClass(PostUpdateRequest.class);
//
//        List<MultipartFile> addedImages = List.of(
//                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
//                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes())
//        );
//        List<Long> deletedImages = List.of(1L, 2L);
//
//        PostUpdateRequest req = createPostUpdateRequest("title", "content", 1234L, addedImages, deletedImages);
//        //mockMvc를 사용하여 특정 POST 요청을 시뮬레이션하고, 해당 요청을 PUT 방식으로 변경합니다
//        // when, then
//        mockMvc.perform(
//                        multipart("/api/posts/{id}", 1L)
//                                .file("addedImages", addedImages.get(0).getBytes())
//                                .file("addedImages", addedImages.get(1).getBytes())
//                                .param("deletedImages", String.valueOf(deletedImages.get(0)), String.valueOf(deletedImages.get(1)))
//                                .param("title", req.getTitle())
//                                .param("content", req.getContent())
//                                .param("price", String.valueOf(req.getPrice()))
//                                .with(requestPostProcessor -> {
//                                    requestPostProcessor.setMethod("PUT");
//                                    return requestPostProcessor;
//                                })
//                                .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk());
//
//        verify(postService).update(anyLong(), postUpdateRequestArgumentCaptor.capture());
//
//        PostUpdateRequest capturedRequest = postUpdateRequestArgumentCaptor.getValue();
//        List<MultipartFile> capturedAddedImages = capturedRequest.getAddedImages();
//        assertThat(capturedAddedImages.size()).isEqualTo(2);
//
//        List<Long> capturedDeletedImages = capturedRequest.getDeletedImages();
//        assertThat(capturedDeletedImages.size()).isEqualTo(2);
//        assertThat(capturedDeletedImages).contains(deletedImages.get(0), deletedImages.get(1));
//    }
//    @Test
//    void readAllTest() throws Exception{
//        PostReadCondition cond = createPostReadCondition(0, 1, List.of(1L, 2L), List.of(1L, 2L));
//
//        mockMvc.perform(
//                get("/api/posts")
//                        .param("page",String.valueOf(cond.getPage())).param("size",String.valueOf(cond.getSize()))
//                        .param("categoryId",String.valueOf(cond.getCategoryId().get(0)),String.valueOf(cond.getCategoryId().get(1)))
//                        .param("memberId",String.valueOf(cond.getMemberId().get(0)),String.valueOf(cond.getMemberId().get(1)))
//        ).andExpect(status().isOk());
//        verify(postService).readAll(cond);
//    }

}