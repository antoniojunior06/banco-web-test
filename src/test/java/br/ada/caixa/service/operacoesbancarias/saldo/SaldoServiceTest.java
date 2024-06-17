package br.ada.caixa.service.operacoesbancarias.saldo;

import br.ada.caixa.entity.Conta;
import br.ada.caixa.exceptions.ValidacaoException;
import br.ada.caixa.respository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.operacoesbancarias.saldo.SaldoService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class SaldoServiceTest {

    private final Random random = new Random();
    private final Long numeroConta = random.nextLong();

    @Mock
    private ContaRepository repository;

    @InjectMocks
    private SaldoService service;


    @Test
    void saldoTest() {

        // given
        final Conta conta = mock(Conta.class);
        final BigDecimal expected = BigDecimal.valueOf(100);

        given(repository.findById(numeroConta)).willReturn(Optional.of(conta));
        given(conta.getSaldo()).willReturn(expected);

        //when
        BigDecimal actual = service.consultarSaldo(numeroConta);

        //then
        assertEquals(expected, actual);

    }

    @Test
    void saldoContaInvalidaTest() {
        // given
        when(repository.findById(numeroConta)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(ValidacaoException.class, () -> service.consultarSaldo(numeroConta));

    }
}