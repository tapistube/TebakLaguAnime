package Kelas;

/**
 * Created by Glory on 15/07/2018.
 */

public class Voucher {
    public  String kode;
    public String nama;
    public int status;

    public Voucher(int status,String kode, String nama) {
        this.kode = kode;
        this.nama = nama;
        this.status = status;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
