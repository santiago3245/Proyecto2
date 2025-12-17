package com.inventario.proveedor.dto;

import com.inventario.proveedor.models.entities.Proveedor.EstadoProveedor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProveedorDto {

    private Long id;

    @NotBlank(message = "El RUC es obligatorio")
    @Size(max = 20, message = "El RUC no puede tener más de 20 caracteres")
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 200, message = "La razón social no puede tener más de 200 caracteres")
    private String razonSocial;

    @Size(max = 200, message = "El nombre comercial no puede tener más de 200 caracteres")
    private String nombreComercial;

    @Size(max = 300, message = "La dirección no puede tener más de 300 caracteres")
    private String direccion;

    @Size(max = 100, message = "La ciudad no puede tener más de 100 caracteres")
    private String ciudad;

    @Size(max = 100, message = "El país no puede tener más de 100 caracteres")
    private String pais;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @Size(max = 200, message = "El sitio web no puede tener más de 200 caracteres")
    private String sitioWeb;

    @Size(max = 100, message = "La categoría no puede tener más de 100 caracteres")
    private String categoria;

    private EstadoProveedor estado;

    public ProveedorDto() {
    }

    public ProveedorDto(Long id, String ruc, String razonSocial, String nombreComercial, 
                        String direccion, String ciudad, String pais, String telefono, 
                        String email, String sitioWeb, String categoria, EstadoProveedor estado) {
        this.id = id;
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.nombreComercial = nombreComercial;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.telefono = telefono;
        this.email = email;
        this.sitioWeb = sitioWeb;
        this.categoria = categoria;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public EstadoProveedor getEstado() {
        return estado;
    }

    public void setEstado(EstadoProveedor estado) {
        this.estado = estado;
    }
}
