package pack;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class TermelesController implements Initializable {

    @FXML
    private TextField tfEv;

    @FXML
    private TableView tvFelhasznalt;

    @FXML
    private TextField tfHonap;

    @FXML
    private TextField tfNap;

    @FXML
    private ListView<dzsuz> lvTermekNevek;

    int ossztomeg = 0;

    @FXML
    private void pdfNevek(ActionEvent event) {
        String fajlnev = tfEv.getText()+tfHonap.getText()+tfNap.getText()+"Nevek";
        if(!lvTermekNevek.getItems().isEmpty()){

            Document document = new Document();
            try {

                PdfWriter.getInstance(document, new FileOutputStream(fajlnev.substring(2)+".pdf"));
                document.open();

                Chunk title = new Chunk(tfEv.getText()+"-"+tfHonap.getText()+"-"+tfNap.getText());
                Paragraph base = new Paragraph(title);
                document.add(base);

                ObservableList<Vasarlas> vasarlasok = db.getVasarlasList(tfEv.getText(), tfHonap.getText(), tfNap.getText());
                ArrayList<Integer>nevekid = new ArrayList<Integer>();
                for (Vasarlas v: vasarlasok) {
                    if(!nevekid.contains(Integer.parseInt(v.getNevId()))){
                        nevekid.add(Integer.parseInt(v.getNevId()));
                    }
                }
                for (int id:nevekid){
                    String nev = db.getNev(id);
                    //lista vasarlasokkal id szerint és kész
                    ObservableList<Vasarlas> nevszerint = db.getVasarlasListId(tfEv.getText(), tfHonap.getText(), tfNap.getText(),id);
                    ArrayList<String>termekek = new ArrayList<>();
                    for (Vasarlas v: nevszerint) {
                        String term = v.getNev()+" ("+v.getKn()+")";
                        if(!termekek.contains(term)){
                            termekek.add(term);
                        }
                    }

                    //...........
                    float[] columWidts = {5,5};
                    PdfPTable table = new PdfPTable(columWidts);
                    table.setWidthPercentage(50);

                    PdfPCell cell = new PdfPCell(new Phrase(nev));
                    cell.setBackgroundColor(GrayColor.GRAYWHITE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(2);
                    table.addCell(cell);

                    table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    //............

                    for (String t: termekek) {
                        int db =0;
                        for (Vasarlas v: nevszerint) {
                            String term = v.getNev()+" ("+v.getKn()+")";
                            if(term.equals(t)){
                               db++;
                            }
                        }
                        System.out.println(t+" "+db);
                        table.addCell(t);
                        table.addCell(String.valueOf(db));
                    }
                    document.add(table);
                    Chunk hely = new Chunk("\n");
                    Paragraph enter = new Paragraph(hely);
                    document.add(enter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            document.close();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba!");
            alert.setHeaderText(null);
            alert.setContentText("Hiányos a beírt dátum, vagy nincs találat!");

            alert.showAndWait();
        }
    }

    @FXML
    private void pdfGen(ActionEvent event) {

        // Document document = new Document();
        String fajlnev = tfEv.getText()+tfHonap.getText()+tfNap.getText();
        if(fajlnev.length()==8 && !lvTermekNevek.getItems().isEmpty()){

            Document document = new Document();
            try {

                PdfWriter.getInstance(document, new FileOutputStream(fajlnev.substring(2)+".pdf"));
                document.open();

                Chunk title = new Chunk(tfEv.getText()+"-"+tfHonap.getText()+"-"+tfNap.getText());
                Paragraph base = new Paragraph(title);
                document.add(base);

                for (dzsuz item : lvTermekNevek.getItems()) {
                    fogyottData = db.getElemek();
                    ossztomeg = 0;
                    for (Vasarlas v : eladasokData) {
                        if (item.getId().equals(v.getId())) {
                            ciklusData = db.getOsszetevok(Integer.parseInt(v.getId()));
                            for (osszetevo co : ciklusData) {
                                for (osszetevo fo : fogyottData) {
                                    if (co.getId().equals(fo.getId())) {
                                        int i = 0;
                                        if (v.getKn().equals("kicsi")) {
                                            i = Integer.parseInt(co.getGr());
                                        } else {
                                            i = Integer.parseInt(co.getGrn());
                                        }
                                        ossztomeg += i;
                                        fo.grHozzaad(i);
                                    }
                                }
                            }
                        }
                    }
                    Chunk hely = new Chunk("\n");
                    Paragraph enter = new Paragraph(hely);
                    document.add(enter);
                    //3cellhozzáad
                    float[] columWidts = {5,5};
                    PdfPTable table = new PdfPTable(columWidts);
                    table.setWidthPercentage(50);

                    PdfPCell cell = new PdfPCell(new Phrase(item.getNev()));
                    cell.setBackgroundColor(GrayColor.GRAYWHITE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(2);
                    table.addCell(cell);

                    table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

                    for (osszetevo fo : fogyottData) {
                        if(!fo.getGr().equals("0")){
                            table.addCell(fo.getNev());
                            table.addCell(fo.getGr());
                        }
                        //table.addCell(fo);
                    }
                    document.add(table);
                    Chunk ossz = new Chunk("\t\tÖssztömeg: "+ossztomeg+" gr");
                    Paragraph osszp = new Paragraph(ossz);
                    osszp.setAlignment(Element.ALIGN_CENTER);
                    document.add(osszp);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            document.close();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba!");
            alert.setHeaderText(null);
            alert.setContentText("Hiányos a beírt dátum, vagy nincs találat!");

            alert.showAndWait();
        }
    }

    ObservableList<Vasarlas> eladasokData;
    ObservableList<osszetevo> fogyottData;
    ObservableList<osszetevo> ciklusData;

    private DB db;

    public void feltolt(DB db) {
        this.db = db;
    }

    private boolean nemtartalmaz = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        TableColumn elem = new TableColumn("Összetevő név");
        elem.setMinWidth(130);
        elem.setCellFactory(TextFieldTableCell.forTableColumn());
        elem.setCellValueFactory(new PropertyValueFactory<osszetevo, String>("nev"));
        elem.setStyle("-fx-alignment: CENTER;");//-fx-font-size:15px;

        TableColumn fmenny = new TableColumn("Elfogyott mennyiség (gr)");
        fmenny.setMinWidth(190);
        fmenny.setCellFactory(TextFieldTableCell.forTableColumn());
        fmenny.setCellValueFactory(new PropertyValueFactory<osszetevo, String>("gr"));
        fmenny.setStyle("-fx-alignment: CENTER;");

        tvFelhasznalt.getColumns().addAll(elem, fmenny);

        tfNap.textProperty().addListener((observable, oldValue, newValue) -> {


            lvTermekNevek.getItems().clear();

            eladasokData = db.getVasarlasList(tfEv.getText(), tfHonap.getText(), newValue);
            for (Vasarlas v : eladasokData) {
                nemtartalmaz = true;
                dzsuz dzs = new dzsuz(Integer.parseInt(v.getId()), db.getTermekNev(Integer.parseInt(v.getId())));

                for (dzsuz dzsz : lvTermekNevek.getItems()) {
                    if (dzs.getId().equals(dzsz.getId())) {
                        nemtartalmaz = false;
                    }
                }

                if (nemtartalmaz) {
                    lvTermekNevek.getItems().add(dzs);
                }

                lvTermekNevek.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                lvTermekNevek.getSelectionModel().selectFirst();
                dzs = null;
            }

        });

        lvTermekNevek.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<dzsuz>() {
            @Override
            public void changed(ObservableValue<? extends dzsuz> observable, dzsuz oldValue, dzsuz newValue) {
                if (newValue != null) {
                    dzsuz item = lvTermekNevek.getSelectionModel().getSelectedItem();
                    fogyottData = db.getElemek();
                    for (Vasarlas v : eladasokData) {
                        if (item.getId().equals(v.getId())) {
                            ciklusData = db.getOsszetevok(Integer.parseInt(v.getId()));
                            for (osszetevo co : ciklusData) {
                                for (osszetevo fo : fogyottData) {
                                    if (co.getId().equals(fo.getId())) {
                                        int i = 0;
                                        if (v.getKn().equals("kicsi")) {
                                            i = Integer.parseInt(co.getGr());
                                        } else {
                                            i = Integer.parseInt(co.getGrn());
                                        }
                                        fo.grHozzaad(i);
                                    }
                                }
                            }
                        }
                    }
                    //tvFelhasznalt.setItems(fogyottData);
                    tvFelhasznalt.getItems().clear();
                    for (osszetevo vasarlas : fogyottData) {
                        if(!vasarlas.getGr().equals("0")){
                            tvFelhasznalt.getItems().add(vasarlas);
                        }
                    }
                }
            }
        });
        //lvTermekNevek.getItems().setAll(db.getTermekNevek());
    }
}

