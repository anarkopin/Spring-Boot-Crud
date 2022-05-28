package com.proyecto.aplicacioncrudspring.repository;

import com.proyecto.aplicacioncrudspring.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

}
