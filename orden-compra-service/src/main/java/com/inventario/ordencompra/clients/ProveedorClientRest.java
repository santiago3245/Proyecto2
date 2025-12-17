package com.inventario.ordencompra.clients;

import com.inventario.ordencompra.dto.ProveedorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "proveedor-client", url = "${PROVEEDOR_SERVICE_HOST:localhost}:${PROVEEDOR_SERVICE_PORT:8082}")
public interface ProveedorClientRest {
    @GetMapping("/api/proveedores/{id}")
    Optional<ProveedorDto> findById(@PathVariable Long id);
}
