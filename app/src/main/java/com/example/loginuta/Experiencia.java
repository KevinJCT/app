package com.example.loginuta;

public class Experiencia {

    String empresa;
    String cargo;
    String telefono;

    public Experiencia(String empresa, String cargo, String telefono) {
        this.empresa = empresa;
        this.cargo = cargo;
        this.telefono = telefono;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}