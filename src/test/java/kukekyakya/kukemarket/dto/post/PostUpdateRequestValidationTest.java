package kukekyakya.kukemarket.dto.post;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static kukekyakya.kukemarket.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class PostUpdateRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest(){
        PostUpdateRequest req = createPostUpdateRequest("title","content",1234L,List.of(), List.of());
        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);
        assertThat(validate).isEmpty();

    }
    @Test
    void invalidateByEmptyTitleTest(){
        String invalidValue =null;
        PostUpdateRequest req =createPostUpdateRequest(invalidValue,"content",1234L, List.of(),List.of());

        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByBlankTitleTest(){
        String invalidValue=" ";
        PostUpdateRequest req =createPostUpdateRequest(invalidValue,"content",1234L, List.of(),List.of());

        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByEmptyContentTest(){

        String invalidValue=null;
        PostUpdateRequest req =createPostUpdateRequest("title",invalidValue,1234L, List.of(),List.of());

        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByBlankContentTest(){

        String invalidValue=" ";
        PostUpdateRequest req =createPostUpdateRequest("title",invalidValue,1234L, List.of(),List.of());

        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByNullPriceTest(){

        Long invalidValue=null;
        PostUpdateRequest req =createPostUpdateRequest("title","content",invalidValue, List.of(),List.of());

        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByNegativePriceTest(){

        Long invalidValue=-1L;
        PostUpdateRequest req =createPostUpdateRequest("title","content",invalidValue, List.of(),List.of());

        Set<ConstraintViolation<PostUpdateRequest>> validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }
}