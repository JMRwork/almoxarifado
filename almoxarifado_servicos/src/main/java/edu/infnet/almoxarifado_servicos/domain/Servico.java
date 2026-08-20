package edu.infnet.almoxarifado_servicos.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "servico")
@Data
@NoArgsConstructor
@Builder
public class Servico {

    public Servico(Long id, String identificador, String descricao, List<Item> items) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException("Identificador inválido");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição inválida");
        }
        if (items == null) {
            throw new IllegalArgumentException("Itens são obrigatórios");
        }
        if (items.isEmpty()) {
            throw new IllegalArgumentException("O serviço deve conter pelo menos um item");
        }
        this.id = id;
        this.identificador = identificador;
        this.descricao = descricao;
        this.items = new ArrayList<>(items);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identificador;

    @Column(nullable = false)
    private String descricao;

    @ElementCollection
    @CollectionTable(name = "servico_itens", joinColumns = @JoinColumn(name = "servico_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "item_id")),
            @AttributeOverride(name = "nome", column = @Column(name = "item_nome")),
            @AttributeOverride(name = "codigo", column = @Column(name = "item_codigo")),
            @AttributeOverride(name = "quantidade", column = @Column(name = "item_quantidade"))
    })
    @Builder.Default
    private List<Item> items = new ArrayList<>();
}
