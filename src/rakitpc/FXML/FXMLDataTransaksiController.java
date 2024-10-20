package rakitpc.FXML;

import rakitpc.DB.DBTransaksi;
import rakitpc.DB.Koneksi;
import rakitpc.Model.TransaksiModel;
import rakitpc.Model.RakitDetailModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.scene.control.TableRow;
import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FXMLDataTransaksiController implements Initializable {

    private Koneksi koneksi;
    private DBTransaksi dbTransaksi;
    private ObservableList<TransaksiModel> listTransaksi = FXCollections.observableArrayList();
    private ObservableList<RakitDetailModel> listDetilTransaksi = FXCollections.observableArrayList();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    private TextField txtnotransaksi;
    @FXML
    private Button btnfirst;
    @FXML
    private Button btnprev;
    @FXML
    private Button btnnext;
    @FXML
    private Button btnlast;
    @FXML
    private Button btndelete;
    @FXML
    private TableView<TransaksiModel> tbvtransaksi;
    @FXML
    private TableView<RakitDetailModel> tbvdetiltransaksi;
    @FXML
    private TextField txttotaldetiltransaksi;
    @FXML
    private TextField txtnamacustomer;
    @FXML
    private TextField txtnorakit;
    @FXML
    private TextField txttanggaltransaksi;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        koneksi = new Koneksi();
        koneksi.bukaKoneksi();
        dbTransaksi = new DBTransaksi();

        setupTableTransaksi();
        setupTableDetilTransaksi();

        loadTransaksi();

        tbvtransaksi.setRowFactory(new Callback<TableView<TransaksiModel>, TableRow<TransaksiModel>>() {
            @Override
            public TableRow<TransaksiModel> call(TableView<TransaksiModel> tableView) {
                return new TableRow<TransaksiModel>() {
                    @Override
                    protected void updateItem(TransaksiModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setStyle("");
                        } else {

                            setStyle("");
                        }
                    }
                };
            }
        });

        tbvtransaksi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayTransaksiDetails(newValue);
            }
        });
    }

    private void setupTableTransaksi() {
        tbvtransaksi.getColumns().clear();

        TableColumn<TransaksiModel, String> colNoTransaksi = new TableColumn<>("No Transaksi");
        colNoTransaksi.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIdtransaksi()));
        colNoTransaksi.setPrefWidth(150);

        TableColumn<TransaksiModel, String> colIdMember = new TableColumn<>("ID Member");
        colIdMember.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIdmember()));
        colIdMember.setPrefWidth(100);

        tbvtransaksi.getColumns().addAll(colNoTransaksi, colIdMember);
        tbvtransaksi.setItems(listTransaksi);
    }

    private void setupTableDetilTransaksi() {
        tbvdetiltransaksi.getColumns().clear();

        TableColumn<RakitDetailModel, String> colKode = new TableColumn<>("Kode Komponen");
        colKode.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKodekomponen()));
        colKode.setPrefWidth(150);

        TableColumn<RakitDetailModel, String> colNama = new TableColumn<>("Nama Komponen");
        colNama.setCellValueFactory(data -> {
            String kode = data.getValue().getKodekomponen();
            String nama = dbTransaksi.getNamaKomponen(kode);
            return new javafx.beans.property.SimpleStringProperty(nama);
        });
        colNama.setPrefWidth(200);

        TableColumn<RakitDetailModel, Integer> colJumlah = new TableColumn<>("Jumlah");
        colJumlah.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getJumlah()).asObject());
        colJumlah.setPrefWidth(100);

        TableColumn<RakitDetailModel, String> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(data -> {
            double subtotal = data.getValue().getSubtotal();
            return new javafx.beans.property.SimpleStringProperty(formatRupiah(subtotal));
        });
        colSubtotal.setPrefWidth(150);

        tbvdetiltransaksi.getColumns().addAll(colKode, colNama, colJumlah, colSubtotal);
        tbvdetiltransaksi.setItems(listDetilTransaksi);
    }

    private void loadTransaksi() {
        listTransaksi.clear();
        try {
            String sql = "SELECT * FROM transaksi";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TransaksiModel transaksi = new TransaksiModel();
                transaksi.setIdtransaksi(rs.getString("idtransaksi"));
                transaksi.setIdmember(rs.getString("idmember"));
                transaksi.setNorakit(rs.getInt("norakit"));
                transaksi.setTanggal(rs.getTimestamp("tanggal"));
                transaksi.setTotalbayar(rs.getDouble("totalbayar"));
                listTransaksi.add(transaksi);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayTransaksiDetails(TransaksiModel transaksi) {
        txtnotransaksi.setText(transaksi.getIdtransaksi());
        txtnorakit.setText(String.valueOf(transaksi.getNorakit()));
        txttanggaltransaksi.setText(transaksi.getTanggal().toLocalDateTime().format(dateTimeFormatter));
        txttotaldetiltransaksi.setText(formatRupiah(transaksi.getTotalbayar()));

        String namaCustomer = dbTransaksi.getNamaCustomer(transaksi.getIdmember());
        txtnamacustomer.setText(namaCustomer);

        loadDetilTransaksi(transaksi.getNorakit());
    }

    private void loadDetilTransaksi(int norakit) {
        listDetilTransaksi.clear();
        try {
            String sql = "SELECT * FROM rakit_detail WHERE norakit = ?";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ps.setInt(1, norakit);
            ResultSet rs = ps.executeQuery();
            double totalBayar = 0;
            while (rs.next()) {
                RakitDetailModel detail = new RakitDetailModel(
                        rs.getInt("norakit"),
                        rs.getString("kodekomponen"),
                        rs.getInt("jumlah"),
                        rs.getDouble("subtotal")
                );
                listDetilTransaksi.add(detail);
                totalBayar += detail.getSubtotal();
            }
            rs.close();
            ps.close();

            double ppn = totalBayar * 0.12;
            double totalDenganPpn = totalBayar + ppn;
            txttotaldetiltransaksi.setText(formatRupiah(totalDenganPpn));

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
    private void firstclick(ActionEvent event) {
        if (!listTransaksi.isEmpty()) {
            tbvtransaksi.getSelectionModel().selectFirst();
            tbvtransaksi.scrollTo(0);
        }
    }

    @FXML
    private void prevclick(ActionEvent event) {
        int selectedIndex = tbvtransaksi.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            tbvtransaksi.getSelectionModel().selectPrevious();
            tbvtransaksi.scrollTo(selectedIndex - 1);
        }
    }

    @FXML
    private void nextclick(ActionEvent event) {
        int selectedIndex = tbvtransaksi.getSelectionModel().getSelectedIndex();
        if (selectedIndex < listTransaksi.size() - 1) {
            tbvtransaksi.getSelectionModel().selectNext();
            tbvtransaksi.scrollTo(selectedIndex + 1);
        }
    }

    @FXML
    private void lastclick(ActionEvent event) {
        if (!listTransaksi.isEmpty()) {
            tbvtransaksi.getSelectionModel().selectLast();
            tbvtransaksi.scrollTo(listTransaksi.size() - 1);
        }
    }

    @FXML
    private void deleteclick(ActionEvent event) {
        TransaksiModel selectedTransaksi = tbvtransaksi.getSelectionModel().getSelectedItem();
        if (selectedTransaksi != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Konfirmasi");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Apakah Anda yakin ingin menghapus transaksi ini?");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean deleted = dbTransaksi.deleteTransaksi(selectedTransaksi.getIdtransaksi());
                if (deleted) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sukses");
                    alert.setHeaderText(null);
                    alert.setContentText("Transaksi berhasil dihapus.");
                    alert.showAndWait();
                    loadTransaksi();
                    clearDetails();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gagal");
                    alert.setHeaderText(null);
                    alert.setContentText("Transaksi gagal dihapus.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih transaksi yang akan dihapus.");
            alert.showAndWait();
        }
    }

    private void clearDetails() {
        txtnotransaksi.clear();
        txtnamacustomer.clear();
        txtnorakit.clear();
        txttanggaltransaksi.clear();
        txttotaldetiltransaksi.clear();
        listDetilTransaksi.clear();
    }
}
