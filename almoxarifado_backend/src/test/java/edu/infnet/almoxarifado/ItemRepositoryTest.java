package edu.infnet.almoxarifado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import edu.infnet.almoxarifado.models.Item;
import edu.infnet.almoxarifado.repositories.ItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void deveSalvarEEncontrarItemPorCodigo() {
        Item item = Item.builder()
                .nome("Notebook")
                .codigo("NB-001")
                .quantidade(10)
                .quantidadeMinima(5)
                .localizacao("Estante 1")
                .build();
        itemRepository.save(item);

        Optional<Item> encontrado = itemRepository.findByCodigo("NB-001");
        assertTrue(encontrado.isPresent());
        assertEquals("Notebook", encontrado.get().getNome());
    }

    @Test
    public void deveListarItensComEstoqueBaixo() {
        // cenário de teste
        List<Item> baixos = itemRepository.findItensComEstoqueBaixo();
        assertTrue(baixos.stream().allMatch(i -> i.getQuantidade() < i.getQuantidadeMinima()));
    }
}