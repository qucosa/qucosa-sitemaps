package de.qucosa.repository.services;

import de.qucosa.repository.Dao;
import de.qucosa.repository.exceptions.DeleteFailed;
import de.qucosa.repository.exceptions.NotFound;
import de.qucosa.repository.exceptions.SaveFailed;
import de.qucosa.repository.model.Url;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UrlService {

    private Dao<Url> dao;

    public UrlService() {
    }

    public void setDao(Dao<Url> dao) {
        this.dao = dao;
    }

    public Url save(Url url) throws SaveFailed {
        return dao.saveAndSetIdentifier(url);
    }

    public void deleteUrl(Url url) throws DeleteFailed {
        dao.delete(url);
    }

    public Url findUrl(String column, String value) throws NotFound {
        return dao.findRowByPropertyAndValue(column, value);
    }

    public Collection<Url> findUrllist(String property, String value) throws NotFound {
        return dao.findRowsByPropertyAndValue(property, value);
    }

    public Collection<Url> findAll() throws NotFound {
        return dao.findAll();
    }
}
