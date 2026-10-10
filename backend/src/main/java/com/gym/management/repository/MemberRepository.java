package com.gym.management.repository;
import com.gym.management.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import com.gym.management.dto.MemberAdminOptionDto;
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByFullNameContainingIgnoreCaseOrMemberCodeContainingIgnoreCase(String name, String code);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email,Long id);
    @Query("select new com.gym.management.dto.MemberAdminOptionDto(m.id,m.fullName,m.memberCode,m.email) from Member m where not exists (select u.id from GymUser u where u.member=m) order by m.fullName")
    List<MemberAdminOptionDto> findAllWithoutLoginAccount();
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select m from Member m where m.id=:id") java.util.Optional<Member> lockById(@Param("id") Long id);
}
