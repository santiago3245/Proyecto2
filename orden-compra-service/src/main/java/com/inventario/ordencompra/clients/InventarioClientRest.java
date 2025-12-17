package com.inventario.ordencompra.clients;

import com.inventario.ordencompra.dto.BodegaCapacidadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventario-client", url = "${INVENTARIO_SERVICE_HOST:localhost}:${INVENTARIO_SERVICE_PORT:8083}")
public interface InventarioClientRest {
    @PostMapping("/api/inventario/agregar-stock")
    ResponseEntity<?> agregarStock(@RequestParam Long productoId, 
                                    @RequestParam Long bodegaId, 
                                    @RequestParam Integer cantidad);
    
    @GetMapping("/api/bodegas/{id}/capacidad")
    BodegaCapacidadDto getCapacidadBodega(@PathVariable Long id);
}
