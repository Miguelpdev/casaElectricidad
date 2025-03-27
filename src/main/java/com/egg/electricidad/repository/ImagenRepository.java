package com.egg.electricidad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.electricidad.model.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, String> {

}
