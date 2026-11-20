package com.tecsup.controller;

import com.tecsup.model.Pedido;
import com.tecsup.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @GetMapping
    public List<Pedido> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Pedido> guardar(@Valid @RequestBody Pedido pedido) {
        return new ResponseEntity<>(service.guardar(pedido), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtener(@PathVariable Long id) {
        Pedido pedido = service.obtener(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<Pedido> anular(@PathVariable Long id) {
        Pedido pedido = service.anular(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}/atender")
    public ResponseEntity<Pedido> atender(@PathVariable Long id) {
        Pedido pedido = service.atender(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        if (estado == null) {
            return ResponseEntity.badRequest().build();
        }
        Pedido pedido = service.actualizarEstado(id, estado);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }

    // Regla: No eliminar físicamente, sino cambiar estado a ANULADO
    @DeleteMapping("/{id}")
    public ResponseEntity<Pedido> eliminar(@PathVariable Long id) {
        Pedido pedido = service.anular(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido);
    }
}
