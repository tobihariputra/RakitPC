package rakitpc.FXML;

import rakitpc.Model.KomponenModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLInputKomponenController implements Initializable {

    boolean editdata = false;

    @FXML
    private TextField txtkodekomponen;
    @FXML
    private ComboBox<String> cmbkategoriutama;
    @FXML
    private TextField txtnamakomponen;
    @FXML
    private TextField txtharga;
    @FXML
    private TextField txtstok;
    @FXML
    private TextField txtgambar;
    @FXML
    private Button btngambar;
    @FXML
    private Button btnsave;
    @FXML
    private Button btncancel;
    @FXML
    private Button btnexit;
    @FXML
    private Label lblkategori1;
    @FXML
    private Label lblkategori2;
    @FXML
    private Label lblkategori3;
    @FXML
    private Label lblkategori4;
    @FXML
    private ComboBox<String> cmbkategori1;
    @FXML
    private ComboBox<String> cmbkategori2;
    @FXML
    private ComboBox<String> cmbkategori3;
    @FXML
    private ComboBox<String> cmbkategori4;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hideAllCategories();

        cmbkategoriutama.getItems().addAll("Processor", "Motherboard", "RAM", "VGA", "Storage", "Power Supply", "CPU Cooler", "Casing PC");

        cmbkategoriutama.setOnAction(event -> {
            clearSelection();
            handleCategorySelection();
        });
    }

    private void hideAllCategories() {
        lblkategori1.setVisible(false);
        lblkategori2.setVisible(false);
        lblkategori3.setVisible(false);
        lblkategori4.setVisible(false);
        cmbkategori1.setVisible(false);
        cmbkategori2.setVisible(false);
        cmbkategori3.setVisible(false);
        cmbkategori4.setVisible(false);
    }

    private void handleCategorySelection() {
        hideAllCategories();
        cmbkategori1.setOnAction(null);

        String selectedCategory = cmbkategoriutama.getValue();

        switch (selectedCategory) {
            case "Processor":
                lblkategori1.setText("Brand");
                lblkategori1.setVisible(true);
                cmbkategori1.getItems().setAll("AMD", "Intel");
                cmbkategori1.setVisible(true);

                lblkategori2.setText("Socket");
                lblkategori2.setVisible(true);
                cmbkategori2.setVisible(false);

                cmbkategori1.setOnAction(e -> {
                    cmbkategori2.getItems().clear();
                    String brand = cmbkategori1.getValue();
                    if ("AMD".equals(brand)) {
                        cmbkategori2.getItems().setAll("AM4", "AM5");
                    } else if ("Intel".equals(brand)) {
                        cmbkategori2.getItems().setAll("LGA1200", "LGA1700");
                    }
                    cmbkategori2.setVisible(true);

                });
                break;

            case "Motherboard":
                lblkategori1.setText("Brand");
                lblkategori1.setVisible(true);
                cmbkategori1.getItems().setAll("AMD", "Intel");
                cmbkategori1.setVisible(true);

                lblkategori2.setText("Socket");
                lblkategori2.setVisible(true);
                cmbkategori2.setVisible(false);

                cmbkategori1.setOnAction(e -> {
                    cmbkategori2.getItems().clear();
                    String brand = cmbkategori1.getValue();
                    if ("AMD".equals(brand)) {
                        cmbkategori2.getItems().setAll("AM4", "AM5");
                    } else if ("Intel".equals(brand)) {
                        cmbkategori2.getItems().setAll("LGA1200", "LGA1700");
                    }
                    cmbkategori2.setVisible(true);

                });

                lblkategori3.setText("Jenis Memori");
                lblkategori3.setVisible(true);
                cmbkategori3.getItems().setAll("DDR4", "DDR5");
                cmbkategori3.setVisible(true);
                break;

            case "RAM":
            case "VGA":
            case "Storage":
            case "Power Supply":
                lblkategori1.setText(selectedCategory.equals("RAM") ? "Jenis Memori"
                        : selectedCategory.equals("VGA") ? "Brand"
                        : selectedCategory.equals("Storage") ? "Jenis Storage" : "Jenis Power Supply");
                lblkategori1.setVisible(true);
                cmbkategori1.getItems().setAll(selectedCategory.equals("RAM") ? new String[]{"DDR4", "DDR5"}
                        : selectedCategory.equals("VGA") ? new String[]{"AMD", "NVIDIA"}
                        : selectedCategory.equals("Storage") ? new String[]{"HDD", "SSD SATA", "SSD NVME"}
                        : new String[]{"PSU 80+ Bronze", "PSU 80+ Silver", "PSU 80+ Gold", "PSU 80+ Platinum"});
                cmbkategori1.setVisible(true);

                cmbkategori2.setVisible(false);
                lblkategori2.setVisible(false);
                cmbkategori2.setValue(null);

                break;

            case "CPU Cooler":
            case "Casing PC":
                cmbkategori2.setVisible(false);
                lblkategori2.setVisible(false);
                cmbkategori2.setValue(null);
                break;
        }
        generateKodeKomponen(selectedCategory);
    }

    private void clearSelection() {
        cmbkategori1.getItems().clear();
        cmbkategori2.getItems().clear();
        cmbkategori3.getItems().clear();
        cmbkategori4.getItems().clear();

        cmbkategori1.setValue(null);
        cmbkategori2.setValue(null);
        cmbkategori3.setValue(null);
        cmbkategori4.setValue(null);

        txtnamakomponen.clear();
        txtharga.clear();
        txtstok.clear();
        txtgambar.clear();

        hideAllCategories();

        cmbkategori2.setVisible(false);
        lblkategori2.setVisible(false);
        cmbkategori2.setValue(null);
    }

    private void generateKodeKomponen(String category) {
        String prefix = "";
        switch (category) {
            case "Processor":
                prefix = "PROC";
                break;
            case "Motherboard":
                prefix = "MOBO";
                break;
            case "RAM":
                prefix = "RAM";
                break;
            case "VGA":
                prefix = "VGA";
                break;
            case "Storage":
                prefix = "STOR";
                break;
            case "Power Supply":
                prefix = "PSU";
                break;
            case "CPU Cooler":
                prefix = "COOL";
                break;
            case "Casing PC":
                prefix = "CASE";
                break;
        }

        int lastNumber = FXMLDashboardController.dtkomponen.getLastKodeKomponen(prefix);
        String newKodeKomponen = prefix + "-" + String.format("%03d", lastNumber + 1);
        txtkodekomponen.setText(newKodeKomponen);
    }

    public void execute(KomponenModel d) {
        if (!d.getKodekomponen().isEmpty()) {
            editdata = true;
            txtkodekomponen.setText(d.getKodekomponen());
            txtnamakomponen.setText(d.getNamakomponen());
            txtharga.setText(String.valueOf(d.getHarga()));
            txtstok.setText(String.valueOf(d.getStok()));
            txtgambar.setText(d.getGambar());

            cmbkategoriutama.setValue(d.getKategori());
            cmbkategoriutama.setDisable(true);

            if ("Processor".equals(d.getKategori())) {
                cmbkategori1.setValue(d.getBrand());
                cmbkategori2.setValue(d.getSocket());
            }
            if ("Motherboard".equals(d.getKategori())) {
                cmbkategori1.setValue(d.getBrand());
                cmbkategori2.setValue(d.getSocket());
                cmbkategori3.setValue(d.getJenismemori());
            }
            if ("RAM".equals(d.getKategori())) {
                cmbkategori1.setValue(d.getJenismemori());
            }

            txtkodekomponen.setEditable(false);
            txtnamakomponen.requestFocus();
        }
    }

    @FXML
    private void gambarclick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Gambar Barang");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                String fileName = selectedFile.getName();

                String projectDir = System.getProperty("user.dir");
                File destDir = new File(projectDir + "\\img");

                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                File destFile = new File(destDir, fileName);

                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                txtgambar.setText("img\\" + fileName);

            } catch (IOException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Gagal memindahkan gambar", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    @FXML
    private void saveclick(ActionEvent event) {
        if (cmbkategoriutama.getValue() == null || cmbkategoriutama.getValue().isEmpty()) {
            showErrorAlert("Kategori utama harus dipilih", cmbkategoriutama);
            return;
        }
        if (txtkodekomponen.getText().isEmpty()) {
            showErrorAlert("Kode Komponen wajib terisi, pilih kategori utama!", txtkodekomponen);
            return;
        }

        if (txtnamakomponen.getText().isEmpty()) {
            showErrorAlert("Nama komponen tidak boleh kosong", txtnamakomponen);
            return;
        }
        if (txtharga.getText().isEmpty()) {
            showErrorAlert("Harga tidak boleh kosong", txtharga);
            return;
        }
        if (txtstok.getText().isEmpty()) {
            showErrorAlert("Stok tidak boleh kosong", txtstok);
            return;
        }
        if (cmbkategori1.isVisible() && (cmbkategori1.getValue() == null || cmbkategori1.getValue().isEmpty())) {
            showErrorAlert("Pilihan pada kategori 1 tidak boleh kosong", cmbkategori1);
            return;
        }
        if (cmbkategori2.isVisible() && (cmbkategori2.getValue() == null || cmbkategori2.getValue().isEmpty())) {
            showErrorAlert("Pilihan pada kategori 2 tidak boleh kosong", cmbkategori2);
            return;
        }
        if (cmbkategori3.isVisible() && (cmbkategori3.getValue() == null || cmbkategori3.getValue().isEmpty())) {
            showErrorAlert("Pilihan pada kategori 3 tidak boleh kosong", cmbkategori3);
            return;
        }
        if (cmbkategori4.isVisible() && (cmbkategori4.getValue() == null || cmbkategori4.getValue().isEmpty())) {
            showErrorAlert("Pilihan pada kategori 4 tidak boleh kosong", cmbkategori4);
            return;
        }

        KomponenModel n = new KomponenModel();
        n.setKodekomponen(txtkodekomponen.getText());
        n.setNamakomponen(txtnamakomponen.getText());
        n.setHarga(Double.parseDouble(txtharga.getText()));
        n.setStok(Integer.parseInt(txtstok.getText()));
        n.setGambar(txtgambar.getText());

        n.setKategori(cmbkategoriutama.getValue());

        if ("Processor".equals(cmbkategoriutama.getValue())) {
            n.setBrand(cmbkategori1.getValue());
            n.setSocket(cmbkategori2.getValue());
        } else if ("Motherboard".equals(cmbkategoriutama.getValue())) {
            n.setBrand(cmbkategori1.getValue());
            n.setSocket(cmbkategori2.getValue());
            n.setJenismemori(cmbkategori3.getValue());
        } else if ("RAM".equals(cmbkategoriutama.getValue())) {
            n.setJenismemori(cmbkategori1.getValue());
        } else if ("VGA".equals(cmbkategoriutama.getValue())) {
            n.setBrand(cmbkategori1.getValue());
        }

        FXMLDashboardController.dtkomponen.setKomponenModel(n);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (editdata) {
            if (FXMLDashboardController.dtkomponen.update()) {
                showSuccessAlert("Data berhasil diubah");
                txtkodekomponen.setEditable(true);
                cancelclick(event);
                stage.close();
            } else {
                showErrorAlert("Data gagal diubah", null);
            }
        } else if (FXMLDashboardController.dtkomponen.validasi(n.getKodekomponen()) <= 0) {
            if (FXMLDashboardController.dtkomponen.insert()) {
                showSuccessAlert("Data berhasil disimpan");
                cancelclick(event);
                stage.close();
            } else {
                showErrorAlert("Data gagal disimpan", null);
            }
        } else {
            showErrorAlert("Data sudah ada", txtkodekomponen);
        }
        System.out.println("Setting Jenis Memori: " + cmbkategori3.getValue());
        System.out.println("Setting Socket: " + cmbkategori2.getValue());

    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Sukses");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showErrorAlert(String message, Node node) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (node != null) {
            node.requestFocus();
        }
    }

    @FXML
    private void cancelclick(ActionEvent event) {
        clearSelection();
        txtkodekomponen.requestFocus();
    }

    @FXML
    private void exitclick(ActionEvent event) {
        btnexit.getScene().getWindow().hide();
    }
}
