package kukekyakya.kukemarket.learning;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OptionalTest {

    @Test
    void doseNotInvokeOptionalInnerFunctionByOuterNullValueTest() { // 1
        // given, when
        Long result = Optional.empty()
                .map(id -> Optional.<Long>empty().orElseThrow(RuntimeException::new))
                .orElse(5L);

        // then
        assertThat(result).isEqualTo(5L);
    }

    @Test
    void catchWhenExceptionIsThrownInOptionalInnerFunctionTest() { // 2
        // given, when, then
        assertThatThrownBy(
                () -> Optional.ofNullable(5L)
                        .map(id -> Optional.<Long>empty().orElseThrow(RuntimeException::new))
                        .orElse(1L))
                .isInstanceOf(RuntimeException.class);
    }
}