package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.MascotaDao;
import com.example.entities.Mascota;

@Service

public class MascotaServiceImpl implements MascotaService{

    @Autowired
    MascotaDao mascotaDao; 

    @Override
    public List<Mascota> findByIdCliente(long id) {
        
        return mascotaDao.findMascotasByIdCliente(id); 
    }
    
}
