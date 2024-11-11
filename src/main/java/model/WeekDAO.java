package model;

import connection.DataBase;
import connection.ResultDataBase;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WeekDAO {
    private final DataBase connection;

    public WeekDAO() {
        this.connection = DataBase.getInstance();
    }

    public ResultDataBase addNewWeek(Week week) {
        String query = "INSERT INTO week (name, id_type_jornal, id_user_create, date_create) VALUES (?, ?, ?, ?)";
        Object[] params = {
                week.getName(),
                week.getId_type_jornal(),
                32, // id_user_create siempre es 32
                new Timestamp(System.currentTimeMillis())
        };
        return connection.create(query, params);


    }

    public Week readWeek(int id_week) {
        Week week = null;
        String query = "SELECT * FROM week WHERE id_week = ?";
        ResultDataBase result = connection.select(query, id_week);

        if (result.getSuccess()) {
            List<Object> rows = (List<Object>) result.getObject();
            if (!rows.isEmpty()) {
                List<Object> rowData = (List<Object>) rows.get(0);
                week = new Week();
                week.setId_week((Integer) rowData.get(0));
                week.setName((String) rowData.get(1));
                week.setId_type_jornal((Integer) rowData.get(2));
                week.setId_user_create((Integer) rowData.get(3));
                week.setId_user_update((Integer) rowData.get(4));
                week.setDate_create(rowData.get(5) instanceof Timestamp ? (Timestamp) rowData.get(5) : null);
                week.setDate_update(rowData.get(6) instanceof Timestamp ? (Timestamp) rowData.get(6) : null);
            }
        } else {
            System.out.println("Error al obtener la semana: " + result.getMessage());
        }
        return week;
    }

    public List<Week> getAllWeeks() {
        List<Week> weekList = new ArrayList<>();
        String query = "SELECT * FROM week";
        ResultDataBase result = connection.select(query);

        if (result.getSuccess()) {
            List<Object> rows = (List<Object>) result.getObject();
            for (Object row : rows) {
                List<Object> rowData = (List<Object>) row;
                Week week = new Week();

                // Asegurarse de que los valores nulos se manejen correctamente
                week.setId_week(rowData.get(0) != null ? (Integer) rowData.get(0) : 0);
                week.setName((String) rowData.get(1));
                week.setId_type_jornal(rowData.get(2) != null ? (Integer) rowData.get(2) : null);
                week.setId_user_create(rowData.get(3) != null ? (Integer) rowData.get(3) : 0);
                week.setId_user_update(rowData.get(4) != null ? (Integer) rowData.get(4) : 0);
                week.setDate_create(rowData.get(5) instanceof Timestamp ? (Timestamp) rowData.get(5) : null);
                week.setDate_update(rowData.get(6) instanceof Timestamp ? (Timestamp) rowData.get(6) : null);

                weekList.add(week);
            }
        } else {
            System.out.println("Error al obtener las semanas: " + result.getMessage());
        }
        return weekList;
    }


    public ResultDataBase updateWeek(Week week) {
        String query = "UPDATE week SET name = ?, id_type_jornal = ?, id_user_update = ?, date_update = ? WHERE id_week = ?";
        Object[] params = {
                week.getName(),
                week.getId_type_jornal(),
                32,  // id_user_update siempre es 32
                new Timestamp(System.currentTimeMillis()),
                week.getId_week()
        };
        return connection.update(query, params);
    }

    public ResultDataBase deleteWeek(int id_week) {
        String query = "DELETE FROM week WHERE id_week = ?";
        return connection.delete(query, id_week);
    }
}
