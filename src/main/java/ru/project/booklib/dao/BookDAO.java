package ru.project.booklib.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.project.booklib.models.Book;
import ru.project.booklib.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Book> index() {
        return jdbcTemplate.query("select id, name, author, year from book", new BeanPropertyRowMapper<>(Book.class));
    }
    public void save(Book book) {;
        jdbcTemplate.update("insert into Book(name, author, year) values(?,?,?)", book.getName(), book.getAuthor(), book.getYear());
    }

    public Book show(int id) {
        return jdbcTemplate.query("SELECT id, name, author, year, person_id from book where id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Book.class))
                .stream().findFirst().orElse(null);
    }

    public void update (int id, Book updatedBook) {
        jdbcTemplate.update("update book set name=?, author=?, year=? where id=?",
                updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void assignBook(Book book) {
        jdbcTemplate.update("update book set person_id=? where id=?",book.getPerson_id(), book.getId());
    }

    public List<Person> findUser(int id) {
        return jdbcTemplate.query("select person.name from book left join person on person.id=book.person_id where book.id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class));
    }

    public void freeBook(Book book) {
        jdbcTemplate.update("UPDATE book SET person_id = NULL WHERE id = ?", book.getId());
    }
    public void delete (int id) {
        jdbcTemplate.update("delete from book where id=?", id);
    }
}
