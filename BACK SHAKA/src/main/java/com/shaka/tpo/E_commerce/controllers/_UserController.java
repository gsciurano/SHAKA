package com.shaka.tpo.E_commerce.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shaka.tpo.E_commerce.entity._User;
import com.shaka.tpo.E_commerce.entity._UserLogin;
import com.shaka.tpo.E_commerce.repository._UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users") 
public class _UserController {

    private final _UserRepository userRepository;

    public _UserController(_UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody _User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El email ya esta registrado");
        }
        _User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody _UserLogin userLogin) {
        Optional<_User> existing = userRepository.findByEmail(userLogin.getEmail());
        if (existing.isEmpty()) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        if (!existing.get().getPassword().equals(userLogin.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }
        return ResponseEntity.ok("Login exitoso para: " + userLogin.getEmail());
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
