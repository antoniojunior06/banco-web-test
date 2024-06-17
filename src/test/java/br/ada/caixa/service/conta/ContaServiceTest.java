package br.ada.caixa.service.conta;

import br.ada.caixa.entity.Cliente;
import br.ada.caixa.entity.Conta;
import br.ada.caixa.entity.TipoConta;
import br.ada.caixa.exceptions.ValidacaoException;
import br.ada.caixa.respository.ClienteRepository;
import br.ada.caixa.respository.ContaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.conta.ContaService;
import service.operacoesbancarias.deposito.DepositoService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    private final Random random = new Random();
    private final Long numeroConta = random.nextLong();

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ContaService service;

    @Test
    void abrirContaPoupancaPFTest() {

        // given
        final Conta contaPoupanca = new Conta();
        final Cliente cliente = mock(Cliente.class);
        final String cpf = "12345678901";
        cliente.setDocumento(cpf);

        contaPoupanca.setTipo(TipoConta.CONTA_POUPANCA);
        contaPoupanca.setCliente(cliente);
        contaPoupanca.setSaldo(BigDecimal.ZERO);

        given(clienteRepository.findByDocumento(cpf)).willReturn(Optional.of(cliente));
        given(contaRepository.save(contaPoupanca)).willReturn(contaPoupanca);

        //when
        Conta contaCriada = service.abrirContaPoupanca(cpf);

        //then
        assertEquals(contaPoupanca, contaCriada);


    }

    @Test
    void abrirContaPoupancaClienteInvalidoTest() {
        // given
        final String cpf = "12345678901";

        given(clienteRepository.findByDocumento(cpf)).willReturn(Optional.empty());

        // when
        // then
        assertThrows(ValidacaoException.class, () -> service.abrirContaPoupanca(cpf));
        verify(contaRepository, never()).save(any());
    }
}