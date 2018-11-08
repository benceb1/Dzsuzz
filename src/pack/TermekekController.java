package pack;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class TermekekController {
    DB db;

    @FXML
    private ListView<dzsuz> lvTermekek;

    @FXML
    private TableView tvTermekek;

    ObservableList<osszetevo> data;

    @FXML
    public void termekTorles(ActionEvent event){
        dzsuz item = lvTermekek.getSelectionModel().getSelectedItem();
        db.elemTorles(Integer.parseInt(item.getId()));
        lvTermekek.getItems().clear();
        tvTermekek.getItems().clear();
        lvTermekek.getItems().setAll(db.getTermekNevek());
        lvTermekek.getSelectionModel().selectFirst();
    }

    public void betolt(DB db){
        this.db = db;
        TableColumn ossznev = new TableColumn("Összetevő");
        ossznev.setMinWidth(100);
        ossznev.setCellFactory(TextFieldTableCell.forTableColumn());
        ossznev.setCellValueFactory(new PropertyValueFactory<osszetevo, String>("nev"));
        ossznev.setStyle("-fx-alignment: CENTER;");//-fx-font-size:15px;

        TableColumn kicsi = new TableColumn("kicsi(gr)");
        kicsi.setMinWidth(100);
        kicsi.setCellFactory(TextFieldTableCell.forTableColumn());
        kicsi.setCellValueFactory(new PropertyValueFactory<osszetevo, String>("gr"));
        kicsi.setStyle("-fx-alignment: CENTER;");

//        kicsi.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<osszetevo, String>>() {
//            @Override
//            public void handle(TableColumn.CellEditEvent<osszetevo, String> t) {
//                osszetevo o = (osszetevo) t.getTableView().getItems().get(t.getTablePosition().getRow());
//                o.setGr(t.getNewValue());
//            }
//
//        });

        TableColumn nagy = new TableColumn("nagy(gr)");
        nagy.setMinWidth(100);
        nagy.setCellFactory(TextFieldTableCell.forTableColumn());
        nagy.setCellValueFactory(new PropertyValueFactory<osszetevo, String>("grn"));
        nagy.setStyle("-fx-alignment: CENTER;");

        tvTermekek.getColumns().addAll(ossznev, kicsi, nagy);

        lvTermekek.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<dzsuz>() {
            @Override
            public void changed(ObservableValue<? extends dzsuz> observable, dzsuz oldValue, dzsuz newValue) {
                if (newValue != null) {
                    dzsuz item = lvTermekek.getSelectionModel().getSelectedItem();
                    data = db.getOsszetevok(Integer.parseInt(item.getId()));
                    tvTermekek.setItems(data);
                }
            }
        });

        lvTermekek.getItems().setAll(db.getTermekNevek());
        lvTermekek.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lvTermekek.getSelectionModel().selectFirst();
    }
}
