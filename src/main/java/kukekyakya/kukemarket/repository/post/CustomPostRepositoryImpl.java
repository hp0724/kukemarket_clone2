package kukekyakya.kukemarket.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kukekyakya.kukemarket.dto.post.PostReadCondition;
import kukekyakya.kukemarket.dto.post.PostSimpleDto;
import kukekyakya.kukemarket.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;
import static kukekyakya.kukemarket.entity.post.QPost.post;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
//빈으로 등록된 리포지토리에서 해당 구현체의 메소드를 이용
//조회를 수행하므로 readOnly 설정
@Transactional(readOnly = true)//1
//페이징 간단하게 처리 QuerydslRepositorySupport
public class CustomPostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {//2
    //쿼리 빌드
    private final JPAQueryFactory jpaQueryFactory;//3
    public CustomPostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory=jpaQueryFactory;
    }

    @Override
    public Page<PostSimpleDto> findAllByCondition(PostReadCondition cond) {//5
        Pageable pageable = PageRequest.of(cond.getPage(),cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate,pageable),pageable,fetchCount(predicate));
    }

    //게시글 목록 조회 반환
    private List<PostSimpleDto> fetchAll(Predicate predicate,Pageable pageable){//6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(PostSimpleDto.class,post.id ,post.title,post.member.nickname,post.createdAt))
                        .from(post)
                        .join(post.member)
                        .where(predicate)
                        .orderBy(post.id.desc())
        ).fetch();
    }
    //파라미터로 전달받은 조건식의 count 쿼리를 수행한 결과를 반환해줍니다
    private Long fetchCount(Predicate predicate){//7
        return jpaQueryFactory.select(post.count()).from(post).where(predicate).fetchOne();
    }
    //전달받은 PostReadCondition으로 Predicate을 빌드하여 반환해줍니다. 같은 조건 사이에는 OR 연산으로,
    // 이렇게 묶인 다른 조건 사이에는 AND 연산으로 묶이게 되었습니다.
    private Predicate createPredicate(PostReadCondition cond){//8
        return new BooleanBuilder()
                .and(orConditionsByEqCategoryIds(cond.getCategoryId()))
                .and(orConditionsByEqMemberIds(cond.getMemberId()));
    }

    private Predicate orConditionsByEqCategoryIds(List<Long> categoryIds) { // 9
        return orConditions(categoryIds, post.category.id::eq);
    }

    private Predicate orConditionsByEqMemberIds(List<Long> memberIds) { // 10
        return orConditions(memberIds, post.member.id::eq);
    }

    private <T> Predicate orConditions(List<T> values , Function<T, BooleanExpression> term){
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}