package service.operacoesbancarias.deposito;

import br.ada.caixa.exceptions.ValidacaoException;
import br.ada.caixa.respository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepositoService {

    private final ContaRepository contaRepository;

    public void depositar(Long numeroConta, BigDecimal valor) {
        contaRepository.findById(numeroConta)
                .ifPresentOrElse(conta -> {
                    conta.setSaldo(conta.getSaldo().add(valor));
                    contaRepository.save(conta);
                }, () -> { throw new ValidacaoException("Conta inválida!"); });
    }

}
