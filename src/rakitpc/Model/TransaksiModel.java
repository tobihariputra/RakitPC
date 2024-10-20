package rakitpc.Model;

import java.sql.Timestamp;

public class TransaksiModel {

    private String idtransaksi;
    private String idmember;
    private int norakit;
    private Timestamp tanggal;
    private double totalbayar;

    public String getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(String idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public String getIdmember() {
        return idmember;
    }

    public void setIdmember(String idmember) {
        this.idmember = idmember;
    }

    public int getNorakit() {
        return norakit;
    }

    public void setNorakit(int norakit) {
        this.norakit = norakit;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }

    public double getTotalbayar() {
        return totalbayar;
    }

    public void setTotalbayar(double totalbayar) {
        this.totalbayar = totalbayar;
    }
}
