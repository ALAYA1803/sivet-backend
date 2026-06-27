package com.sivet.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Cuenta de acceso. Pertenece a una clínica (tenant). El {@code password} se
 * almacena hasheado (bcrypt/argon2) y NUNCA se expone en respuestas.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    /** Hash de la contraseña. Nunca se serializa hacia el cliente. */
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String rol;

    /**
     * Marca a un usuario que debe cambiar su contraseña antes de operar (p. ej. tras el
     * onboarding B2B, que entrega una clave temporal). Por defecto {@code false}.
     */
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean requiereCambioPassword = false;

    /** Tenant del usuario (FK → clinicas.id). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;
}
