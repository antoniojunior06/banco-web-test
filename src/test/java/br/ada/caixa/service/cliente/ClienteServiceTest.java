package br.ada.caixa.service.cliente;

import br.ada.caixa.dto.request.RegistrarClientePFRequestDto;
import br.ada.caixa.dto.response.ClienteResponseDto;
import br.ada.caixa.dto.response.RegistrarClienteResponseDto;
import br.ada.caixa.entity.Cliente;
import br.ada.caixa.entity.Conta;
import br.ada.caixa.entity.TipoCliente;
import br.ada.caixa.entity.TipoConta;
import br.ada.caixa.enums.StatusCliente;
import br.ada.caixa.respository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import service.cliente.ClienteService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClienteService service;

    @Test
    void registrarClientePFTest() {
        // given
        RegistrarClientePFRequestDto clienteDto = mock(RegistrarClientePFRequestDto.class);
        Cliente cliente = mock(Cliente.class);
        TipoCliente pf = TipoCliente.PF;
        StatusCliente ativo = StatusCliente.ATIVO;

        given(modelMapper.map(clienteDto, Cliente.class)).willReturn(cliente);
        given(repository.save(cliente)).willReturn(cliente);
        given(cliente.getTipo()).willReturn(pf);
        given(cliente.getStatus()).willReturn(ativo);

        // when
        RegistrarClienteResponseDto actual = service.registrarPF(clienteDto);

        // then
        verify(modelMapper).map(clienteDto, Cliente.class);
        verify(repository).save(cliente);
        assertEquals(pf, cliente.getTipo());
        assertEquals(ativo, cliente.getStatus());
        assertNotNull(actual);
    }

    @Test
    void registrarClientePJTest() {
        // given
        RegistrarClientePFRequestDto clienteDto = mock(RegistrarClientePFRequestDto.class);
        Cliente cliente = mock(Cliente.class);
        TipoCliente pj = TipoCliente.PJ;
        StatusCliente ativo = StatusCliente.ATIVO;

        given(modelMapper.map(clienteDto, Cliente.class)).willReturn(cliente);
        given(repository.save(cliente)).willReturn(cliente);
        given(cliente.getTipo()).willReturn(pj);
        given(cliente.getStatus()).willReturn(ativo);

        // when
        RegistrarClienteResponseDto actual = service.registrarPF(clienteDto);

        // then
        verify(modelMapper).map(clienteDto, Cliente.class);
        verify(repository).save(cliente);
        assertEquals(pj, cliente.getTipo());
        assertEquals(ativo, cliente.getStatus());
        assertNotNull(actual);
    }

    @Test
    void listarTodosPFSemTipoCliente() {
        // given
        List<Cliente> clientes = new ArrayList<>();
        Cliente cliente = mock(Cliente.class);
        clientes.add(cliente);
        ClienteResponseDto clienteResponseDto = new ClienteResponseDto();
        clienteResponseDto.setTipo(TipoCliente.PF.name());

        given(repository.findAll()).willReturn(clientes);
        given(cliente.getTipo()).willReturn(TipoCliente.PF);
        given(modelMapper.map(cliente, ClienteResponseDto.class)).willReturn(clienteResponseDto);

        // when
        List<ClienteResponseDto> actual = service.listarTodos();

        // then
        assertEquals(1, actual.size());
        assertEquals(TipoCliente.PF.name(), actual.get(0).getTipo());
        verify(repository, times(1)).findAll();
        verify(modelMapper, times(1)).map(cliente, ClienteResponseDto.class);
    }

    @Test
    void listarTodosPJSemTipoCliente() {
        // given
        List<Cliente> clientes = new ArrayList<>();
        Cliente cliente = mock(Cliente.class);
        clientes.add(cliente);
        ClienteResponseDto clienteResponseDto = new ClienteResponseDto();
        clienteResponseDto.setTipo(TipoCliente.PJ.name());

        given(repository.findAll()).willReturn(clientes);
        given(cliente.getTipo()).willReturn(TipoCliente.PJ);
        given(modelMapper.map(cliente, ClienteResponseDto.class)).willReturn(clienteResponseDto);

        // when
        List<ClienteResponseDto> actual = service.listarTodos();

        // then
        assertEquals(1, actual.size());
        assertEquals(TipoCliente.PJ.name(), actual.get(0).getTipo());
        verify(repository, times(1)).findAll();
        verify(modelMapper, times(1)).map(cliente, ClienteResponseDto.class);
    }

    @Test
    void listarTodosTiposTest() {
        // given
        List<Cliente> clientes = new ArrayList<>();

        Cliente clientePF = mock(Cliente.class);
        clientes.add(clientePF);

        Cliente clientePJ = mock(Cliente.class);
        clientes.add(clientePJ);

        ClienteResponseDto clienteResponseDtoPF = new ClienteResponseDto();
        clienteResponseDtoPF.setTipo(TipoCliente.PF.name());

        ClienteResponseDto clienteResponseDtoPJ = new ClienteResponseDto();
        clienteResponseDtoPJ.setTipo(TipoCliente.PJ.name());

        given(clientePF.getTipo()).willReturn(TipoCliente.PF);
        given(clientePJ.getTipo()).willReturn(TipoCliente.PJ);
        given(repository.findAll()).willReturn(clientes);
        given(modelMapper.map(clientePF, ClienteResponseDto.class)).willReturn(clienteResponseDtoPF);
        given(modelMapper.map(clientePJ, ClienteResponseDto.class)).willReturn(clienteResponseDtoPJ);

        // when
        List<ClienteResponseDto> actual = service.listarTodos();

        // Verificando o resultado
        assertEquals(2, actual.size());
        assertTrue(actual.stream().anyMatch(c -> c.getTipo().equals(TipoCliente.PF.name())));
        assertTrue(actual.stream().anyMatch(c -> c.getTipo().equals(TipoCliente.PJ.name())));
        verify(repository, times(1)).findAll();
        verify(modelMapper, times(1)).map(clientePF, ClienteResponseDto.class);
        verify(modelMapper, times(1)).map(clientePJ, ClienteResponseDto.class);
    }

}