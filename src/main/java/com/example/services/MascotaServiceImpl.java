package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.MascotaDao;
import com.example.entities.Cliente;
import com.example.entities.Mascota;

@Service

public class MascotaServiceImpl implements MascotaService{

    @Autowired
    MascotaDao mascotaDao;

    @Override
    public List<Mascota> findAll() {
        
        return mascotaDao.findAll(); 
    }

    @Override
    public Mascota findById(long idMascota) {
        
        return mascotaDao.findById(idMascota).get(); 
    }

    @Override
    public void deleteById(long idMascota) {
        
        mascotaDao.deleteById(idMascota);
    }

    @Override
    @Transactional
    public Mascota save(Mascota mascota) {
       
        return mascotaDao.save(mascota); 
    }

    @Override
    @Transactional
    public void deleteByCliente(Cliente cliente) {
        
        mascotaDao.deleteByCliente(cliente); 
    }

    @Override
    public List<Mascota> findByCliente(Cliente cliente) {
        
        return mascotaDao.findByCliente(cliente); 
    }

    


    
}
