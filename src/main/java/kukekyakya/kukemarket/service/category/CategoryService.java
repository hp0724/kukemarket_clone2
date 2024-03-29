package kukekyakya.kukemarket.service.category;

import kukekyakya.kukemarket.dto.category.CategoryCreateRequest;
import kukekyakya.kukemarket.dto.category.CategoryDto;
import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.exception.CategoryNotFoundException;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> readAll() {
        List<Category> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
        return CategoryDto.toDtoList(categories);
    }

    @Transactional
    public void create(CategoryCreateRequest req) {
        categoryRepository.save(CategoryCreateRequest.toEntity(req, categoryRepository));
    }

    //deleteById의 경우 select+ delete 이다
    @Transactional
    public void delete(Long id) {
        Category category =categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
    }

}
