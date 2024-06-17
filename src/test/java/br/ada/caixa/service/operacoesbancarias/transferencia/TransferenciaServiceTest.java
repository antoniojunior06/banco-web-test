package br.ada.caixa.service.operacoesbancarias.transferencia;

import br.ada.caixa.service.operacoesbancarias.deposito.DepositoService;
import br.ada.caixa.service.operacoesbancarias.saque.SaqueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    private final Random random = new Random();
    private final Long numeroContaOrigem  = random.nextLong();
    private final Long numeroContaDestino = random.nextLong();
    private final BigDecimal valor = BigDecimal.valueOf(random.nextDouble()).setScale(2, RoundingMode.HALF_UP);

    @Mock
    private SaqueService saqueService;
    @Mock
    private DepositoService depositoService;

    @InjectMocks
    private TransferenciaService service;

    @Test
    void transferirTest() {
        // given
        // when
        service.transferir(numeroContaOrigem, numeroContaDestino, valor);

        // then
        verify(saqueService, times(1))
                .sacar(numeroContaOrigem, valor);
        verify(depositoService, times(1))
                .depositar(numeroContaDestino, valor);
    }


    @Test
    void transferirSaqueFailsTest() {
        // given
        doThrow(new RuntimeException("ERRO NO SAQUE"))
                .when(saqueService)
                .sacar(numeroContaOrigem, valor);

        // when
        // then
        assertThrows(RuntimeException.class,
                     () -> service.transferir(numeroContaOrigem, numeroContaDestino, valor));
        verify(saqueService, atMostOnce())
                .sacar(numeroContaOrigem, valor);
        verify(depositoService, never())
                .depositar(numeroContaDestino, valor);
    }

}