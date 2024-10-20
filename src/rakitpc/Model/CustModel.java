package rakitpc.Model;

public class CustModel {

    private String idmember, nama, alamat, status;
    private double totalbiayarakit;

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

    public double getTotalbiayarakit() {
        return totalbiayarakit;
    }

    public void setTotalbiayarakit(double totalbiayarakit) {
        this.totalbiayarakit = totalbiayarakit;
    }
}
