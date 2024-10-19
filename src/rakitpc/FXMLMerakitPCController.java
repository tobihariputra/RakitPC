package rakitpc;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;

public class FXMLMerakitPCController implements Initializable {

    @FXML
    private ComboBox<String> cmbProcessor;
    @FXML
    private ComboBox<String> cmbMotherboard;
    @FXML
    private ComboBox<String> cmbRAM;
    @FXML
    private ComboBox<String> cmbVGA;
    @FXML
    private ComboBox<String> cmbStorage;
    @FXML
    private ComboBox<String> cmbPowerSupply;
    @FXML
    private ComboBox<String> cmbCasingPC;
    @FXML
    private Button btnsave;
    @FXML
    private Button btncancel;
    @FXML
    private Button btnexit;

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        fillComboBox(cmbProcessor, "Processor");
        fillComboBox(cmbMotherboard, "Motherboard");
        fillComboBox(cmbRAM, "RAM");
        fillComboBox(cmbVGA, "VGA");
        fillComboBox(cmbStorage, "Storage");
        fillComboBox(cmbPowerSupply, "Power Supply");
        fillComboBox(cmbCasingPC, "Casing PC");
    }
 
    private void fillComboBox(ComboBox<String> comboBox, String kategori) {
        ObservableList<KomponenModel> komponenList = FXMLDashboardController.dtkomponen.getKomponenByCategory(kategori);
        if (komponenList != null && !komponenList.isEmpty()) {
            for (KomponenModel komponen : komponenList) {
                comboBox.getItems().add(komponen.getNamakomponen() + " (" + komponen.getKodekomponen() + ")");
            }
        } else {
            comboBox.setPromptText("Data tidak ditemukan");
        }
    }

    @FXML
    private void saveclick(ActionEvent event) { 
        if (cmbProcessor.getValue() == null || cmbMotherboard.getValue() == null || cmbRAM.getValue() == null
                || cmbVGA.getValue() == null || cmbStorage.getValue() == null || cmbPowerSupply.getValue() == null
                || cmbCasingPC.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "Semua komponen harus dipilih!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
 
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "PC berhasil dirakit!", ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void cancelclick(ActionEvent event) {
        cmbProcessor.setValue(null);
        cmbMotherboard.setValue(null);
        cmbRAM.setValue(null);
        cmbVGA.setValue(null);
        cmbStorage.setValue(null);
        cmbPowerSupply.setValue(null);
        cmbCasingPC.setValue(null);
    }

    @FXML
    private void exitclick(ActionEvent event) {
        btnexit.getScene().getWindow().hide();
    }
}
