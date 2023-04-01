package com.example.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Cliente;
import com.example.entities.Mascota;
import com.example.services.ClienteService;
import com.example.services.MascotaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")

public class ClienteController {
    
    @Autowired 
    private ClienteService clienteService; 

    @Autowired 
    private MascotaService mascotaService; 

    //APARTADO 2

    //Método que permita ver un listado de clientes. 

    //http://localhost:8080/clientes?page=1&size=1

    @GetMapping
    public ResponseEntity<List<Cliente>> findAll
    (@RequestParam(name = "page", required = false) Integer page, 
    @RequestParam(name = "size", required = false) Integer size) {

        ResponseEntity<List<Cliente>> responseEntity = null; 

        List<Cliente> clientes = new ArrayList<>(); 

        //Se puede sort por varias propiedades, podría probar por apellidos?
        //Por probar

        Sort sortByNombre = Sort.by("nombre"); 

        if(page != null && size != null){

            try {
                
                Pageable pageable = PageRequest.of(page, size, sortByNombre); 
                Page<Cliente> clientesPaginados = clienteService.findAll(pageable); 
                clientes = clientesPaginados.getContent(); 

                responseEntity = new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK); 

            } catch (Exception e) {
                //Si no hay páginas es que la petición era errónea. 

                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
            }
        } else {

            //Por otra parte, si solo se busca que esté ordenado 

            try {
                
                clientes = clienteService.findAll(sortByNombre); 

                responseEntity = new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK); 

            } catch (Exception e) {
                
                responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }

        return responseEntity; 


    }



    //APARTADO INVENTADO 

    //Método para recuperar las mascotas de un cliente que luego voy a usar en el método de abajo. 

    @GetMapping("/{id}/mascotas")
public ResponseEntity<Map<String, Object>> getMascotasByClienteId(@PathVariable(name = "id") Integer id) {
    ResponseEntity<Map<String, Object>> responseEntity = null;
    Map<String, Object> responseAsMap = new HashMap<>();

    try {
        List<Mascota> mascotas = mascotaService.findByIdCliente(id); 

        if (mascotas != null) {
            String successMessage = "Se encontraron las mascotas del cliente con id " + id;
            responseAsMap.put("mensaje", successMessage);
            responseAsMap.put("mascotas", mascotas);

            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);
        } else {
            String errorMessage = "No se encontraron mascotas para el cliente con id " + id;
            responseAsMap.put("error", errorMessage);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.NOT_FOUND);
        }

    } catch (Exception e) {
        String errorMessage = "Ocurrió un error al obtener las mascotas del cliente con id " + id;
        responseAsMap.put("error", errorMessage);
        responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return responseEntity;
}




    //APARTADO INVENTADO

    //Método para recuperar cliente a partir del id 
    //http://localhost:8080/clientes/3

    @GetMapping("/{id}")

    public ResponseEntity<Map<String, Object>> findById(@PathVariable(name = "id") Integer id){

        ResponseEntity<Map<String, Object>> responseEntity = null; 
        Map<String, Object> responseAsMap = new HashMap<>(); 

        try {
            
            Cliente cliente = clienteService.findById(id); 

            if(cliente != null){

                // cliente.setMascotas(clienteService.get);

                String succesMessage = "Cliente con id " + id + " ha sido encontrado :)"; 
                responseAsMap.put("mensaje", succesMessage); 
                responseAsMap.put("cliente", cliente); 

                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK); 

            } else {

                String errorMessage = "Cliente con id " + id + " no ha sido encontrado :("; 
                responseAsMap.put("error", errorMessage); 
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.NOT_FOUND); 

            }

        } catch (Exception e) {
            String errorGrave = "Error grave"; 
            responseAsMap.put("error", errorGrave); 
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR); 
        }

        return responseEntity; 
    }


    //APARTADO 3. DAR DE ALTA A UN CLIENTE. ESPERO QUE MASCOTAS NO SE INVOLUCRE 

    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> insert(@Valid @RequestBody Cliente cliente, 
    BindingResult result){

        Map<String, Object> responseAsMap = new HashMap<>(); 
        ResponseEntity<Map<String, Object>> responseEntity = null; 

        LocalDate fechaActual = LocalDate.now(); 


        if(result.hasErrors()){
            //Ahora se guardan aquí los erroes
            List<String> errorMessages = new ArrayList<>(); 

            for (ObjectError error : result.getAllErrors()){

                errorMessages.add(error.getDefaultMessage()); 

            }

            responseAsMap.put("errores", errorMessages); 

            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST); 

            return responseEntity; 
         }

         Cliente clienteDB = clienteService.save(cliente); 

         

        try {
            
            if(clienteDB != null){

                if(cliente.getFechaAlta().isAfter(fechaActual)){

                    String errorFecha = "Error al introducir la fecha de alta ._."; 
                    responseAsMap.put("error", errorFecha); 
                    responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);
                    return responseEntity;  
                }

                String mensaje = "Se ha dado de alta al cliente correctamente"; 
                responseAsMap.put("mensaje", mensaje);
                responseAsMap.put("cliente", clienteDB); 
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.CREATED);

            } else {

                //No se ha dado de alta a nadie. 
            }


        } catch (DataAccessException e) {
            
            String errorGrave = "Ha tenido lugar un error grave " + ", y la causa más probable puede ser" 
            + e.getMostSpecificCause(); 
            responseAsMap.put("errorGrave", errorGrave); 
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR); 
        }


        return responseEntity; 
    }



}
