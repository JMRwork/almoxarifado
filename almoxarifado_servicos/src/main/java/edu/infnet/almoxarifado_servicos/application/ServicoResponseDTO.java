package edu.infnet.almoxarifado_servicos.application;

import java.util.List;

import edu.infnet.almoxarifado_servicos.domain.Item;
import edu.infnet.almoxarifado_servicos.domain.Servico;

public record ServicoResponseDTO(Long id, String identificador, String descricao, List<Item> items) {

    public static ServicoResponseDTO from(Servico servico) {
        return new ServicoResponseDTO(
                servico.getId(),
                servico.getIdentificador(),
                servico.getDescricao(),
                servico.getItems());
    }
}
