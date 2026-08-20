package edu.infnet.almoxarifado.service;

import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

import edu.infnet.almoxarifado.models.Item;
import edu.infnet.almoxarifado.repositories.ItemRepository;

@Service
public class ItemHistoricoService {
    private final ItemRepository revisionRepository;

    public ItemHistoricoService(ItemRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    public Revisions<Integer, Item> obterHistoricoItem(Long itemId) {
        return revisionRepository.findRevisions(itemId);
    }
}