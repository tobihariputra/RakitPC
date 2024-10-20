package rakitpc.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rakitpc.Model.CustModel;

public class DBCust {

    private CustModel dt = new CustModel();

    public CustModel getCustModel() {
        return (dt);
    }

    public void setCustModel(CustModel s) {
        dt = s;
    }

    public ObservableList<CustModel> Load() {
        try {
            ObservableList<CustModel> tableData = FXCollections.observableArrayList();
            Koneksi con = new Koneksi();
            con.bukaKoneksi();
            con.statement = con.dbKoneksi.createStatement();
            ResultSet rs = con.statement.executeQuery("Select idmember, nama, alamat, totalbiayarakit from customer");

            while (rs.next()) {
                CustModel d = new CustModel();
                d.setIdmember(rs.getString("idmember"));
                d.setNama(rs.getString("nama"));
                d.setAlamat(rs.getString("alamat"));
                d.setTotalbiayarakit(rs.getDouble("totalbiayarakit"));

                double totalbiayarakit = rs.getDouble("totalbiayarakit");
                String status;

                if (totalbiayarakit >= 50000000) {
                    status = "gold";
                } else if (totalbiayarakit >= 10000000) {
                    status = "silver";
                } else {
                    status = "reguler";
                }
                d.setStatus(status);

                tableData.add(d);
            }

            con.tutupKoneksi();
            return tableData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getLastIdMember() {
        String lastId = null;
        try {
            Koneksi con = new Koneksi();
            con.bukaKoneksi();
            con.statement = con.dbKoneksi.createStatement();
            ResultSet rs = con.statement.executeQuery("SELECT idmember FROM customer ORDER BY idmember DESC LIMIT 1");

            if (rs.next()) {
                lastId = rs.getString("idmember");
            }
            con.tutupKoneksi();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastId;
    }

    public ObservableList<CustModel> CariCust(String kode, String nama) {
        try {
            ObservableList<CustModel> tableData;
            tableData = FXCollections.observableArrayList();
            Koneksi con = new Koneksi();
            con.bukaKoneksi();
            con.statement = con.dbKoneksi.createStatement();
            ResultSet rs = con.statement.executeQuery("select * from customer WHERE idmember LIKE '" + kode + "%' OR nama LIKE '" + nama + "%'");

            while (rs.next()) {
                CustModel d = new CustModel();
                d.setIdmember(rs.getString("idmember"));
                d.setNama(rs.getString("nama"));
                d.setAlamat(rs.getString("alamat"));
                d.setTotalbiayarakit(rs.getDouble("totalbiayarakit"));

                double totalbiayarakit = rs.getDouble("totalbiayarakit");
                String status;
                if (totalbiayarakit >= 1000000) {
                    status = "gold";
                } else if (totalbiayarakit >= 500000) {
                    status = "silver";
                } else {
                    status = "reguler";
                }
                d.setStatus(status);

                tableData.add(d);
            }

            con.tutupKoneksi();
            return tableData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int validasi(String nomor) {
        int val = 0;
        try {
            Koneksi con = new Koneksi();
            con.bukaKoneksi();
            con.statement = con.dbKoneksi.createStatement();
            ResultSet rs = con.statement.executeQuery("select count(*) as jml from customer where idmember = '" + nomor + "'");
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
            con.preparedStatement = con.dbKoneksi.prepareStatement("insert into customer (idmember,nama, alamat, totalbiayarakit) values (?,?,?,?)");
            con.preparedStatement.setString(1, getCustModel().getIdmember());
            con.preparedStatement.setString(2, getCustModel().getNama());
            con.preparedStatement.setString(3, getCustModel().getAlamat());
            con.preparedStatement.setDouble(4, getCustModel().getTotalbiayarakit());
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

    public boolean delete(String nomor) {
        boolean berhasil = false;
        Koneksi con = new Koneksi();
        try {
            con.bukaKoneksi();
            con.preparedStatement = con.dbKoneksi.prepareStatement("delete from customer where idmember  = ? ");
            con.preparedStatement.setString(1, nomor);
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
            con.preparedStatement = con.dbKoneksi.prepareStatement("update customer set nama = ?, alamat = ?, totalbiayarakit = ?  where  idmember = ? ");
            con.preparedStatement.setString(1, getCustModel().getNama());
            con.preparedStatement.setString(2, getCustModel().getAlamat());
            con.preparedStatement.setDouble(3, getCustModel().getTotalbiayarakit());
            con.preparedStatement.setString(4, getCustModel().getIdmember());
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
