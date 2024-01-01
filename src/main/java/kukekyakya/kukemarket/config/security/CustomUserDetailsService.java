package kukekyakya.kukemarket.config.security;

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
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //토큰에서 추출한 id로 member 조회
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseGet(() -> new Member(null, null, null, null, List.of()));
        //찾은 member 를 이용해서 권한 찾기
        return new CustomUserDetails(
                String.valueOf(member.getId()),
                member.getRoles().stream().map(memberRole -> memberRole.getRole())
                        .map(role -> role.getRoleType())
                        //roleType enum 이니깐 string 으로 변경
                        .map(roleType -> roleType.toString())
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
    }
}
