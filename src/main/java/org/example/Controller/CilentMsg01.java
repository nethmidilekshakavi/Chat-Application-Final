package org.example.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class CilentMsg01 {
    @FXML
    private TextField txtMessage;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox messageVBox;

    @FXML
    private Button btnSend;

    @FXML
    private Button btnImage;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String message= "";

    @FXML
    public void initialize() {
        try {

            if (messageVBox != null) {
                messageVBox.setSpacing(10);
                scrollPane.setContent(messageVBox);
                scrollPane.setFitToWidth(true);

                messageVBox.heightProperty().addListener((observable, oldValue, newValue) ->
                        scrollPane.setVvalue(1.0));
            } else {
                System.err.println("Error: messageVBox is null. Check FXML file for proper fx:id");
                return;
            }

            new Thread(() -> {
                try {
                    socket = new Socket("localhost", 3000);
                    Platform.runLater(() -> addMessage("Client Connected"));

                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    while (!message.equals("Bye")) {
                        message = dataInputStream.readUTF();

                        if (message.startsWith("[IMAGE]")) {
                            String imagePath = message.substring(7);
                            Platform.runLater(() -> {
                                displayImage(imagePath);
                                /* addMessage("Server: [Image Received]");*/
                            });
                        } else {
                            Platform.runLater(() -> addMessage("Server: " + message));
                        }
                    }
                } catch (IOException e) {
                    Platform.runLater(() -> addMessage("Error: Server not found or disconnected"));
                    e.printStackTrace();
                }
            }).start();


            new Thread(() -> {
                try {
                    socket = new Socket("localhost", 3000);
                    Platform.runLater(() -> addMessage("Client Connected"));

                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    while (!message.equals("Bye")) {
                        message = dataInputStream.readUTF();

                        if (message.startsWith("[IMAGE]")) {
                            String imagePath = message.substring(7);
                            Platform.runLater(() -> {
                                displayImage(imagePath);
                                /* addMessage("Server: [Image Received]");*/
                            });
                        } else {
                            Platform.runLater(() -> addMessage("Server: " + message));
                        }
                    }
                } catch (IOException e) {
                    Platform.runLater(() -> addMessage("Error: Server not found or disconnected"));
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMessage(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        messageVBox.getChildren().add(label);
    }

    private void displayImage(String imagePath) {
        try {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            messageVBox.getChildren().add(imageView);
        } catch (Exception e) {
            addMessage("Error loading image");
            e.printStackTrace();
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        try {
            String message = txtMessage.getText().trim();
            if (!message.isEmpty()) {
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                addMessage("Client: " + message);
                txtMessage.clear();
            }
        } catch (IOException e) {
            addMessage("Error: Failed to send message");
            e.printStackTrace();
        }
    }

    @FXML
    void btnImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                displayImage(selectedFile.getPath());
                dataOutputStream.writeUTF("[IMAGE]" + selectedFile.getPath());
                dataOutputStream.flush();
                addMessage("Client: [Image Sent]");
            } catch (IOException e) {
                addMessage("Error: Failed to send image");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void txtMsgOnAction(ActionEvent event) {
        btnSendOnAction(event);
    }
}