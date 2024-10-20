package rakitpc.Model;

import java.sql.Date;
import java.sql.Timestamp;

public class LaporanTransaksiModel {

    private String idTransaksi;
    private String namaMember;
    private String alamat;
    private int nomorRakit;
    private Timestamp tanggalTransaksi;
    private double totalBayar;

    private String kodeKomponen;
    private String namaKomponen;
    private String brand;
    private int jumlahKomponen;
    private double subtotalKomponen;

    private Date tanggalPendapatan;
    private double totalPendapatan;

    private int totalTerjual;

    private double totalBiayaRakit;
    private double totalBayarPelanggan;

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getNamaMember() {
        return namaMember;
    }

    public void setNamaMember(String namaMember) {
        this.namaMember = namaMember;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getNomorRakit() {
        return nomorRakit;
    }

    public void setNomorRakit(int nomorRakit) {
        this.nomorRakit = nomorRakit;
    }

    public Timestamp getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(Timestamp tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(double totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getKodeKomponen() {
        return kodeKomponen;
    }

    public void setKodeKomponen(String kodeKomponen) {
        this.kodeKomponen = kodeKomponen;
    }

    public String getNamaKomponen() {
        return namaKomponen;
    }

    public void setNamaKomponen(String namaKomponen) {
        this.namaKomponen = namaKomponen;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getJumlahKomponen() {
        return jumlahKomponen;
    }

    public void setJumlahKomponen(int jumlahKomponen) {
        this.jumlahKomponen = jumlahKomponen;
    }

    public double getSubtotalKomponen() {
        return subtotalKomponen;
    }

    public void setSubtotalKomponen(double subtotalKomponen) {
        this.subtotalKomponen = subtotalKomponen;
    }

    public Date getTanggalPendapatan() {
        return tanggalPendapatan;
    }

    public void setTanggalPendapatan(Date tanggalPendapatan) {
        this.tanggalPendapatan = tanggalPendapatan;
    }

    public double getTotalPendapatan() {
        return totalPendapatan;
    }

    public void setTotalPendapatan(double totalPendapatan) {
        this.totalPendapatan = totalPendapatan;
    }

    public int getTotalTerjual() {
        return totalTerjual;
    }

    public void setTotalTerjual(int totalTerjual) {
        this.totalTerjual = totalTerjual;
    }

    public double getTotalBiayaRakit() {
        return totalBiayaRakit;
    }

    public void setTotalBiayaRakit(double totalBiayaRakit) {
        this.totalBiayaRakit = totalBiayaRakit;
    }

    public double getTotalBayarPelanggan() {
        return totalBayarPelanggan;
    }

    public void setTotalBayarPelanggan(double totalBayarPelanggan) {
        this.totalBayarPelanggan = totalBayarPelanggan;
    }

}
