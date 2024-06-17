package br.ada.caixa.service.operacoesbancarias.saque;

import br.ada.caixa.entity.Conta;
import br.ada.caixa.exceptions.ValidacaoException;
import br.ada.caixa.respository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.operacoesbancarias.saque.OperacaoSaque;
import service.operacoesbancarias.saque.SaqueService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class SaqueServiceTest {

    private final Random random = new Random();
    private final Long numeroConta = random.nextLong();

    @Mock
    private ContaRepository repository;

    @InjectMocks
    private SaqueService service;

    @Test
    void sacarTest() {

        // given
        final Conta conta = mock(Conta.class);
        final BigDecimal saldo = BigDecimal.valueOf(100);
        final BigDecimal valor = BigDecimal.valueOf(50);
        final BigDecimal novoSaldo = saldo.subtract(valor);

        given(repository.findById(numeroConta)).willReturn(Optional.of(conta));
        given(conta.getSaldo()).willReturn(saldo);

        // when
        service.sacar(numeroConta, valor);

        // then
        verify(conta).setSaldo(novoSaldo);
        verify(repository, times(1)).save(conta);

    }

    @Test
    void sacarSaldoInsuficienteTest() {
        // given
        final Conta conta = mock(Conta.class);
        final BigDecimal saldo = BigDecimal.valueOf(100);
        final BigDecimal valor = BigDecimal.valueOf(150);


        given(repository.findById(numeroConta)).willReturn(Optional.of(conta));
        given(conta.getSaldo()).willReturn(saldo);

        // when
        // then
        assertThrows(ValidacaoException.class, () -> service.sacar(numeroConta, valor));
        verify(conta, never()).setSaldo(any());
        verify(repository, never()).save(any());
    }

    @Test
    void sacarContaInvalidaTest() {
        // given
        BigDecimal valor = BigDecimal.valueOf(100);

        when(repository.findById(numeroConta)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(ValidacaoException.class, () -> service.sacar(numeroConta, valor));
        verify(repository, never()).save(any());
    }

}