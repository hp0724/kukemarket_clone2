package kukekyakya.kukemarket.repository.post;

import kukekyakya.kukemarket.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface PostRepository extends JpaRepository<Post,Long>,CustomPostRepository {
    /*
    join fetch p.member: 이 부분은 Post 엔터티의 member 속성과 조인하고,
    fetch 키워드를 사용함으로써 관련 엔터티를 즉시 로딩하도록 지시합니다.

    where p.id=:id: 이 부분은 Post 엔터티의 id 속성이 지정된 값과 일치하는지를 확인하는 조건입니다.
    :id는 파라미터 바인딩을 나타내며, 실제 실행 시에 이 부분이 특정 값으로 대체됩니다.
     */
    /*  param 을 통해서 파라미터 알려주기
    * */
    @Query("select p from Post p join fetch p.member where p.id = :postId")
    Optional<Post> findByIdWithMember(@Param("postId") Long id);
}
