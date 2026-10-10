package com.gym.management;
import com.gym.management.entity.*;import com.gym.management.exception.ConflictException;import com.gym.management.repository.AttendanceRepository;import com.gym.management.service.*;
import org.junit.jupiter.api.Test;import java.time.*;import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;import static org.mockito.Mockito.*;
class AttendanceServiceTests {
 @Test void checksInAndOut(){AttendanceRepository repo=mock(AttendanceRepository.class);GymService gym=mock(GymService.class);Member m=mock(Member.class);when(gym.lockMember(1L)).thenReturn(m);when(m.getMembershipStatus()).thenReturn("ACTIVE");when(repo.save(any())).thenAnswer(i->i.getArgument(0));AttendanceService svc=new AttendanceService(repo,gym);Attendance created=svc.checkIn(1L);assertNotNull(created.getCheckInAt());when(repo.findFirstByMemberIdAndCheckOutAtIsNullOrderByCheckInAtDesc(1L)).thenReturn(Optional.of(created));assertNotNull(svc.checkOut(1L).getCheckOutAt());}
 @Test void rejectsDuplicateCheckIn(){AttendanceRepository repo=mock(AttendanceRepository.class);GymService gym=mock(GymService.class);Member m=mock(Member.class);when(gym.lockMember(1L)).thenReturn(m);when(m.getMembershipStatus()).thenReturn("ACTIVE");when(repo.existsByMemberIdAndCheckOutAtIsNull(1L)).thenReturn(true);assertThrows(ConflictException.class,()->new AttendanceService(repo,gym).checkIn(1L));}
 @Test void rejectsInactiveMembership(){GymService gym=mock(GymService.class);Member m=mock(Member.class);when(gym.lockMember(1L)).thenReturn(m);when(m.getMembershipStatus()).thenReturn("EXPIRED");assertThrows(ConflictException.class,()->new AttendanceService(mock(AttendanceRepository.class),gym).checkIn(1L));}
}
