package com.gym.management.dto;
import com.gym.management.entity.Member;
import java.time.LocalDate;
public record MemberProfileDto(String fullName,String username,String email,String phone,String memberCode,String category,LocalDate dateOfBirth,String address,LocalDate joiningDate) {
 public static MemberProfileDto from(Member m,String username){return new MemberProfileDto(m.getFullName(),username,m.getEmail(),m.getPhone(),m.getMemberCode(),m.getCategory().name(),m.getDateOfBirth(),m.getAddress(),m.getJoiningDate());}
}
