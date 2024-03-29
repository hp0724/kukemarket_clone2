package kukekyakya.kukemarket.config.security;

import kukekyakya.kukemarket.config.token.TokenHelper;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//명칭은 service 지만 security 에서 사용하기에 component
//파라미터로 전달 받은 사용자 id 값을 이용해서, 데이터베이스에서 Member를 조회하고 있습니다.
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    //DB에서 데이터 조회를 안함
    private final TokenHelper accessTokenHelper;
    @Override
    public CustomUserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
//        //토큰에서 추출한 id로 member 조회
//        Member member = memberRepository.findWithRolesById(Long.valueOf(userId))
//                .orElseGet(() -> new Member(null, null, null, null, List.of()));
//        //찾은 member 를 이용해서 권한 찾기
//        return new CustomUserDetails(
//                String.valueOf(member.getId()),
//                //member 의 memberRole 을 조회하면서 쿼리 한번 더
//                member.getRoles().stream().map(memberRole -> memberRole.getRole())
//                        //각 memberRole 의 RoleType 을 확인하기 위해 Role 을 다시 조회하면서 N번의 쿼리
//                        .map(role -> role.getRoleType())
//                        //roleType enum 이니깐 string 으로 변경
//                        .map(roleType -> roleType.toString())
//                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
//        );
        return accessTokenHelper.parse(token)
                .map(this::convert)
                .orElse(null);
    }

    private CustomUserDetails convert ( TokenHelper.PrivateClaims privateClaims){
        return new CustomUserDetails(
                privateClaims.getMemberId(),
                privateClaims.getRoleTypes().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
    }
}
