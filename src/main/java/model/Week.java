package model;

import java.sql.Timestamp;

public class Week {
    private int id_week;
    private String name;
    private Integer id_type_jornal;
    private int id_user_create;
    private int id_user_update;
    private Timestamp date_create;
    private Timestamp date_update;

    // Getters y Setters
    public int getId_week() {
        return id_week;
    }

    public void setId_week(int id_week) {
        this.id_week = id_week;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId_type_jornal() {
        return id_type_jornal;
    }

    public void setId_type_jornal(Integer id_type_jornal) {
        this.id_type_jornal = id_type_jornal;
    }

    public int getId_user_create() {
        return id_user_create;
    }

    public void setId_user_create(int id_user_create) {
        this.id_user_create = id_user_create;
    }

    public int getId_user_update() {
        return id_user_update;
    }

    public void setId_user_update(int id_user_update) {
        this.id_user_update = id_user_update;
    }

    public Timestamp getDate_create() {
        return date_create;
    }

    public void setDate_create(Timestamp date_create) {
        this.date_create = date_create;
    }

    public Timestamp getDate_update() {
        return date_update;
    }

    public void setDate_update(Timestamp date_update) {
        this.date_update = date_update;
    }
}
