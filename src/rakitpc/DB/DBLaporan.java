package rakitpc.DB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rakitpc.Model.LaporanCustomerModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import rakitpc.Model.LaporanKomponenModel;
import rakitpc.Model.LaporanTransaksiModel;

public class DBLaporan {

    private Koneksi koneksi;

    public DBLaporan() {
        this.koneksi = new Koneksi();
    }

    public ObservableList<LaporanCustomerModel> getLaporanCustomer() {
        ObservableList<LaporanCustomerModel> list = FXCollections.observableArrayList();

        koneksi.bukaKoneksi();
        Connection conn = koneksi.dbKoneksi;

        try {
            String query = "SELECT c.idmember, c.nama, c.alamat, c.totalbiayarakit, "
                    + "GROUP_CONCAT(t.idtransaksi ORDER BY t.tanggal ASC SEPARATOR ',\n') AS idtransaksi, "
                    + "GROUP_CONCAT(t.norakit ORDER BY t.tanggal ASC SEPARATOR ',\n') AS norakit, "
                    + "GROUP_CONCAT(t.tanggal ORDER BY t.tanggal ASC SEPARATOR ',\n') AS tanggal "
                    + "FROM customer c "
                    + "LEFT JOIN transaksi t ON c.idmember = t.idmember "
                    + "GROUP BY c.idmember, c.nama, c.alamat, c.totalbiayarakit";

            ResultSet resultSet = conn.createStatement().executeQuery(query);

            while (resultSet.next()) {
                LaporanCustomerModel model = new LaporanCustomerModel();
                model.setIdmember(resultSet.getString("idmember"));
                model.setNama(resultSet.getString("nama"));
                model.setAlamat(resultSet.getString("alamat"));
                model.setTotalbiayarakit(resultSet.getDouble("totalbiayarakit"));
                model.setIdtransaksi(resultSet.getString("idtransaksi"));
                model.setNorakit(resultSet.getString("norakit"));
                model.setTanggal(resultSet.getString("tanggal"));

                list.add(model);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            koneksi.tutupKoneksi();
        }

        return list;
    }

    public ObservableList<LaporanKomponenModel> getLaporanKomponen() {
        ObservableList<LaporanKomponenModel> list = FXCollections.observableArrayList();

        koneksi.bukaKoneksi();
        Connection conn = koneksi.dbKoneksi;

        try {
            String query = "SELECT k.kodekomponen, k.namakomponen, k.kategori, "
                    + "IFNULL(k.brand, 'Hanya untuk processor') AS brand, "
                    + "IFNULL(k.socket, 'Hanya untuk processor') AS socket, "
                    + "IFNULL(k.jenismemori, 'Hanya untuk RAM dan motherboard') AS jenismemori, "
                    + "k.harga, k.stok, "
                    + "IFNULL(SUM(rd.jumlah), 0) AS jumlah_terpakai, "
                    + "IFNULL(GROUP_CONCAT(DISTINCT r.norakit ORDER BY r.norakit ASC SEPARATOR ', '), 'Belum pernah terpakai') AS no_rakit "
                    + "FROM komponen k "
                    + "LEFT JOIN rakit_detail rd ON k.kodekomponen = rd.kodekomponen "
                    + "LEFT JOIN rakit r ON rd.norakit = r.norakit "
                    + "GROUP BY k.kodekomponen, k.namakomponen, k.kategori, k.brand, k.socket, k.jenismemori, k.harga, k.stok";

            ResultSet resultSet = conn.createStatement().executeQuery(query);

            while (resultSet.next()) {
                LaporanKomponenModel model = new LaporanKomponenModel();
                model.setKodeKomponen(resultSet.getString("kodekomponen"));
                model.setNamaKomponen(resultSet.getString("namakomponen"));
                model.setKategori(resultSet.getString("kategori"));
                model.setBrand(resultSet.getString("brand"));
                model.setSocket(resultSet.getString("socket"));
                model.setJenisMemori(resultSet.getString("jenismemori"));
                model.setHarga(resultSet.getDouble("harga"));
                model.setStokTersedia(resultSet.getInt("stok"));
                model.setJumlahTerpakai(resultSet.getString("jumlah_terpakai"));
                model.setNoRakit(resultSet.getString("no_rakit"));

                list.add(model);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            koneksi.tutupKoneksi();
        }

        return list;
    }

    public ObservableList<LaporanTransaksiModel> getDaftarTransaksi() {
        ObservableList<LaporanTransaksiModel> list = FXCollections.observableArrayList();

        koneksi.bukaKoneksi();
        Connection conn = koneksi.dbKoneksi;

        try {
            String query = "SELECT t.idtransaksi, c.nama, c.alamat, t.norakit, t.tanggal, t.totalbayar "
                    + "FROM transaksi t "
                    + "JOIN customer c ON t.idmember = c.idmember";

            ResultSet resultSet = conn.createStatement().executeQuery(query);

            while (resultSet.next()) {
                LaporanTransaksiModel model = new LaporanTransaksiModel();
                model.setIdTransaksi(resultSet.getString("idtransaksi"));
                model.setNamaMember(resultSet.getString("nama"));
                model.setAlamat(resultSet.getString("alamat"));
                model.setNomorRakit(resultSet.getInt("norakit"));
                model.setTanggalTransaksi(resultSet.getTimestamp("tanggal"));
                model.setTotalBayar(resultSet.getDouble("totalbayar"));

                list.add(model);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            koneksi.tutupKoneksi();
        }

        return list;
    }

    public ObservableList<LaporanTransaksiModel> getDetailKomponen() {
        ObservableList<LaporanTransaksiModel> list = FXCollections.observableArrayList();

        koneksi.bukaKoneksi();
        Connection conn = koneksi.dbKoneksi;

        try {
            String query = "SELECT t.idtransaksi, rd.kodekomponen, k.namakomponen, k.brand, rd.jumlah, rd.subtotal "
                    + "FROM transaksi t "
                    + "JOIN rakit_detail rd ON t.norakit = rd.norakit "
                    + "JOIN komponen k ON rd.kodekomponen = k.kodekomponen";

            ResultSet resultSet = conn.createStatement().executeQuery(query);

            while (resultSet.next()) {
                LaporanTransaksiModel model = new LaporanTransaksiModel();
                model.setIdTransaksi(resultSet.getString("idtransaksi"));
                model.setKodeKomponen(resultSet.getString("kodekomponen"));
                model.setNamaKomponen(resultSet.getString("namakomponen"));
                model.setBrand(resultSet.getString("brand"));
                model.setJumlahKomponen(resultSet.getInt("jumlah"));
                model.setSubtotalKomponen(resultSet.getDouble("subtotal"));

                list.add(model);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            koneksi.tutupKoneksi();
        }

        return list;
    }

    public ObservableList<LaporanTransaksiModel> getPendapatanPerTanggal() {
        ObservableList<LaporanTransaksiModel> list = FXCollections.observableArrayList();

        koneksi.bukaKoneksi();
        Connection conn = koneksi.dbKoneksi;

        try {
            String query = "SELECT DATE(t.tanggal) AS tanggal, SUM(t.totalbayar) AS total_pendapatan "
                    + "FROM transaksi t "
                    + "GROUP BY DATE(t.tanggal)";

            ResultSet resultSet = conn.createStatement().executeQuery(query);

            while (resultSet.next()) {
                LaporanTransaksiModel model = new LaporanTransaksiModel();
                model.setTanggalPendapatan(resultSet.getDate("tanggal"));
                model.setTotalPendapatan(resultSet.getDouble("total_pendapatan"));

                list.add(model);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            koneksi.tutupKoneksi();
        }

        return list;
    }

    public ObservableList<LaporanTransaksiModel> getKomponenTerlaris() {
        ObservableList<LaporanTransaksiModel> list = FXCollections.observableArrayList();

        koneksi.bukaKoneksi();
        Connection conn = koneksi.dbKoneksi;

        try {
            String query = "SELECT k.namakomponen, SUM(rd.jumlah) AS total_terjual "
                    + "FROM rakit_detail rd "
                    + "JOIN komponen k ON rd.kodekomponen = k.kodekomponen "
                    + "GROUP BY k.namakomponen "
                    + "ORDER BY total_terjual DESC";

            ResultSet resultSet = conn.createStatement().executeQuery(query);

            while (resultSet.next()) {
                LaporanTransaksiModel model = new LaporanTransaksiModel();
                model.setNamaKomponen(resultSet.getString("namakomponen"));
                model.setTotalTerjual(resultSet.getInt("total_terjual"));

                list.add(model);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            koneksi.tutupKoneksi();
        }

        return list;
    }

    public ObservableList<LaporanTransaksiModel> getPelangganTertinggi() {
        ObservableList<LaporanTransaksiModel> list = FXCollections.observableArrayList();

        koneksi.bukaKoneksi();
        Connection conn = koneksi.dbKoneksi;

        try {
            String query = "SELECT c.nama, c.totalbiayarakit, SUM(t.totalbayar) AS total_bayar "
                    + "FROM customer c "
                    + "JOIN transaksi t ON c.idmember = t.idmember "
                    + "GROUP BY c.nama, c.totalbiayarakit "
                    + "ORDER BY total_bayar DESC";

            ResultSet resultSet = conn.createStatement().executeQuery(query);

            while (resultSet.next()) {
                LaporanTransaksiModel model = new LaporanTransaksiModel();
                model.setNamaMember(resultSet.getString("nama"));
                model.setTotalBiayaRakit(resultSet.getDouble("totalbiayarakit"));
                model.setTotalBayarPelanggan(resultSet.getDouble("total_bayar"));

                list.add(model);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            koneksi.tutupKoneksi();
        }

        return list;
    }
}
