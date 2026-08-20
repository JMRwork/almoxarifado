package edu.infnet.almoxarifado_servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.infnet.almoxarifado_servicos.application.ServicoUseCase;
import edu.infnet.almoxarifado_servicos.domain.Item;
import edu.infnet.almoxarifado_servicos.domain.Servico;
import edu.infnet.almoxarifado_servicos.infrastructure.ServicoRepository;
import edu.infnet.almoxarifado_servicos.interfaces.ItemClient;

class ServicoCrudUseCaseTest {

    private ServicoRepository repositoryMock;
    private ItemClient itemClientMock;
    private ServicoUseCase useCase;

    @BeforeEach
    void configurar() {
        repositoryMock = Mockito.mock(ServicoRepository.class);
        itemClientMock = Mockito.mock(ItemClient.class);
        useCase = new ServicoUseCase(repositoryMock, itemClientMock);
    }

    @Test
    void deveCriarServico() {
        when(repositoryMock.save(Mockito.any(Servico.class))).thenAnswer(invocation -> new Servico(
                1L,
                invocation.getArgument(0, Servico.class).getIdentificador(),
                invocation.getArgument(0, Servico.class).getDescricao(),
                invocation.getArgument(0, Servico.class).getItems()));

        Servico servico = useCase.criar(
                "srv-01",
                "Atendimento ao almoxarifado",
                List.of(new Item(1L, "Item 1", "ITM-01", 5), new Item(2L, "Item 2", "ITM-02", 3)),
                List.of(new Item(1L, "Item 1", "ITM-01", 5), new Item(2L, "Item 2", "ITM-02", 3)));

        assertThat(servico).isNotNull();
        assertThat(servico.getIdentificador()).isEqualTo("srv-01");
        assertThat(servico.getDescricao()).isEqualTo("Atendimento ao almoxarifado");
        assertThat(servico.getItems()).containsExactly(new Item(1L, "Item 1", "ITM-01", 5),
                new Item(2L, "Item 2", "ITM-02", 3));
    }

    @Test
    void deveListarTodosOsServicos() {
        Servico servico = servicoExistente();
        when(repositoryMock.findAll()).thenAnswer(invocation -> List.of(servico));

        assertThat(useCase.listarTodos()).isNotEmpty();
    }

    @Test
    void deveBuscarServicoPorId() {
        Servico servico = servicoExistente();
        when(repositoryMock.findById(servico.getId())).thenAnswer(invocation -> {
            return Optional.of(servico);
        });

        assertThat(useCase.buscarPorId(servico.getId())).isNotNull();
    }

    @Test
    void deveAtualizarServico() {
        Servico servico = servicoExistente();
        when(repositoryMock.findById(servico.getId())).thenReturn(Optional.of(servico));
        when(repositoryMock.existsById(Mockito.anyLong())).thenAnswer(invocation -> invocation.getArgument(0) != null);
        when(repositoryMock.save(Mockito.any(Servico.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(itemClientMock.buscarPorId(Mockito.anyLong())).thenAnswer(invocation -> {
            Long itemId = invocation.getArgument(0);
            if (itemId == 1L) {
                return new Item(1L, "Item 1", "ITM-01", 5);
            } else if (itemId == 2L) {
                return new Item(2L, "Item 2", "ITM-02", 3);
            } else if (itemId == 3L) {
                return new Item(3L, "Item 3", "ITM-03", 10);
            }
            return null;
        });

        Servico atualizado = useCase.atualizar(servico.getId(), "srv-01", "Atualizado",
                List.of(new Item(1L, "Item 1", "ITM-01", 5), new Item(2L, "Item 2", "ITM-02", 3)),
                List.of(new Item(1L, "Item 1", "ITM-01", 10), new Item(2L, "Item 2", "ITM-02", 3)));

        assertThat(atualizado.getDescricao()).isEqualTo("Atualizado");
    }

    @Test
    void deveDeletarServico() {
        Servico servico = servicoExistente();
        when(repositoryMock.existsById(servico.getId())).thenReturn(true);

        useCase.deletar(servico.getId());

        verify(repositoryMock).deleteById(servico.getId());
    }

    private Servico servicoExistente() {
        return new Servico(1L, "srv-01", "Atendimento ao almoxarifado", List.of(
                new Item(1L, "Item 1", "ITM-01", 5),
                new Item(2L, "Item 2", "ITM-02", 3)));
    }
}
