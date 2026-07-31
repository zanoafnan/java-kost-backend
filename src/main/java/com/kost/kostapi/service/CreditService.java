package com.kost.kostapi.service;

import com.kost.kostapi.enums.CreditAmount;
import com.kost.kostapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final UserRepository userRepository;

    @Transactional
    public void rechargeMonthlyCredits() {

        var users = userRepository.findAll();

        users.forEach(user -> {

            int credit = switch (user.getRole()) {
                case OWNER -> CreditAmount.OWNER.getValue();
                case REGULAR -> CreditAmount.REGULAR.getValue();
                case PREMIUM -> CreditAmount.PREMIUM.getValue();
            };

            user.setCredit(credit);
        });

        userRepository.saveAll(users);
    }
}