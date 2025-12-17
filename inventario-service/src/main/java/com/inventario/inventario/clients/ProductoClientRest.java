package com.inventario.inventario.clients;

import com.inventario.inventario.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "producto", url = "${PRODUCTO_SERVICE_HOST:localhost}:${PRODUCTO_SERVICE_PORT:8081}")
public interface ProductoClientRest {
    @GetMapping("/api/productos/{id}")
    Optional<ProductoDto> findById(@PathVariable Long id);
}
