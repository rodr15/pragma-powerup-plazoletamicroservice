package com.ti.acelera.plazoletamicroservice.domain.model;

public class Restaurant {
    private Long id;
    private String name;
    private String direction;
    private String idProprietary;
    private String phone;
    private String urlLogo;
    private String nit;

    public Restaurant(Long id, String name, String direction, String idProprietary, String phone, String urlLogo, String nit) {
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.idProprietary = idProprietary;
        this.phone = phone;
        this.urlLogo = urlLogo;
        this.nit = nit;
    }

    public Restaurant() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getIdProprietary() {
        return idProprietary;
    }

    public void setIdProprietary(String idProprietary) {
        this.idProprietary = idProprietary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }
}
