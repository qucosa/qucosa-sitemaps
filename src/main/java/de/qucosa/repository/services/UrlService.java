package de.qucosa.repository.services;

import de.qucosa.repository.Dao;
import de.qucosa.repository.model.Url;
import org.springframework.stereotype.Component;

@Component
public class UrlService {

    private Dao<Url> dao;

    public UrlService() {
    }

    public void setDao(Dao<Url> dao) {
        this.dao = dao;
    }
}
