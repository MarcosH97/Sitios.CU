package com.mahostudios.sitioscu;

public class Sitio2 {
    private int id, cost, comunicaciones, culturales, entretenimiento, radio, tv, revista, periodico, medio_ambiente, salud, universidades, ministerios, comercio, informativos, provinciales, educativos;
    private String name, url, description, category;
    public Sitio2(int id,String category, int cost,String name, String url, String description, int comunicaciones, int culturales, int entretenimiento, int radio, int tv, int revista, int periodico, int medio_ambiente, int salud, int universidades, int ministerios, int comercio, int informativos, int provinciales, int educativos ) {
        this.id = id;
        this.cost = cost;
        this.comunicaciones = comunicaciones;
        this.culturales = culturales;
        this.entretenimiento = entretenimiento;
        this.radio = radio;
        this.tv = tv;
        this.revista = revista;
        this.periodico = periodico;
        this.medio_ambiente = medio_ambiente;
        this.salud = salud;
        this.universidades = universidades;
        this.ministerios = ministerios;
        this.comercio = comercio;
        this.informativos = informativos;
        this.provinciales = provinciales;
        this.educativos = educativos;
        this.name = name;
        this.url = url;
        this.description = description;
        this.category = category;
    }

    public int getComunicaciones() { return comunicaciones; }

    public void setComunicaciones(int comunicaciones) { this.comunicaciones = comunicaciones; }

    public int getCulturales() { return culturales; }

    public void setCulturales(int culturales) { this.culturales = culturales; }

    public int getEntretenimiento() { return entretenimiento; }

    public void setEntretenimiento(int entretenimiento) { this.entretenimiento = entretenimiento; }

    public int getRadio() { return radio; }

    public void setRadio(int radio) { this.radio = radio; }

    public int getTv() { return tv; }

    public void setTv(int tv) { this.tv = tv; }

    public int getRevista() { return revista; }

    public void setRevista(int revista) { this.revista = revista; }

    public int getPeriodico() { return periodico; }

    public void setPeriodico(int periodico) { this.periodico = periodico; }

    public int getMedio_ambiente() { return medio_ambiente; }

    public void setMedio_ambiente(int medio_ambiente) { this.medio_ambiente = medio_ambiente; }

    public int getSalud() { return salud; }

    public void setSalud(int salud) { this.salud = salud; }

    public int getUniversidades() { return universidades; }

    public void setUniversidades(int universidades) { this.universidades = universidades; }

    public int getMinisterios() { return ministerios; }

    public void setMinisterios(int ministerios) { this.ministerios = ministerios; }

    public int getComercio() { return comercio; }

    public void setComercio(int comercio) { this.comercio = comercio; }

    public int getInformativos() { return informativos; }

    public void setInformativos(int informativos) { this.informativos = informativos; }

    public int getProvinciales() { return provinciales; }

    public void setProvinciales(int provinciales) { this.provinciales = provinciales; }

    public int getEducativos() { return educativos; }

    public void setEducativos(int educativos) { this.educativos = educativos; }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
