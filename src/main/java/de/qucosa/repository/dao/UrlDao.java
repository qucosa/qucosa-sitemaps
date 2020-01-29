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


package de.qucosa.repository.dao;

import de.qucosa.repository.Dao;
import de.qucosa.repository.exceptions.DeleteFailed;
import de.qucosa.repository.exceptions.NotFound;
import de.qucosa.repository.exceptions.SaveFailed;
import de.qucosa.repository.model.Url;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    public Collection<Url> saveAndSetIdentifier(Collection<Url> objects) {
        return null;
    }

    @Override
    public Url update(Url object) {
        return null;
    }

    @Override
    public Collection<Url> update(Collection<Url> objects) {
        return null;
    }

    @Override
    public Collection<Url> findAll() throws NotFound {
        Collection<Url> list = new ArrayList<>();
        String sql = "SELECT * FROM url";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                list.add(getUrlObject(resultSet));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new NotFound("SQL-ERROR: Cannot found url entries.", e);
        }

        return list;
    }

    @Override
    public Url findById(String id) {
        return null;
    }

    @Override
    public Collection<Url> findRowsByPropertyAndValue(String property, String value) throws NotFound {
        Collection<Url> list = new ArrayList<>();
        String sql = "SELECT * FROM url WHERE " + property + "=?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, value);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                list.add(getUrlObject(resultSet));
            }

            if (list.size() == 0) {
                throw new NotFound("Url collections size is 0.");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new NotFound("SQL-ERROR: Cannot found url collection.", e);
        }

        return list;
    }

    @Override
    public Url findRowByPropertyAndValue(String property, String value) throws NotFound {
        Url url = new Url();
        String sql = "SELECT * FROM url WHERE " + property + "=?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, value);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                url.setLoc(resultSet.getString("loc"));
                url.setChangefreq(resultSet.getString("changefreq"));
                url.setLastmod(resultSet.getString("lastmod"));
                url.setPriority(resultSet.getString("priority"));
                url.setUrlSetUri(resultSet.getString("urlset_uri"));
            }

            if (url.getLoc() == null || url.getLoc().isEmpty()) {
                throw new NotFound("Cannot found url.");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new NotFound("SQL-ERROR: Cannot found url.", e);
        }

        return url;
    }

    @Override
    public Url findByMultipleValues(String clause, String... values) {
        return null;
    }

    @Override
    public Collection<Url> findRowsByMultipleValues(String clause, String... values) {
        return null;
    }

    @Override
    public Collection<Url> findLastRowsByProperty(String property, int limit) {
        return null;
    }

    @Override
    public Collection<Url> findFirstRowsByProperty(String property, int limit) {
        return null;
    }

    @Override
    public void delete() {

    }

    @Override
    public void delete(String column, String value) {
    }

    @Override
    public void delete(Url object) throws DeleteFailed {
        String sql = "DELETE FROM url WHERE loc = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, object.getLoc());
            int deletedRows = statement.executeUpdate();

            if (deletedRows == 0) {
                throw new DeleteFailed("Cannot delete url.");
            }

            statement.close();
        } catch (SQLException e) {
            throw new DeleteFailed("SQL-ERROR: Cannot delete url.", e);
        }
    }

    private Url getUrlObject(ResultSet resultSet) throws SQLException {
        Url url = new Url();
        url.setLoc(resultSet.getString("loc"));
        url.setChangefreq(resultSet.getString("changefreq"));
        url.setLastmod(resultSet.getString("lastmod"));
        url.setPriority(resultSet.getString("priority"));
        url.setUrlSetUri(resultSet.getString("urlset_uri"));
        return url;
    }
}
