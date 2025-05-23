package org.example.inpexamfinal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {

    @FXML
    private TextField PasswordTextFeild;

    @FXML
    private TextField UserNameTextFeild;

    @FXML
    void LoginButtonOnAction(ActionEvent event) {
        //get the Username and Password
        String password=PasswordTextFeild.getText();
        String username=UserNameTextFeild.getText();
         //check both user and password is correct using valid Login
        if (isValidLogin(username,password)){
            //if valid login open the window of Client
            openChatWindow(username);
        }else{
            //if not case an error
            showAlert("Invalid Login", "Incorrect username or password");
        }
    }

    private void openChatWindow(String username) {
        try{
            //Load the chat-Form Fxml
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/View/Chat-Form.fxml"));

            Parent root=loader.load();

            ChatFormController chatFormController=loader.getController();
            //Send to the ChatFormController to set the username to the Label
            chatFormController.setUserName(username);
            clearFields();

            //show the Stage
            Stage stage=new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Chat- "+username);
            stage.show();


        }catch (IOException e){
            //If not case the eror
            e.printStackTrace();
            showAlert("Error", "Unable to Open chat Window");
        }
    }

    private void showAlert(String title, String message) {
        //Reqiured Instances show alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        //clear the text after the task is done
        PasswordTextFeild.clear();
        UserNameTextFeild.clear();
    }

    private boolean isValidLogin(String username, String password) {
        //check is the valid login
        return !username.isEmpty() && password.equals("1234");
    }

}
