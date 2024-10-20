package rakitpc.Model;

import java.util.Date;
import java.util.List;

public class RakitModel {

    private int norakit;
    private Date tanggal;
    private List<RakitDetailModel> details;

    public int getNorakit() {
        return norakit;
    }

    public void setNorakit(int norakit) {
        this.norakit = norakit;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public List<RakitDetailModel> getDetails() {
        return details;
    }

    public void setDetails(List<RakitDetailModel> details) {
        this.details = details;
    }
}
