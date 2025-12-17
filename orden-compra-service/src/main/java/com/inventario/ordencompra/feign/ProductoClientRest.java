package com.inventario.ordencompra.feign;

import com.inventario.ordencompra.dtos.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service", url = "${producto.service.url}")
public interface ProductoClientRest {
    
    @GetMapping("/api/productos/{id}")
    ProductoDto getProductoById(@PathVariable("id") Long id);
}
