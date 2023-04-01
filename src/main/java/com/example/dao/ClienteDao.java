package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entities.Cliente;

public interface ClienteDao extends JpaRepository<Cliente, Long> {
    
    //#1.- Método que me recupere la lista de clientes ordenados. 
    @Query(value = "select c from Cliente c left join fetch c.hotel")

    public List<Cliente> findAll(Sort sort); 

    //#2.- Método para sacar una página de cliente
    @Query(value = "select c from Cliente c left join fetch c.hotel", 
    countQuery = "select count(c) from Cliente c left join c.hotel")

    public Page<Cliente> findAll(Pageable pageable); 

    //#3.- Método que recupera un cliente por su id, 
    //tal vez pueda hacer un método que recupere a un cliente por su mascota (?)

    //No muestra las mascotas porque la unión es de onetomany en clientes. No es capaz de devolver 
    //múltiples filas para el mismo registro. 
    @Query(value = "select c from Cliente c left join fetch c.hotel left join fetch c.mascotas where c.id = :id")
    public Cliente findById(long id); 





}
