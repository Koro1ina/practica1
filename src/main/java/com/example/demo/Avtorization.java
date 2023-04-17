package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Avtorization implements Initializable {

    @FXML
    private Button reserch;

    @FXML
    private TextField password;
    @FXML
    private  TextField code;
    @FXML
    private Button vxod;
    @FXML
    private Label errors;

    @FXML
    private TextField number;

    DB db = new DB();

    int countdownStarter = 10;
    String builder = new String();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ImageView imageView = new ImageView(new Image("onovlenie.jpg",20, 20, false, true ));
       // reserch.setGraphic(imageView);
    }

    @FXML
    private void onPassword(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            if ((db.getPassword(number.getText()).equals(password.getText()))) {
                AlertDialog();
            } else {
                errors.setText("Введен неверный пароль");
            }

        }
    }

    private void AlertDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        final char[] captchaSymbols = {
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        };



        Random random = new Random();
        for (int i = 0; i < 8; i++){
            builder += captchaSymbols[random.nextInt(captchaSymbols.length)];
            System.out.println(i);
        }
        System.out.println(builder);
        alert.setHeaderText(String.valueOf(builder));
        Optional<ButtonType> op = alert.showAndWait();
        if(op.get() == ButtonType.OK){
            code.setDisable(false);
            vxod.setDisable(false);
            reserch.setDisable(false);
            final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            final Runnable runnable = new Runnable() {


                public void run() {

                    System.out.println(countdownStarter);
                    countdownStarter--;

                    if (countdownStarter < 0) {
                        scheduler.shutdown();
                    }
                }
            };
            scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);
        }
    }

    public void onCodeAvtorization(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(builder.toString().equals(code.getText()) && countdownStarter > 0){
            errors.setText("Ваша роль: "+db.getRole(number.getText()));
        }else if (builder.toString().equals(code.getText()) && countdownStarter <= 0){
            errors.setText("Истекло время кода");
        }else {
            errors.setText("Неверно введен код");
        }
    }

    @FXML
    private void onNumber(KeyEvent keyEvent) throws ClassNotFoundException, SQLException {
        if(keyEvent.getCode() == KeyCode.ENTER){
            String textNumber = number.getText();
            Integer proverca = db.getSotrydnic(textNumber);

            if(proverca == 1){
            password.setDisable(false);
            errors.setText(" ");

            }else{
                errors.setText("Такого номера нет в базе данных.");
            }
        }


    }

    public void onNewCode(ActionEvent actionEvent){
        countdownStarter = 10;
        builder = "";
        AlertDialog();
    }

    public void onOtmena(ActionEvent actionEvent){
        code.clear();
        number.clear();
        password.clear();
    }

}