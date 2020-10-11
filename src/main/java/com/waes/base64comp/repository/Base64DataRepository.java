package com.waes.base64comp.repository;

import com.waes.base64comp.model.Base64Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The Base64DataRepository class control the connections of database.
 */
@Repository
public class Base64DataRepository {

    /**
     * Use to access the data stored in database.
     */
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Insert new record into Base64Data table.
     *
     * @param id the primary key of the data.
     * @param column the column that data save (left_data/right_data).
     * @param data the base64 encode data.
     * @return the number of rows affected.
     */
    public int insertBase64Data(int id, String column, String data) {
        return jdbcTemplate.update("insert into Base64Data (id, " + column + ")"
                        + " values (?, ?)",
                new Object[] {id, data});
    }

    /**
     * Update record in Base64Data table by id.
     *
     * @param id the primary key of the data.
     * @param column the column that data update into (left_data/right_data).
     * @param data the base64 encode data.
     * @return the number of rows affected.
     */
    public int updateBase64Data(int id, String column, String data) {
        return jdbcTemplate.update("update Base64Data set " + column + " = ?"
                        + " where id = ?",
                new Object[] {data, id});
    }

    /**
     * Get base64 encode data by id.
     *
     * @param id the primary key of the data would like to query.
     * @return Base64Data object.
     */
    public Base64Data getBase64DataById(int id) {
        try {
            return jdbcTemplate.queryForObject("select * from Base64Data where id = ?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Base64Data.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
