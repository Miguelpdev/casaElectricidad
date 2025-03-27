package com.egg.electricidad.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.egg.electricidad.enums.Rol;
import com.egg.electricidad.exception.MiException;
import com.egg.electricidad.model.Usuario;
import com.egg.electricidad.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para registrar un usuario
    @Transactional
    public void registrar(String nombre, String apellido, String email, String password, String password2)
            throws MiException {

        validar(nombre, email, password, password2);// llamo al método validar lo necesario

        Usuario usuario = new Usuario();// Instancio un objeto del tipo Usuario
        usuario.setNombre(nombre);// Seteo el atributo, con el valor recibido como parámetro
        usuario.setEmail(email);// Seteo el atributo, con el valor recibido como parámetro
        usuario.setApellido(apellido);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));// Seteo el atributo, con el valor recibido
                                                                          // como parámetro
        usuario.setRol(Rol.USER);// Seteo el atributo, con el valor recibido como parámetro

        // if (archivo != null && !archivo.isEmpty()) { // Si el archivo existe y no
        // está vacío
        // Imagen imagen = imagenServicio.guardar(archivo); // guardo la imagen del
        // usuario
        // usuario.setImagen(imagen); // seteo la imagen al usuario
        // }

        usuarioRepository.save(usuario); // persisto el dato en mi BBDD
    }

    private void validar(String nombre, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacío");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.buscarPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }

    }

}
