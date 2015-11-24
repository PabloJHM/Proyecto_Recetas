package com.example.pablo.proyecto_recetas.receta_ingrediente;

public class RecetaIngrediente {
    public long id;
    public long idReceta,idIngrediente;
    public int cantidad;

    public RecetaIngrediente() {
    }

    public RecetaIngrediente(long idReceta, long idIngrediente, int cantidad) {

        this.idReceta = idReceta;
        this.idIngrediente = idIngrediente;
        this.cantidad = cantidad;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(long idReceta) {
        this.idReceta = idReceta;
    }

    public long getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(long idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "RecetaIngrediente{" +
                "id=" + id +
                ", idReceta=" + idReceta +
                ", idIngrediente=" + idIngrediente +
                ", cantidad=" + cantidad +
                '}';
    }
}
