package rakitpc.FXML;

import rakitpc.Model.CustModel;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tobih
 */
public class FXMLDataCustController implements Initializable {

    @FXML
    private TableView<CustModel> tbvcust;
    @FXML
    private Button btnfirst;
    @FXML
    private Button btnprev;
    @FXML
    private Button btnnext;
    @FXML
    private Button btnlast;
    @FXML
    private Button btninsert;
    @FXML
    private Button btndelete;
    @FXML
    private Button btnupdate;
    @FXML
    private Button btnexit;
    @FXML
    private TextField searchcust;
    @FXML
    private Button btnsearch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showdata();
    }

    private void addColumn(String columnName, String property) {
        TableColumn<CustModel, String> col = new TableColumn<>(columnName);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        tbvcust.getColumns().add(col);
    }

    public void showdata() {
        ObservableList<CustModel> data = FXMLDashboardController.dtcust.Load();
        tbvcust.getColumns().clear();
        tbvcust.getItems().clear();

        TableColumn<CustModel, String> col1 = new TableColumn<>("idmember");
        col1.setCellValueFactory(new PropertyValueFactory<>("idmember"));

        TableColumn<CustModel, String> col2 = new TableColumn<>("nama");
        col2.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<CustModel, String> col3 = new TableColumn<>("alamat");
        col3.setCellValueFactory(new PropertyValueFactory<>("alamat"));

        TableColumn<CustModel, String> col4 = new TableColumn<>("totalbiayarakit");
        col4.setCellValueFactory(totaldata -> {
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String formattedCurrency = currencyFormatter.format(totaldata.getValue().getTotalbiayarakit());
            return new javafx.beans.property.SimpleStringProperty(formattedCurrency);
        });

        TableColumn<CustModel, String> col5 = new TableColumn<>("status");
        col5.setCellValueFactory(new PropertyValueFactory<>("status"));

        tbvcust.getColumns().addAll(col1, col2, col3, col4, col5);

        if (data == null || data.isEmpty()) {
            tbvcust.setPlaceholder(new Label("Tidak ada data untuk ditampilkan."));
        } else {
            tbvcust.setItems(data);
        }
    }

    @FXML
    private void cariData(KeyEvent event) {
        searchAndDisplay(searchcust.getText());
    }

    @FXML
    private void searchclick(ActionEvent event) {
        searchAndDisplay(searchcust.getText());
    }

    private void searchAndDisplay(String key) {
        if (!key.isEmpty()) {
            ObservableList<CustModel> data = FXMLDashboardController.dtcust.CariCust(key, key);
            if (data != null) {
                tbvcust.setItems(data);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Data kosong", ButtonType.OK);
                a.showAndWait();
            }
        } else {
            showdata();
        }
    }

    @FXML
    private void firstclick(ActionEvent event) {
        if (!tbvcust.getItems().isEmpty()) {
            tbvcust.getSelectionModel().selectFirst();
            tbvcust.requestFocus();
        }
    }

    @FXML
    private void prevclick(ActionEvent event) {
        if (!tbvcust.getItems().isEmpty()) {
            tbvcust.getSelectionModel().selectPrevious();
            tbvcust.requestFocus();
        }
    }

    @FXML
    private void nextclick(ActionEvent event) {
        if (!tbvcust.getItems().isEmpty()) {
            tbvcust.getSelectionModel().selectNext();
            tbvcust.requestFocus();
        }
    }

    @FXML
    private void lastclick(ActionEvent event) {
        if (!tbvcust.getItems().isEmpty()) {
            tbvcust.getSelectionModel().selectLast();
            tbvcust.requestFocus();
        }
    }

    @FXML
    private void insertclick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInputCust.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stg = new Stage();
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.setResizable(false);
            stg.setIconified(false);
            stg.setScene(scene);
            stg.showAndWait();
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Gagal memuat data", ButtonType.OK);
            a.showAndWait();
            e.printStackTrace();
        }
        showdata();
        firstclick(event);
    }

    @FXML
    private void deleteclick(ActionEvent event) {
        CustModel s = tbvcust.getSelectionModel().getSelectedItem();
        if (s != null) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Mau dihapus?", ButtonType.YES, ButtonType.NO);
            a.showAndWait();
            if (a.getResult() == ButtonType.YES) {
                if (FXMLDashboardController.dtcust.delete(s.getIdmember())) {
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
    private void updateclick(ActionEvent event) {
        CustModel s = tbvcust.getSelectionModel().getSelectedItem();
        if (s != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInputCust.fxml"));
                Parent root = loader.load();
                FXMLInputCustController isidt = loader.getController();
                isidt.execute(s);
                Scene scene = new Scene(root);
                Stage stg = new Stage();
                stg.initModality(Modality.APPLICATION_MODAL);
                stg.setResizable(false);
                stg.setIconified(false);
                stg.setScene(scene);
                stg.showAndWait();
            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Gagal memuat data", ButtonType.OK);
                a.showAndWait();
                e.printStackTrace();
            }
            showdata();
            firstclick(event);
        }
    }

    @FXML
    private void exitclick(ActionEvent event) {
        btnexit.getScene().getWindow().hide();
    }

}
