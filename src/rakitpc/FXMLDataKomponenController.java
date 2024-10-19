package rakitpc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLDataKomponenController implements Initializable {

    @FXML
    private Button btnexit;
    @FXML
    private Button btnupdate;
    @FXML
    private Button btndelete;
    @FXML
    private Button btninsert;
    @FXML
    private TableView<KomponenModel> tbvkomponen;
    @FXML
    private ImageView imgkomponen;
    @FXML
    private Button btnsearch;
    @FXML
    private TextField searchkomponen;
    @FXML
    private Button btnlast;
    @FXML
    private Button btnnext;
    @FXML
    private Button btnprev;
    @FXML
    private Button btnfirst;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns(); 
        showdata();
        tbvkomponen.getSelectionModel().selectFirst();
        showgambar();
    }

    private void setupTableColumns() { 
        TableColumn<KomponenModel, String> col1 = new TableColumn<>("kodekomponen");
        col1.setCellValueFactory(new PropertyValueFactory<>("kodekomponen"));
        
        TableColumn<KomponenModel, String> col2 = new TableColumn<>("namakomponen");
        col2.setCellValueFactory(new PropertyValueFactory<>("namakomponen"));
        
        TableColumn<KomponenModel, String> col3 = new TableColumn<>("brand");
        col3.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<KomponenModel, String> col4 = new TableColumn<>("socket");
        col4.setCellValueFactory(new PropertyValueFactory<>("socket"));

        TableColumn<KomponenModel, String> col5 = new TableColumn<>("kategori");
        col5.setCellValueFactory(new PropertyValueFactory<>("kategori"));

        TableColumn<KomponenModel, String> col6 = new TableColumn<>("harga");
        col6.setCellValueFactory(new PropertyValueFactory<>("harga"));

        TableColumn<KomponenModel, String> col7 = new TableColumn<>("stok");
        col7.setCellValueFactory(new PropertyValueFactory<>("stok"));

        tbvkomponen.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7);
        tbvkomponen.setPlaceholder(new Label("Tidak ada data untuk ditampilkan.")); // Placeholder
    }

    public void showdata() {
        ObservableList<KomponenModel> data = FXMLDashboardController.dtkomponen.Load();
        tbvkomponen.getItems().clear(); 

        if (data != null && !data.isEmpty()) {
            tbvkomponen.setItems(data);
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR, "Data kosong", ButtonType.OK);
            a.showAndWait();
        }
    }

    public void showgambar() {
        Image gambar = null;
        try {
            if (tbvkomponen.getSelectionModel().getSelectedItem() != null) {
                gambar = new Image(new FileInputStream(tbvkomponen.getSelectionModel().getSelectedItem().getGambar()));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDataKomponenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        imgkomponen.setImage(gambar);
    }

    @FXML
    private void exitclick(ActionEvent event) {
        btnexit.getScene().getWindow().hide();
    }

    @FXML
    private void updateclick(ActionEvent event) {
        KomponenModel s = tbvkomponen.getSelectionModel().getSelectedItem();
        if (s != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInputKomponen.fxml"));
                Parent root = loader.load();
                FXMLInputKomponenController isidt = loader.getController();
                isidt.execute(s);
                Stage stg = new Stage();
                stg.initModality(Modality.APPLICATION_MODAL);
                stg.setResizable(false);
                stg.setScene(new Scene(root));
                stg.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
            showdata();
            firstclick(event);
        }
    }

    @FXML
    private void deleteclick(ActionEvent event) {
        KomponenModel s = tbvkomponen.getSelectionModel().getSelectedItem();
        if (s != null) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Mau dihapus?", ButtonType.YES, ButtonType.NO);
            a.showAndWait();
            if (a.getResult() == ButtonType.YES) {
                if (FXMLDashboardController.dtkomponen.delete(s.getKodekomponen())) {
                    Alert b = new Alert(Alert.AlertType.INFORMATION, "Data berhasil dihapus", ButtonType.OK);
                    b.showAndWait();
                } else {
                    Alert b = new Alert(Alert.AlertType.ERROR, "Data gagal dihapus", ButtonType.OK);
                    b.showAndWait();
                }
                showdata();
                firstclick(event);
            }
        }
    }

    @FXML
    private void insertclick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInputKomponen.fxml"));
            Parent root = loader.load();
            Stage stg = new Stage();
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.setResizable(false);
            stg.setScene(new Scene(root));
            stg.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        showdata();
        firstclick(event);
    }

    @FXML
    private void tableclick(MouseEvent event) {
        showgambar();
    }

    @FXML
    private void searchclick(ActionEvent event) {
        String key = searchkomponen.getText();
        searchKomponen(key);
    }

    @FXML
    private void cariData(KeyEvent event) {
        String key = searchkomponen.getText();
        searchKomponen(key);
    }

    private void searchKomponen(String key) {
        if (!key.isEmpty()) {
            ObservableList<KomponenModel> data = FXMLDashboardController.dtkomponen.CariKomponen(key, key);
            if (data != null) {
                tbvkomponen.setItems(data);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Data kosong", ButtonType.OK);
                a.showAndWait();
            }
        } else {
            showdata();
        }
    }

    @FXML
    private void lastclick(ActionEvent event) {
        tbvkomponen.getSelectionModel().selectLast();
        showgambar();
        tbvkomponen.requestFocus();
    }

    @FXML
    private void nextclick(ActionEvent event) {
        tbvkomponen.getSelectionModel().selectNext();
        showgambar();
        tbvkomponen.requestFocus();
    }

    @FXML
    private void prevclick(ActionEvent event) {
        tbvkomponen.getSelectionModel().selectPrevious();
        showgambar();
        tbvkomponen.requestFocus();
    }

    @FXML
    private void firstclick(ActionEvent event) {
        tbvkomponen.getSelectionModel().selectFirst();
        showgambar();
        tbvkomponen.requestFocus();
    }
}
