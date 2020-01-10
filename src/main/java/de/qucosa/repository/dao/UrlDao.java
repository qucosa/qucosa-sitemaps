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
import de.qucosa.repository.model.Url;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class UrlDao<T extends Url> implements Dao<Url> {

    private Connection connection;

    public UrlDao(Connection connection) {

        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }

        this.connection = connection;
    }

    @Override
    public Url saveAndSetIdentifier(Url object) throws SaveFailed {
        String sql = "INSERT INTO url (loc, changefreq, lastmod, priority, urlset_uri) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (loc) " +
                "DO UPDATE SET " +
                "changefreq=?, lastmod=?, priority=?, urlset_uri=? " +
                "WHERE url.loc=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, object.getLoc());
            statement.setString(2, object.getChangefreq());
            statement.setString(3, object.getLastmod());
            statement.setString(4, object.getPriority());
            statement.setString(5, object.getUrlSetUri());
            statement.setString(6, object.getChangefreq());
            statement.setString(7, object.getLastmod());
            statement.setString(8, object.getPriority());
            statement.setString(9, object.getUrlSetUri());
            statement.setString(10, object.getLoc());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SaveFailed("Cannot save url.");
            }



            statement.close();
        } catch (SQLException e) {
            throw new SaveFailed("SQL-ERROR: Cannot save url.", e);
        }

        return object;
    }

    @Override
    public Collection<Url> saveAndSetIdentifier(Collection<Url> objects) throws SaveFailed {
        return null;
    }

    @Override
    public Url update(Url object) throws UpdateFailed {
        return null;
    }

    @Override
    public Collection<Url> update(Collection<Url> objects) throws UpdateFailed {
        return null;
    }

    @Override
    public Collection<Url> findAll() throws NotFound {
        return null;
    }

    @Override
    public Url findById(String id) throws NotFound {
        return null;
    }

    @Override
    public Collection<Url> findRowsByPropertyAndValue(String property, String value) throws NotFound {
        return null;
    }

    @Override
    public Url findRowByPropertyAndValue(String property, String value) throws NotFound {
        return null;
    }

    @Override
    public Url findByMultipleValues(String clause, String... values) throws NotFound {
        return null;
    }

    @Override
    public Collection<Url> findRowsByMultipleValues(String clause, String... values) throws NotFound {
        return null;
    }

    @Override
    public Collection<Url> findLastRowsByProperty(String property, int limit) throws NotFound {
        return null;
    }

    @Override
    public Collection<Url> findFirstRowsByProperty(String property, int limit) throws NotFound {
        return null;
    }

    @Override
    public void delete() throws DeleteFailed {

    }

    @Override
    public void delete(String ident) throws DeleteFailed {

    }

    @Override
    public void delete(Url object) throws DeleteFailed {

    }
}
