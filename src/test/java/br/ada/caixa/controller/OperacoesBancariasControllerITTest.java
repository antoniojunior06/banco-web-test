package br.ada.caixa.controller;

import br.ada.caixa.dto.request.DepositoRequestDto;
import br.ada.caixa.dto.request.InvestimentoRequestDto;
import br.ada.caixa.dto.request.SaqueRequestDto;
import br.ada.caixa.dto.request.TransferenciaRequestDto;
import br.ada.caixa.dto.response.SaldoResponseDto;
import br.ada.caixa.entity.Cliente;
import br.ada.caixa.entity.Conta;
import br.ada.caixa.entity.TipoCliente;
import br.ada.caixa.entity.TipoConta;
import br.ada.caixa.enums.StatusCliente;
import br.ada.caixa.respository.ClienteRepository;
import br.ada.caixa.respository.ContaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OperacoesBancariasControllerITTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

    private String url;
    private Conta conta;

    @BeforeEach
    void setup() {
        url = "http://localhost:" + port + "/operacoes";

        // Criando um cliente PF
        Cliente clientePF = Cliente.builder()
                .documento("12345678910")
                .nome("Cliente PF")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .status(StatusCliente.ATIVO)
                .tipo(TipoCliente.PF)
                .createdAt(LocalDate.now())
                .build();
        Cliente clientePF1 = Cliente.builder()
                .documento("12345678911")
                .nome("Cliente PF")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .status(StatusCliente.ATIVO)
                .tipo(TipoCliente.PF)
                .createdAt(LocalDate.now())
                .build();
        clienteRepository.saveAll(List.of(clientePF, clientePF1));

        // Criando uma conta corrente para o cliente PF
        var conta = Conta.builder()
                .numero(1L)
                .saldo(BigDecimal.ZERO)
                .cliente(clientePF)
                .createdAt(LocalDate.now())
                .tipo(TipoConta.CONTA_CORRENTE)
                .build();
        var conta1 = Conta.builder()
                .numero(2L)
                .saldo(BigDecimal.ZERO)
                .cliente(clientePF)
                .createdAt(LocalDate.now())
                .tipo(TipoConta.CONTA_CORRENTE)
                .build();
        contaRepository.saveAll(List.of(conta, conta1));
    }

    @AfterEach
    void tearDown() {
        contaRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Test
    void depositarTest() {
        // given
        final var valorDeposito = new BigDecimal("100.00");
        final var numeroConta = 1L;
        final var depositoDto = DepositoRequestDto.builder()
                .numeroConta(numeroConta)
                .valor(valorDeposito)
                .build();

        // when
        ResponseEntity<Void> response = restTemplate.postForEntity(url + "/depositar", depositoDto, Void.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Conta contaAtualizada = contaRepository.findById(numeroConta).orElseThrow();
        assertEquals(valorDeposito, contaAtualizada.getSaldo());
    }

    @Test
    void consultarSaldoTest() {
        // given
        final var numeroConta = 1L;
        final var valor = new BigDecimal("0.00");

        // when
        ResponseEntity<SaldoResponseDto> response = restTemplate.getForEntity(url + "/saldo/{numeroConta}",
                SaldoResponseDto.class, numeroConta);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(numeroConta, response.getBody().getNumeroConta());
        assertEquals(valor, response.getBody().getSaldo());
    }

    @Test
    void investirPFTest() {
        // given
        final var documentoCliente = "12345678910";
        final var pf = TipoCliente.PF;
        final var valor = BigDecimal.valueOf(500.0);
        final var valorComRendimento = valor.multiply(BigDecimal.valueOf(1.01));

        final var investimentoRequestDto = InvestimentoRequestDto.builder()
                .documentoCliente(documentoCliente)
                .valor(valor)
                .build();

        // when
        ResponseEntity<SaldoResponseDto> response = restTemplate.postForEntity(url + "/investimento",
                investimentoRequestDto, SaldoResponseDto.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pf, TipoCliente.PF);
        assertTrue(response.getBody().getNumeroConta() > 0);
        assertEquals(valorComRendimento, response.getBody().getSaldo());

    }

    @Test
    void investirPJTest() {
        // given
        final var documentoCliente = "12345678910";
        final var pj = TipoCliente.PJ;
        final var valor = BigDecimal.valueOf(500.0);
        final var valorComRendimento = valor.multiply(BigDecimal.valueOf(1.01));

        final var investimentoRequestDto = InvestimentoRequestDto.builder()
                .documentoCliente(documentoCliente)
                .valor(valor)
                .build();

        // when
        ResponseEntity<SaldoResponseDto> response = restTemplate.postForEntity(url + "/investimento",
                investimentoRequestDto, SaldoResponseDto.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pj, TipoCliente.PJ);
        assertTrue(response.getBody().getNumeroConta() > 0);
        assertEquals(valorComRendimento, response.getBody().getSaldo());

    }


    @Test
    void abrirContaPoupancaTest() {
        // given
        final var cpf = "12345678910";
        final var valor = BigDecimal.ZERO;

        // when
        ResponseEntity<SaldoResponseDto> response = restTemplate.postForEntity(url + "/abrir-conta-poupanca/{cpf}",
                null, SaldoResponseDto.class, cpf);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getNumeroConta() > 0);
        assertEquals(valor, response.getBody().getSaldo());
    }

}