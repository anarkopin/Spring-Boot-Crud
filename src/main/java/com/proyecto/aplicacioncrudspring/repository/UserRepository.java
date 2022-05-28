package com.proyecto.aplicacioncrudspring.repository;


import com.proyecto.aplicacioncrudspring.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
