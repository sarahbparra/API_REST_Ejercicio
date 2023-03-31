package com.example.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Cliente implements Serializable{

    private static final long serialVersionUID = 1L; 
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; 

    private String nombre; 
    private String apellidos; 

    //Fecha de alta 
    // @Temporal(TemporalType.TIMESTAMP)
    private LocalDate fechaAlta; 

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) 
    private Hotel hotel; 

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "cliente") 
    private List<Mascota> mascotas; 


}
