package com.tecsup.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @JsonAlias({"apellido", "apellidos"})
    private String apellidos;

    @NotBlank(message = "La dirección es obligatoria")
    @JsonAlias({"direccion", "dirección"})
    private String direccion;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotBlank(message = "El nro_dni es obligatorio")
    @Pattern(regexp = "^\\d{8}$", message = "El nro_dni debe contener 8 dígitos exacto")
    @JsonAlias({"nro_dni", "nroDni", "dni"})
    @JsonProperty("nro_dni")
    private String nroDni;

    @JsonProperty("apellido")
    public String getApellido() {
        return apellidos;
    }

    public void setApellido(String apellido) {
        this.apellidos = apellido;
    }
}
