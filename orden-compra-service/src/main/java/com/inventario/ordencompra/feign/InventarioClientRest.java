package com.inventario.ordencompra.feign;

import com.inventario.ordencompra.dtos.AgregarStockRequest;
import com.inventario.ordencompra.dtos.BodegaCapacidadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventario-service", url = "${inventario.service.url}")
public interface InventarioClientRest {
    
    @GetMapping("/api/bodegas/{id}/capacidad")
    BodegaCapacidadDto getBodegaCapacidad(@PathVariable("id") Long id);
    
    @PostMapping("/api/inventario/agregar-stock")
    void agregarStock(@RequestBody AgregarStockRequest request);
}
