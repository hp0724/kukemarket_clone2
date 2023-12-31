package kukekyakya.kukemarket.entity.member;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//@IdClass로 사용될 클래스는 Serializable 구현
public class MemberRoleId implements Serializable {
    private Member member;
    private Role role;
}
