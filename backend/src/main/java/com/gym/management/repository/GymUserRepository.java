package com.gym.management.repository;
import com.gym.management.entity.GymUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface GymUserRepository extends JpaRepository<GymUser,Long>{
 Optional<GymUser> findByUsernameIgnoreCase(String username);
 boolean existsByUsernameIgnoreCase(String username);
 boolean existsByMemberId(Long memberId);
 @Query("select u from GymUser u join u.member m where lower(m.email)=lower(:email)")
 Optional<GymUser> findMemberAccountByEmail(@Param("email") String email);
}
