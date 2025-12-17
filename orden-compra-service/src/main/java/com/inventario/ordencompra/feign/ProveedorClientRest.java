package com.inventario.ordencompra.feign;

import com.inventario.ordencompra.dtos.ProveedorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "proveedor-service", url = "${proveedor.service.url}")
public interface ProveedorClientRest {
    
    @GetMapping("/api/proveedores/{id}")
    ProveedorDto getProveedorById(@PathVariable("id") Long id);
}
