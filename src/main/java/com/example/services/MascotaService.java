package com.example.services;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.example.entities.Cliente;
import com.example.entities.Mascota;

public interface MascotaService {
    
    public List<Mascota> findAll(); 
    public Mascota findById(long idMascota); 
    public void deleteById(long idMascota); 
    public Mascota save (Mascota mascota); 

    public void deleteByCliente(Cliente cliente); 
    public List<Mascota> findByCliente (Cliente cliente); 
    
}
