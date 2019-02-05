package mypackage;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import mypackage.interfaces.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
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
    private Label interfaceLabel;

    @FXML
    private Label resultLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextField firstArgumentTextField;

    @FXML
    private TextField secondArgumentTextField;

    @FXML
    private TextArea textArea = new TextArea();

    @FXML
    void initialize() {
        fileLoader = new FileLoader(this);
        directoryChosen = new File("compiled");
        pathLabel.setText(directoryChosen.getAbsolutePath());
        resetLabels();
    }

    @FXML
    void openMenuItemClicked() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();

        File currentDirFile = new File("compiled");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select directory with jars and/or .class files.");
        directoryChooser.setInitialDirectory(currentDirFile);
        directoryChosen = directoryChooser.showDialog(stage);
        if (directoryChosen == null) {
            pathLabel.setText("Choose directory");
        } else {
            pathLabel.setText(directoryChosen.getAbsolutePath());
        }
    }

    @FXML
    void loadMenuItemClicked() throws Exception {
        textArea.setText("");
        if (directoryChosen == null) {
            return;
        }
        clearComboBox(methodComboBox);
        fileLoader.addFilesFromDirectory(directoryChosen);
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
        interfaceLabel.setText(interfacesName(method.getDeclaringClass().getGenericInterfaces()));
        signatureLabel.setText(method.toString());

        MetaData description = method.getAnnotation(MetaData.class);
        if (description != null) {
            descriptionLabel.setText(description.metadata());
        }

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
            Object object = method.getDeclaringClass().getConstructor().newInstance();

            Type type = method.getDeclaringClass().getGenericInterfaces()[0];
            String interfaceName = type.getTypeName();
            String methodName = method.getName();
            switch (interfaceName) {
                case "mypackage.interfaces.IntegerOperable":
                    IntegerOperable integerOperable = (IntegerOperable) object;
                    Integer int1, int2, int3;
                    switch (methodName) {
                        case "max":
                            int1 = Integer.parseInt(firstArgumentTextField.getText());
                            int2 = Integer.parseInt(secondArgumentTextField.getText());
                            int3 = integerOperable.max(int1, int2);
                            resultLabel.setText(int3.toString());
                            break;
                        case "min":
                            int1 = Integer.parseInt(firstArgumentTextField.getText());
                            int2 = Integer.parseInt(secondArgumentTextField.getText());
                            int3 = integerOperable.min(int1, int2);
                            resultLabel.setText(int3.toString());
                            break;
                        case "add":
                            int1 = Integer.parseInt(firstArgumentTextField.getText());
                            int2 = Integer.parseInt(secondArgumentTextField.getText());
                            int3 = integerOperable.add(int1, int2);
                            resultLabel.setText(int3.toString());
                            break;
                        case "subtract":
                            int1 = Integer.parseInt(firstArgumentTextField.getText());
                            int2 = Integer.parseInt(secondArgumentTextField.getText());
                            int3 = integerOperable.subtract(int1, int2);
                            resultLabel.setText(int3.toString());
                            break;
                        case "multiply":
                            int1 = Integer.parseInt(firstArgumentTextField.getText());
                            int2 = Integer.parseInt(secondArgumentTextField.getText());
                            int3 = integerOperable.multiply(int1, int2);
                            resultLabel.setText(int3.toString());
                            break;
                    }
                    break;
                case "mypackage.interfaces.BooleanOperable":
                    BooleanOperable booleanOperable = (BooleanOperable) object;
                    Boolean boolean1, boolean2, boolean3;
                    if((!(firstArgumentTextField.getText().equals("true") || firstArgumentTextField.getText().equals("false"))) ||
                            (!(secondArgumentTextField.getText().equals("true") || secondArgumentTextField.getText().equals("false")))) {
                        throw new IllegalArgumentException();
                    }
                    switch (methodName) {
                        case "and":
                            boolean1 = Boolean.parseBoolean(firstArgumentTextField.getText());
                            boolean2 = Boolean.parseBoolean(secondArgumentTextField.getText());
                            boolean3 = booleanOperable.and(boolean1, boolean2);
                            resultLabel.setText(boolean3.toString());
                            break;
                        case "or":
                            boolean1 = Boolean.parseBoolean(firstArgumentTextField.getText());
                            boolean2 = Boolean.parseBoolean(secondArgumentTextField.getText());
                            boolean3 = booleanOperable.or(boolean1, boolean2);
                            resultLabel.setText(boolean3.toString());
                            break;
                        case "xor":
                            boolean1 = Boolean.parseBoolean(firstArgumentTextField.getText());
                            boolean2 = Boolean.parseBoolean(secondArgumentTextField.getText());
                            boolean3 = booleanOperable.xor(boolean1, boolean2);
                            resultLabel.setText(boolean3.toString());
                            break;
                    }
                    break;
                case "mypackage.interfaces.ByteOperable":
                    ByteOperable byteOperable = (ByteOperable) object;
                    Byte byte1, byte2;
                    Integer integer3;
                    switch (methodName) {
                        case "and":
                            byte1 = Byte.parseByte(firstArgumentTextField.getText());
                            byte2 = Byte.parseByte(secondArgumentTextField.getText());
                            integer3 = byteOperable.and(byte1, byte2);
                            resultLabel.setText(integer3.toString());
                            break;
                        case "or":
                            byte1 = Byte.parseByte(firstArgumentTextField.getText());
                            byte2 = Byte.parseByte(secondArgumentTextField.getText());
                            integer3 = byteOperable.or(byte1, byte2);
                            resultLabel.setText(integer3.toString());
                            break;
                        case "xor":
                            byte1 = Byte.parseByte(firstArgumentTextField.getText());
                            byte2 = Byte.parseByte(secondArgumentTextField.getText());
                            integer3 = byteOperable.xor(byte1, byte2);
                            resultLabel.setText(integer3.toString());
                            break;
                        case "negate":
                            byte1 = Byte.parseByte(firstArgumentTextField.getText());
                            integer3 = byteOperable.negate(byte1);
                            resultLabel.setText(integer3.toString());
                            break;
                    }
                    break;
                case "mypackage.interfaces.StringOperable":
                    StringOperable stringOperable = (StringOperable) object;
                    String string1, string2;
                    String string3;
                    Integer s1;
                    switch (methodName) {
                        case "concat":
                            string1 = firstArgumentTextField.getText();
                            string2 = secondArgumentTextField.getText();
                            string3 = stringOperable.concat(string1, string2);
                            resultLabel.setText(string3);
                            break;
                        case "toLowerCase":
                            string1 = firstArgumentTextField.getText();
                            string3 = stringOperable.toLowerCase(string1);
                            resultLabel.setText(string3);
                            break;
                        case "toUpperCase":
                            string1 = firstArgumentTextField.getText();
                            string3 = stringOperable.toUpperCase(string1);
                            resultLabel.setText(string3);
                            break;
                        case "length":
                            string1 = firstArgumentTextField.getText();
                            s1 = stringOperable.length(string1);
                            resultLabel.setText(s1.toString());
                            break;
                    }
                break;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            errorLabel.setVisible(true);

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


    void appendTextArea(String s) {
        textArea.appendText(s);
    }

    private void resetLabels() {
        errorLabel.setVisible(false);
        nameLabel.setText("-");
        classNameLabel.setText("-");
        returnTypeLabel.setText("-");
        argumentsLabel.setText("-");
        interfaceLabel.setText("-");
        signatureLabel.setText("-");
        resultLabel.setText("-");
        descriptionLabel.setText("-");
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

    private String interfacesName(Type[] types) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Type type : types) {
            stringBuilder.append(type);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

}
