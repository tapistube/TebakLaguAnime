package Kelas;

/**
 * Created by Glory on 13/07/2018.
 */

public class UserDaftar {
    public String uid;
    public String displayName;
    public String token;
    public int coin;
    public int exp;

    public UserDaftar(String uid, String displayName, String token, int coin, int exp, int freeCoin) {
        this.uid = uid;
        this.displayName = displayName;
        this.token = token;
        this.coin = coin;
        this.exp = exp;
        this.freeCoin = freeCoin;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getFreeCoin() {
        return freeCoin;
    }

    public void setFreeCoin(int freeCoin) {
        this.freeCoin = freeCoin;
    }

    public int freeCoin;
}
