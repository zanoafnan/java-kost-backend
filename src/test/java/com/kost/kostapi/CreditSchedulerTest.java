package com.kost.kostapi;

import com.kost.kostapi.scheduler.CreditScheduler;
import com.kost.kostapi.service.CreditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreditSchedulerTest {

    @Mock
    private CreditService creditService;

    @InjectMocks
    private CreditScheduler scheduler;

    @Test
    void scheduler_should_call_credit_service() {

        scheduler.rechargeCredits();

        verify(creditService, times(1))
                .rechargeMonthlyCredits();
    }
}