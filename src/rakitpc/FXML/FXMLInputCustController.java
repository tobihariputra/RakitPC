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

    private DBCust dbc = new DBCust(); // Instance dari DBCust untuk mengambil idmember terakhir

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!editdata) {
            // Jika mode bukan edit, maka buat idmember baru secara otomatis
            generateNewIdMember();
            txtidmember.setEditable(false); // Disable edit di idmember
        }
    }

    // Metode untuk mendapatkan idmember terbaru dan meng-generate id berikutnya
    private void generateNewIdMember() {
        String lastId = dbc.getLastIdMember();
        if (lastId == null) {
            // Jika belum ada data, idmember pertama
            txtidmember.setText("C00001");
        } else {
            // Ambil angka dari id terakhir (contoh: c00001 -> 00001)
            int nextId = Integer.parseInt(lastId.substring(1)) + 1;
            // Format angka menjadi 5 digit dengan leading zeros (contoh: 00002)
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
            txtidmember.setEditable(false); // Tetap disabled saat mengedit data
            txtnama.requestFocus();
        }
    }

    @FXML
    private void saveclick(ActionEvent event) {
        // Validasi input
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

        // Jika validasi lolos, simpan data
        CustModel n = new CustModel();
        n.setIdmember(txtidmember.getText()); // idmember sudah otomatis
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
                closeForm(); // Tutup form setelah data disimpan
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
        txtidmember.setText("");
        txtnama.setText("");
        txtalamat.setText("");
        txttotal.setText("");
        txtidmember.requestFocus();
    }

    @FXML
    private void exitclick(ActionEvent event) {
        closeForm();
    }

    // Metode untuk menutup form
    private void closeForm() {
        Stage stage = (Stage) btnexit.getScene().getWindow();
        stage.close();
    }
}
