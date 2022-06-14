package com.proyecto.aplicacioncrudspring.Exception;

public class UserOrIdNotFound extends Exception {

    /**
     *
     */
    public UserOrIdNotFound() {
        super("Usuario o Id no encontrado");
    }

    public UserOrIdNotFound(String message) {
        super(message);
    }


}

