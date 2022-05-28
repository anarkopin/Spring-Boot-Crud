package com.proyecto.aplicacioncrudspring.service;

import com.proyecto.aplicacioncrudspring.entities.User;

public interface UserService{
    public Iterable<User> getAllUsers();
}
