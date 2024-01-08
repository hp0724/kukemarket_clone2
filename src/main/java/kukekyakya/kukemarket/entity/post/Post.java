package kukekyakya.kukemarket.entity.post;

import kukekyakya.kukemarket.dto.post.PostUpdateRequest;
import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.entity.common.EntityDate;
import kukekyakya.kukemarket.entity.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//date 컬럼 자동추가를 위한 EntityDate
public class Post extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @OneToMany(mappedBy = "post",cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images;

    public Post( String title, String content, Long price, Member member, Category category, List<Image> images) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.member = member;
        this.category = category;
        this.images = new ArrayList<>();
        addImages(images);
    }
    //새롭게 업데이트된 이미지 결과에 대해서 파일 저장소에 반영해주어야합니다
    public ImageUpdatedResult update(PostUpdateRequest req) { // 1
        this.title = req.getTitle();
        this.content = req.getContent();
        this.price = req.getPrice();
        //추가된 이미지들에 대한 정보와 삭제된 이미지들에 대한 정보를 반환
        ImageUpdatedResult result = findImageUpdatedResult(req.getAddedImages(), req.getDeletedImages());
        addImages(result.getAddedImages());
        deleteImages(result.getDeletedImages());
        return result;
    }

    private void addImages(List<Image> added) {
        added.stream().forEach(i -> {
            images.add(i);
            i.initPost(this);
        });
    }
    //this.images에서 삭제될 이미지를 제거해줍니다. 파라미터로 전달받은 이미지와 this.images는 영속된 상태일 것이고,
    // orphanRemoval=true에 의해 Post와 연관 관계가 끊어지며 고아 객체가 된 Image는 데이터베이스에서도 제거될 것입니다
    private void deleteImages(List<Image> deleted) { // 2
        deleted.stream().forEach(di -> this.images.remove(di));
    }

    // 3
    //업데이트되어야할 이미지 결과 정보를 만들어줍니다.
    private ImageUpdatedResult findImageUpdatedResult(List<MultipartFile> addedImageFiles, List<Long> deletedImageIds) {
        List<Image> addedImages = convertImageFilesToImages(addedImageFiles);
        List<Image> deletedImages = convertImageIdsToImages(deletedImageIds);
        return new ImageUpdatedResult(addedImageFiles, addedImages, deletedImages);
    }

    private List<Image> convertImageIdsToImages(List<Long> imageIds) {
        return imageIds.stream()
                .map(id -> convertImageIdToImage(id))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(toList());
    }

    private Optional<Image> convertImageIdToImage(Long id) {
        return this.images.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    private List<Image> convertImageFilesToImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream().map(imageFile -> new Image(imageFile.getOriginalFilename())).collect(toList());
    }
    //update를 호출한 클라이언트에게 전달될 이미지 업데이트 결과입니다. 클라이언트는 이 정보를 가지고,
    // 실제 파일 저장소에서 추가될 이미지는 업로드하고, 삭제될 이미지는 제거할 것입니다.
    @Getter
    @AllArgsConstructor
    public static class ImageUpdatedResult { // 4
        private List<MultipartFile> addedImageFiles;
        private List<Image> addedImages;
        private List<Image> deletedImages;
    }
}
