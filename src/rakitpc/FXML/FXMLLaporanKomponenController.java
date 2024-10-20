package rakitpc.FXML;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rakitpc.DB.DBLaporan;
import rakitpc.Model.LaporanKomponenModel;

public class FXMLLaporanKomponenController implements Initializable {

    @FXML
    private TableView<LaporanKomponenModel> tableViewKomponen;
    @FXML
    private TableColumn<LaporanKomponenModel, String> columnKodeKomponen;
    @FXML
    private TableColumn<LaporanKomponenModel, String> columnNamaKomponen;
    @FXML
    private TableColumn<LaporanKomponenModel, String> columnKategori;
    @FXML
    private TableColumn<LaporanKomponenModel, String> columnBrand;
    @FXML
    private TableColumn<LaporanKomponenModel, String> columnSocket;
    @FXML
    private TableColumn<LaporanKomponenModel, String> columnJenisMemori;
    @FXML
    private TableColumn<LaporanKomponenModel, Double> columnHarga;
    @FXML
    private TableColumn<LaporanKomponenModel, Integer> columnStokTersedia;
    @FXML
    private TableColumn<LaporanKomponenModel, String> columnJumlahTerpakai;
    @FXML
    private TableColumn<LaporanKomponenModel, String> columnNoRakit;

    private DBLaporan dbLaporan;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbLaporan = new DBLaporan();

        // Set cell value factories
        columnKodeKomponen.setCellValueFactory(new PropertyValueFactory<>("kodeKomponen"));
        columnNamaKomponen.setCellValueFactory(new PropertyValueFactory<>("namaKomponen"));
        columnKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        columnBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        columnSocket.setCellValueFactory(new PropertyValueFactory<>("socket"));
        columnJenisMemori.setCellValueFactory(new PropertyValueFactory<>("jenisMemori"));
        columnHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        columnStokTersedia.setCellValueFactory(new PropertyValueFactory<>("stokTersedia"));
        columnJumlahTerpakai.setCellValueFactory(new PropertyValueFactory<>("jumlahTerpakai"));
        columnNoRakit.setCellValueFactory(new PropertyValueFactory<>("noRakit"));

        // Load data
        loadData();
    }

    private void loadData() {
        ObservableList<LaporanKomponenModel> data = dbLaporan.getLaporanKomponen();
        tableViewKomponen.setItems(data);
    }
}
