package com.example.pablo.proyecto_recetas.receta;

public class Receta {
    private long id,idCat;
    private String nombre,foto,Instrucciones;

    public Receta() {
    }

    public Receta(long id, String nombre, String foto, String instrucciones) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        Instrucciones = instrucciones;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getInstrucciones() {
        return Instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        Instrucciones = instrucciones;
    }

    public long getIdCat() {
        return idCat;
    }

    public void setIdCat(long idCat) {
        this.idCat = idCat;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", foto='" + foto + '\'' +
                ", Instrucciones='" + Instrucciones + '\'' +
                '}';
    }
}
