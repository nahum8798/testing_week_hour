package model;

import static org.junit.jupiter.api.Assertions.*;

import model.HourDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import connection.DataBase;
import connection.ResultDataBase;
import model.Hour;

import java.sql.Timestamp;
import java.util.List;

public class HourDAOTest {
    private HourDAO hourDAO;
    private DataBase connection;

    @BeforeEach
    public void setUp() {
        connection = DataBase.getInstance();
        hourDAO = new HourDAO(); // Instancia la clase real

    }

    @Test
    void testAddNewHour() {
        // Crear una nueva hora de prueba
        Hour testHour = new Hour("08:00", 1);

        // Agregar la nueva hora y verificar que fue exitosa
        ResultDataBase result = hourDAO.addNewHour(testHour);
        assertTrue(result.getSuccess(), "The hour should be added successfully.");

        // Recuperar todas las horas insertadas
        List<Hour> hours = hourDAO.getAllHours(); // Asegúrate de tener este método en HourDAO
        assertFalse(hours.isEmpty(), "There should be at least one hour in the database.");

        // Obtener la última hora insertada
        Hour addedHour = hours.get(hours.size() - 1); // Suponiendo que la hora recién agregada está al final
        assertNotNull(addedHour, "The hour should exist in the database after being added.");

        // Verificar que los atributos de la hora coinciden
        assertEquals(testHour.getName(), addedHour.getName(), "The hour name should match.");
        assertEquals(testHour.getId_week(), addedHour.getId_week(), "The week ID should match.");
    }

    @Test
    public void testReadHourSuccess() {
        Hour hour = hourDAO.readHour(2);

        assertNotNull(hour, "Week should not be null for id 1");


        assertEquals("01:00 - 02:00", hour.getName(), "The name of the hour should match the expected value");
    }



    @Test
    public void testReadHourNotFound() {
        Hour hour = hourDAO.readHour(999); // Asumiendo que 999 no existe
        assertNull(hour, "La hora debería ser null si no existe");
    }

    @Test
    public void testGetAllHoursSuccess() {
        hourDAO.addNewHour(new Hour("10:00", 1));
        hourDAO.addNewHour(new Hour("11:00", 1));

        List<Hour> hours = hourDAO.getAllHours();

        assertFalse(hours.isEmpty(), "La lista de horas no debería estar vacía");
        assertTrue(hours.size() >= 2, "Debería haber al menos dos horas en la base de datos");
    }

    @Test
    public void testUpdateHourSuccess() {

        Hour hour = hourDAO.readHour(1);
        hour.setName("00:30 - 01:00");
        ResultDataBase result = hourDAO.updateHour(hour);
        assertTrue(result.getSuccess());

    }

    @Test
    public void testDeleteHourSuccess() {


        hourDAO.deleteHour(25);
        assertNull(hourDAO.readHour(25));

    }
}
