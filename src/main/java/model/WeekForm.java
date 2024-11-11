package model;

import connection.ResultDataBase;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class WeekForm extends JFrame {
    private JTextField nameField;
    private JTextField idTypeJornalField;
    private JSpinner idSpinner;
    private JButton createButton;
    private JButton readButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextArea outputArea;
    private WeekDAO weekDAO;

    public WeekForm() {
        weekDAO = new WeekDAO();

        setTitle("Week CRUD Form");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        nameField = new JTextField(20);
        idTypeJornalField = new JTextField(20);
        idSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        createButton = new JButton("Create");
        readButton = new JButton("Read");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        outputArea = new JTextArea(10, 50);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Week Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Type Jornal ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        add(idTypeJornalField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("ID (for Update/Delete):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        add(idSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(createButton, gbc);
        gbc.gridy = 4;
        add(readButton, gbc);
        gbc.gridy = 5;
        add(updateButton, gbc);
        gbc.gridy = 6;
        add(deleteButton, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(new JScrollPane(outputArea), gbc);

        createButton.addActionListener(e -> createWeek());
        readButton.addActionListener(e -> readWeeks());
        updateButton.addActionListener(e -> updateWeek());
        deleteButton.addActionListener(e -> deleteWeek());
    }

    private void createWeek() {
        String name = nameField.getText();
        String idTypeJornalText = idTypeJornalField.getText();

        if (!name.isEmpty() && !idTypeJornalText.isEmpty()) {
            try {
                int idTypeJornal = Integer.parseInt(idTypeJornalText);
                Week newWeek = new Week();
                newWeek.setName(name);
                newWeek.setId_type_jornal(idTypeJornal);
                ResultDataBase result = weekDAO.addNewWeek(newWeek);
                outputArea.setText(result.getSuccess() ? "Week created successfully." : "Error creating week: " + result.getMessage());
            } catch (NumberFormatException ex) {
                outputArea.setText("Type Jornal ID must be a number.");
            }
        } else {
            outputArea.setText("Please fill all fields.");
        }
    }

    private void readWeeks() {
        List<Week> weeks = weekDAO.getAllWeeks();
        StringBuilder resultText = new StringBuilder();

        for (Week week : weeks) {
            resultText.append("ID: ").append(week.getId_week())
                    .append(", Name: ").append(week.getName())
                    .append(", Type Jornal ID: ").append(week.getId_type_jornal())
                    .append(", Created by User: ").append(week.getId_user_create())
                    .append(", Updated by User: ").append(week.getId_user_update())
                    //.append(", Created at: ").append(week.getDate_create())
                    .append(", Updated at: ").append(week.getDate_update()).append("\n");
        }
        outputArea.setText(resultText.toString());
    }

    private void updateWeek() {
        int idWeek = (int) idSpinner.getValue();
        String name = nameField.getText();
        String idTypeJornalText = idTypeJornalField.getText();

        if (!name.isEmpty() && !idTypeJornalText.isEmpty()) {
            try {
                int idTypeJornal = Integer.parseInt(idTypeJornalText);
                Week week = weekDAO.readWeek(idWeek);

                if (week != null) {
                    week.setName(name);
                    week.setId_type_jornal(idTypeJornal);
                    ResultDataBase result = weekDAO.updateWeek(week);
                    outputArea.setText(result.getSuccess() ? "Week updated successfully." : "Error updating week: " + result.getMessage());
                } else {
                    outputArea.setText("Week not found.");
                }
            } catch (NumberFormatException ex) {
                outputArea.setText("Type Jornal ID must be a number.");
            }
        } else {
            outputArea.setText("Please fill all fields.");
        }
    }

    private void deleteWeek() {
        int idWeek = (int) idSpinner.getValue();
        ResultDataBase result = weekDAO.deleteWeek(idWeek);
        outputArea.setText(result.getSuccess() ? "Week deleted successfully." : "Error deleting week: " + result.getMessage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WeekForm form = new WeekForm();
            form.setVisible(true);
        });
    }
}
