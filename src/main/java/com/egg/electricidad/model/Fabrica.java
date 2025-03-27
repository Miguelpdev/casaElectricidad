package com.egg.electricidad.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Lombok generará getters, setters, toString, equals y hashCode
@NoArgsConstructor
public class Fabrica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFabrica;

    @Column(nullable = false)
    private String nombreFabrica;

}
