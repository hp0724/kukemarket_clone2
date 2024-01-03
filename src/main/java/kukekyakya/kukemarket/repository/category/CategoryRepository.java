package kukekyakya.kukemarket.repository.category;

import kukekyakya.kukemarket.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//entity 값과 타입 id 타입 적어주기
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("select c from Category c left join c.parent p order by p.id asc nulls first,c.id asc")
    List<Category> findAllOrderByParentIdAscNullsFirstCategoryIdAsc();

}
