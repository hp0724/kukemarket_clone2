package kukekyakya.kukemarket.entity.post;

import kukekyakya.kukemarket.exception.UnsupportedImageFormatException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uniqueName;

    @Column(nullable = false)
    private String originName;

    //Image는 Post와 연관이 맺어져있을 경우에만 저장될 수 있도록,nullable=false
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post; // 1

    private final static String supportedExtension[] = {"jpg", "jpeg", "gif", "bmp", "png"}; // 2

    public Image(String originName) {
        this.uniqueName = generateUniqueName(extractExtension(originName)); // 3
        this.originName = originName;
    }

    public void initPost(Post post) { // 4
        if(this.post == null) {
            this.post = post;
        }
    }
    //이미지 구분을 위한 고유한 이름
    private String generateUniqueName(String extension) { // 5
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String extractExtension(String originName) { // 6
        try {
            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            if(isSupportedFormat(ext)) return ext;
        } catch (StringIndexOutOfBoundsException e) { }
        throw new UnsupportedImageFormatException();
    }

    private boolean isSupportedFormat(String ext) { // 7
        return Arrays.stream(supportedExtension).anyMatch(e -> e.equalsIgnoreCase(ext));
    }

}
