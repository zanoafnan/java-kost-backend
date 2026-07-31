package com.kost.kostapi.scheduler;

import com.kost.kostapi.service.CreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditScheduler {

    private final CreditService creditService;


    @Scheduled(cron = "0 0 0 1 * *")
    public void rechargeCredits() {

        log.info("Starting monthly credit recharge...");

        creditService.rechargeMonthlyCredits();

        log.info("Monthly credit recharge completed.");
    }
}