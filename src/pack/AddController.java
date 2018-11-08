package pack;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class AddController {
    @FXML
    TableView tvElemek;

    @FXML
    TextField tfNev;

    @FXML
    public void hozzaad(ActionEvent e) {
        ObservableList<osszetevo> adalek = FXCollections.observableArrayList();

        boolean lefut = true;

        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).getGr().equals("0")) {
                try {
                    //vizsgálat
                    Integer.parseInt(data.get(i).getGr());
                    adalek.add(data.get(i));
                } catch (Exception ex) {
                    i = data.size();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Hiba");
                    alert.setContentText("Helytelen adat");
                    alert.showAndWait();
                    lefut = false;
                }
            }
        }
        if(lefut){
            //db rögzites
            getDb().ujTermek(tfNev.getText(), adalek);
        }

        setData(getDb().getElemek());
        tvElemek.setItems(data);
        tfNev.clear();
    }

    ObservableList<osszetevo> data = FXCollections.observableArrayList();

    private DB db;

    public void setDb(DB db) {
        this.db = db;
    }

    public DB getDb() {
        return this.db;
    }

    public void setData(ObservableList<osszetevo> data) {
        this.data = data;
    }

    public void feltolt(DB db) {
        setDb(db);

        setData(getDb().getElemek());

        TableColumn nev = new TableColumn("Összetevő");
        nev.setMinWidth(150);
        nev.setCellFactory(TextFieldTableCell.forTableColumn());
        nev.setCellValueFactory(new PropertyValueFactory<osszetevo, String>("nev"));
        nev.setStyle("-fx-alignment: CENTER;");
        nev.setEditable(false);

        TableColumn gr = new TableColumn("gr");
        gr.setMinWidth(100);
        gr.setCellFactory(TextFieldTableCell.forTableColumn());
        gr.setCellValueFactory(new PropertyValueFactory<osszetevo, String>("gr"));
        gr.setStyle("-fx-alignment: CENTER;");

        gr.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<osszetevo, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<osszetevo, String> t) {
                    osszetevo o = (osszetevo) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    o.setGr(t.getNewValue());
            }

        });





        tvElemek.getColumns().addAll(nev, gr);
        tvElemek.setItems(data);
    }
}
