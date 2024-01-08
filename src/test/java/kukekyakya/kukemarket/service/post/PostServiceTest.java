package kukekyakya.kukemarket.service.post;

import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.entity.post.Image;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.CategoryNotFoundException;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.exception.PostNotFoundException;
import kukekyakya.kukemarket.exception.UnsupportedImageFormatException;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.post.PostRepository;
import kukekyakya.kukemarket.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static kukekyakya.kukemarket.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static kukekyakya.kukemarket.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithImages;
import static kukekyakya.kukemarket.factory.entity.CategoryFactory.createCategory;
import static kukekyakya.kukemarket.factory.entity.ImageFactory.*;
import static kukekyakya.kukemarket.factory.entity.MemberFactory.createMember;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPostWithImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks PostService postService;
    @Mock
    PostRepository postRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    FileService fileService;

    @Test
    void createTest(){
        PostCreateRequest req = createPostCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));
        given(postRepository.save(any())).willReturn(createPostWithImages(
                IntStream.range(0,req.getImages().size()).mapToObj(i->createImage()).collect(toList())
        ));

        postService.create(req);

        verify(postRepository).save(any());
        verify(fileService,times(req.getImages().size())).upload(any(),anyString());
    }

    @Test
    void createExceptionByMemberNotFoundTest(){
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThatThrownBy(()->postService.create(createPostCreateRequest())).isInstanceOf(MemberNotFoundException.class);

    }

    @Test
    void createExceptionByCategoryNotFoundTest(){
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(()->postService.create(createPostCreateRequest())).isInstanceOf(CategoryNotFoundException.class);

    }

    @Test
    void createExceptionByUnsupportedImageFormatExceptionTest(){
        PostCreateRequest req= createPostCreateRequestWithImages(
                List.of(new MockMultipartFile("test","test.txt", MediaType.TEXT_PLAIN_VALUE,"test".getBytes()))
        );

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));

        assertThatThrownBy(()-> postService.create(req)).isInstanceOf(UnsupportedImageFormatException.class);

    }

//    @Test
//    void readTest(){
//        Post post = createPostWithImages(List.of(createImage(),createImage()));
//        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
//
//        PostDto postDto = postService.read(1L);
//
//        assertThat(postDto.getTitle()).isEqualTo(post.getTitle());
//        assertThat(postDto.getImages().size()).isEqualTo(post.getImages().size());
//    }
//
//    @Test
//    void readExceptionByPostNotFoundTest(){
//        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
//        assertThatThrownBy(()->postService.read(1L)).isInstanceOf(PostNotFoundException.class);
//    }
//
//
//    @Test
//    void deleteTest(){
//        Post post = createPostWithImages(List.of(createImage(),createImage()));
//        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
//
//        postService.delete(1L);
//        // delete 메서드가 Post 객체와 연관된 이미지의 수와 동일한 횟수로 호출되는지 확인합니다.
//        // anyString()을 사용하여 이 메서드가 호출된 횟수가 중요하며 삭제되는 특정 문자열은 중요하지 않음을 나타냅니다.
//        verify(fileService,times(post.getImages().size())).delete(anyString());
//        // any() 인자 매처는 delete 메서드가 어떤 인수로 호출되었는지는 중요하지 않고 메서드 호출 자체만을 확인합니다.
//        verify(postRepository).delete(any());
//    }
//    @Test
//    void deleteExceptionByNotFoundPostTest(){
//        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
//        assertThatThrownBy(()->postService.delete(1L)).isInstanceOf(PostNotFoundException.class);
//    }
//
//    @Test
//    void updateTest(){
//        Image a = createImageWithIdAndOriginName(1L,"a.png");
//        Image b = createImageWithIdAndOriginName(2L,"b.png");
//
//        Post post = createPostWithImages(List.of(a,b));
//        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
//        MockMultipartFile cFile =new MockMultipartFile("c","c.png",MediaType.IMAGE_PNG_VALUE,"c".getBytes());
//        PostUpdateRequest postUpdateRequest = createPostUpdateRequest("title","content",1000L,List.of(cFile),List.of(a.getId()));
//
//        postService.update(1L,postUpdateRequest);
//
//        List<Image> images =post.getImages();
//        List<String> originNames = images.stream().map(i->i.getOriginName()).collect(toList());
//
//        assertThat(originNames.size()).isEqualTo(2);
//        assertThat(originNames).contains(b.getOriginName(),cFile.getOriginalFilename());
//
//        verify(fileService,times(1)).upload(any(),anyString());
//        verify(fileService,times(1)).delete(anyString());
//    }
//
//    @Test
//    void updateExceptionByPostNotFoundTest(){
//        given(postRepository.findById(anyLong())).willReturn(Optional.empty());
//
//        assertThatThrownBy(()->postService.update(1L,createPostUpdateRequest("title","content",1234L,List.of(),List.of())))
//                .isInstanceOf(PostNotFoundException.class);
//    }
//
//    @Test
//    void readAllTest(){
//        given(postRepository.findAllByCondition(any())).willReturn(Page.empty());
//        PostListDto postListDto = postService.readAll(createPostReadCondition(1,1));
//        assertThat(postListDto.getPostList().size()).isZero();
//    }
}