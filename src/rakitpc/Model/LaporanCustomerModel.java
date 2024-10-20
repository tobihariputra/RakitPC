package rakitpc.Model;

public class LaporanCustomerModel {

    private String idmember, nama, alamat, status, idtransaksi;
    private double totalbiayarakit;
    private String norakit;
    private String tanggal;

    public String getIdmember() {
        return idmember;
    }

    public void setIdmember(String idmember) {
        this.idmember = idmember;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(String idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public double getTotalbiayarakit() {
        return totalbiayarakit;
    }

    public void setTotalbiayarakit(double totalbiayarakit) {
        this.totalbiayarakit = totalbiayarakit;
    }

    public String getNorakit() {
        return norakit;
    }

    public void setNorakit(String norakit) {
        this.norakit = norakit;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

}
