package rakitpc.FXML;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rakitpc.DB.DBLaporan;
import rakitpc.Model.LaporanCustomerModel;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.control.cell.TextFieldTableCell;

public class FXMLLaporanCustomerController implements Initializable {

    @FXML
    private TableView<LaporanCustomerModel> tbvlaporancustomer;

    @FXML
    private TableColumn<LaporanCustomerModel, String> colIdMember;

    @FXML
    private TableColumn<LaporanCustomerModel, String> colNama;

    @FXML
    private TableColumn<LaporanCustomerModel, String> colAlamat;

    @FXML
    private TableColumn<LaporanCustomerModel, Double> colTotalBiaya;

    @FXML
    private TableColumn<LaporanCustomerModel, String> colIdTransaksi;

    @FXML
    private TableColumn<LaporanCustomerModel, String> colNoRakit;

    @FXML
    private TableColumn<LaporanCustomerModel, String> colTanggal;

    private DBLaporan dbLaporan;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbLaporan = new DBLaporan();

        colIdMember.setCellValueFactory(new PropertyValueFactory<>("idmember"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colIdTransaksi.setCellValueFactory(new PropertyValueFactory<>("idtransaksi"));
        colNoRakit.setCellValueFactory(new PropertyValueFactory<>("norakit"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));

        colTotalBiaya.setCellValueFactory(new PropertyValueFactory<>("totalbiayarakit"));
        colTotalBiaya.setCellFactory(column -> {
            return new TextFieldTableCell<>(new javafx.util.StringConverter<Double>() {
                private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                @Override
                public String toString(Double value) {
                    return value != null ? currencyFormatter.format(value) : "";
                }

                @Override
                public Double fromString(String string) {
                    try {
                        return currencyFormatter.parse(string).doubleValue();
                    } catch (Exception e) {
                        return null;
                    }
                }
            });
        });

        loadTableData();
    }

    private void loadTableData() {
        ObservableList<LaporanCustomerModel> data = dbLaporan.getLaporanCustomer();
        tbvlaporancustomer.setItems(data);
    }
}
