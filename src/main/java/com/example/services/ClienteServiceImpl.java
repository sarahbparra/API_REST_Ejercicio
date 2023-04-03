package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.ClienteDao;
// import com.example.dao.MascotaDao;
import com.example.entities.Cliente;

@Service

public class ClienteServiceImpl implements ClienteService{

    @Autowired
    private ClienteDao clienteDao; 

    // @Autowired
    // private MascotaDao mascotaDao; 

    @Override
    public List<Cliente> findAll(Sort sort) {
        
        return clienteDao.findAll(sort); 
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        
        return clienteDao.findAll(pageable); 
    }

    @Override
    public Cliente findById(long id) {

        // Cliente cliente = clienteDao.findById(id); 
        // List<Mascota> mascotas = cliente.getMascotas(); 
        // mascotas.size(); 
        // return cliente; 
        // cliente.setMascotas(mascotaDao.findByIdCliente(id)); 
        // return cliente;
        
        return clienteDao.findById(id); 
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        
        return clienteDao.save(cliente); 
    }

    @Override
    @Transactional
    public void delete(Cliente cliente) {
        
        clienteDao.delete(cliente);
    }

    
}
