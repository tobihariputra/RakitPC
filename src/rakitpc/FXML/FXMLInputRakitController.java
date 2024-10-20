package rakitpc.FXML;

import rakitpc.Model.KomponenModel;
import rakitpc.Model.RakitDetailModel;
import rakitpc.Model.RakitModel;
import rakitpc.DB.DBRakit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.Date;

public class FXMLInputRakitController implements Initializable {

    private DBRakit dbrakit;
    private NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private boolean isEditMode = false;
    private RakitModel rakitToEdit;

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        if (isEditMode) {
            btnsimpan.setText("Update");
            lblnorakit.setDisable(true);
        }
    }

    public void setRakitModel(RakitModel rakit) {
        this.rakitToEdit = rakit;
        populateFieldsWithRakit(rakit);
    }

    private void populateFieldsWithRakit(RakitModel rakit) {
        lblnorakit.setText(String.valueOf(rakit.getNorakit()));
    }
    @FXML
    private ComboBox<String> cmbbrand;
    @FXML
    private ComboBox<String> cmbsocket;
    @FXML
    private ComboBox<String> cmbcpu;
    @FXML
    private ComboBox<String> cmbmotherboard;
    @FXML
    private ComboBox<String> cmbram;
    @FXML
    private ComboBox<String> cmbvga;
    @FXML
    private ComboBox<String> cmbstorage;
    @FXML
    private ComboBox<String> cmbpsu;
    @FXML
    private ComboBox<String> cmbcpucooler;
    @FXML
    private ComboBox<String> cmbcasing;
    @FXML
    private Button btnsimpan;
    @FXML
    private Button btnreset;
    @FXML
    private Button btnexit;
    @FXML
    private TextField qtycpu;
    @FXML
    private TextField qtymotherboard;
    @FXML
    private TextField qtyram;
    @FXML
    private TextField qtyvga;
    @FXML
    private TextField qtystorage;
    @FXML
    private TextField qtypsu;
    @FXML
    private TextField qtycpucooler;
    @FXML
    private TextField qtycasing;
    @FXML
    private TextField subtotalcpu;
    @FXML
    private TextField subtotalmotherboard;
    @FXML
    private TextField subtotalram;
    @FXML
    private TextField subtotalvga;
    @FXML
    private TextField subtotalstorage;
    @FXML
    private TextField subtotalpsu;
    @FXML
    private TextField subtotalcpucooler;
    @FXML
    private TextField subtotalcasing;
    @FXML
    private TextField totalbayar;
    @FXML
    private Label lblnorakit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbrakit = new DBRakit();
        loadInitialData();
        setupListeners();
        generateNorakit();
        subtotalcpu.setText(rupiahFormat.format(0));
        subtotalmotherboard.setText(rupiahFormat.format(0));
        subtotalram.setText(rupiahFormat.format(0));
        subtotalvga.setText(rupiahFormat.format(0));
        subtotalstorage.setText(rupiahFormat.format(0));
        subtotalpsu.setText(rupiahFormat.format(0));
        subtotalcpucooler.setText(rupiahFormat.format(0));
        subtotalcasing.setText(rupiahFormat.format(0));
        totalbayar.setText(rupiahFormat.format(0));
    }

    private void loadInitialData() {

        ObservableList<String> brands = FXCollections.observableArrayList();
        try {
            String query = "SELECT DISTINCT brand FROM komponen WHERE kategori IN ('Processor', 'Motherboard')";
            Statement stmt = dbrakit.koneksi.dbKoneksi.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String brand = rs.getString("brand");
                if (brand != null && !brands.contains(brand)) {
                    brands.add(brand);
                }
            }
            cmbbrand.setItems(brands);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadComboBox("Processor", cmbcpu);
        loadComboBox("Motherboard", cmbmotherboard);
        loadComboBox("RAM", cmbram);
        loadComboBox("VGA", cmbvga);
        loadComboBox("Storage", cmbstorage);
        loadComboBox("Power Supply", cmbpsu);
        loadComboBox("CPU Cooler", cmbcpucooler);
        loadComboBox("Casing PC", cmbcasing);
    }

    private void loadComboBox(String kategori, ComboBox<String> comboBox) {
        ObservableList<String> items = FXCollections.observableArrayList();
        try {
            String query = "SELECT namakomponen FROM komponen WHERE kategori = ?";
            PreparedStatement pstmt = dbrakit.koneksi.dbKoneksi.prepareStatement(query);
            pstmt.setString(1, kategori);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(rs.getString("namakomponen"));
            }
            comboBox.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        cmbbrand.setOnAction(e -> onBrandSelected());
        cmbsocket.setOnAction(e -> onSocketSelected());
        cmbmotherboard.setOnAction(e -> onMotherboardSelected());

        qtycpu.textProperty().addListener((obs, oldVal, newVal) -> updateSubtotal("Processor", qtycpu, subtotalcpu));
        qtymotherboard.textProperty().addListener((obs, oldVal, newVal) -> updateSubtotal("Motherboard", qtymotherboard, subtotalmotherboard));
        qtyram.textProperty().addListener((obs, oldVal, newVal) -> updateSubtotal("RAM", qtyram, subtotalram));
        qtyvga.textProperty().addListener((obs, oldVal, newVal) -> updateSubtotal("VGA", qtyvga, subtotalvga));
        qtystorage.textProperty().addListener((obs, oldVal, newVal) -> updateSubtotal("Storage", qtystorage, subtotalstorage));
        qtypsu.textProperty().addListener((obs, oldVal, newVal) -> updateSubtotal("Power Supply", qtypsu, subtotalpsu));
        qtycpucooler.textProperty().addListener((obs, oldVal, newVal) -> updateSubtotal("CPU Cooler", qtycpucooler, subtotalcpucooler));
        qtycasing.textProperty().addListener((obs, oldVal, newVal) -> updateSubtotal("Casing PC", qtycasing, subtotalcasing));

        btnsimpan.setOnAction(this::simpanclick);
        btnreset.setOnAction(this::resetclick);
        btnexit.setOnAction(this::exitclick);
    }

    private void onBrandSelected() {
        String selectedBrand = cmbbrand.getValue();
        if (selectedBrand != null) {

            ObservableList<String> sockets = FXCollections.observableArrayList();
            try {
                String query = "SELECT DISTINCT socket FROM komponen WHERE brand = ? AND kategori IN ('Processor', 'Motherboard')";
                PreparedStatement pstmt = dbrakit.koneksi.dbKoneksi.prepareStatement(query);
                pstmt.setString(1, selectedBrand);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String socket = rs.getString("socket");
                    if (socket != null && !sockets.contains(socket)) {
                        sockets.add(socket);
                    }
                }
                cmbsocket.setItems(sockets);
                cmbsocket.setValue(null);
                cmbcpu.setItems(FXCollections.observableArrayList());
                cmbmotherboard.setItems(FXCollections.observableArrayList());
                cmbram.setItems(FXCollections.observableArrayList());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSocketSelected() {
        String selectedBrand = cmbbrand.getValue();
        String selectedSocket = cmbsocket.getValue();
        if (selectedBrand != null && selectedSocket != null) {

            ObservableList<String> cpus = FXCollections.observableArrayList();
            try {
                String query = "SELECT namakomponen FROM komponen WHERE kategori = 'Processor' AND brand = ? AND socket = ?";
                PreparedStatement pstmt = dbrakit.koneksi.dbKoneksi.prepareStatement(query);
                pstmt.setString(1, selectedBrand);
                pstmt.setString(2, selectedSocket);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    cpus.add(rs.getString("namakomponen"));
                }
                cmbcpu.setItems(cpus);
                cmbcpu.setValue(null);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ObservableList<String> motherboards = FXCollections.observableArrayList();
            try {
                String query = "SELECT namakomponen FROM komponen WHERE kategori = 'Motherboard' AND brand = ? AND socket = ?";
                PreparedStatement pstmt = dbrakit.koneksi.dbKoneksi.prepareStatement(query);
                pstmt.setString(1, selectedBrand);
                pstmt.setString(2, selectedSocket);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    motherboards.add(rs.getString("namakomponen"));
                }
                cmbmotherboard.setItems(motherboards);
                cmbmotherboard.setValue(null);
                cmbram.setItems(FXCollections.observableArrayList());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void onMotherboardSelected() {
        String selectedMotherboard = cmbmotherboard.getValue();
        if (selectedMotherboard != null) {

            String jenismemori = null;
            try {
                String query = "SELECT jenismemori FROM komponen WHERE kategori = 'Motherboard' AND namakomponen = ?";
                PreparedStatement pstmt = dbrakit.koneksi.dbKoneksi.prepareStatement(query);
                pstmt.setString(1, selectedMotherboard);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    jenismemori = rs.getString("jenismemori");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (jenismemori != null) {

                ObservableList<String> rams = FXCollections.observableArrayList();
                try {
                    String query = "SELECT namakomponen FROM komponen WHERE kategori = 'RAM' AND jenismemori = ?";
                    PreparedStatement pstmt = dbrakit.koneksi.dbKoneksi.prepareStatement(query);
                    pstmt.setString(1, jenismemori);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        rams.add(rs.getString("namakomponen"));
                    }
                    cmbram.setItems(rams);
                    cmbram.setValue(null);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateSubtotal(String kategori, TextField qtyField, TextField subtotalField) {
        String selectedItem = null;
        switch (kategori) {
            case "Processor":
                selectedItem = cmbcpu.getValue();
                break;
            case "Motherboard":
                selectedItem = cmbmotherboard.getValue();
                break;
            case "RAM":
                selectedItem = cmbram.getValue();
                break;
            case "VGA":
                selectedItem = cmbvga.getValue();
                break;
            case "Storage":
                selectedItem = cmbstorage.getValue();
                break;
            case "Power Supply":
                selectedItem = cmbpsu.getValue();
                break;
            case "CPU Cooler":
                selectedItem = cmbcpucooler.getValue();
                break;
            case "Casing PC":
                selectedItem = cmbcasing.getValue();
                break;
            default:
                break;
        }

        if (selectedItem != null && !selectedItem.isEmpty() && !qtyField.getText().isEmpty()) {
            try {
                double harga = dbrakit.getHargaKomponen(selectedItem);
                int qty = Integer.parseInt(qtyField.getText());
                double subtotal = harga * qty;
                subtotalField.setText(rupiahFormat.format(subtotal));
                updateTotalBayar();
            } catch (NumberFormatException nfe) {

                subtotalField.setText(rupiahFormat.format(0));
                updateTotalBayar();
            }
        } else {
            subtotalField.setText(rupiahFormat.format(0));
            updateTotalBayar();
        }
    }

    private void updateTotalBayar() {
        double total = 0;
        try {
            total += parseRupiah(subtotalcpu.getText());
            total += parseRupiah(subtotalmotherboard.getText());
            total += parseRupiah(subtotalram.getText());
            total += parseRupiah(subtotalvga.getText());
            total += parseRupiah(subtotalstorage.getText());
            total += parseRupiah(subtotalpsu.getText());
            total += parseRupiah(subtotalcpucooler.getText());
            total += parseRupiah(subtotalcasing.getText());
        } catch (ParseException e) {
            total = 0;
        }
        totalbayar.setText(rupiahFormat.format(total));
    }

    private void generateNorakit() {
        try {
            int nextNorakit = dbrakit.getNextNorakit();
            lblnorakit.setText(String.valueOf(nextNorakit));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan saat menghasilkan nomor rakit.");
        }
    }

    @FXML
    private void simpanclick(ActionEvent event) {

        if (cmbbrand.getValue() == null || cmbsocket.getValue() == null || cmbcpu.getValue() == null
                || cmbmotherboard.getValue() == null || cmbram.getValue() == null || cmbvga.getValue() == null
                || cmbstorage.getValue() == null || cmbpsu.getValue() == null || cmbcpucooler.getValue() == null
                || cmbcasing.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Harap lengkapi semua pilihan komponen.");
            return;
        }

        try {
            int norakit = Integer.parseInt(lblnorakit.getText());
            Date tanggal = new Date();

            List<RakitDetailModel> details = new ArrayList<>();

            addDetail(details, cmbcpu.getValue(), Integer.parseInt(qtycpu.getText()), subtotalcpu.getText());
            addDetail(details, cmbmotherboard.getValue(), Integer.parseInt(qtymotherboard.getText()), subtotalmotherboard.getText());
            addDetail(details, cmbram.getValue(), Integer.parseInt(qtyram.getText()), subtotalram.getText());
            addDetail(details, cmbvga.getValue(), Integer.parseInt(qtyvga.getText()), subtotalvga.getText());
            addDetail(details, cmbstorage.getValue(), Integer.parseInt(qtystorage.getText()), subtotalstorage.getText());
            addDetail(details, cmbpsu.getValue(), Integer.parseInt(qtypsu.getText()), subtotalpsu.getText());
            addDetail(details, cmbcpucooler.getValue(), Integer.parseInt(qtycpucooler.getText()), subtotalcpucooler.getText());
            addDetail(details, cmbcasing.getValue(), Integer.parseInt(qtycasing.getText()), subtotalcasing.getText());

            RakitModel rakit = new RakitModel();
            rakit.setNorakit(norakit);
            rakit.setTanggal(tanggal);
            rakit.setDetails(details);

            if (!dbrakit.validateStok(details)) {
                showAlert(Alert.AlertType.ERROR, "Stok Tidak Cukup", "Salah satu atau lebih komponen memiliki stok yang tidak mencukupi.");
                return;
            }

            boolean success;
            if (isEditMode) {

                success = dbrakit.updateRakit(rakit);
            } else {

                success = dbrakit.insertRakit(rakit);
            }

            if (success) {
                String message = isEditMode ? "Rakit PC berhasil diupdate." : "Rakit PC berhasil disimpan.";
                showAlert(Alert.AlertType.INFORMATION, "Success", message);
                resetForm();
                if (isEditMode) {

                    btnsimpan.getScene().getWindow().hide();
                } else {
                    generateNorakit();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan saat menyimpan data.");
            }

        } catch (NumberFormatException nfe) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Jumlah harus berupa angka.");
        }
    }

    /**
     * Membantu menambahkan detail rakit ke list.
     *
     * @param details List detail rakit
     * @param namaKomponen Nama komponen
     * @param jumlah Jumlah komponen
     * @param subtotal Subtotal harga
     */
    private void addDetail(List<RakitDetailModel> details, String namaKomponen, int jumlah, String subtotal) {
        String kodekomponen = dbrakit.getKodeKomponen(namaKomponen);
        double sub = 0;
        try {
            sub = parseRupiah(subtotal);
        } catch (ParseException e) {
            sub = 0;
        }
        RakitDetailModel detail = new RakitDetailModel(Integer.parseInt(lblnorakit.getText()), kodekomponen, jumlah, sub);
        details.add(detail);
    }

    @FXML
    private void resetclick(ActionEvent event) {
        resetForm();
    }

    private void resetForm() {

        List<ComboBox<String>> dependentComboBoxes = Arrays.asList(cmbbrand, cmbsocket, cmbcpu, cmbmotherboard, cmbram);
        for (ComboBox<String> comboBox : dependentComboBoxes) {
            comboBox.setValue(null);
            if (comboBox != cmbbrand && comboBox != cmbsocket && comboBox != cmbcpu && comboBox != cmbmotherboard && comboBox != cmbram) {
                comboBox.setItems(FXCollections.observableArrayList());
            }
        }

        List<ComboBox<String>> independentComboBoxes = Arrays.asList(cmbvga, cmbstorage, cmbpsu, cmbcpucooler, cmbcasing);
        for (ComboBox<String> comboBox : independentComboBoxes) {
            comboBox.setValue(null);

        }

        qtycpu.setText("");
        qtymotherboard.setText("");
        qtyram.setText("");
        qtyvga.setText("");
        qtystorage.setText("");
        qtypsu.setText("");
        qtycpucooler.setText("");
        qtycasing.setText("");

        subtotalcpu.setText(rupiahFormat.format(0));
        subtotalmotherboard.setText(rupiahFormat.format(0));
        subtotalram.setText(rupiahFormat.format(0));
        subtotalvga.setText(rupiahFormat.format(0));
        subtotalstorage.setText(rupiahFormat.format(0));
        subtotalpsu.setText(rupiahFormat.format(0));
        subtotalcpucooler.setText(rupiahFormat.format(0));
        subtotalcasing.setText(rupiahFormat.format(0));

        totalbayar.setText(rupiahFormat.format(0));
        lblnorakit.setText("");
        generateNorakit();
    }

    @FXML
    private void exitclick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Apakah Anda yakin ingin keluar?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Konfirmasi Keluar");
        alert.setHeaderText(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            btnexit.getScene().getWindow().hide();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Mengurai string Rupiah menjadi angka.
     *
     * @param rupiah String dalam format Rupiah
     * @return Angka desimal
     * @throws ParseException jika format tidak valid
     */
    private double parseRupiah(String rupiah) throws ParseException {
        Number number = rupiahFormat.parse(rupiah);
        return number.doubleValue();
    }

}
