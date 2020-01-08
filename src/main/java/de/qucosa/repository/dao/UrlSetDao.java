/*
 * Copyright (C) 2016 Saxon State and University Library Dresden (SLUB)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.qucosa.repository.dao;

import de.qucosa.repository.Dao;
import de.qucosa.repository.exceptions.DeleteFailed;
import de.qucosa.repository.exceptions.NotFound;
import de.qucosa.repository.exceptions.SaveFailed;
import de.qucosa.repository.exceptions.UpdateFailed;
import de.qucosa.repository.model.UrlSet;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.Collection;

@Repository
public class UrlSetDao<T extends UrlSet> implements Dao<UrlSet> {

    private Connection connection;

    public UrlSetDao(Connection connection) {

        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }

        this.connection = connection;
    }

    @Override
    public UrlSet saveAndSetIdentifier(UrlSet object) throws SaveFailed {
        return null;
    }

    @Override
    public Collection<UrlSet> saveAndSetIdentifier(Collection<UrlSet> objects) throws SaveFailed {
        return null;
    }

    @Override
    public UrlSet update(UrlSet object) throws UpdateFailed {
        return null;
    }

    @Override
    public Collection<UrlSet> update(Collection<UrlSet> objects) throws UpdateFailed {
        return null;
    }

    @Override
    public Collection<UrlSet> findAll() throws NotFound {
        return null;
    }

    @Override
    public UrlSet findById(String id) throws NotFound {
        return null;
    }

    @Override
    public Collection<UrlSet> findRowsByPropertyAndValue(String property, String value) throws NotFound {
        return null;
    }

    @Override
    public UrlSet findRowByPropertyAndValue(String property, String value) throws NotFound {
        return null;
    }

    @Override
    public UrlSet findByMultipleValues(String clause, String... values) throws NotFound {
        return null;
    }

    @Override
    public Collection<UrlSet> findRowsByMultipleValues(String clause, String... values) throws NotFound {
        return null;
    }

    @Override
    public Collection<UrlSet> findLastRowsByProperty(String property, int limit) throws NotFound {
        return null;
    }

    @Override
    public Collection<UrlSet> findFirstRowsByProperty(String property, int limit) throws NotFound {
        return null;
    }

    @Override
    public void delete() throws DeleteFailed {

    }

    @Override
    public void delete(String ident) throws DeleteFailed {

    }

    @Override
    public void delete(UrlSet object) throws DeleteFailed {

    }
}
