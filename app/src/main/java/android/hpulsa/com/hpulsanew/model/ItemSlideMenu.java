package android.hpulsa.com.hpulsanew.model;

/**
 * Created by ozi on 07/10/2017.
 */

public class ItemSlideMenu {
    private int imgId;
    private int imgPembatas;
    private String title;
    private String saldo;

    public ItemSlideMenu(int imgId, int imgPembatas, String title, String saldo) {
        this.imgId = imgId;
        this.imgPembatas = imgPembatas;
        this.title = title;
        this.saldo = saldo;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public int getImgPembatas() {
        return imgPembatas;
    }

    public void setImgPembatas(int imgPembatas) {
        this.imgPembatas = imgPembatas;
    }
}
