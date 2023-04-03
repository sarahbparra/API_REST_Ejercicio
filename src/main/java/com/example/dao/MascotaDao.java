package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Cliente;
import com.example.entities.Mascota;

public interface MascotaDao extends JpaRepository<Mascota, Long>{
    
    //Si tengo tiempo puedo hacer lo mismo pero llamando a cliente, 
    //para que me saque las mascotas :) 

    //#1.- Método que recupera las mascotas de un cliente a partir del id del cliente(?)
    
    // @Query(value = "select m from Mascota m join fetch m.cliente where cliente.id = :id")
    // @Query("SELECT m FROM Mascota m left join fetch m.cliente c WHERE c.id = :id")
    // public List<Mascota> findMascotasByIdCliente(@Param("id") Long id); 
    List<Mascota> findByCliente(Cliente cliente);
    long deleteByCliente (Cliente cliente); 

}
