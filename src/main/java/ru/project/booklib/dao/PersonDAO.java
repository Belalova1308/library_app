package ru.project.booklib.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.project.booklib.models.Book;
import ru.project.booklib.models.Person;

import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("select * from person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query("select * from person where id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream().findFirst().orElse(null);
    }

    public void save(Person person) {;
        jdbcTemplate.update("insert into person(name, birthyear) values(?,?)", person.getName(), person.getBirthyear());

    }

    public void update (int id, Person updatedPerson) {
        jdbcTemplate.update("update person set name=?, birthyear=? where id=?",
                updatedPerson.getName(), updatedPerson.getBirthyear(), id);
    }
    public void delete (int id) {
        jdbcTemplate.update("delete from person where id=?", id);
    }

    public boolean hasBooks (int id) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from book where person_id=?",
                new Object[]{id},
                Integer.class);
        System.out.println(count);
        return count > 0;
    }

    public List<Book> showBooks (int id) {
        return jdbcTemplate.query("select book.name, book.author, book.year from person left join book on person.id=book.person_id where person.id=?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
