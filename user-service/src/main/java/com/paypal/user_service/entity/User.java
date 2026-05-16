package com.paypal.user_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
