package rakitpc.FXML;

import rakitpc.DB.DBTransaksi;
import rakitpc.DB.Koneksi;
import rakitpc.Model.CustModel;
import rakitpc.Model.RakitDetailModel;
import rakitpc.Model.TransaksiModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.scene.control.TableRow;
import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;

public class FXMLInputTransaksiController implements Initializable {

    private Koneksi koneksi;
    private DBTransaksi dbTransaksi;
    private ObservableList<CustModel> listCust = FXCollections.observableArrayList();
    private ObservableList<Map<String, Object>> listRakit = FXCollections.observableArrayList();
    private ObservableList<RakitDetailModel> listDetilRakit = FXCollections.observableArrayList();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @FXML
    private TextField txtnotransaksi;
    @FXML
    private DatePicker dtptanggal;
    @FXML
    private Button btnsave;
    @FXML
    private TableView<CustModel> tbvcust;
    @FXML
    private TableView<Map<String, Object>> tbvrakit;
    @FXML
    private TableView<RakitDetailModel> tbvdetilrakit;
    @FXML
    private Button btnbatal;
    @FXML
    private TextField txtidmember;
    @FXML
    private TextField txtnorakit;
    @FXML
    private TextField txtsubtotalbayar;
    @FXML
    private TextField txtppn;
    @FXML
    private TextField txttotalbayar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        koneksi = new Koneksi();
        koneksi.bukaKoneksi();
        dbTransaksi = new DBTransaksi();

        setupTableCust();
        setupTableRakit();
        setupTableDetilRakit();

        loadCustomers();
        loadRakit();

        txtnotransaksi.setText(dbTransaksi.generateNoTransaksi());
        txtnotransaksi.setEditable(false);
        txtidmember.setEditable(false);
        txtnorakit.setEditable(false);

        txtsubtotalbayar.setEditable(false);
        txtppn.setEditable(false);
        txttotalbayar.setEditable(false);

