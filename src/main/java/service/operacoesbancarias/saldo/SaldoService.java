package service.operacoesbancarias.saldo;

import br.ada.caixa.entity.Conta;
import br.ada.caixa.exceptions.ValidacaoException;
import br.ada.caixa.respository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SaldoService {

    private final ContaRepository contaRepository;

    public BigDecimal consultarSaldo(Long numeroConta) {
        return contaRepository.findById(numeroConta)
                .map(Conta::getSaldo)
                .orElseThrow(() -> new ValidacaoException("Conta inexistente!"));
    }

}
