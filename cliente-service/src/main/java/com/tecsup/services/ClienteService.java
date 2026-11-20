package com.tecsup.services;

import com.tecsup.model.Cliente;
import com.tecsup.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public List<Cliente> listar() {
        return repository.findAll();
    }

    public Cliente guardar(Cliente cliente) {
        return repository.save(cliente);
    }

    public Cliente obtener(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Cliente actualizar(Long id, Cliente datos) {
        Cliente actual = repository.findById(id).orElse(null);
        if (actual != null) {
            actual.setNombre(datos.getNombre());
            actual.setApellidos(datos.getApellidos());
            actual.setDireccion(datos.getDireccion());
            actual.setEstado(datos.getEstado());
            actual.setNroDni(datos.getNroDni());
            return repository.save(actual);
        }
        return null;
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
