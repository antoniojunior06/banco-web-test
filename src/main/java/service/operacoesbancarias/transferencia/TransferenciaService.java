package service.operacoesbancarias.transferencia;

import service.operacoesbancarias.deposito.DepositoService;
import service.operacoesbancarias.saque.SaqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final SaqueService saqueService;
    private final DepositoService depositoService;

    public void transferir(Long numeroContaOrigem,
                           Long numeroContaDestino,
                           BigDecimal valor) {
        saqueService.sacar(numeroContaOrigem, valor);
        depositoService.depositar(numeroContaDestino, valor);
    }

}
