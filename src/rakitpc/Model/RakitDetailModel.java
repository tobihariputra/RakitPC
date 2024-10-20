package rakitpc.Model;

public class RakitDetailModel {

    private int norakit;
    private String kodekomponen;
    private int jumlah;
    private double subtotal;

    public RakitDetailModel(int norakit, String kodekomponen, int jumlah, double subtotal) {
        this.norakit = norakit;
        this.kodekomponen = kodekomponen;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    public int getNorakit() {
        return norakit;
    }

    public void setNorakit(int norakit) {
        this.norakit = norakit;
    }

    public String getKodekomponen() {
        return kodekomponen;
    }

    public void setKodekomponen(String kodekomponen) {
        this.kodekomponen = kodekomponen;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
