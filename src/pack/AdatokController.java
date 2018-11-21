package pack;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


public class AdatokController implements Initializable {

    @FXML
    private TableView tvVasarlasok;

    @FXML
    private TextField tfNap;

    @FXML
    private TableView tvFelhasznalt;

    @FXML
    private TextField tfEv;

    @FXML
    private TextField tfHonap;

    ObservableList<Vasarlas> eladasokData;
    ObservableList<osszetevo> fogyottData;
    ObservableList<osszetevo> ciklusData;


    private DB db;

    public void feltolt(DB db) {
        this.db = db;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn term = new TableColumn("Termék");
        term.setMinWidth(120);
        term.setCellFactory(TextFieldTableCell.forTableColumn());
        term.setCellValueFactory(new PropertyValueFactory<Vasarlas, String>("nev"));
        term.setStyle("-fx-alignment: CENTER;");//-fx-font-size:15px;

        TableColumn meret = new TableColumn("Méret");
        meret.setMinWidth(90);
        meret.setCellFactory(TextFieldTableCell.forTableColumn());
        meret.setCellValueFactory(new PropertyValueFactory<Vasarlas, String>("kn"));
        meret.setStyle("-fx-alignment: CENTER;");

        TableColumn dat = new TableColumn("Dátum");
        dat.setMinWidth(150);
        dat.setCellFactory(TextFieldTableCell.forTableColumn());
        dat.setCellValueFactory(new PropertyValueFactory<Vasarlas, String>("ido"));
        dat.setStyle("-fx-alignment: CENTER;");

        tvVasarlasok.getColumns().addAll(term, meret, dat);

        TableColumn elem = new TableColumn("Összetevő név");
        elem.setMinWidth(150);
        elem.setCellFactory(TextFieldTableCell.forTableColumn());
        elem.setCellValueFactory(new PropertyValueFactory<osszetevo, String>("nev"));
        elem.setStyle("-fx-alignment: CENTER;");//-fx-font-size:15px;

        TableColumn fmenny = new TableColumn("Fogyott mennyiség (gr)");
        fmenny.setMinWidth(180);
        fmenny.setCellFactory(TextFieldTableCell.forTableColumn());
        fmenny.setCellValueFactory(new PropertyValueFactory<osszetevo, String>("gr"));
        fmenny.setStyle("-fx-alignment: CENTER;");

        tvFelhasznalt.getColumns().addAll(elem, fmenny);



        tfNap.textProperty().addListener((observable, oldValue, newValue) -> {

            eladasokData = db.getVasarlasList(tfEv.getText(),tfHonap.getText(), newValue);
            tvVasarlasok.setItems(eladasokData);
            if(eladasokData != null){
                fogyottData = db.getElemek();

                for (Vasarlas v : eladasokData) {
                    ciklusData = db.getOsszetevok(Integer.parseInt(v.getId()));
                    for (osszetevo co : ciklusData) {
                        for (osszetevo fo : fogyottData) {
                            if(co.getId().equals(fo.getId())){
                                int i = 0;
                                if(v.getKn().equals("kicsi")){
                                    i = Integer.parseInt(co.getGr());
                                } else{
                                    i = Integer.parseInt(co.getGrn());
                                }
                                fo.grHozzaad(i);
                            }
                        }
                    }
                }

                tvFelhasznalt.setItems(fogyottData);
            }
        });
    }

}
