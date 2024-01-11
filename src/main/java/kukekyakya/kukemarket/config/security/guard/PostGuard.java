package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.AccessDeniedException;
import kukekyakya.kukemarket.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostGuard extends Guard{
    private final PostRepository postRepository;
    private List<RoleType> roleTypes = List.of(RoleType.ROLE_ADMIN);

    @Override
    protected List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return postRepository.findById(id)
                .map(post -> post.getMember())
                .map(member -> member.getId())
                .filter(memberId -> memberId.equals(AuthHelper.extractMemberId()))
                .isPresent();
    }



}
