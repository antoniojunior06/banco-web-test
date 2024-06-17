package service.operacoesbancarias.saque;


import br.ada.caixa.exceptions.ValidacaoException;
import br.ada.caixa.respository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaqueService {

    private final ContaRepository contaRepository;
    private final List<OperacaoSaque> operacaoSaqueList;

    public void sacar(Long numeroConta, BigDecimal valor) {
        contaRepository.findById(numeroConta)
                .ifPresentOrElse(conta -> {
                    if (valor.compareTo(conta.getSaldo()) <= 0) {
                        conta.setSaldo(conta.getSaldo().subtract(valor));
                        contaRepository.save(conta);
                    } else {
                        throw new ValidacaoException("Saldo insuficiente!");
                    }
                }, () -> { throw new ValidacaoException("Conta inválida!"); });
    }

}
