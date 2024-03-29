package kukekyakya.kukemarket.entity.member;

import kukekyakya.kukemarket.entity.common.EntityDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(
        name = "Member.roles",
        attributeNodes = @NamedAttributeNode(value = "roles",subgraph = "Member.roles.role"),
        subgraphs = @NamedSubgraph(name = "Member.roles.role",attributeNodes = @NamedAttributeNode("role"))

)
public class Member extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false,unique = true,length = 30,updatable = true)
    private String email;

    private String password;

    @Column(nullable = false,length = 20)
    private String username;

    @Column(nullable = false,unique = true,length = 20)
    private String nickname;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<MemberRole> roles;

    public Member(String email,String password,String username,String nickname,List<Role>roles){
        this.email=email;
        this.password=password;
        this.username = username;
        this.nickname=nickname;
        this.roles = roles.stream().map(r->new MemberRole(this,r)).collect(toSet());
    }

    public void updateNickname(String nickname){
        this.nickname=nickname;
    }
}
