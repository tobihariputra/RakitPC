package rakitpc.FXML;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import rakitpc.DB.DBLaporan;
import rakitpc.Model.LaporanTransaksiModel;

public class FXMLLaporanTransaksiController implements Initializable {

    @FXML
    private TableView<LaporanTransaksiModel> tableDaftarTransaksi;
    @FXML
    private TableColumn<LaporanTransaksiModel, String> colIdTransaksi;
    @FXML
    private TableColumn<LaporanTransaksiModel, String> colNamaMember;
    @FXML
    private TableColumn<LaporanTransaksiModel, String> colAlamatMember;
    @FXML
    private TableColumn<LaporanTransaksiModel, Integer> colNomorRakit;
    @FXML
    private TableColumn<LaporanTransaksiModel, Timestamp> colTanggalTransaksi;
    @FXML
    private TableColumn<LaporanTransaksiModel, Double> colTotalBayar;

    @FXML
    private TableView<LaporanTransaksiModel> tableDetailKomponen;
    @FXML
    private TableColumn<LaporanTransaksiModel, String> colIdTransaksiKomponen;
    @FXML
    private TableColumn<LaporanTransaksiModel, String> colKodeKomponen;
    @FXML
    private TableColumn<LaporanTransaksiModel, String> colNamaKomponen;
    @FXML
    private TableColumn<LaporanTransaksiModel, String> colBrandKomponen;
    @FXML
    private TableColumn<LaporanTransaksiModel, Integer> colJumlahKomponen;
    @FXML
    private TableColumn<LaporanTransaksiModel, Double> colSubtotalKomponen;

    @FXML
    private TableView<LaporanTransaksiModel> tablePendapatanTanggal;
    @FXML
    private TableColumn<LaporanTransaksiModel, Date> colTanggalPendapatan;
    @FXML
    private TableColumn<LaporanTransaksiModel, Double> colTotalPendapatan;

    @FXML
    private TableView<LaporanTransaksiModel> tableKomponenTerlaris;
    @FXML
    private TableColumn<LaporanTransaksiModel, String> colNamaKomponenTerlaris;
    @FXML
    private TableColumn<LaporanTransaksiModel, Integer> colTotalTerjual;

    @FXML
    private TableView<LaporanTransaksiModel> tablePelangganTertinggi;
    @FXML
    private TableColumn<LaporanTransaksiModel, String> colNamaPelanggan;
    @FXML
    private TableColumn<LaporanTransaksiModel, Double> colTotalBiayaRakit;
    @FXML
    private TableColumn<LaporanTransaksiModel, Double> colTotalBayarPelanggan;

    private DBLaporan dbLaporan;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbLaporan = new DBLaporan();
        configureTables();
        setCurrencyCellFactory(colTotalBayar, colSubtotalKomponen, colTotalPendapatan, colTotalBiayaRakit, colTotalBayarPelanggan);
        loadTableData();
    }

    private void setCurrencyCellFactory(TableColumn<LaporanTransaksiModel, Double>... columns) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        for (TableColumn<LaporanTransaksiModel, Double> column : columns) {
            column.setCellFactory(col -> new TextFieldTableCell<>(new javafx.util.StringConverter<Double>() {
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
            }));
        }
    }

    private void configureTables() {
        setColumnResizePolicy(tableDaftarTransaksi, tableDetailKomponen, tablePendapatanTanggal, tableKomponenTerlaris, tablePelangganTertinggi);
    }

    private void setColumnResizePolicy(TableView<?>... tables) {
        for (TableView<?> table : tables) {
            table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        }
    }

    private void loadTableData() {
        loadDaftarTransaksi();
        loadDetailKomponen();
        loadPendapatanPerTanggal();
        loadKomponenTerlaris();
        loadPelangganTertinggi();
    }

    private void loadDaftarTransaksi() {
        ObservableList<LaporanTransaksiModel> data = dbLaporan.getDaftarTransaksi();
        configureColumns(colIdTransaksi, "idTransaksi", colNamaMember, "namaMember", colAlamatMember, "alamat",
                colNomorRakit, "nomorRakit", colTanggalTransaksi, "tanggalTransaksi", colTotalBayar, "totalBayar");
        tableDaftarTransaksi.setItems(data);
    }

    private void loadDetailKomponen() {
        ObservableList<LaporanTransaksiModel> data = dbLaporan.getDetailKomponen();
        configureColumns(colIdTransaksiKomponen, "idTransaksi", colKodeKomponen, "kodeKomponen", colNamaKomponen, "namaKomponen",
                colBrandKomponen, "brand", colJumlahKomponen, "jumlahKomponen", colSubtotalKomponen, "subtotalKomponen");
        tableDetailKomponen.setItems(data);
    }

    private void loadPendapatanPerTanggal() {
        ObservableList<LaporanTransaksiModel> data = dbLaporan.getPendapatanPerTanggal();
        configureColumns(colTanggalPendapatan, "tanggalPendapatan", colTotalPendapatan, "totalPendapatan");
        tablePendapatanTanggal.setItems(data);
    }

    private void loadKomponenTerlaris() {
        ObservableList<LaporanTransaksiModel> data = dbLaporan.getKomponenTerlaris();
        configureColumns(colNamaKomponenTerlaris, "namaKomponen", colTotalTerjual, "totalTerjual");
        tableKomponenTerlaris.setItems(data);
    }

    private void loadPelangganTertinggi() {
        ObservableList<LaporanTransaksiModel> data = dbLaporan.getPelangganTertinggi();
        configureColumns(colNamaPelanggan, "namaMember", colTotalBiayaRakit, "totalBiayaRakit", colTotalBayarPelanggan, "totalBayarPelanggan");
        tablePelangganTertinggi.setItems(data);
    }

    private void configureColumns(Object... columnsAndProperties) {
        for (int i = 0; i < columnsAndProperties.length; i += 2) {
            TableColumn<LaporanTransaksiModel, ?> column = (TableColumn<LaporanTransaksiModel, ?>) columnsAndProperties[i];
            String property = (String) columnsAndProperties[i + 1];
            column.setCellValueFactory(new PropertyValueFactory<>(property));
        }
    }
}
