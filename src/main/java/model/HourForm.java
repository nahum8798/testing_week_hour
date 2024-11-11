package model;

import connection.ResultDataBase;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class HourForm extends JFrame {
    private JTextField nameField; // Campo para el nombre de la hora
    private JSpinner idSpinner; // Selector para el ID de la hora
    private JTextField idWeekField; // Campo para ingresar el ID de la semana
    private JButton createButton; // Botón para crear una nueva hora
    private JButton readButton; // Botón para leer horas
    private JButton updateButton; // Botón para actualizar una hora
    private JButton deleteButton; // Botón para eliminar una hora
    private JTextArea outputArea; // Área de texto para mostrar resultados
    private HourDAO hourDAO; // Objeto para acceso a datos de horas

    /**
     * Constructor de la clase HourForm.
     * Inicializa los componentes de la interfaz y configura el formulario.
     */
    public HourForm() {
        hourDAO = new HourDAO();

        setTitle("Hour CRUD Form");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        nameField = new JTextField(20); // Campo para el nombre de la hora
        idSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        idWeekField = new JTextField(20); // Campo para ingresar el ID de la semana

        createButton = new JButton("Create");
        readButton = new JButton("Read");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        outputArea = new JTextArea(10, 50);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);

        // Configuración del diseño de la interfaz
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Hour Name:"), gbc); // Etiqueta para el nombre de la hora
        gbc.gridx = 1; gbc.gridy = 0;
        add(nameField, gbc); // Campo para el nombre de la hora

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Week ID:"), gbc); // Etiqueta para el ID de la semana
        gbc.gridx = 1; gbc.gridy = 1;
        add(idWeekField, gbc); // Campo para ingresar el ID de la semana

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("ID (for Update/Delete):"), gbc); // Etiqueta para el ID de actualización/eliminación
        gbc.gridx = 1; gbc.gridy = 2;
        add(idSpinner, gbc); // Selector para el ID de la hora

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(createButton, gbc); // Botón de crear
        gbc.gridy = 4;
        add(readButton, gbc); // Botón de leer
        gbc.gridy = 5;
        add(updateButton, gbc); // Botón de actualizar
        gbc.gridy = 6;
        add(deleteButton, gbc); // Botón de eliminar

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(new JScrollPane(outputArea), gbc); // Área de texto para resultados

        // Agregar acciones a los botones
        createButton.addActionListener(e -> createHour());
        readButton.addActionListener(e -> readHours());
        updateButton.addActionListener(e -> updateHour());
        deleteButton.addActionListener(e -> deleteHour());
    }

    /**
     * Crea una nueva hora y la agrega a la base de datos.
     */
    private void createHour() {
        String name = nameField.getText(); // Obtener el nombre
        String idWeekText = idWeekField.getText(); // Obtener el ID de la semana

        if (!name.isEmpty() && !idWeekText.isEmpty()) {
            try {
                int idWeek = Integer.parseInt(idWeekText); // Convertir a int
                Hour newHour = new Hour(name, idWeek); // Crear un nuevo objeto Hour
                ResultDataBase result = hourDAO.addNewHour(newHour); // Agregar a la base de datos
                outputArea.setText(result.getSuccess() ? "Hour created successfully." : "Error creating hour: " + result.getMessage());
            } catch (NumberFormatException ex) {
                outputArea.setText("Week ID must be a number."); // Manejar excepción de formato
            }
        } else {
            outputArea.setText("Hour name and week ID cannot be empty."); // Validar campos vacíos
        }
    }

    /**
     * Lee todas las horas de la base de datos y las muestra en el área de salida.
     */
    private void readHours() {
        List<Hour> hours = hourDAO.getAllHours(); // Obtener todas las horas
        StringBuilder sb = new StringBuilder();
        for (Hour hour : hours) {
            sb.append("ID: ").append(hour.getId_hour())
                    .append(", Name: ").append(hour.getName())
                    .append(", Week ID: ").append(hour.getId_week())
                    .append(", Created by: ").append(hour.getId_user_create())
                    .append("\n");
        }
        outputArea.setText(sb.toString()); // Mostrar en el área de salida
    }

    /**
     * Actualiza una hora existente en la base de datos.
     */
    private void updateHour() {
        try {
            int id = (Integer) idSpinner.getValue(); // Obtener ID del spinner
            String newName = nameField.getText(); // Obtener el nuevo nombre
            String idWeekText = idWeekField.getText(); // Obtener el ID de la semana
            Hour hour = hourDAO.readHour(id); // Leer la hora existente
            if (hour != null) {
                hour.setName(newName); // Actualizar el nombre
                if (!idWeekText.isEmpty()) {
                    int idWeek = Integer.parseInt(idWeekText); // Convertir a int
                    hour.setId_week(idWeek); // Cambiar el ID de la semana
                }
                ResultDataBase result = hourDAO.updateHour(hour); // Actualizar en la base de datos
                outputArea.setText(result.getSuccess() ? "Hour updated successfully." : "Error updating hour: " + result.getMessage());
            } else {
                outputArea.setText("Hour with ID " + id + " not found."); // Manejar hora no encontrada
            }
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage()); // Manejar excepciones
        }
    }

    /**
     * Elimina una hora de la base de datos.
     */
    private void deleteHour() {
        try {
            int id = (Integer) idSpinner.getValue(); // Obtener ID del spinner
            ResultDataBase result = hourDAO.deleteHour(id); // Eliminar de la base de datos
            outputArea.setText(result.getSuccess() ? "Hour deleted successfully." : "Error deleting hour: " + result.getMessage());
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage()); // Manejar excepciones
        }
    }

    /**
     * Método principal que inicia la aplicación.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HourForm().setVisible(true)); // Iniciar la interfaz gráfica
    }
}

