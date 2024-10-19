package rakitpc;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBKomponen {

    private KomponenModel dt = new KomponenModel();

    public KomponenModel getKomponenModel() {
        return dt;
    }

    public void setKomponenModel(KomponenModel s) {
        dt = s;
    }

    public ObservableList<KomponenModel> getKomponenByCategory(String category) {
        try {
            ObservableList<KomponenModel> filteredData = FXCollections.observableArrayList();
            Koneksi con = new Koneksi();
            con.bukaKoneksi();
            con.statement = con.dbKoneksi.createStatement();
            ResultSet rs = con.statement.executeQuery("SELECT * FROM komponen WHERE kategori = '" + category + "'");

            while (rs.next()) {
                KomponenModel d = new KomponenModel();
                d.setKodekomponen(rs.getString("kodekomponen"));
                d.setNamakomponen(rs.getString("namakomponen"));
                d.setBrand(rs.getString("brand"));
                d.setSocket(rs.getString("socket"));
                d.setKategori(rs.getString("kategori"));
                d.setHarga(rs.getDouble("harga"));
                d.setStok(rs.getInt("stok"));
                d.setGambar(rs.getString("gambar"));

                filteredData.add(d);
            }
            con.tutupKoneksi();
            return filteredData;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<KomponenModel> Load() {
        try {
            ObservableList<KomponenModel> tableData = FXCollections.observableArrayList();
            Koneksi con = new Koneksi();
            con.bukaKoneksi();
            con.statement = con.dbKoneksi.createStatement();
            ResultSet rs = con.statement.executeQuery("Select kodekomponen, namakomponen, brand, socket, kategori, harga, stok, gambar FROM komponen");

            int i = 1;
            while (rs.next()) {
                KomponenModel d = new KomponenModel();
                d.setKodekomponen(rs.getString("kodekomponen"));
                d.setNamakomponen(rs.getString("namakomponen"));
                d.setBrand(rs.getString("brand"));
                d.setSocket(rs.getString("socket"));
                d.setKategori(rs.getString("kategori"));
                d.setHarga(rs.getDouble("harga"));
                d.setStok(rs.getInt("stok"));
                d.setGambar(rs.getString("gambar"));

                tableData.add(d);
                i++;
            }
            con.tutupKoneksi();
            return tableData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ObservableList<KomponenModel> CariKomponen(String kode, String nama) {
        try {
            ObservableList<KomponenModel> tableData = FXCollections.observableArrayList();
            Koneksi con = new Koneksi();
            con.bukaKoneksi();
            con.statement = con.dbKoneksi.createStatement();
            ResultSet rs = con.statement.executeQuery(
                    "select * from komponen WHERE kodekomponen LIKE '" + kode + "%' OR namakomponen LIKE '" + nama + "%'");
            int i = 1;
            while (rs.next()) {
                KomponenModel d = new KomponenModel();
                d.setKodekomponen(rs.getString("kodekomponen"));
                d.setNamakomponen(rs.getString("namakomponen"));
                d.setBrand(rs.getString("brand"));
                d.setSocket(rs.getString("socket"));
                d.setKategori(rs.getString("kategori"));
                d.setHarga(rs.getDouble("harga"));
                d.setStok(rs.getInt("stok"));
                d.setGambar(rs.getString("gambar"));

                tableData.add(d);
                i++;
            }
            con.tutupKoneksi();
            return tableData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getLastKodeKomponen(String prefix) {
        int lastNumber = 0;
        try {
            Koneksi con = new Koneksi();
            con.bukaKoneksi();
            String query = "SELECT kodekomponen FROM komponen WHERE kodekomponen LIKE ? ORDER BY kodekomponen DESC LIMIT 1";
            con.preparedStatement = con.dbKoneksi.prepareStatement(query);
            con.preparedStatement.setString(1, prefix + "%");
            ResultSet rs = con.preparedStatement.executeQuery();

            if (rs.next()) {
                String kodekomponen = rs.getString("kodekomponen");
                String numberPart = kodekomponen.substring(prefix.length() + 1);
                lastNumber = Integer.parseInt(numberPart);
            }

            con.tutupKoneksi();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastNumber;
    }

    public int validasi(String nomor) {
        int val = 0;
        try {
            Koneksi con = new Koneksi();
            con.bukaKoneksi();
            con.statement = con.dbKoneksi.createStatement();
            ResultSet rs = con.statement.executeQuery("select count(*) as jml from komponen where kodekomponen = '" + nomor + "'");
            while (rs.next()) {
                val = rs.getInt("jml");
            }
            con.tutupKoneksi();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return val;
    }

    public boolean insert() {
        boolean berhasil = false;
        Koneksi con = new Koneksi();
        try {
            con.bukaKoneksi();
            String query = "INSERT INTO komponen (kodekomponen, namakomponen, brand, socket, kategori, harga, stok, gambar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            con.preparedStatement = con.dbKoneksi.prepareStatement(query);
            con.preparedStatement.setString(1, dt.getKodekomponen());
            con.preparedStatement.setString(2, dt.getNamakomponen());
            con.preparedStatement.setString(3, dt.getBrand());
            con.preparedStatement.setString(4, dt.getSocket());
            con.preparedStatement.setString(5, dt.getKategori());
            con.preparedStatement.setDouble(6, dt.getHarga());
            con.preparedStatement.setInt(7, dt.getStok());
            con.preparedStatement.setString(8, dt.getGambar());
            con.preparedStatement.executeUpdate();
            berhasil = true;
        } catch (Exception e) {
            e.printStackTrace();
            berhasil = false;
        } finally {
            con.tutupKoneksi();
            return berhasil;
        }
    }

    public boolean delete(String kodekomponen) {
        boolean berhasil = false;
        Koneksi con = new Koneksi();
        try {
            con.bukaKoneksi();
            String query = "DELETE FROM komponen WHERE kodekomponen = ?";
            con.preparedStatement = con.dbKoneksi.prepareStatement(query);
            con.preparedStatement.setString(1, kodekomponen);
            con.preparedStatement.executeUpdate();
            berhasil = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.tutupKoneksi();
            return berhasil;
        }
    }

    public boolean update() {
        boolean berhasil = false;
        Koneksi con = new Koneksi();
        try {
            con.bukaKoneksi();
            String query = "UPDATE komponen SET namakomponen = ?, brand = ?, socket = ?, kategori = ?, harga = ?, stok = ?, gambar = ? WHERE kodekomponen = ?";
            con.preparedStatement = con.dbKoneksi.prepareStatement(query);
            con.preparedStatement.setString(1, dt.getNamakomponen());
            con.preparedStatement.setString(2, dt.getBrand());
            con.preparedStatement.setString(3, dt.getSocket());
            con.preparedStatement.setString(4, dt.getKategori());
            con.preparedStatement.setDouble(5, dt.getHarga());
            con.preparedStatement.setInt(6, dt.getStok());
            con.preparedStatement.setString(7, dt.getGambar());
            con.preparedStatement.setString(8, dt.getKodekomponen());
            con.preparedStatement.executeUpdate();
            berhasil = true;
        } catch (Exception e) {
            e.printStackTrace();
            berhasil = false;
        } finally {
            con.tutupKoneksi();
            return berhasil;
        }
    }
}
