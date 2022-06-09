package com.example.ch14ex1;

import com.example.ch14ex1.exception.AccountNotFoundException;
import com.example.ch14ex1.model.Account;
import com.example.ch14ex1.repository.AccountRepository;
import com.example.ch14ex1.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransferServiceWithAnnotationUnitTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    public void moneyTransferHappyFlow(){
        Account sender = new Account();
        sender.setId(1);
        sender.setName("Jack");
        sender.setAmount(new BigDecimal(1000));

        Account receiver = new Account();
        receiver.setId(2);
        receiver.setName("Luke");
        receiver.setAmount(new BigDecimal(2000));


        given(accountRepository.findById(sender.getId()))
                .willReturn(Optional.of(sender));
        given(accountRepository.findById(receiver.getId()))
                .willReturn(Optional.of(receiver));

        transferService.transferMoney(1,2,new BigDecimal(50));

        verify(accountRepository).changeAmount(1,new BigDecimal(950));
        verify(accountRepository).changeAmount(2,new BigDecimal(2050));
    }

    @Test
    public void moneyTransferReceiverAccountNotFoundFlow(){
        Account sender = new Account();
        sender.setId(1);
        sender.setName("Jack");
        sender.setAmount(new BigDecimal(1000));


        given(accountRepository.findById(sender.getId()))
                .willReturn(Optional.of(sender));
        given(accountRepository.findById(2L))
                .willReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transferMoney(1,2,new BigDecimal(50))
        );

        verify(accountRepository, never())
                .changeAmount(anyLong(), any());
    }
}
