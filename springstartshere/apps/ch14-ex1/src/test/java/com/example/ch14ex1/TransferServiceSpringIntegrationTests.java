package com.example.ch14ex1;

import com.example.ch14ex1.model.Account;
import com.example.ch14ex1.repository.AccountRepository;
import com.example.ch14ex1.service.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransferServiceSpringIntegrationTests {

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private TransferService transferService;

    @Test
    public void transferServiceTransferAmountTest(){
        Account sender = new Account();
        sender.setId(1);
        sender.setName("Jack");
        sender.setAmount(new BigDecimal(1000));

        Account receiver = new Account();
        receiver.setId(2);
        receiver.setName("Luke");
        receiver.setAmount(new BigDecimal(2000));

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(sender));
        given(accountRepository.findById(2L))
                .willReturn(Optional.of(receiver));

        transferService.transferMoney(1,2,new BigDecimal(50));

        verify(accountRepository)
                .changeAmount(1, new BigDecimal(950));
        verify(accountRepository)
                .changeAmount(2,new BigDecimal(2050));
    }
}
