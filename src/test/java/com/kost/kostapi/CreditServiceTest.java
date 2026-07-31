package com.kost.kostapi;

import com.kost.kostapi.entity.User;
import com.kost.kostapi.enums.CreditAmount;
import com.kost.kostapi.enums.UserRole;
import com.kost.kostapi.repository.UserRepository;
import com.kost.kostapi.service.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreditService creditService;

    private User regular;
    private User premium;
    private User owner;

    @BeforeEach
    void setUp() {

        regular = User.builder()
                .role(UserRole.REGULAR)
                .credit(0)
                .build();

        premium = User.builder()
                .role(UserRole.PREMIUM)
                .credit(5)
                .build();

        owner = User.builder()
                .role(UserRole.OWNER)
                .credit(999)
                .build();
    }

    @Test
    void recharge_should_reset_all_users_credit() {

        when(userRepository.findAll())
                .thenReturn(List.of(
                        regular,
                        premium,
                        owner
                ));

        creditService.rechargeMonthlyCredits();

        verify(userRepository).saveAll(anyList());

        assertEquals(
                CreditAmount.REGULAR.getValue(),
                regular.getCredit()
        );

        assertEquals(
                CreditAmount.PREMIUM.getValue(),
                premium.getCredit()
        );

        assertEquals(
                CreditAmount.OWNER.getValue(),
                owner.getCredit()
        );
    }

    @Test
    void recharge_should_save_once() {

        when(userRepository.findAll())
                .thenReturn(List.of(
                        regular,
                        premium,
                        owner
                ));

        creditService.rechargeMonthlyCredits();

        verify(userRepository, times(1))
                .saveAll(anyList());

        verify(userRepository, times(1))
                .findAll();
    }

    @Test
    void recharge_when_no_users_should_not_fail() {

        when(userRepository.findAll())
                .thenReturn(List.of());

        creditService.rechargeMonthlyCredits();

        verify(userRepository, times(1))
                .findAll();

        verify(userRepository, times(1))
                .saveAll(anyList());
    }
}