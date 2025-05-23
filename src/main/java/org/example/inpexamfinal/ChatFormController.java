package org.example.inpexamfinal;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;

public class ChatFormController {

    @FXML
    private Label NameLabel;

    @FXML
    private VBox Vbox;


    @FXML
    private Button btnSend;


    @FXML
    private TextField txtinput;

    private Socket socket;
    private String username;
    private PrintWriter writer;
    private BufferedReader reader;


    //Set user name to the relevant label
    public void setUserName(String username) {
        this.username=username;
        NameLabel.setText("Client:"+username);
        connectToServer();

    }

    private void connectToServer() {
        try{
            //Give host and port number to the Socket
            socket=new Socket("localhost",5000);
            //Get Input
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Get Output and autoFlush true
            writer=new PrintWriter(socket.getOutputStream(),true);

            //Send Username to The Server
            writer.println(username);

            //Initialize New Thread
            new Thread(this::receiveMessage).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() {
        try{
            String message;
            //check the message is Null
            while ((message=reader.readLine())!=null){
                String finalMessage=message;

                //Execute in A New PlatForm
                Platform.runLater(()->{
                    boolean isCurrentUser=finalMessage.startsWith(username+":");


                    //put the message to the Vbox
                    Label label=new Label(finalMessage);
                    label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size: 14px;");
                    HBox hBox=new HBox(label);
                    //give Alignment to the message
                    hBox.setAlignment(isCurrentUser? Pos.CENTER_RIGHT:Pos.CENTER_LEFT);
                    Vbox.getChildren().add(hBox);

                });
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }




    @FXML
    void btnSendOnAction(ActionEvent event) {
        String message=username+":"+txtinput.getText();
        if (!message.trim().isEmpty()){

            //send the messagee to the Server
            writer.println(message);
            writer.flush();



            //put the message to the Vbox
            Label label=new Label(message);
            label.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-weight: bold; -fx-font-size: 14px;");
            HBox hBox=new HBox(label);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            Vbox.getChildren().add(hBox);

            txtinput.clear();


        }
    }

}
