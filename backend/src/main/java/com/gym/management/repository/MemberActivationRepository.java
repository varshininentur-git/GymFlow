package com.gym.management.repository;

import com.gym.management.entity.MemberActivation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface MemberActivationRepository extends JpaRepository<MemberActivation, Long> {
    Optional<MemberActivation> findByMemberId(Long memberId);
    boolean existsByMemberId(Long memberId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from MemberActivation a join fetch a.member where a.tokenHash=:hash")
    Optional<MemberActivation> lockByTokenHash(@Param("hash") String hash);
}
