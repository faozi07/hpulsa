package android.hpulsa.com.hpulsanew.model;

/**
 * Created by ozi on 08/10/2017.
 */

public class modRiwayat {
    private String noTrx;
    private String statusTrx;
    private String noHp;
    private String nominal;
    private String harga;
    private String tglTrx;
    private String provider;
    private String pembayaran;
    private String statusPembayaran;

    public modRiwayat(String noTrx, String statusTrx, String noHp, String nominal, String harga, String tglTrx) {
        this.setNoTrx(noTrx);
        this.setStatusTrx(statusTrx);
        this.setNoHp(noHp);
        this.setNominal(nominal);
        this.setHarga(harga);
        this.setTglTrx(tglTrx);
    }


    public String getNoTrx() {
        return noTrx;
    }

    public void setNoTrx(String noTrx) {
        this.noTrx = noTrx;
    }

    public String getStatusTrx() {
        return statusTrx;
    }

    public void setStatusTrx(String statusTrx) {
        this.statusTrx = statusTrx;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTglTrx() {
        return tglTrx;
    }

    public void setTglTrx(String tglTrx) {
        this.tglTrx = tglTrx;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(String pembayaran) {
        this.pembayaran = pembayaran;
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public void setStatusPembayaran(String statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }
}
