package com.example.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entities.Cliente;
import com.example.model.FileUploadResponse;
import com.example.services.ClienteService;
import com.example.utilities.FileDownloadUtil;
import com.example.utilities.FileUploadUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")

public class ClienteController {
    
    @Autowired 
    private ClienteService clienteService; 

    // @Autowired 
    // private MascotaService mascotaService; 

    @Autowired 
    private FileUploadUtil fileUploadUtil; 

    @Autowired
    private FileDownloadUtil fileDownloadUtil; 

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

//     @GetMapping("/{id}/mascotas")
// public ResponseEntity<Map<String, Object>> getMascotasByClienteId(@PathVariable(name = "id") Long id) {
   
//     ResponseEntity<Map<String, Object>> responseEntity = null;
//     Map<String, Object> responseAsMap = new HashMap<>();

//     try {
//         Cliente cliente = clienteService.findById(id);

//         List<Mascota> mascotas = mascotaService.findMascotasByIdCliente(cliente.getId()); 

//         if (mascotas != null) {
//             String successMessage = "Se encontraron las mascotas del cliente con id " + id;
//             responseAsMap.put("mensaje", successMessage);
//             responseAsMap.put("mascotas", mascotas);

//             responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);
//         } else {
//             String errorMessage = "No se encontraron mascotas para el cliente con id " + id;
//             responseAsMap.put("error", errorMessage);
//             responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.NOT_FOUND);
//         }

//     } catch (Exception e) {
//         String errorMessage = "Ocurrió un error al obtener las mascotas del cliente con id " + id;
//         responseAsMap.put("error", errorMessage);
//         responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
//     }

//     return responseEntity;
// }


//OTRA PRUEBA 

//Método para recuperar las mascotas de un cliente. 

// @GetMapping("/{id}/mascotas")
//     public ResponseEntity<List<Mascota>> getMascotasCliente(@PathVariable Long id) {
//         // Cliente cliente = clienteService.findById(id);
//         List<Mascota> mascotas = clienteService.getMascotasById(id); 
//         return new ResponseEntity<List<Mascota>>(mascotas, HttpStatus.OK);
//     }



    //APARTADO INVENTADO

    //Método para recuperar cliente a partir del id 
    //http://localhost:8080/clientes/3

    @GetMapping("/{id}")

    public ResponseEntity<Map<String, Object>> findById(@PathVariable(name = "id") Long id){

        ResponseEntity<Map<String, Object>> responseEntity = null; 
        Map<String, Object> responseAsMap = new HashMap<>(); 

        try {
            
            Cliente cliente = clienteService.findById(id);  

            if(cliente != null){

                // cliente.setMascotas(clienteService.get);

                String succesMessage = "Cliente con id " + id + " ha sido encontrado :)"; 
                responseAsMap.put("mensaje", succesMessage); 
                responseAsMap.put("cliente", cliente); 
                // List<Mascota> mascotas = mascotaService.findMascotasByIdCliente(cliente.getId()); 
                // responseAsMap.put("mascotas", mascotas); 

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

    @PostMapping(consumes = "multipart/form-data")
    @Transactional
    // public ResponseEntity<Map<String, Object>> insert(
    //     @Valid 
    //     @RequestBody Cliente cliente, 
    //     BindingResult result) Sin imágenes
    public ResponseEntity<Map<String, Object>> insert(
        @Valid
        @RequestPart(name = "cliente") Cliente cliente, 
        BindingResult result, 
        @RequestPart(name = "file") MultipartFile file) throws IOException {

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
         if(!file.isEmpty()) {

            String fileCode = fileUploadUtil.saveFile(file.getOriginalFilename(), file); 
            cliente.setImagenCliente(fileCode + "-" + file.getOriginalFilename());

            //Devolver información respecto al file recibido. 

            FileUploadResponse fileUploadResponse = FileUploadResponse
            .builder()
            .fileName(fileCode + "-" + file.getOriginalFilename())
            .downloadURI("/clientes/downloadFile/" + fileCode + "-" + file.getOriginalFilename())
            .size(file.getSize())
            .build(); 

            responseAsMap.put("info del archivo", fileUploadResponse); 

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


    @DeleteMapping("/{id}")
    @Transactional

    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id){

        ResponseEntity<String> responseEntity = null; 

        try {
            
            Cliente cliente = clienteService.findById(id); 

            if(cliente != null){

                clienteService.delete(cliente);
                responseEntity = new ResponseEntity<String>("Borrado correctamente", HttpStatus.OK); 
            } else {

                responseEntity = new ResponseEntity<String>("No se puede realizar ya que no hay cliente", HttpStatus.NOT_FOUND); 
            }
        } catch (DataAccessException e) {
            
            e.getMostSpecificCause(); 
            responseEntity = new ResponseEntity<String>("Error fatal", HttpStatus.INTERNAL_SERVER_ERROR); 
        }


        return responseEntity; 
    }


    /** Método para actualizar a un cliente */

    @PutMapping("/{id}")
    @Transactional
   public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Cliente cliente, 
         BindingResult result, 
         @PathVariable(name = "id") Integer id){

     Map<String, Object> responseAsMap = new HashMap<>(); 
     ResponseEntity<Map<String, Object>> responseEntity = null; 

     /**
      * Primero. Comprobar si hay errores en el cliente recibido. 
      */

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

      //Si no hay errores, entonces persistimos el producto 
      //Vinculando previamente el id que se recibe con el producto

      cliente.setId(id);

      Cliente clienteDB = clienteService.save(cliente); 

      try {

         if(clienteDB != null){

             String mensaje = "El cliente se ha creado correctamente."; 
             responseAsMap.put("mensaje", mensaje); 
             responseAsMap.put("cliente", clienteDB); 
             responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK); 
 
          } else {
 
             //No se ha actualizado el cliente. 
 
          }
         
      } catch (DataAccessException e) {

         String errorGrave = "Ha tenido lugar un error grave " + ", y la causa más probable puede ser" 
         + e.getMostSpecificCause(); 
         responseAsMap.put("errorGrave", errorGrave); 
         responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR); 
      }

      

     return responseEntity; 

   }

     /**
     *  Implementa filedownnload end point API 
     **/    
    @GetMapping("/downloadFile/{fileCode}")
    public ResponseEntity<?> downloadFile(@PathVariable(name = "fileCode") String fileCode) {

        Resource resource = null;

        try {
            resource = fileDownloadUtil.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found ", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
        .body(resource);

    }

}
