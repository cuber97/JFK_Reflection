package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sample.interfaces.BooleanOperable;
import sample.interfaces.ByteOperable;
import sample.interfaces.IntegerOperable;
import sample.interfaces.StringOperable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private ComboBox<Method> methodComboBox;

    @FXML
    private Label pathLabel;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button executeButton;

    @FXML
    private File directoryChosen;

    @FXML
    private FileLoader fileLoader;

    @FXML
    private Label nameLabel;

    @FXML
    private Label classNameLabel;

    @FXML
    private Label returnTypeLabel;

    @FXML
    private Label argumentsLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Label signatureLabel;

    @FXML
    private Label resultLabel;

    @FXML
    private TextField firstArgumentTextField;

    @FXML
    private TextField secondArgumentTextField;

    @FXML
    private TextArea textArea;

    @FXML
    void initialize() {
        fileLoader = new FileLoader();
        directoryChosen = new File(".");
        pathLabel.setText(directoryChosen.getAbsolutePath());
        resetLabels();
    }

    @FXML
    void openMenuItemClicked() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();

        File currentDirFile = new File(".");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select directory with jars and/or .class files.");
        directoryChooser.setInitialDirectory(currentDirFile);
        directoryChosen = directoryChooser.showDialog(stage);
        if (directoryChosen == null) {
            pathLabel.setText("Wybierz katalog");
        } else {
            pathLabel.setText(directoryChosen.getAbsolutePath());
        }
    }

    @FXML
    void loadMenuItemClicked() throws Exception {
        textArea.setText("");
        if(directoryChosen == null) {
            return;
        }
        clearComboBox(methodComboBox);
        System.out.println(directoryChosen);
        fileLoader.addFilesFromDirectory(directoryChosen, textArea);
        fillComboBox(methodComboBox, fileLoader.getMethodNames());

    }

    @FXML
    void methodComboBoxSelected() {
        resetLabels();

        if (methodComboBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        executeButton.setDisable(false);
        Method method = methodComboBox.getSelectionModel().getSelectedItem();
        nameLabel.setText(method.getName());
        classNameLabel.setText(method.getDeclaringClass().toString());
        returnTypeLabel.setText(method.getGenericReturnType().toString());
        argumentsLabel.setText(typesName(method.getGenericParameterTypes()));
        signatureLabel.setText(method.toString());

        switch (method.getParameterCount()) {
            case 1:
                firstArgumentTextField.setDisable(false);
                secondArgumentTextField.setDisable(true);
                break;
            case 2:
                firstArgumentTextField.setDisable(false);
                secondArgumentTextField.setDisable(false);
                break;
        }
    }

    @FXML
    void executeButtonClicked() {
        resultLabel.setText("-");
        errorLabel.setVisible(false);
        Method method = methodComboBox.getSelectionModel().getSelectedItem();
        try {
        List<Object> args = fillArguments(method);
            Object result = method.invoke(method.getDeclaringClass().newInstance(), args.toArray());
            resultLabel.setText(result.toString());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            errorLabel.setVisible(true);

        } catch (InvocationTargetException e) {

            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void fillComboBox(ComboBox<Method> comboBox, List<Method> list) {
        for (Method method : list) {
            comboBox.getItems().add(method);
        }
    }

    private void clearComboBox(ComboBox<Method> comboBox) {
        comboBox.getItems().clear();
    }

    private List<Object> fillArguments(Method method) throws IllegalArgumentException {
        List<Object> args = new ArrayList<>();
        Class<?>[] interfaces = method.getDeclaringClass().getInterfaces();
        System.out.println("Interfaces: " + method.getDeclaringClass());

        for (Class<?> i : interfaces) {
            System.out.println(i.getName());
            if (i.getName().equals(BooleanOperable.class.getName())) {
                if (!(firstArgumentTextField.getText().equals("true") || firstArgumentTextField.getText().equals("false"))
                        || !(secondArgumentTextField.getText().equals("true") || secondArgumentTextField.getText().equals("false"))) {
                    throw new IllegalArgumentException();
                }
                switch (method.getParameterCount()) {
                    case 1:
                        args.add(Boolean.parseBoolean(firstArgumentTextField.getText()));
                        break;
                    case 2:
                        args.add(Boolean.parseBoolean(firstArgumentTextField.getText()));
                        args.add(Boolean.parseBoolean(secondArgumentTextField.getText()));
                        break;
                }
            }
            if (i.getName().equals(StringOperable.class.getName())) {
                switch (method.getParameterCount()) {
                    case 1:
                        args.add(firstArgumentTextField.getText());
                        break;
                    case 2:
                        args.add(firstArgumentTextField.getText());
                        args.add(secondArgumentTextField.getText());
                        break;
                }
            }
            if (i.getName().equals(ByteOperable.class.getName())) {
                switch (method.getParameterCount()) {
                    case 1:
                        args.add(Byte.parseByte(firstArgumentTextField.getText()));
                        break;
                    case 2:
                        args.add(Byte.parseByte(firstArgumentTextField.getText()));
                        args.add(Byte.parseByte(secondArgumentTextField.getText()));
                        break;
                }
            }
            if (i.getName().equals(IntegerOperable.class.getName())) {
                switch (method.getParameterCount()) {
                    case 1:
                        args.add(Integer.parseInt(firstArgumentTextField.getText()));
                        break;
                    case 2:
                        args.add(Integer.parseInt(firstArgumentTextField.getText()));
                        args.add(Integer.parseInt(secondArgumentTextField.getText()));
                        break;
                }
            }
        }
        return args;
    }

    private void resetLabels() {
        errorLabel.setVisible(false);
        nameLabel.setText("-");
        classNameLabel.setText("-");
        returnTypeLabel.setText("-");
        argumentsLabel.setText("-");
        signatureLabel.setText("-");
        resultLabel.setText("-");
        firstArgumentTextField.setText("");
        secondArgumentTextField.setText("");
        executeButton.setDisable(true);
        firstArgumentTextField.setDisable(true);
        secondArgumentTextField.setDisable(true);
        textArea.setEditable(false);
    }

    private String typesName(Type[] types) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Type type : types) {
            stringBuilder.append(type);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

}
