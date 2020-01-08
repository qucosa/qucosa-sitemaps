package de.qucosa.repository.services;

import de.qucosa.repository.Dao;
import de.qucosa.repository.model.UrlSet;
import org.springframework.stereotype.Component;

@Component
public class UrlSetService {
    private Dao<UrlSet> dao;

    public UrlSetService() {
    }

    public void setDao(Dao<UrlSet> dao) {
        this.dao = dao;
    }
}
