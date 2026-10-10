package com.gym.management.repository;
import com.gym.management.entity.*;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
public interface PaymentRepository extends JpaRepository<Payment,Long>{
 Optional<Payment> findByIdempotencyKey(String key); Optional<Payment> findByTransactionReference(String ref);
 boolean existsByMemberId(Long memberId);
 @EntityGraph(attributePaths={"member","plan"}) List<Payment> findByMemberIdOrderByRequestedAtDesc(Long memberId); List<Payment> findAllByOrderByRequestedAtDesc();
 @EntityGraph(attributePaths={"member","plan"}) List<Payment> findTop10ByMemberIdOrderByRequestedAtDesc(Long memberId);
 long countByMemberIdAndStatus(Long memberId,PaymentStatus status);
 java.util.Optional<Payment> findFirstByMemberIdAndStatusOrderByCompletedAtDesc(Long memberId,PaymentStatus status);
 @Query("select coalesce(sum(p.amount),0) from Payment p where p.member.id=:memberId and p.status=com.gym.management.entity.PaymentStatus.SUCCESS")
 BigDecimal successfulTotalForMember(@Param("memberId") Long memberId);
 @Query("select case when count(p)>0 then true else false end from Payment p where p.member.id=:memberId and p.status=com.gym.management.entity.PaymentStatus.PENDING and p.purpose in (com.gym.management.entity.PaymentPurpose.MEMBERSHIP_PURCHASE,com.gym.management.entity.PaymentPurpose.RENEWAL)")
 boolean existsPendingMembership(@Param("memberId") Long memberId);
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select p from Payment p where p.id=:id") Optional<Payment> lockById(@Param("id")Long id);
 @Query("select coalesce(sum(p.amount),0) from Payment p where p.status=com.gym.management.entity.PaymentStatus.SUCCESS") BigDecimal successfulTotal();
 @Query("select coalesce(sum(p.amount),0) from Payment p where p.status=com.gym.management.entity.PaymentStatus.SUCCESS and p.completedAt>=:from and p.completedAt<:to") BigDecimal successfulBetween(@Param("from")LocalDateTime from,@Param("to")LocalDateTime to);
 long countByStatus(PaymentStatus status);
}
