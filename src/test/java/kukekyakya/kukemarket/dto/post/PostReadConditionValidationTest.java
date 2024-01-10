package kukekyakya.kukemarket.dto.post;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;


import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static kukekyakya.kukemarket.factory.dto.PostReadConditionFactory.createPostReadCondition;
import static org.assertj.core.api.Assertions.assertThat;

public class PostReadConditionValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest(){
        PostReadCondition cond = createPostReadCondition(1,1);
        Set<ConstraintViolation<PostReadCondition>>  validate = validator.validate(cond);
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByNullPageTest(){
        Integer invalidValue = null;
        PostReadCondition req = createPostReadCondition(invalidValue,1);

        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);


        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativePageTest(){
        Integer invalidValue = -1;
        PostReadCondition req = createPostReadCondition(invalidValue,1);

        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);


        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNullSizeTest(){
        Integer invalidValue = null;
        PostReadCondition req = createPostReadCondition(1,invalidValue);

        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);


        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativeOrZeroSizeTest(){
        Integer invalidValue = 0;
        PostReadCondition req = createPostReadCondition(1,invalidValue);

        Set<ConstraintViolation<PostReadCondition>> validate = validator.validate(req);


        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }
}