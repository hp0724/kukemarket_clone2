package kukekyakya.kukemarket.entity.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    //EnumType.STRING 으로 데이터베이스 지정할때 문자열로 저장
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private RoleType roleType;

    public Role(RoleType roleType){
        this.roleType=roleType;
    }
}
