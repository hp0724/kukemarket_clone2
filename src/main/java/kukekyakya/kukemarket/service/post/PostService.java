package kukekyakya.kukemarket.service.post;

import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.dto.post.PostCreateResponse;
import kukekyakya.kukemarket.dto.post.PostDto;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.post.Image;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.PostNotFoundException;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.post.PostRepository;
import kukekyakya.kukemarket.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Transactional
    public PostCreateResponse create(PostCreateRequest req) {
        Post post = postRepository.save(
                PostCreateRequest.toEntity(
                        req,
                        memberRepository,
                        categoryRepository
                )
        );
        uploadImages(post.getImages(), req.getImages());
        return new PostCreateResponse(post.getId());
    }

    public PostDto read(Long id) {
        //findById 를 통해서 post 찾기
        return PostDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }
}
