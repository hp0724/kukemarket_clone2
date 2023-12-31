package kukekyakya.kukemarket.entity.member;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//MemberRole 은 Member 에서 Set으로 저장되기 때문에 equals 와 hashcode 저으이
@EqualsAndHashCode
//여러개의 필드를 primary key 로 사용하기위해 IdClass 선언
//MemberRoleId 클래스에 정의된 필드와 동일한 필드를,
// MemberRole에서 @Id로 선언해주면, composite key로 만들어낼 수 있다.
@IdClass(MemberRoleId.class)
public class MemberRole {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}