        tbvrakit.setRowFactory(new Callback<TableView<Map<String, Object>>, TableRow<Map<String, Object>>>() {
            @Override
            public TableRow<Map<String, Object>> call(TableView<Map<String, Object>> tableView) {
                return new TableRow<Map<String, Object>>() {
                    @Override
                    protected void updateItem(Map<String, Object> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setDisable(false);
                            setStyle("");
                        } else {
                            int norakit = (Integer) item.get("norakit");
                            boolean exists = dbTransaksi.isRakitInTransaksi(norakit);
                            if (exists) {
                                setDisable(true);
                                setStyle("-fx-background-color: red;");
                            } else {
                                setDisable(false);
                                setStyle("");
                            }
                        }
                    }
                };
            }
        });
    }

    private void setupTableCust() {
        tbvcust.getColumns().clear();

        TableColumn<CustModel, String> colId = new TableColumn<>("ID Member");
        colId.setCellValueFactory(new PropertyValueFactory<>("idmember"));

        TableColumn<CustModel, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<CustModel, String> colAlamat = new TableColumn<>("Alamat");
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));

        tbvcust.getColumns().addAll(colId, colNama, colAlamat);
        tbvcust.setItems(listCust);
    }

    private void setupTableRakit() {
        tbvrakit.getColumns().clear();

        TableColumn<Map<String, Object>, Integer> colNorakit = new TableColumn<>("No Rakit");
        colNorakit.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty((Integer) data.getValue().get("norakit")).asObject());

        TableColumn<Map<String, Object>, String> colTanggal = new TableColumn<>("Tanggal");
        colTanggal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("tanggal").toString()));

        tbvrakit.getColumns().addAll(colNorakit, colTanggal);
        tbvrakit.setItems(listRakit);
    }

    private void setupTableDetilRakit() {
        tbvdetilrakit.getColumns().clear();

        TableColumn<RakitDetailModel, String> colKode = new TableColumn<>("Kode Komponen");
        colKode.setCellValueFactory(new PropertyValueFactory<>("kodekomponen"));

        TableColumn<RakitDetailModel, String> colNama = new TableColumn<>("Nama Komponen");
        colNama.setCellValueFactory(data -> {
            String kode = data.getValue().getKodekomponen();
            String nama = dbTransaksi.getNamaKomponen(kode);
            return new javafx.beans.property.SimpleStringProperty(nama);
        });

        TableColumn<RakitDetailModel, Integer> colJumlah = new TableColumn<>("Jumlah");
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));

        TableColumn<RakitDetailModel, String> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(data -> {
            double subtotal = data.getValue().getSubtotal();
            return new javafx.beans.property.SimpleStringProperty(formatRupiah(subtotal));
        });

        tbvdetilrakit.getColumns().addAll(colKode, colNama, colJumlah, colSubtotal);
        tbvdetilrakit.setItems(listDetilRakit);
    }

    private void loadCustomers() {
        listCust.clear();
        try {
            String sql = "SELECT * FROM customer";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CustModel cust = new CustModel();
                cust.setIdmember(rs.getString("idmember"));
                cust.setNama(rs.getString("nama"));
                cust.setAlamat(rs.getString("alamat"));
                cust.setTotalbiayarakit(rs.getDouble("totalbiayarakit"));
                listCust.add(cust);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRakit() {
        listRakit.clear();
        try {
            String sql = "SELECT * FROM rakit";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> rakit = new HashMap<>();
                rakit.put("norakit", rs.getInt("norakit"));

                Timestamp dbTimestamp = rs.getTimestamp("tanggal");
                LocalDateTime currentDateTime = LocalDateTime.now();

                rakit.put("tanggal", dbTimestamp.toLocalDateTime().toLocalDate().toString());
                listRakit.add(rakit);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDetilRakit(int norakit) {
        listDetilRakit.clear();
        try {
            String sql = "SELECT * FROM rakit_detail WHERE norakit = ?";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ps.setInt(1, norakit);
            ResultSet rs = ps.executeQuery();
            double subtotalBayar = 0;
            while (rs.next()) {
                RakitDetailModel detail = new RakitDetailModel(
                        rs.getInt("norakit"),
                        rs.getString("kodekomponen"),
                        rs.getInt("jumlah"),
                        rs.getDouble("subtotal")
                );
                listDetilRakit.add(detail);
                subtotalBayar += detail.getSubtotal();
            }
            rs.close();
            ps.close();

            double ppn = subtotalBayar * 0.12;
            double totalBayar = subtotalBayar + ppn;

            txtsubtotalbayar.setText(formatRupiah(subtotalBayar));
            txtppn.setText(formatRupiah(ppn));
            txttotalbayar.setText(formatRupiah(totalBayar));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatRupiah(double number) {
        return currencyFormat.format(number);
    }

    private double parseRupiah(String rupiah) {
        try {
            return currencyFormat.parse(rupiah).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @FXML
    private void saveklik(ActionEvent event) {
        if (txtidmember.getText().isEmpty() || txtnorakit.getText().isEmpty() || dtptanggal.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih Customer, Rakit, dan tanggal transaksi.");
            alert.showAndWait();
            return;
        }

        String idtransaksi = txtnotransaksi.getText();
        String idmember = txtidmember.getText();
        int norakit = Integer.parseInt(txtnorakit.getText());

        LocalDateTime selectedDate = dtptanggal.getValue().atStartOfDay();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime tanggalInput = selectedDate.withHour(currentTime.getHour())
                .withMinute(currentTime.getMinute())
                .withSecond(currentTime.getSecond())
                .withNano(currentTime.getNano());
        Timestamp timestamp = Timestamp.valueOf(tanggalInput);

        double subtotalBayar = parseRupiah(txtsubtotalbayar.getText());
        double ppn = parseRupiah(txtppn.getText());
        double totalDenganPpn = parseRupiah(txttotalbayar.getText());

        TransaksiModel transaksi = new TransaksiModel();
        transaksi.setIdtransaksi(idtransaksi);
        transaksi.setIdmember(idmember);
        transaksi.setNorakit(norakit);
        transaksi.setTanggal(timestamp);
        transaksi.setTotalbayar(totalDenganPpn);

        boolean simpan = dbTransaksi.simpanTransaksi(transaksi);
        boolean updateCust = dbTransaksi.updateTotalBiayaRakit(idmember, totalDenganPpn);

        if (simpan && updateCust) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukses");
            alert.setHeaderText(null);
            alert.setContentText("Transaksi berhasil disimpan.");
            alert.showAndWait();
            batalklik(event);
            txtnotransaksi.setText(dbTransaksi.generateNoTransaksi());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Gagal");
            alert.setHeaderText(null);
            alert.setContentText("Transaksi gagal disimpan.");
            alert.showAndWait();
        }
    }

    @FXML
    private void batalklik(ActionEvent event) {
        txtnotransaksi.clear();
        txtidmember.clear();
        txtnorakit.clear();
        dtptanggal.setValue(null);
        listDetilRakit.clear();
        txtsubtotalbayar.clear();
        txtppn.clear();
        txttotalbayar.clear();
        txtnotransaksi.setText(dbTransaksi.generateNoTransaksi());
    }

    @FXML
    private void tbvcustklik(MouseEvent event) {
        if (event.getClickCount() == 2) {
            CustModel selectedCust = tbvcust.getSelectionModel().getSelectedItem();
            if (selectedCust != null) {
                txtidmember.setText(selectedCust.getIdmember());
            }
        }
    }

    @FXML
    private void tbvrakitklik(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Map<String, Object> selectedRakit = tbvrakit.getSelectionModel().getSelectedItem();
            if (selectedRakit != null) {
                txtnorakit.setText(String.valueOf(selectedRakit.get("norakit")));
                int norakit = (Integer) selectedRakit.get("norakit");
                loadDetilRakit(norakit);
            }
        }
    }
}
