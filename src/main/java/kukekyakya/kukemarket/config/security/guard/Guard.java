package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.config.security.guard.AuthHelper;

import java.util.List;
public abstract class  Guard {
    //인증된 사용자만 서비스 로직에 접근
    //Guard 에서는 인증된 사용자인지 검사할 필요가 없다.
    public final boolean check(Long id) {
        return hasRole(getRoleTypes()) || isResourceOwner(id);
    }

    abstract protected List<RoleType> getRoleTypes();
    abstract protected boolean isResourceOwner(Long id);

    private boolean hasRole(List<RoleType> roleTypes) {
        return roleTypes.stream().allMatch(roleType -> AuthHelper.extractMemberRoles().contains(roleType));
    }
}
