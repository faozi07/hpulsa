package android.hpulsa.com.hpulsanew.model;

public class modNomPulsa {
    private int id;
    private String jnsProduk;
    private String provider;
    private String kode;
    private String nominal;
    private String hrg;
    private String hrgJual;
    private String stok;
    private String status;

    public String getJnsProduk() {
        return jnsProduk;
    }

    public void setJnsProduk(String jnsProduk) {
        this.jnsProduk = jnsProduk;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getHrg() {
        return hrg;
    }

    public void setHrg(String hrg) {
        this.hrg = hrg;
    }

    public String getHrgJual() {
        return hrgJual;
    }

    public void setHrgJual(String hrgJual) {
        this.hrgJual = hrgJual;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}
}
