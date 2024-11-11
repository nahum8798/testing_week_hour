package model;

import connection.DataBase;
import connection.ResultDataBase;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HourDAO {
    private final DataBase connection;

    /**
     * Constructor de la clase HourDAO.
     * Inicializa la conexión a la base de datos.
     */
    public HourDAO() {
        this.connection = DataBase.getInstance();
    }

    /**
     * Agrega un nuevo registro de hora a la base de datos.
     *
     * @param hour El objeto Hour que se desea agregar.
     * @return Resultado de la operación de creación en la base de datos.
     */
    public ResultDataBase addNewHour(Hour hour) {
        String query = "INSERT INTO hour (name, id_week, id_user_create, date_create) VALUES (?, ?, ?, ?)";
        Object[] params = {
                hour.getName(),
                hour.getId_week(),
                32,  // id_user_create siempre es 32
                new Timestamp(System.currentTimeMillis())
        };
        return connection.create(query, params);
    }

    /**
     * Lee un registro de hora de la base de datos por su ID.
     *
     * @param id_hour El ID de la hora que se desea leer.
     * @return Un objeto Hour que representa la hora leída, o null si no se encontró.
     */
    public Hour readHour(int id_hour) {
        Hour hour = null;
        String query = "SELECT * FROM hour WHERE id_hour = ?";
        ResultDataBase result = connection.select(query, id_hour);

        if (result.getSuccess()) {
            List<Object> rows = (List<Object>) result.getObject();
            if (!rows.isEmpty()) {
                List<Object> rowData = (List<Object>) rows.get(0);
                hour = new Hour();
                hour.setId_hour((Integer) rowData.get(0));
                hour.setName((String) rowData.get(1));
                hour.setId_week((Integer) rowData.get(2)); // ID de la semana
                hour.setId_user_create(32);  // Siempre 32
                hour.setDate_create(rowData.get(3) instanceof Timestamp ? (Timestamp) rowData.get(3) : null);
                hour.setDate_update(rowData.get(4) instanceof Timestamp ? (Timestamp) rowData.get(4) : null);
            }
        } else {
            System.out.println("Error al obtener la hora: " + result.getMessage());
        }
        return hour;
    }

    /**
     * Obtiene todos los registros de horas de la base de datos.
     *
     * @return Una lista de objetos Hour que representan todas las horas en la base de datos.
     */
    public List<Hour> getAllHours() {
        List<Hour> hourList = new ArrayList<>();
        String query = "SELECT * FROM hour";
        ResultDataBase result = connection.select(query);

        if (result.getSuccess()) {
            List<Object> rows = (List<Object>) result.getObject();
            for (Object row : rows) {
                List<Object> rowData = (List<Object>) row;
                Hour hour = new Hour();

                if (rowData.get(0) != null) {
                    hour.setId_hour((Integer) rowData.get(0));
                }

                if (rowData.get(1) != null) {
                    hour.setName((String) rowData.get(1));
                }

                if (rowData.get(2) != null) {
                    hour.setId_week((Integer) rowData.get(2)); // ID de la semana
                }

                if (rowData.get(3) instanceof Timestamp) {
                    hour.setDate_create((Timestamp) rowData.get(3));
                } else {
                    hour.setDate_create(null);
                }

                if (rowData.get(4) instanceof Timestamp) {
                    hour.setDate_update((Timestamp) rowData.get(4));
                } else {
                    hour.setDate_update(null);
                }

                hour.setId_user_create(32); // Siempre 32

                hourList.add(hour);
            }
        } else {
            System.out.println("Error al obtener las horas: " + result.getMessage());
        }
        return hourList;
    }

    /**
     * Actualiza un registro de hora en la base de datos.
     *
     * @param hour El objeto Hour que contiene los datos a actualizar.
     * @return Resultado de la operación de actualización en la base de datos.
     */
    public ResultDataBase updateHour(Hour hour) {
        String query = "UPDATE hour SET name = ?, id_week = ?, id_user_update = ?, date_update = ? WHERE id_hour = ?";
        Object[] params = {
                hour.getName(),
                hour.getId_week(),  // ID de la semana
                32,  // id_user_update siempre es 32
                new Timestamp(System.currentTimeMillis()),
                hour.getId_hour()
        };
        return connection.update(query, params);
    }

    /**
     * Elimina un registro de hora de la base de datos por su ID.
     *
     * @param id_hour El ID de la hora que se desea eliminar.
     * @return Resultado de la operación de eliminación en la base de datos.
     */
    public ResultDataBase deleteHour(int id_hour) {
        String query = "DELETE FROM hour WHERE id_hour = ?";
        return connection.delete(query, id_hour);
    }
}

