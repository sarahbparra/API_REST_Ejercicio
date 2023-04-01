package com.example.entities;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mascotas")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Mascota implements Serializable{

    private static final long serialVersionUID = 1L; 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private long id; 

    private String nombre; 
    private String raza; 
    private Genero genero; 

    public enum Genero{
        MACHO, HEMBRA
    }

    //Fecha de nacimiento 
    // @Temporal(TemporalType.TIMESTAMP)
    private LocalDate fechaNacimiento; 

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) 
    private Cliente cliente; 

}
