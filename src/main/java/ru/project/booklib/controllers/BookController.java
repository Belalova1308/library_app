package ru.project.booklib.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.project.booklib.dao.BookDAO;
import ru.project.booklib.dao.PersonDAO;
import ru.project.booklib.models.Book;
import ru.project.booklib.models.Person;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        List<Person> people = new ArrayList<>();
        people = personDAO.index();
        Person owner = null;
        List<Person> owners = bookDAO.findUser(id);
        if (!owners.isEmpty()) {
            owner = owners.get(0);
        }
        model.addAttribute("book", bookDAO.show(id));
        model.addAttribute("people", people);
        model.addAttribute("owner", owner);
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@ModelAttribute("book") Book book, @PathVariable("id") int id) {
        bookDAO.assignBook(book);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/freebook")
    public String freebook(@ModelAttribute("book") Book book, @PathVariable("id") int id) {
        bookDAO.freeBook(book);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookDAO.delete(id);
        return "redirect:/books";
    }
}
