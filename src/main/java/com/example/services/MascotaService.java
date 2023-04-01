package com.example.services;

import java.util.List;

import com.example.entities.Mascota;

public interface MascotaService {
    
    public List<Mascota> findByIdCliente(long id); 
}
