package edu.infnet.almoxarifado_servicos.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Item {
    private Long id;
    private String nome;
    private String codigo;
    private Integer quantidade;

    public Item(Long id, String nome, String codigo, Integer quantidade) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código inválido");
        }
        if (quantidade == null || quantidade < 0) {
            throw new IllegalArgumentException("Quantidade inválida");
        }
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.quantidade = quantidade;
    }
}