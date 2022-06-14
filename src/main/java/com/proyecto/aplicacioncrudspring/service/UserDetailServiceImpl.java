package com.proyecto.aplicacioncrudspring.service;

import com.proyecto.aplicacioncrudspring.entities.Role;
import com.proyecto.aplicacioncrudspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    //carga el usuario desde la base de datos
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //buscar nombre de usuario en nuestra base de datos

        com.proyecto.aplicacioncrudspring.entities.User appUser =
                userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Login Username Invalido"));

        Set grantList = new HashSet();

        for (Role roles: appUser.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roles.getDescription());
            grantList.add(grantedAuthority);

        }

        //user de spring security
        UserDetails user = (UserDetails) new User(username, appUser.getPassword(), grantList);

        return user;
    }
}
