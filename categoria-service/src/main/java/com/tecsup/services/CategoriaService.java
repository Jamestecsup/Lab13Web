package com.tecsup.services;

import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> listar() {
        return repository.findAll();
    }

    public Categoria guardar(Categoria categoria) {
        return repository.save(categoria);
    }

    public Categoria obtener(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Categoria actualizar(Long id, Categoria datos) {
        Categoria actual = repository.findById(id).orElse(null);
        if (actual != null) {
            actual.setNombre(datos.getNombre());
            actual.setEstado(datos.getEstado());
            return repository.save(actual);
        }
        return null;
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
