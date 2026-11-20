package com.tecsup.services;

import com.tecsup.client.CategoriaClient;
import com.tecsup.dto.CategoriaDTO;
import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repo;

    @Autowired
    private CategoriaClient categoriaClient;

    public List<Producto> listar() {
        List<Producto> productos = repo.findAll();
        for (Producto p : productos) {
            enriquecerCategoria(p);
        }
        return productos;
    }

    public Producto guardar(Producto p) {
        Producto producto = repo.save(p);
        enriquecerCategoria(producto);
        return producto;
    }

    public Producto obtener(Long id) {
        Producto producto = repo.findById(id).orElse(null);
        if (producto != null) {
            enriquecerCategoria(producto);
        }
        return producto;
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto actual = repo.findById(id).orElse(null);
        if (actual != null) {
            actual.setNombre(datos.getNombre());
            actual.setPrecio(datos.getPrecio());
            actual.setStock(datos.getStock());
            actual.setCategoriaId(datos.getCategoriaId());
            Producto guardado = repo.save(actual);
            enriquecerCategoria(guardado);
            return guardado;
        }
        return null;
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    private void enriquecerCategoria(Producto producto) {
        if (producto.getCategoriaId() != null) {
            try {
                CategoriaDTO categoria = categoriaClient.obtenerCategoria(producto.getCategoriaId());
                producto.setCategoria(categoria);
            } catch (Exception e) {
                // Si el servicio de categorías no está disponible o la categoría no existe
                producto.setCategoria(null);
            }
        }
    }
}
