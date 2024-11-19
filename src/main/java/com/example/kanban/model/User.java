package com.example.kanban.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Tabela renomeada para evitar conflito com palavras reservadas
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String roles;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username; // Adicionado método getUsername()
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password; // Método getPassword() já esperado pelo Spring Security
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
