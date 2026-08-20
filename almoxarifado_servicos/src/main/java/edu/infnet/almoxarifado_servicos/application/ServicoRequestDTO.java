package edu.infnet.almoxarifado_servicos.application;

import java.util.List;

import edu.infnet.almoxarifado_servicos.domain.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ServicoRequestDTO(
                @NotBlank(message = "Identificador do serviço é obrigatório") @Size(max = 50, message = "Identificador deve ter no máximo 50 caracteres") String identificador,

                @NotBlank(message = "Descrição do serviço é obrigatória") @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres") String descricao,

                @NotEmpty(message = "Informe ao menos um item para o serviço") List<Item> items) {
}
