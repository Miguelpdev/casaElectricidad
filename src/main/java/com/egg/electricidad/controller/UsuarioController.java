package com.egg.electricidad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.egg.electricidad.exception.MiException;
import com.egg.electricidad.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registrar") // Acá es donde realizamos el mapeo
    public String registrar() {
        return "registrousuario.html"; // Acá es que retornamos con el método.
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2, ModelMap model) {
        try {
            usuarioService.registrar(nombre, apellido, email, password, password2);
            model.put("exito", "El usuario fue creado correctamente.");
            return "index.html";

        } catch (MiException ex) {
            model.put("error", ex.getMessage());
            model.put("nombre", nombre);
            model.put("email", email);

            return "registrousuario.html"; // Retornamos al formulario de registro con el mensaje de error.
        }
    }

}
