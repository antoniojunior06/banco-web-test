package br.ada.caixa.service.operacoesbancarias.deposito;

import br.ada.caixa.entity.Conta;
import br.ada.caixa.exceptions.ValidacaoException;
import br.ada.caixa.respository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.operacoesbancarias.deposito.DepositoService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Random;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DepositoServiceTest {

    private final Random random = new Random();
    private final Long numeroConta = random.nextLong();

    @Mock
    private ContaRepository repository;

    @InjectMocks
    private DepositoService service;

    @Test
    void depositoTest() {

        // given
        final Conta conta = mock(Conta.class);
        final BigDecimal saldo = BigDecimal.valueOf(100);
        final BigDecimal valor = BigDecimal.valueOf(200);
        final BigDecimal novoSaldo = saldo.add(valor);

        given(repository.findById(numeroConta)).willReturn(Optional.of(conta));
        given(conta.getSaldo()).willReturn(saldo);

        //when
        service.depositar(numeroConta, valor);

        //then
        verify(conta).setSaldo(novoSaldo);
        verify(repository, times(1)).save(conta);

    }

    @Test
    void depositarContaInvalidaTest() {
        // given
        BigDecimal valor = BigDecimal.valueOf(100);

        given(repository.findById(numeroConta)).willReturn(Optional.empty());

        // when
        // then
        assertThrows(ValidacaoException.class, () -> service.depositar(numeroConta, valor));
        verify(repository, never()).save(any());
    }
}