package rakitpc.Model;

public class KomponenModel {

    private String kategori, kodekomponen, namakomponen, brand, socket, jenismemori, gambar;
    private double harga;
    private int stok;

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getKodekomponen() {
        return kodekomponen;
    }

    public void setKodekomponen(String kodekomponen) {
        this.kodekomponen = kodekomponen;
    }

    public String getNamakomponen() {
        return namakomponen;
    }

    public void setNamakomponen(String namakomponen) {
        this.namakomponen = namakomponen;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getJenismemori() {
        return jenismemori;
    }

    public void setJenismemori(String jenismemori) {
        this.jenismemori = jenismemori;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    @Override
    public String toString() {
        return namakomponen;
    }
}
