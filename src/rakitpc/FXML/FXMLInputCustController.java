package rakitpc.FXML;

import rakitpc.Model.CustModel;
import rakitpc.DB.DBCust;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tobih
 */
public class FXMLInputCustController implements Initializable {

    private boolean editdata = false;
    private DBCust dbc = new DBCust();

    @FXML
    private TextField txtidmember;
    @FXML
    private TextField txtnama;
    @FXML
    private TextField txtalamat;
    @FXML
    private TextField txttotal;
    @FXML
    private Button btnsave;
    @FXML
    private Button btncancel;
    @FXML
    private Button btnexit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!editdata) {

            generateNewIdMember();
            txtidmember.setEditable(false);
        }
    }

    private void generateNewIdMember() {
        String lastId = dbc.getLastIdMember();
        if (lastId == null) {

            txtidmember.setText("C00001");
        } else {

            int nextId = Integer.parseInt(lastId.substring(1)) + 1;

            String newId = String.format("C%05d", nextId);
            txtidmember.setText(newId);
        }
    }

    public void execute(CustModel d) {
        if (!d.getIdmember().isEmpty()) {
            editdata = true;
            txtidmember.setText(d.getIdmember());
            txtnama.setText(d.getNama());
            txtalamat.setText(d.getAlamat());
            txttotal.setText(String.valueOf(d.getTotalbiayarakit()));
            txtidmember.setEditable(false);
            txtnama.requestFocus();
        }
    }

    @FXML
    private void saveclick(ActionEvent event) {

        if (txtnama.getText().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Nama tidak boleh kosong!", ButtonType.OK);
            a.showAndWait();
            txtnama.requestFocus();
            return;
        }

        if (txtalamat.getText().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Alamat tidak boleh kosong!", ButtonType.OK);
            a.showAndWait();
            txtalamat.requestFocus();
            return;
        }

        double totalBiaya = 0.0;
        try {
            if (!txttotal.getText().isEmpty()) {
                totalBiaya = Double.parseDouble(txttotal.getText());
            }
        } catch (NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Total biaya rakit harus berupa angka!", ButtonType.OK);
            a.showAndWait();
            txttotal.requestFocus();
            return;
        }

        CustModel n = new CustModel();
        n.setIdmember(txtidmember.getText());
        n.setNama(txtnama.getText());
        n.setAlamat(txtalamat.getText());
        n.setTotalbiayarakit(totalBiaya);

        FXMLDashboardController.dtcust.setCustModel(n);

        if (editdata) {
            if (FXMLDashboardController.dtcust.update()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Data berhasil diubah", ButtonType.OK);
                a.showAndWait();
                closeForm();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Data gagal diubah", ButtonType.OK);
                a.showAndWait();
            }
        } else if (FXMLDashboardController.dtcust.validasi(n.getIdmember()) <= 0) {
            if (FXMLDashboardController.dtcust.insert()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Data berhasil disimpan", ButtonType.OK);
                a.showAndWait();
                closeForm();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Data gagal disimpan", ButtonType.OK);
                a.showAndWait();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR, "Data sudah ada", ButtonType.OK);
            a.showAndWait();
            txtidmember.requestFocus();
        }
    }

    @FXML
    private void cancelclick(ActionEvent event) {
        txtnama.setText("");
        txtalamat.setText("");
        txttotal.setText("");
        txtidmember.requestFocus();
    }

    @FXML
    private void exitclick(ActionEvent event) {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) btnexit.getScene().getWindow();
        stage.close();
    }
}
