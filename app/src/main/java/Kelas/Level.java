package Kelas;

/**
 * Created by Glory on 13/07/2018.
 */

public class Level {
    public String wolf;
    public String tiger;
    public String shark;
    public String unicorn;
    public String pegasus;

    public String getWolf() {
        return wolf;
    }

    public Level(String wolf, String tiger, String shark, String unicorn, String pegasus) {
        this.wolf = wolf;
        this.tiger = tiger;
        this.shark = shark;
        this.unicorn = unicorn;
        this.pegasus = pegasus;
    }

    public void setWolf(String wolf) {
        this.wolf = wolf;
    }

    public String getTiger() {
        return tiger;
    }

    public void setTiger(String tiger) {
        this.tiger = tiger;
    }

    public String getShark() {
        return shark;
    }

    public void setShark(String shark) {
        this.shark = shark;
    }

    public String getUnicorn() {
        return unicorn;
    }

    public void setUnicorn(String unicorn) {
        this.unicorn = unicorn;
    }

    public String getPegasus() {
        return pegasus;
    }

    public void setPegasus(String pegasus) {
        this.pegasus = pegasus;
    }
}
