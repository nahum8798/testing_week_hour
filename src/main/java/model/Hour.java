package model;

import java.sql.Timestamp;

public class Hour {
    private int id_hour;             // Identificador único para la hora (autoincremental)
    private String name;             // Nombre del rango horario (por ejemplo, "08:00-09:00")
    private int id_week;             // ID de la semana asociada
    private int id_user_create = 32; // ID del usuario que creó el registro de la hora (siempre 32)
    private int id_user_update = 32; // ID del usuario que actualizó por última vez el registro de la hora (siempre 32)
    private Timestamp date_create;   // Marca de tiempo cuando se creó el registro de la hora
    private Timestamp date_update;   // Marca de tiempo cuando se actualizó el registro de la hora

    /**
     * Constructor de la clase Hour con parámetros.
     *
     * @param name   El nombre del rango horario.
     * @param id_week El ID de la semana asociada.
     */
    public Hour(String name, int id_week) {
        this.name = name;
        this.id_week = id_week;
    }

    /**
     * Constructor por defecto de la clase Hour.
     */
    public Hour() {}

    // Getters y Setters

    /**
     * Obtiene el identificador único para la hora.
     *
     * @return El identificador único para la hora.
     */
    public int getId_hour() {
        return id_hour;
    }

    /**
     * Establece el identificador único para la hora.
     *
     * @param id_hour El identificador único a establecer.
     */
    public void setId_hour(int id_hour) {
        this.id_hour = id_hour;
    }

    /**
     * Obtiene el nombre del rango horario.
     *
     * @return El nombre del rango horario.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del rango horario.
     *
     * @param name El nombre del rango horario a establecer.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el ID de la semana asociada.
     *
     * @return El ID de la semana asociada.
     */
    public int getId_week() {
        return id_week;
    }

    /**
     * Establece el ID de la semana asociada.
     *
     * @param id_week El ID de la semana a establecer.
     */
    public void setId_week(int id_week) {
        this.id_week = id_week;
    }

    /**
     * Obtiene el ID del usuario que creó el registro de la hora.
     *
     * @return El ID del usuario que creó el registro de la hora.
     */
    public int getId_user_create() {
        return id_user_create;
    }

    /**
     * Establece el ID del usuario que creó el registro de la hora.
     * Este valor siempre se establece en 32.
     *
     * @param id_user_create El ID del usuario a establecer (ignorando el valor).
     */
    public void setId_user_create(int id_user_create) {
        this.id_user_create = 32;
    }

    /**
     * Obtiene el ID del usuario que actualizó por última vez el registro de la hora.
     *
     * @return El ID del usuario que actualizó por última vez el registro de la hora.
     */
    public int getId_user_update() {
        return id_user_update;
    }

    /**
     * Establece el ID del usuario que actualizó por última vez el registro de la hora.
     * Este valor siempre se establece en 32.
     *
     * @param id_user_update El ID del usuario a establecer (ignorando el valor).
     */
    public void setId_user_update(int id_user_update) {
        this.id_user_update = 32;
    }

    /**
     * Obtiene la marca de tiempo cuando se creó el registro de la hora.
     *
     * @return La marca de tiempo de creación del registro.
     */
    public Timestamp getDate_create() {
        return date_create;
    }

    /**
     * Establece la marca de tiempo cuando se creó el registro de la hora.
     *
     * @param date_create La marca de tiempo a establecer.
     */
    public void setDate_create(Timestamp date_create) {
        this.date_create = date_create;
    }

    /**
     * Obtiene la marca de tiempo cuando se actualizó por última vez el registro de la hora.
     *
     * @return La marca de tiempo de la última actualización.
     */
    public Timestamp getDate_update() {
        return date_update;
    }

    /**
     * Establece la marca de tiempo cuando se actualizó por última vez el registro de la hora.
     *
     * @param date_update La marca de tiempo a establecer.
     */
    public void setDate_update(Timestamp date_update) {
        this.date_update = date_update;
    }
}

