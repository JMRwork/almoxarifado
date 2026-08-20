package edu.infnet.almoxarifado_servicos.interfaces;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.infnet.almoxarifado_servicos.application.ServicoRequestDTO;
import edu.infnet.almoxarifado_servicos.application.ServicoResponseDTO;
import edu.infnet.almoxarifado_servicos.application.ServicoUseCase;
import edu.infnet.almoxarifado_servicos.domain.Item;
import edu.infnet.almoxarifado_servicos.domain.Servico;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoUseCase useCase;

    public ServicoController(ServicoUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> criar(@Valid @RequestBody ServicoRequestDTO request) {
        List<Item> itemsCadastrados = useCase.validarItems(request.items());
        Servico servico = useCase.criar(request.identificador(), request.descricao(), request.items(),
                itemsCadastrados);
        return ResponseEntity.status(HttpStatus.CREATED).body(ServicoResponseDTO.from(servico));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(useCase.listarTodos().stream().map(ServicoResponseDTO::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ServicoResponseDTO.from(useCase.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody ServicoRequestDTO request) {
        List<Item> itemsCadastrados = useCase.validarItems(request.items());
        Servico servico = useCase.atualizar(id, request.identificador(), request.descricao(), request.items(),
                itemsCadastrados);
        return ResponseEntity.ok(ServicoResponseDTO.from(servico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        useCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
