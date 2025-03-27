package com.egg.electricidad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.egg.electricidad.model.Articulo;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    @Query("SELECT MAX(a.nroArticulo) FROM Articulo a")
    public Integer findMaxNroArticulo();

}
