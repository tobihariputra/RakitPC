package rakitpc.DB;

import rakitpc.Model.TransaksiModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rakitpc.Model.RakitDetailModel;

public class DBTransaksi {

    private Koneksi koneksi;

    public DBTransaksi() {
        koneksi = new Koneksi();
        koneksi.bukaKoneksi();
    }

    public String generateNoTransaksi() {
        String prefix = "T";
        String noTransaksi = prefix + "00001";
        try {
            String sql = "SELECT idtransaksi FROM transaksi ORDER BY idtransaksi DESC LIMIT 1";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("idtransaksi");
                // Extract numeric part
                int numericPart = Integer.parseInt(lastId.substring(prefix.length()));
                numericPart += 1;
                noTransaksi = String.format(prefix + "%05d", numericPart);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noTransaksi;
    }

    public boolean simpanTransaksi(TransaksiModel transaksi) {
        boolean sukses = false;
        try {
            String sql = "INSERT INTO transaksi (idtransaksi, idmember, norakit, tanggal, totalbayar) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ps.setString(1, transaksi.getIdtransaksi());
            ps.setString(2, transaksi.getIdmember());
            ps.setInt(3, transaksi.getNorakit());
            ps.setTimestamp(4, transaksi.getTanggal());
            ps.setDouble(5, transaksi.getTotalbayar());
            ps.executeUpdate();
            ps.close();
            sukses = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sukses;
    }

    public boolean updateTotalBiayaRakit(String idmember, double tambahanBiaya) {
        boolean sukses = false;
        try {
            String sql = "UPDATE customer SET totalbiayarakit = totalbiayarakit + ? WHERE idmember = ?";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ps.setDouble(1, tambahanBiaya);
            ps.setString(2, idmember);
            ps.executeUpdate();
            ps.close();
            sukses = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sukses;
    }

    public String getNamaKomponen(String kodekomponen) {
        String nama = "";
        try {
            String sql = "SELECT namakomponen FROM komponen WHERE kodekomponen = ?";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ps.setString(1, kodekomponen);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nama = rs.getString("namakomponen");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nama;
    }

    public boolean isRakitInTransaksi(int norakit) {
        boolean exists = false;
        try {
            String sql = "SELECT COUNT(*) AS count FROM transaksi WHERE norakit = ?";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ps.setInt(1, norakit);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exists = rs.getInt("count") > 0;
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    public String getNamaCustomer(String idmember) {
        String nama = "";
        try {
            String sql = "SELECT nama FROM customer WHERE idmember = ?";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ps.setString(1, idmember);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nama = rs.getString("nama");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nama;
    }

    public boolean deleteTransaksi(String idtransaksi) {
        boolean sukses = false;
        try {
            String sql = "DELETE FROM transaksi WHERE idtransaksi = ?";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ps.setString(1, idtransaksi);
            ps.executeUpdate();
            ps.close();
            sukses = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sukses;
    }

    public ObservableList<TransaksiModel> getAllTransaksi() {
        ObservableList<TransaksiModel> list = FXCollections.observableArrayList();
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
                list.add(transaksi);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<RakitDetailModel> getDetilTransaksi(int norakit) {
        ObservableList<RakitDetailModel> list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM rakit_detail WHERE norakit = ?";
            PreparedStatement ps = koneksi.dbKoneksi.prepareStatement(sql);
            ps.setInt(1, norakit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RakitDetailModel detail = new RakitDetailModel(
                        rs.getInt("norakit"),
                        rs.getString("kodekomponen"),
                        rs.getInt("jumlah"),
                        rs.getDouble("subtotal")
                );
                list.add(detail);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
