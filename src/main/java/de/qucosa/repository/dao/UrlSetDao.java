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
import de.qucosa.repository.model.UrlSet;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
        String sql = "INSERT INTO urlset (uri, lastmod, loc) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (uri) " +
                "DO NOTHING";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, object.getUri());
            ps.setString(2, object.getLastmod());
            ps.setString(3, object.getLoc());
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SaveFailed("Cannot save urlset.");
            }

            ps.close();
        } catch (SQLException e) {
            throw new SaveFailed("SQL-ERROR: Cannot save urlset.", e);
        }

        return object;
    }

    @Override
    public Collection<UrlSet> saveAndSetIdentifier(Collection<UrlSet> objects) {
        return null;
    }

    @Override
    public UrlSet update(UrlSet object) {
        return null;
    }

    @Override
    public Collection<UrlSet> update(Collection<UrlSet> objects) {
        return null;
    }

    @Override
    public Collection<UrlSet> findAll() {
        String sql = "SELECT * FROM urlset";
        Collection<UrlSet> urlSets = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                UrlSet urlset = new UrlSet();
                urlset.setUri(resultSet.getString("uri"));
                urlset.setLastmod(resultSet.getString("lastmod"));
                urlset.setLoc(resultSet.getString("loc"));
                urlSets.add(urlset);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return urlSets;
    }

    @Override
    public UrlSet findById(String id) {
        return null;
    }

    @Override
    public Collection<UrlSet> findRowsByPropertyAndValue(String property, String value) {
        return null;
    }

    @Override
    public UrlSet findRowByPropertyAndValue(String property, String value) throws NotFound {
        UrlSet urlset = new UrlSet();
        String sql = "SELECT * FROM urlset where " + property + " = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, value);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                urlset.setUri(resultSet.getString("uri"));
                urlset.setLastmod(resultSet.getString("lastmod"));
                urlset.setLoc(resultSet.getString("loc"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new NotFound("SQL ERROR: Canot found urlset.", e);
        }

        return urlset;
    }

    @Override
    public UrlSet findByMultipleValues(String clause, String... values) {
        return null;
    }

    @Override
    public Collection<UrlSet> findRowsByMultipleValues(String clause, String... values) {
        return null;
    }

    @Override
    public Collection<UrlSet> findLastRowsByProperty(String property, int limit) {
        return null;
    }

    @Override
    public Collection<UrlSet> findFirstRowsByProperty(String property, int limit) {
        return null;
    }

    @Override
    public void delete() {

    }

    @Override
    public void delete(String column, String value) throws DeleteFailed {
        String sql = "DELETE FROM urlset where " + column + "=?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, value);
            int deletedRows = statement.executeUpdate();

            if (deletedRows == 0) {
                throw new DeleteFailed("Cannot delete urlset.");
            }

            statement.close();
        } catch (SQLException e) {
            throw new DeleteFailed("SQL-ERROR: Cannot delete urlset.", e);
        }
    }

    @Override
    public void delete(UrlSet object) {

    }
}
