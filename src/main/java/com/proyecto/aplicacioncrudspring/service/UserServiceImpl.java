package com.proyecto.aplicacioncrudspring.service;

import com.proyecto.aplicacioncrudspring.Exception.UserOrIdNotFound;
import com.proyecto.aplicacioncrudspring.dto.ChangePasswordForm;
import com.proyecto.aplicacioncrudspring.entities.User;
import com.proyecto.aplicacioncrudspring.repository.UserRepository;
import com.proyecto.aplicacioncrudspring.util.PassGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) throws Exception {
        if (checkUsernameAvailable(user) && checkPasswordValid(user)){
            String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
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
    public User getUserById(Long id) throws UserOrIdNotFound {
        return userRepository.findById(id).orElseThrow(() -> new UserOrIdNotFound("El id del usuario no existe"));
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deteleteUser(Long id) throws UserOrIdNotFound {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public User changePassword(ChangePasswordForm form) throws Exception {
        User user = getUserById(form.getId());

        if(!isLoggedUserADMIN() && !bCryptPasswordEncoder.matches(form.getCurrentPassword(),user.getPassword())){
            throw new Exception ("Current Password Invalido");
        }
        if( bCryptPasswordEncoder.matches(form.getNewPassword(),user.getPassword()) ) {
            throw new Exception("Nuevo password debe ser diferente al actual");
        }
        if ( !form.getNewPassword().equals(form.getConfirmPassword()) ){
            throw new Exception("Nuevo password y current password no coinciden");
        }
        String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());
        user.setPassword(encodePassword);
        user = userRepository.save(user);
        return user;
    }

    public boolean isLoggedUserADMIN(){
        return loggedUserHasRole("ROLE_ADMIN");
    }

    public boolean loggedUserHasRole(String role) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails loggedUser = null;
        Object roles = null;
        if (principal instanceof UserDetails) {
            loggedUser = (UserDetails) principal;

            roles = loggedUser.getAuthorities().stream()
                    .filter(x -> role.equals(x.getAuthority() ))
                    .findFirst().orElse(null); //loggedUser = null;
        }
        return roles != null ?true :false;
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
