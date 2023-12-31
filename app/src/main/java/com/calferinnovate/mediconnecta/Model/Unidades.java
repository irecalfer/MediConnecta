package com.calferinnovate.mediconnecta.Model;

public class Unidades{

    private int id_unidad, fk_area;
    private String nombreUnidad;
    private String unidadActual;

    public Unidades() {
    }

    public Unidades(int id_unidad, int fk_area, String nombreUnidad) {
        this.id_unidad = id_unidad;
        this.fk_area = fk_area;
        this.nombreUnidad = nombreUnidad;
    }

    public int getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(int id_unidad) {
        this.id_unidad = id_unidad;
    }

    public int getFk_area() {
        return fk_area;
    }

    public void setFk_area(int fk_area) {
        this.fk_area = fk_area;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

    public String toString(){
        return nombreUnidad;
    }


    public String getUnidadActual() {
        return unidadActual;
    }

    public void setUnidadActual(String unidadActual) {
        this.unidadActual = unidadActual;
    }
}
