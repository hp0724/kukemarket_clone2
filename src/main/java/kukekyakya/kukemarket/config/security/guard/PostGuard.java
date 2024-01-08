package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.AccessDeniedException;
import kukekyakya.kukemarket.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostGuard {
    private final AuthHelper authHelper;
    private final PostRepository postRepository;

    public boolean check(Long id){
        return authHelper.isAuthenticated() && hasAuthority(id);
    }
    //반드시 관리자 권한 검사가 , 자원의 소유자 검사보다 먼저 이루어져야 한다.
    // 데이터베이스에 접근하는 비용이 비싸기 때문이다.
    private boolean hasAuthority(Long id) {
        return hasAdminRole() || isResourceOwner(id); // 1
    }

    private boolean isResourceOwner(Long id) {
        Post post = postRepository.findById(id).orElseThrow(AccessDeniedException::new);
        Long memberId = authHelper.extractMemberId();
        return post.getMember().getId().equals(memberId);
    }

    private boolean hasAdminRole() {
        return authHelper.extractMemberRoles().contains(RoleType.ROLE_ADMIN);
    }
}
