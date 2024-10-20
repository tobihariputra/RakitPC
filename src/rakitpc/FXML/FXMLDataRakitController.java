package rakitpc.FXML;

import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import rakitpc.DB.DBRakit;
import rakitpc.Model.RakitDetailModel;
import rakitpc.Model.RakitModel;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLDataRakitController implements Initializable {

    private DBRakit dbRakit;
    private final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private Map<String, String> komponenMap = new HashMap<>();

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
    private Button btnsearch;
    @FXML
    private TextField txtnorakit;
    @FXML
    private TextField txttotal;
    @FXML
    private TableView<RakitModel> tbvrakit;
    @FXML
    private TableView<RakitDetailModel> tbvdetilrakit;

    @FXML
    private Button btnupdate;
    @FXML
    private Button btninsert;
    @FXML
    private Button btnexit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbRakit = new DBRakit();
        loadKomponenMap();
        showdata();
        if (!tbvrakit.getItems().isEmpty()) {
            tbvrakit.getSelectionModel().selectFirst();
            RakitModel selectedRakit = tbvrakit.getSelectionModel().getSelectedItem();
            if (selectedRakit != null) {
                txtnorakit.setText(String.valueOf(selectedRakit.getNorakit()));
                showdatadetil(selectedRakit.getNorakit());
                calculateTotal();
            }
        }
    }

    /**
     * Memuat data komponen ke dalam komponenMap.
     */
    private void loadKomponenMap() {
        String query = "SELECT kodekomponen, namakomponen FROM komponen";
        try (Statement stmt = dbRakit.koneksi.dbKoneksi.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String kode = rs.getString("kodekomponen");
                String nama = rs.getString("namakomponen");
                komponenMap.put(kode, nama);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data komponen.");
        }
    }

    /**
     * Memuat data rakit ke dalam TableView tbvrakit secara dinamis.
     */
    public void showdata() {
        ObservableList<RakitModel> data = DBRakitLoadRakit();
        if (data != null) {
            tbvrakit.getColumns().clear();
            tbvrakit.getItems().clear();

            TableColumn<RakitModel, Integer> colNorakit = new TableColumn<>("Norakit");
            colNorakit.setCellValueFactory(new PropertyValueFactory<>("norakit"));
            tbvrakit.getColumns().add(colNorakit);

            TableColumn<RakitModel, java.util.Date> colTanggal = new TableColumn<>("Tanggal");
            colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
            tbvrakit.getColumns().add(colTanggal);

            tbvrakit.setItems(data);

            tbvrakit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    txtnorakit.setText(String.valueOf(newValue.getNorakit()));
                    showdatadetil(newValue.getNorakit());
                    calculateTotal();
                }
            });

        } else {
            Alert a = new Alert(Alert.AlertType.ERROR, "Data kosong", ButtonType.OK);
            a.showAndWait();
            tbvrakit.getScene().getWindow().hide();
        }
    }

    /**
     * Memuat data detail rakit berdasarkan norakit ke dalam TableView
     * tbvdetilrakit secara dinamis.
     *
     * @param norakit Nomor rakit yang dipilih.
     */
    public void showdatadetil(int norakit) {
        ObservableList<RakitDetailModel> data = DBRakitLoadDetail(norakit);
        if (data != null) {
            tbvdetilrakit.getColumns().clear();
            tbvdetilrakit.getItems().clear();

            TableColumn<RakitDetailModel, String> colKodekomponen = new TableColumn<>("Kode Komponen");
            colKodekomponen.setCellValueFactory(new PropertyValueFactory<>("kodekomponen"));
            tbvdetilrakit.getColumns().add(colKodekomponen);

            TableColumn<RakitDetailModel, String> colNamakomponen = new TableColumn<>("Nama Komponen");
            colNamakomponen.setCellValueFactory(cellData -> {
                String kode = cellData.getValue().getKodekomponen();
                String nama = komponenMap.getOrDefault(kode, "Unknown");
                return new ReadOnlyStringWrapper(nama);
            });
            tbvdetilrakit.getColumns().add(colNamakomponen);

            TableColumn<RakitDetailModel, Integer> colJumlah = new TableColumn<>("Jumlah");
            colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
            tbvdetilrakit.getColumns().add(colJumlah);

            TableColumn<RakitDetailModel, Double> colSubtotal = new TableColumn<>("Subtotal");
            colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
            colSubtotal.setCellFactory(column -> new TableCell<RakitDetailModel, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(rupiahFormat.format(item));
                    }
                }
            });
            tbvdetilrakit.getColumns().add(colSubtotal);

            tbvdetilrakit.setItems(data);

            if (!tbvdetilrakit.getItems().isEmpty()) {
                tbvdetilrakit.getSelectionModel().selectLast();
                calculateTotal();
            } else {
                txttotal.clear();
            }

        } else {
            Alert a = new Alert(Alert.AlertType.ERROR, "Data detail kosong", ButtonType.OK);
            a.showAndWait();
            tbvdetilrakit.getScene().getWindow().hide();
        }
    }

    /**
     * Menghitung total subtotal dari detail rakit yang ditampilkan.
     */
    private void calculateTotal() {
        double total = 0;
        for (RakitDetailModel detail : tbvdetilrakit.getItems()) {
            total += detail.getSubtotal();
        }
        txttotal.setText(rupiahFormat.format(total));
    }

    /**
     * Memuat data rakit dari database.
     *
     * @return ObservableList<RakitModel> atau null jika gagal.
     */
    private ObservableList<RakitModel> DBRakitLoadRakit() {
        ObservableList<RakitModel> rakitList = javafx.collections.FXCollections.observableArrayList();
        String query = "SELECT * FROM rakit ORDER BY norakit";
        try (Statement stmt = dbRakit.koneksi.dbKoneksi.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                RakitModel rakit = new RakitModel();
                rakit.setNorakit(rs.getInt("norakit"));
                rakit.setTanggal(rs.getDate("tanggal"));
                rakitList.add(rakit);
            }
            return rakitList;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data rakit.");
            return null;
        }
    }

    /**
     * Memuat data detail rakit dari database berdasarkan nomor rakit.
     *
     * @param norakit Nomor rakit.
     * @return ObservableList<RakitDetailModel> atau null jika gagal.
     */
    private ObservableList<RakitDetailModel> DBRakitLoadDetail(int norakit) {
        ObservableList<RakitDetailModel> detailList = javafx.collections.FXCollections.observableArrayList();
        String query = "SELECT * FROM rakit_detail WHERE norakit = ?";
        try (PreparedStatement pstmt = dbRakit.koneksi.dbKoneksi.prepareStatement(query)) {
            pstmt.setInt(1, norakit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                RakitDetailModel detail = new RakitDetailModel(
                        rs.getInt("norakit"),
                        rs.getString("kodekomponen"),
                        rs.getInt("jumlah"),
                        rs.getDouble("subtotal")
                );
                detailList.add(detail);
            }
            return detailList;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat detail rakit.");
            return null;
        }
    }

    @FXML
    private void firstclick(ActionEvent event) {
        if (!tbvrakit.getItems().isEmpty()) {
            tbvrakit.getSelectionModel().selectFirst();
            tbvrakit.requestFocus();
            tbvrakit.scrollTo(0);
        }
    }

    @FXML
    private void prevclick(ActionEvent event) {
        int selectedIndex = tbvrakit.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            tbvrakit.getSelectionModel().selectPrevious();
            tbvrakit.requestFocus();
            tbvrakit.scrollTo(selectedIndex - 1);
        }
    }

    @FXML
    private void nextclick(ActionEvent event) {
        int selectedIndex = tbvrakit.getSelectionModel().getSelectedIndex();
        if (selectedIndex < tbvrakit.getItems().size() - 1) {
            tbvrakit.getSelectionModel().selectNext();
            tbvrakit.requestFocus();
            tbvrakit.scrollTo(selectedIndex + 1);
        }
    }

    @FXML
    private void lastclick(ActionEvent event) {
        if (!tbvrakit.getItems().isEmpty()) {
            tbvrakit.getSelectionModel().selectLast();
            tbvrakit.requestFocus();
            tbvrakit.scrollTo(tbvrakit.getItems().size() - 1);
        }
    }

    @FXML
    private void deleteclick(ActionEvent event) {
        RakitModel selectedRakit = tbvrakit.getSelectionModel().getSelectedItem();
        if (selectedRakit == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Pilih data rakit yang akan dihapus.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi");
        confirm.setHeaderText("Hapus Data Rakit");
        confirm.setContentText("Apakah Anda yakin ingin menghapus rakit nomor " + selectedRakit.getNorakit() + "?");

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.OK) {
            String deleteDetail = "DELETE FROM rakit_detail WHERE norakit = ?";
            String deleteRakit = "DELETE FROM rakit WHERE norakit = ?";
            try {
                dbRakit.koneksi.dbKoneksi.setAutoCommit(false);

                try (PreparedStatement pstmtDetail = dbRakit.koneksi.dbKoneksi.prepareStatement(deleteDetail)) {
                    pstmtDetail.setInt(1, selectedRakit.getNorakit());
                    pstmtDetail.executeUpdate();
                }

                try (PreparedStatement pstmtRakit = dbRakit.koneksi.dbKoneksi.prepareStatement(deleteRakit)) {
                    pstmtRakit.setInt(1, selectedRakit.getNorakit());
                    pstmtRakit.executeUpdate();
                }

                dbRakit.koneksi.dbKoneksi.commit();
                dbRakit.koneksi.dbKoneksi.setAutoCommit(true);

                showdata();

                Alert a = new Alert(Alert.AlertType.INFORMATION, "Data rakit berhasil dihapus.", ButtonType.OK);
                a.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    dbRakit.koneksi.dbKoneksi.rollback();
                    dbRakit.koneksi.dbKoneksi.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus data rakit karena data rakit sudah pernah dilakukan transaksi.");
            }
        } else {

        }
    }

    @FXML
    private void searchclick(ActionEvent event) {
        String input = txtnorakit.getText().trim();
        if (input.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Masukkan nomor rakit untuk dicari.");
            return;
        }

        try {
            int searchNorakit = Integer.parseInt(input);
            boolean found = false;
            for (int i = 0; i < tbvrakit.getItems().size(); i++) {
                RakitModel rakit = tbvrakit.getItems().get(i);
                if (rakit.getNorakit() == searchNorakit) {
                    tbvrakit.getSelectionModel().select(i);
                    tbvrakit.scrollTo(i);
                    found = true;
                    break;
                }
            }

            if (!found) {
                showAlert(Alert.AlertType.INFORMATION, "Info", "Nomor rakit " + searchNorakit + " tidak ditemukan.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Nomor rakit harus berupa angka.");
        }
    }

    /**
     * Menampilkan alert dengan tipe, judul, dan pesan yang ditentukan.
     *
     * @param type Tipe alert.
     * @param title Judul alert.
     * @param message Pesan alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void updateclick(ActionEvent event) {
        RakitModel selectedRakit = tbvrakit.getSelectionModel().getSelectedItem();
        if (selectedRakit == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Pilih data rakit yang akan diupdate.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInputRakit.fxml"));
            Parent root = loader.load();

            FXMLInputRakitController controller = loader.getController();
            controller.setRakitModel(selectedRakit);
            controller.setEditMode(true);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();

            showdata();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void insertclick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInputRakit.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            Stage stg = new Stage();
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.setResizable(false);
            stg.setIconified(false);
            stg.setScene(scene);
            stg.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        showdata();
        firstclick(event);
    }

    @FXML
    private void exitclick(ActionEvent event) {
        btnexit.getScene().getWindow().hide();
    }
}
