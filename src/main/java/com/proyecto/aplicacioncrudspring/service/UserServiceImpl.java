package com.proyecto.aplicacioncrudspring.service;

import com.proyecto.aplicacioncrudspring.dto.ChangePasswordForm;
import com.proyecto.aplicacioncrudspring.entities.User;
import com.proyecto.aplicacioncrudspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) throws Exception {
        if (checkUsernameAvailable(user) && checkPasswordValid(user)){
            user = userRepository.save(user);
        }
        return user;
    }


    private boolean checkUsernameAvailable(User user) throws Exception {
        Optional<User> userFound = userRepository.findByUsername(user.getUsername());
        if (userFound.isPresent()) {
            throw new Exception("Username no disponible");
        }
        return true;
    }

    private boolean checkPasswordValid(User user) throws Exception {
        if (user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {
            throw new Exception("Confirm es obligatorio");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new Exception("Password y Confirm Password no son iguales");
        }
        return true;
    }

    @Override
    public User getUserById(Long id) throws Exception {
        return userRepository.findById(id).orElseThrow(() -> new Exception("El usuario no existe"));
    }

    /*
    Primero recibe un usuario y luego queremos indicar que vamos a actualizar el usuario recibido
    fromUser a toUser
     */
    @Override
    public User updateUser(User fromUser) throws Exception {
        User toUser = getUserById(fromUser.getId());
        mapUser(fromUser, toUser);
        return userRepository.save(toUser);
    }

    @Override
    public void deteleteUser(Long id) throws Exception {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public User changePassword(ChangePasswordForm form) throws Exception {
        User user = getUserById(form.getId());

        if(!user.getPassword().equals(form.getCurrentPassword())){
            throw new Exception ("Current Password Invalido");
        }
        if( user.getPassword().equals(form.getNewPassword())) {
            throw new Exception("Nuevo password debe ser diferente al actual");
        }
        if ( !form.getNewPassword().equals(form.getConfirmPassword()) ){
            throw new Exception("Nuevo password y current password no coinciden");
        }

        user.setPassword(form.getNewPassword());
        user = userRepository.save(user);
        return user;
    }

    /**
     * Ponemos todo menos el password
     * @param from
     * @param to
     */
    protected void mapUser(User from,User to) {
        to.setUsername(from.getUsername());
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setEmail(from.getEmail());
        to.setRoles(from.getRoles());
        //to.setPassword(from.getPassword());

    }




}
