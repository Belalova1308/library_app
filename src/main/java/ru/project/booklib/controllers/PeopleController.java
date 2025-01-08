package ru.project.booklib.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.project.booklib.dao.PersonDAO;
import ru.project.booklib.models.Book;
import ru.project.booklib.models.Person;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private  final PersonDAO personDAO;

    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        boolean hasBooks = personDAO.hasBooks(id);
        System.out.println(hasBooks);
        if(hasBooks){
            List<Book> personBooks = personDAO.showBooks(id);
            model.addAttribute("personBooks", personBooks);
        }


        model.addAttribute("person", personDAO.show(id));
        model.addAttribute("hasBooks", hasBooks);
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person")  @Valid Person person,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "people/new";
        }
        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,  @PathVariable("id") int id){
        if(bindingResult.hasErrors()){
            return "people/edit";
        }
        personDAO.update(id, person);
        return "redirect:/people";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personDAO.delete(id);
        return "redirect:/people";
    }
}
