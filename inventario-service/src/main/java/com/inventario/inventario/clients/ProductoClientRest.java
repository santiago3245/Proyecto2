package com.inventario.inventario.clients;

import com.inventario.inventario.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service", url = "http://producto-service:8081")
public interface ProductoClientRest {
    
    @GetMapping("/api/productos/{id}")
    ProductoDto getProductoById(@PathVariable Long id);
}
