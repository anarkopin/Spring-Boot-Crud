package com.proyecto.aplicacioncrudspring.service;

import com.proyecto.aplicacioncrudspring.dto.ChangePasswordForm;
import com.proyecto.aplicacioncrudspring.entities.User;

//spring trabaja de manera interna en procesar la implementacion a pesar de que tu llames al servicio en un primer moemento y no la implementacion
public interface UserService{

    public Iterable<User> getAllUsers();
    public User createUser(User user) throws Exception;
    public User getUserById(Long id) throws Exception;
    public User updateUser(User user) throws Exception;
    public void deteleteUser(Long id) throws Exception;
    public User changePassword(ChangePasswordForm form) throws Exception;
    public boolean loggedUserHasRole(String role);

}

