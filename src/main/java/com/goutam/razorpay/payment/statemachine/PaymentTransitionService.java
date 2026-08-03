package com.goutam.razorpay.payment.statemachine;

import com.goutam.razorpay.common.enums.PaymentActor;
import com.goutam.razorpay.common.enums.PaymentEvent;
import com.goutam.razorpay.common.enums.PaymentStatus;
import com.goutam.razorpay.payment.entity.Payment;
import com.goutam.razorpay.payment.entity.PaymentTransitionLog;
import com.goutam.razorpay.payment.repository.PaymentTransitionLogRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent event) {
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);
        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .event(event)
                .toStatus(next)
                .actor(PaymentActor.SYSTEM) //TODO: fetch merchant context to identify actor
                .occurredAt(LocalDateTime.now())
                .build();
        payment.setStatus(next);
        paymentTransitionLogRepository.save(log);
        return next;
    }

}
