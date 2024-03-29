package kukekyakya.kukemarket.controller.category;

import kukekyakya.kukemarket.advice.ExceptionAdvice;
import kukekyakya.kukemarket.controller.category.CategoryController;
import kukekyakya.kukemarket.exception.CannotConvertNestedStructureException;
import kukekyakya.kukemarket.exception.CategoryNotFoundException;
import kukekyakya.kukemarket.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerAdviceTest {
    @InjectMocks
    CategoryController categoryController;
    @Mock
    CategoryService categoryService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).setControllerAdvice(new ExceptionAdvice()).build();
    }
    @Test
    void readAllTest() throws Exception{
        given(categoryService.readAll()).willThrow(CannotConvertNestedStructureException.class);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(-1011));
    }

    @Test
    void deleteTest() throws Exception {
        // given
        doThrow(CategoryNotFoundException.class).when(categoryService).delete(anyLong());

        // when, then
        mockMvc.perform(delete("/api/categories/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1010));
    }
}