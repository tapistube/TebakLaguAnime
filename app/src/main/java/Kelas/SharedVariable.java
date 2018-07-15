package Kelas;

import java.util.ArrayList;
import java.util.List;

import glory.tebaklaguanime.R;

/**
 * Created by Glory on 09/07/2018.
 */

public class SharedVariable {
    public static int interChance = 0;
    public static int isUserLogged = 0;
    public static String nama = "Not Logged in";
    public static int coin;
    public static int exp;
    public static String wolf;
    public static String tiger;
    public static String shark;
    public static String unicorn;
    public static String pegasus;
    public static List<String> list_voucher = new ArrayList();
    int [] resDoa = {R.drawable.icon_normies,R.drawable.icon_adventurer,R.drawable.icon_captain,
            R.drawable.icon_hero,R.drawable.icon_shichibukai,
            R.drawable.icon_yonkou,R.drawable.icon_king,
            R.drawable.icon_gorosei,R.drawable.icon_beast,R.drawable.icon_fallen_angel,R.drawable.icon_sky_angel};


    public static int expToIcon(int expNow){


        if (expNow<2000) {
           return R.drawable.icon_normies;
        }else if (expNow < 3500) {
            return R.drawable.icon_adventurer;
        }else if (expNow < 5500) {
            return R.drawable.icon_captain;
        }else if (expNow < 8500) {
            return R.drawable.icon_hero;
        }else if (expNow < 12000) {
            return R.drawable.icon_shichibukai;
        }else if (expNow < 16000) {
            return R.drawable.icon_yonkou;
        }else if (expNow < 23000){
            return R.drawable.icon_king;
        } else if (expNow < 30000){
            return R.drawable.icon_gorosei;
        }else if (expNow < 38000){
            return R.drawable.icon_beast;
        }else if (expNow < 48000){
            return R.drawable.icon_fallen_angel;
        }else {
            return R.drawable.icon_sky_angel;
        }
    }
}
