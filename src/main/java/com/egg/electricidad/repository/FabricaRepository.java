package com.egg.electricidad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.electricidad.model.Fabrica;

@Repository
public interface FabricaRepository extends JpaRepository<Fabrica, Long> {

}
