package com.tecsup.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteDTO {
    private Long id;
    private String nombre;
    
    @JsonAlias({"apellidos", "apellido"})
    private String apellido;
    
    private String apellidos;
    private String direccion;
    private String estado;
    
    @JsonAlias({"nro_dni", "nroDni", "dni"})
    private String nro_dni;

    public String getApellido() {
        return apellido != null ? apellido : apellidos;
    }
}
