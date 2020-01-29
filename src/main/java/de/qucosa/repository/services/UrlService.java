/*
 * Copyright 2017 Saxon State and University Library Dresden (SLUB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
