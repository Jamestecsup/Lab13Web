package com.tecsup.services;

import com.tecsup.client.ClienteClient;
import com.tecsup.client.ProductoClient;
import com.tecsup.dto.ClienteDTO;
import com.tecsup.dto.ProductoDTO;
import com.tecsup.model.Pedido;
import com.tecsup.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repo;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private ProductoClient productoClient;

    public List<Pedido> listar() {
        List<Pedido> pedidos = repo.findAll();
        for (Pedido p : pedidos) {
            enriquecerPedido(p);
        }
        return pedidos;
    }

    public Pedido guardar(Pedido p) {
        if (p.getEstado() == null || p.getEstado().isBlank()) {
            p.setEstado("PENDIENTE");
        }
        Pedido guardado = repo.save(p);
        enriquecerPedido(guardado);
        return guardado;
    }

    public Pedido obtener(Long id) {
        Pedido pedido = repo.findById(id).orElse(null);
        if (pedido != null) {
            enriquecerPedido(pedido);
        }
        return pedido;
    }

    public Pedido anular(Long id) {
        Pedido pedido = repo.findById(id).orElse(null);
        if (pedido != null) {
            pedido.setEstado("ANULADO");
            Pedido guardado = repo.save(pedido);
            enriquecerPedido(guardado);
            return guardado;
        }
        return null;
    }

    public Pedido atender(Long id) {
        Pedido pedido = repo.findById(id).orElse(null);
        if (pedido != null) {
            pedido.setEstado("ATENDIDO");
            Pedido guardado = repo.save(pedido);
            enriquecerPedido(guardado);
            return guardado;
        }
        return null;
    }

    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = repo.findById(id).orElse(null);
        if (pedido != null) {
            pedido.setEstado(nuevoEstado.toUpperCase());
            Pedido guardado = repo.save(pedido);
            enriquecerPedido(guardado);
            return guardado;
        }
        return null;
    }

    // Regla: No eliminar físicamente, sino cambiar estado a ANULADO
    public void eliminar(Long id) {
        anular(id);
    }

    private void enriquecerPedido(Pedido pedido) {
        if (pedido.getClienteId() != null) {
            try {
                ClienteDTO cliente = clienteClient.obtenerCliente(pedido.getClienteId());
                pedido.setCliente(cliente);
            } catch (Exception e) {
                pedido.setCliente(null);
            }
        }

        if (pedido.getProductoId() != null) {
            try {
                ProductoDTO producto = productoClient.obtenerProducto(pedido.getProductoId());
                pedido.setProducto(producto);
            } catch (Exception e) {
                pedido.setProducto(null);
            }
        }
    }
}
