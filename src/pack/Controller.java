package pack;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Controller implements Initializable {

    DB db = new DB();

    public boolean tobb = true;
    
    @FXML
    private ListView<dzsuz> lvKosar;

    @FXML
    private RadioButton rbKicsi;

    @FXML
    private RadioButton rbNagy;

    @FXML
    private ListView<String> lvNevek;

    @FXML
    private TextField tfNev;

    @FXML
    private ListView<dzsuz> lvTermekek;

    @FXML
    private void kilep(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void kosarTorles(ActionEvent event) {
        lvKosar.getItems().clear();
        tobb = true;
    }

    @FXML
    private void kosarKesz(ActionEvent event) {
        String nev = tfNev.getText();
      
        for (dzsuz dzs : lvKosar.getItems()) {
            db.kosarHozzaad(dzs, nev);
            //System.out.println(dzs.getKn());
        }
        tobb = true;
        lvKosar.getItems().clear();
        tfNev.clear();
        nevekFeltolt();
    }

    @FXML
    private void eladasok(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Adatok.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 737, 577);
            Stage stage = new Stage();
            stage.setTitle("Eladások");
            stage.setScene(scene);
            stage.show();

            AdatokController ac = fxmlLoader.getController();

            ac.feltolt(db);

        } catch (IOException e) {
            System.out.println("valami baj van az uj termek ablak megnyitasakor" + e);
        }
    }

    @FXML
    private void termeles(ActionEvent aevent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Termeles.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 600, 600);
            Stage stage = new Stage();
            stage.setTitle("Termelés");
            stage.setScene(scene);
            stage.show();

            TermelesController hc = fxmlLoader.getController();
            hc.feltolt(db);

        } catch (IOException e) {
            System.out.println("valami baj van az uj termek ablak megnyitasakor" + e);
        }
    }
    
    @FXML
    private void termekek(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Termekek.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 596, 540);
            Stage stage = new Stage();
            stage.setTitle("Termékek");
            stage.setScene(scene);

            stage.show();

            TermekekController tc = fxmlLoader.getController();
            tc.betolt(db);
            stage.setOnCloseRequest(event1 -> {
                lvTermekek.getItems().setAll(db.getTermekNevek());

            });
        } catch (IOException e) {
            System.out.println("valami baj van az uj termek ablak megnyitasakor" + e);
        }
    }

    @FXML
    private void ujTermek(ActionEvent aevent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Add.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 300, 600);
            Stage stage = new Stage();
            stage.setTitle("Új termék hozzáadása");
            stage.setScene(scene);
            stage.show();

            AddController hc = fxmlLoader.getController();
            hc.feltolt(db);

            stage.setOnCloseRequest(event -> {
                lvTermekek.getItems().setAll(db.getTermekNevek());

            });

        } catch (IOException e) {
            System.out.println("valami baj van az uj termek ablak megnyitasakor" + e);
        }
    }

    @FXML
    private void hozzaad(ActionEvent event) {
        dzsuz dzs = lvTermekek.getSelectionModel().getSelectedItem();
        if (rbKicsi.isSelected()) {
            dzs.setKn("kicsi");

        }
        if (rbNagy.isSelected()) {
            dzs.setKn("nagy");

        }

        if (dzs.getKn().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba!");
            alert.setHeaderText(null);
            alert.setContentText("Kérem válasszon méretet!");

            alert.showAndWait();
        } else {
            lvKosar.getItems().add(new dzsuz(Integer.parseInt(dzs.getId()), dzs.getNev(), dzs.getKn()));
        }

        int osszkg = 0;

        for (dzsuz item : lvKosar.getItems()) {
            for (osszetevo o : db.getOsszetevok(Integer.parseInt(item.getId()))) {
                osszkg += item.getKn().equals("kicsi") ? Integer.parseInt(o.getGr()) : Integer.parseInt(o.getGrn());

                //ha nagyobb, mint 210 kilo, akkor jelezze
            }

        }
        if (osszkg > 10000) {
            if (tobb) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Több kör");
                alert.setHeaderText(null);
                alert.setContentText("Az össztömeg több, mint 10 kiló!");

                alert.showAndWait();
                tobb = false;
            }
        }
        dzs = null;
    }

    public void nevekFeltolt(){
        lvNevek.getItems().clear();
        lvNevek.getItems().setAll(db.getVevoNevek());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lvTermekek.getItems().setAll(db.getTermekNevek());
        lvTermekek.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lvTermekek.getSelectionModel().selectFirst();
        nevekFeltolt();
        
        lvNevek.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            String kivalasztott = "";
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tfNev.setText(newValue);
            }
           
        });

        //listview törlés action


        lvNevek.setCellFactory(lv -> {

            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Törlés \"%s\"", cell.itemProperty()));
            deleteItem.setOnAction(event ->{
                String nev = cell.itemProperty().getValue();
                db.nevTorles(nev);
                nevekFeltolt();


            });
            contextMenu.getItems().addAll(deleteItem);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });


        //listview törlés action vége


    }
}
