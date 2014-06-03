/**
 * 
 */
package com.mustbeopen.main;

/**
 * @author mustbeopen
 * 
 * Clase con método main para poder verificar el uso de JdbcTemplate.
 *
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.mustbeopen.domain.Customer;

public class Main {

    public static void main(String args[]) {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUsername("sa");
        dataSource.setUrl("jdbc:h2:mem");
        dataSource.setPassword("");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("Creando tablas");
        jdbcTemplate.execute("drop table customers if exists");
        jdbcTemplate.execute("create table customers(" +
                "id serial, first_name varchar(255), last_name varchar(255))");

        String[] names = "Ariel Gomez;Emanuel Pereyra;Gonzalo Arua;Ariel Soriano".split(";");
        for (String fullname : names) {
            String[] name = fullname.split(" ");
            System.out.printf("Creando cliente %s %s\n", name[0], name[1]);
            jdbcTemplate.update(
                    "INSERT INTO customers(first_name,last_name) values(?,?)",
                    name[0], name[1]);
        }

        System.out.println("Buscando cliente cuyo nombre sea = 'Lionel':");
        List<Customer> results = jdbcTemplate.query(
                "select * from customers where first_name = ?", new Object[] { "Ariel" },
                new RowMapper<Customer>() {
                    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Customer(rs.getLong("id"), rs.getString("first_name"),
                                rs.getString("last_name"));
                    }
                });

        for (Customer customer : results) {
            System.out.println(customer);
        }
    }
}