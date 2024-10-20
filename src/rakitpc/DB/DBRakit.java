package rakitpc.DB;

import rakitpc.Model.RakitModel;
import rakitpc.Model.RakitDetailModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRakit {

    public Koneksi koneksi;

    public DBRakit() {
        koneksi = new Koneksi();
        koneksi.bukaKoneksi();
    }

    public void closeConnection() {
        koneksi.tutupKoneksi();
    }

    /**
     * Mendapatkan nomor rakit berikutnya.
     *
     * @return nomor rakit berikutnya
     * @throws SQLException jika terjadi kesalahan pada query
     */
    public int getNextNorakit() throws SQLException {
        String query = "SELECT IFNULL(MAX(norakit), 0) + 1 AS nextNorakit FROM rakit";
        Statement stmt = koneksi.dbKoneksi.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            return rs.getInt("nextNorakit");
        }
        return 1;
    }

    /**
     * Memvalidasi apakah stok mencukupi untuk semua komponen dalam rakit.
     *
     * @param details List detail rakit
     * @return true jika semua stok mencukupi, false jika tidak
     */
    public boolean validateStok(List<RakitDetailModel> details) {
        for (RakitDetailModel detail : details) {
            String query = "SELECT stok FROM komponen WHERE kodekomponen = ?";
            try (PreparedStatement pstmt = koneksi.dbKoneksi.prepareStatement(query)) {
                pstmt.setString(1, detail.getKodekomponen());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int stok = rs.getInt("stok");
                    if (stok < detail.getJumlah()) {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Memasukkan data rakit dan detailnya ke database.
     *
     * @param model Objek RakitModel yang berisi data rakit dan detailnya
     * @return true jika berhasil, false jika gagal
     */
    public boolean insertRakit(RakitModel model) {
        String insertRakit = "INSERT INTO rakit (norakit, tanggal) VALUES (?, ?)";
        String insertDetail = "INSERT INTO rakit_detail (norakit, kodekomponen, jumlah, subtotal) VALUES (?, ?, ?, ?)";
        String updateStok = "UPDATE komponen SET stok = stok - ? WHERE kodekomponen = ?";

        try {
            koneksi.dbKoneksi.setAutoCommit(false);

            try (PreparedStatement pstmtRakit = koneksi.dbKoneksi.prepareStatement(insertRakit)) {
                pstmtRakit.setInt(1, model.getNorakit());
                pstmtRakit.setTimestamp(2, new java.sql.Timestamp(model.getTanggal().getTime()));
                pstmtRakit.executeUpdate();
            }

            try (PreparedStatement pstmtDetail = koneksi.dbKoneksi.prepareStatement(insertDetail)) {
                for (RakitDetailModel detail : model.getDetails()) {
                    pstmtDetail.setInt(1, detail.getNorakit());
                    pstmtDetail.setString(2, detail.getKodekomponen());
                    pstmtDetail.setInt(3, detail.getJumlah());
                    pstmtDetail.setDouble(4, detail.getSubtotal());
                    pstmtDetail.addBatch();
                }
                pstmtDetail.executeBatch();
            }

            try (PreparedStatement pstmtUpdate = koneksi.dbKoneksi.prepareStatement(updateStok)) {
                for (RakitDetailModel detail : model.getDetails()) {
                    pstmtUpdate.setInt(1, detail.getJumlah());
                    pstmtUpdate.setString(2, detail.getKodekomponen());
                    pstmtUpdate.addBatch();
                }
                pstmtUpdate.executeBatch();
            }

            koneksi.dbKoneksi.commit();
            koneksi.dbKoneksi.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                koneksi.dbKoneksi.rollback();
                koneksi.dbKoneksi.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Mendapatkan kode komponen berdasarkan nama komponen.
     *
     * @param namaKomponen Nama komponen
     * @return kode komponen atau null jika tidak ditemukan
     */
    public String getKodeKomponen(String namaKomponen) {
        String query = "SELECT kodekomponen FROM komponen WHERE namakomponen = ?";
        try (PreparedStatement pstmt = koneksi.dbKoneksi.prepareStatement(query)) {
            pstmt.setString(1, namaKomponen);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("kodekomponen");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mendapatkan harga komponen berdasarkan nama komponen.
     *
     * @param namaKomponen Nama komponen
     * @return harga komponen atau 0 jika tidak ditemukan
     */
    public double getHargaKomponen(String namaKomponen) {
        String query = "SELECT harga FROM komponen WHERE namakomponen = ?";
        try (PreparedStatement pstmt = koneksi.dbKoneksi.prepareStatement(query)) {
            pstmt.setString(1, namaKomponen);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("harga");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Memperbarui data rakit dan detailnya di database.
     *
     * @param model Objek RakitModel yang berisi data rakit dan detailnya
     * @return true jika berhasil, false jika gagal
     */
    public boolean updateRakit(RakitModel model) {
        String updateRakit = "UPDATE rakit SET tanggal = ? WHERE norakit = ?";
        String deleteDetail = "DELETE FROM rakit_detail WHERE norakit = ?";
        String insertDetail = "INSERT INTO rakit_detail (norakit, kodekomponen, jumlah, subtotal) VALUES (?, ?, ?, ?)";
        String updateStokIncrease = "UPDATE komponen SET stok = stok + ? WHERE kodekomponen = ?";
        String updateStokDecrease = "UPDATE komponen SET stok = stok - ? WHERE kodekomponen = ?";

        try {
            koneksi.dbKoneksi.setAutoCommit(false);

            try (PreparedStatement pstmtRakit = koneksi.dbKoneksi.prepareStatement(updateRakit)) {
                pstmtRakit.setTimestamp(1, new java.sql.Timestamp(model.getTanggal().getTime()));
                pstmtRakit.setInt(2, model.getNorakit());
                pstmtRakit.executeUpdate();
            }

            List<RakitDetailModel> previousDetails = getRakitDetails(model.getNorakit());
            for (RakitDetailModel detail : previousDetails) {
                try (PreparedStatement pstmtStokInc = koneksi.dbKoneksi.prepareStatement(updateStokIncrease)) {
                    pstmtStokInc.setInt(1, detail.getJumlah());
                    pstmtStokInc.setString(2, detail.getKodekomponen());
                    pstmtStokInc.executeUpdate();
                }
            }

            try (PreparedStatement pstmtDelete = koneksi.dbKoneksi.prepareStatement(deleteDetail)) {
                pstmtDelete.setInt(1, model.getNorakit());
                pstmtDelete.executeUpdate();
            }

            try (PreparedStatement pstmtInsert = koneksi.dbKoneksi.prepareStatement(insertDetail)) {
                for (RakitDetailModel detail : model.getDetails()) {
                    pstmtInsert.setInt(1, detail.getNorakit());
                    pstmtInsert.setString(2, detail.getKodekomponen());
                    pstmtInsert.setInt(3, detail.getJumlah());
                    pstmtInsert.setDouble(4, detail.getSubtotal());
                    pstmtInsert.addBatch();
                }
                pstmtInsert.executeBatch();
            }

            for (RakitDetailModel detail : model.getDetails()) {
                try (PreparedStatement pstmtStokDec = koneksi.dbKoneksi.prepareStatement(updateStokDecrease)) {
                    pstmtStokDec.setInt(1, detail.getJumlah());
                    pstmtStokDec.setString(2, detail.getKodekomponen());
                    pstmtStokDec.executeUpdate();
                }
            }

            koneksi.dbKoneksi.commit();
            koneksi.dbKoneksi.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                koneksi.dbKoneksi.rollback();
                koneksi.dbKoneksi.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Mendapatkan detail rakit berdasarkan nomor rakit.
     *
     * @param norakit Nomor rakit
     * @return List<RakitDetailModel> atau empty list jika tidak ditemukan
     */
    private List<RakitDetailModel> getRakitDetails(int norakit) {
        List<RakitDetailModel> detailList = new ArrayList<>();
        String query = "SELECT * FROM rakit_detail WHERE norakit = ?";
        try (PreparedStatement pstmt = koneksi.dbKoneksi.prepareStatement(query)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detailList;
    }

}
