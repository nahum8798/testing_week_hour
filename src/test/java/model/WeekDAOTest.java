package model;

import model.Week;
import model.WeekDAO;
import connection.ResultDataBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WeekDAOTest {

    private WeekDAO weekDAO;
    private Week testWeek;

    @BeforeEach
    void setUp() {
        weekDAO = new WeekDAO();

        // Crear una instancia de Week para pruebas
        testWeek = new Week();
        testWeek.setName("Test Week");
        testWeek.setId_type_jornal(1); // ejemplo de id_type_jornal
    }

    @Test
    void testAddNewWeek() {
        // Agregar una nueva semana y verificar que fue exitosa
        ResultDataBase result = weekDAO.addNewWeek(testWeek);
        assertTrue(result.getSuccess(), "The week should be added successfully.");

        // Recuperar la última semana insertada
        List<Week> weeks = weekDAO.getAllWeeks();
        assertFalse(weeks.isEmpty(), "There should be at least one week in the database.");

        Week addedWeek = weeks.get(weeks.size() - 1);  // Suponiendo que la semana recién agregada está al final
        assertNotNull(addedWeek, "The week should exist in the database after being added.");
        assertEquals(testWeek.getName(), addedWeek.getName(), "The week name should match.");
    }

    @Test
    public void testReadWeek() {
        Week week = weekDAO.readWeek(1);

        // Verifica que week no sea nulo
        assertNotNull(week, "Week should not be null for id 22");

        // Verifica que el nombre de la semana sea el esperado
        assertEquals("lunes", week.getName(), "The name of the week should match the expected value");
    }

    @Test
    public void testGetAllWeeks() {
        List<Week> weeks = weekDAO.getAllWeeks();
        assertFalse(weeks.isEmpty()); // Supone que hay datos
    }

    @Test
    public void testUpdateWeek() {
        Week week = weekDAO.readWeek(1);
        week.setName("lunes");
        ResultDataBase result = weekDAO.updateWeek(week);
        assertTrue(result.getSuccess());
    }

    @Test
    public void testDeleteWeek() {
        weekDAO.deleteWeek(25); // Supone que 1 existe
        assertNull(weekDAO.readWeek(25)); // Asegura que ha sido eliminada
    }



}
