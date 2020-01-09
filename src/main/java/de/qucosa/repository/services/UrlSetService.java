package de.qucosa.repository.services;

import de.qucosa.repository.Dao;
import de.qucosa.repository.exceptions.DeleteFailed;
import de.qucosa.repository.exceptions.SaveFailed;
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

    public UrlSet saveUrlSet(UrlSet urlSet) throws SaveFailed {
        return dao.saveAndSetIdentifier(urlSet);
    }

    public void deleteUrlSet(String urlset) throws DeleteFailed {
        dao.delete(urlset);
    }
}
