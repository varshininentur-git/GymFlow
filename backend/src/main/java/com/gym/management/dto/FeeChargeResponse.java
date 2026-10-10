package com.gym.management.dto;
import com.gym.management.entity.Member;
import java.math.BigDecimal;
public record FeeChargeResponse(Long memberId,String memberCode,BigDecimal outstandingBalance,String reason){public static FeeChargeResponse from(Member m,String reason){return new FeeChargeResponse(m.getId(),m.getMemberCode(),m.getOutstandingBalance(),reason);}}
