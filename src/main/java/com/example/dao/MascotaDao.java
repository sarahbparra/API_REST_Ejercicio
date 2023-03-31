package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Mascota;

public interface MascotaDao extends JpaRepository<Mascota, Long>{
    
    //Si tengo tiempo puedo hacer lo mismo pero llamando a cliente, 
    //para que me saque las mascotas :) 
}
