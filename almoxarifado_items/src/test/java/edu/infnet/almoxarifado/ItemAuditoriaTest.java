package edu.infnet.almoxarifado;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import edu.infnet.almoxarifado.models.Item;
import edu.infnet.almoxarifado.repositories.ItemRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
public class ItemAuditoriaTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private TransactionTemplate transactionTemplate;

    @BeforeEach
    public void setUp() {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Test
    public void deveRegistrarHistoricoAoAlterarItem() {
        Long itemId = transactionTemplate.execute(status -> {
            Item item = itemRepository.save(Item.builder()
                    .nome("Item Teste")
                    .codigo("IT-001")
                    .quantidade(10)
                    .quantidadeMinima(1)
                    .dataCriacao(LocalDateTime.now())
                    .build());
            return item.getId();
        });

        transactionTemplate.executeWithoutResult(status -> {
            Item item = itemRepository.findById(itemId).orElseThrow();
            item.setNome("Novo Nome");
            itemRepository.save(item);
        });

        transactionTemplate.executeWithoutResult(status -> {
            AuditReader reader = AuditReaderFactory.get(entityManager);

            List<Number> revisoes = reader.getRevisions(Item.class, itemId);
            assertEquals(2, revisoes.size());

            Item versaoAntiga = reader.find(Item.class, itemId, revisoes.get(0));
            assertEquals("Item Teste", versaoAntiga.getNome());

            Item versaoNova = reader.find(Item.class, itemId, revisoes.get(1));
            assertEquals("Novo Nome", versaoNova.getNome());
        });
    }
}