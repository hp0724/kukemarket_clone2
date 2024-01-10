package kukekyakya.kukemarket.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

//검색 조건을 나타냄

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReadCondition {
    @NotNull(message = "페이지 번호를 입력해주세요.")
    @PositiveOrZero(message = "올바른 페이지 번호를 입력해주세요. (0 이상)")
    private Integer page;

    @NotNull(message = "페이지 크기를 입력해주세요.")
    @Positive(message = "올바른 페이지 크기를 입력해주세요. (1 이상)")
    private Integer size;
    //카테고리 id와 사용자 id는 여러 개 전달받을 수 있습니다
    private List<Long> categoryId = new ArrayList<>();
    private List<Long> memberId = new ArrayList<>();
}
