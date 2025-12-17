package com.inventario.proveedor.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El RUC es obligatorio")
    @Size(max = 20, message = "El RUC no puede tener más de 20 caracteres")
    @Column(unique = true, nullable = false, length = 20)
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 200, message = "La razón social no puede tener más de 200 caracteres")
    @Column(nullable = false, length = 200)
    private String razonSocial;

    @Size(max = 200, message = "El nombre comercial no puede tener más de 200 caracteres")
    @Column(length = 200)
    private String nombreComercial;

    @Size(max = 300, message = "La dirección no puede tener más de 300 caracteres")
    @Column(length = 300)
    private String direccion;

    @Size(max = 100, message = "La ciudad no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String ciudad;

    @Size(max = 100, message = "El país no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String pais;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    @Column(length = 20)
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String email;

    @Size(max = 200, message = "El sitio web no puede tener más de 200 caracteres")
    @Column(length = 200)
    private String sitioWeb;

    @Size(max = 100, message = "La categoría no puede tener más de 100 caracteres")
    @Column(length = 100)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoProveedor estado = EstadoProveedor.ACTIVO;

    public enum EstadoProveedor {
        ACTIVO,
        INACTIVO
    }

    public Proveedor() {
    }

    public Proveedor(String ruc, String razonSocial, String nombreComercial, String direccion, 
                     String ciudad, String pais, String telefono, String email, 
                     String sitioWeb, String categoria, EstadoProveedor estado) {
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
