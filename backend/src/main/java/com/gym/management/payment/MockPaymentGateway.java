package com.gym.management.payment;
import org.springframework.stereotype.Component;
import java.util.UUID;
/** Local college-demo simulation. This component never contacts a bank or moves money. */
@Component
public class MockPaymentGateway {
 public Decision authorize(boolean approved){return approved?new Decision(true,"MOCK-"+UUID.randomUUID(),null):new Decision(false,"MOCK-"+UUID.randomUUID(),"Declined by simulated issuer");}
 public record Decision(boolean approved,String reference,String failureReason){}
}
