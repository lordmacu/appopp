package com.android.opp.models;

/**
 * Created by camilo on 20/6/17.
 */

public class ItemEncuesta {
    int id;
    String encuesta;
    String imagenes;
    String desde;
    String hasta;
    String gender;
    String cantidadPersonas;
    String intereses;
    String localizacion;

    boolean status;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(String encuesta) {
        this.encuesta = encuesta;
    }

    public String getImagenes() {
        return imagenes;
    }

    public void setImagenes(String imagenes) {
        this.imagenes = imagenes;
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(String cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public String getIntereses() {
        return intereses;
    }

    public void setIntereses(String intereses) {
        this.intereses = intereses;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "ItemEncuesta{" +
                "id=" + id +
                ", encuesta='" + encuesta + '\'' +
                ", imagenes='" + imagenes + '\'' +
                ", desde='" + desde + '\'' +
                ", hasta='" + hasta + '\'' +
                ", gender='" + gender + '\'' +
                ", cantidadPersonas='" + cantidadPersonas + '\'' +
                ", intereses='" + intereses + '\'' +
                ", localizacion='" + localizacion + '\'' +
                ", status=" + status +
                '}';
    }
}
