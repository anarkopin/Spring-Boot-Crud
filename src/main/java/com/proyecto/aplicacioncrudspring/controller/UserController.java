package com.proyecto.aplicacioncrudspring.controller;

import com.proyecto.aplicacioncrudspring.entities.User;
import com.proyecto.aplicacioncrudspring.repository.RoleRepository;
import com.proyecto.aplicacioncrudspring.repository.UserRepository;
import com.proyecto.aplicacioncrudspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;


    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/userform")
    public String getUserForm(Model model){
        model.addAttribute("userForm", new User());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("listTab","active");
        return "user-form/user-view";
    }

    /*
    *@Valid valida que se cumplan los parametros puestos en los atributos de la entidad(Size, Notblank, etc)
    *para volver a mostrar lo que ya colocamos volver a mandar el objeto que recibimos
    *
    *
     */
    @PostMapping("/userform")
    public String createUser(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab","active");
        } else {
            try {
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab","active");
                userService.createUser(user);
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage()); //Asi mandamos el error al front
                model.addAttribute("userForm", user);
                model.addAttribute("formTab","active");
                model.addAttribute("roles", roleRepository.findAll());
                model.addAttribute("userList", userService.getAllUsers());
            }

        }
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("userList", userService.getAllUsers());
        return "user-form/user-view";

    }


    @GetMapping("/edituser/{id}")
    public String getEditUserForm(Model model, @PathVariable(name="id")Long id) throws Exception{
        User userToedit = userService.getUserById(id);

        model.addAttribute("userForm", userToedit); //mandamos el usuario que ya encontramos
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("formTab","active");

        model.addAttribute("editMode", true);
        return "user-form/user-view";
    }

    @PostMapping("/edituser")
    public String editUser(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab","active");
            model.addAttribute("editMode", true);
        } else {
            try {
                userService.updateUser(user);
                model.addAttribute("listTab","active");
                model.addAttribute("listTab","active");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage()); //Asi mandamos el error al front
                model.addAttribute("userForm", user);
                model.addAttribute("formTab","active");
                model.addAttribute("roles", roleRepository.findAll());
                model.addAttribute("userList", userService.getAllUsers());
                model.addAttribute("editMode", true);
            }

        }
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("userList", userService.getAllUsers());
        return "user-form/user-view";
    }

    @GetMapping("userform/cancel")
    public String cancelEditUser(ModelMap model) {
        return "redirect:/userform";
    }

    @GetMapping("deleteuser/{id}")
    public String deleteUser(Model model, @PathVariable(name="id")Long id){
        try {
            userService.deteleteUser(id);
        } catch (Exception e) {
            model.addAttribute("listErrorMessage", e.getMessage()); //Asi mandamos el error al front

        }
        return "user-form/user-view";
    }










}
