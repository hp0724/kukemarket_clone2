package kukekyakya.kukemarket.repository.post;

import kukekyakya.kukemarket.dto.post.PostReadCondition;
import kukekyakya.kukemarket.dto.post.PostSimpleDto;
import org.springframework.data.domain.Page;

//QueryDSL을 이용하여 쿼리를 작성하기 위해, 커스텀 리포지토리를 만들어주기
public interface CustomPostRepository {
    //Page로 반환하여 페이징 결과에 대한 각종 정보를 손쉽게 확인
    Page<PostSimpleDto> findAllByCondition(PostReadCondition cond);
}
