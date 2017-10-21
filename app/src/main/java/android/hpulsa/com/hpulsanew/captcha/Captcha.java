package android.hpulsa.com.hpulsanew.captcha;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Random;

/**
 * Created by ozi on 09/10/2017.
 */

public abstract class Captcha {
    protected Bitmap image;
    protected String answer = "";
    public int width = 300;
    public int height = 100;
    protected int x = 0;
    protected int y = 0;
    static List<Integer> usedColors;

    public Captcha() {
    }

    protected abstract Bitmap image();

    public static int color() {
        Random r = new Random();

        int number;
        do {
            number = r.nextInt(9);
        } while (usedColors.contains(Integer.valueOf(number)));

        usedColors.add(Integer.valueOf(number));
        switch (number) {
            case 0:
                return -16777216; // hitam
            case 1:
                return -16777216;
            case 2:
                return -16777216;
            case 3:
                return -16777216;
            case 4:
                return -16777216;
            case 5:
                return -16777216;
            case 6:
                return -16777216;
            case 7:
                return -16777216;
            case 8:
                return -16777216;
            case 9:
                return -16777216;
            default:
                return -16777216;
            /*case 0:
                return -16777216;
            case 1:
                return -16776961;
            case 2:
                return -16711681;
            case 3:
                return -12303292;
            case 4:
                return -7829368;
            case 5:
                return -16711936;
            case 6:
                return -65281;
            case 7:
                return -65536;
            case 8:
                return -256;
            case 9:
                return -1;
            default:
                return -1;*/
        }
    }

    public static int color2() {
        Random r = new Random();

        int number;
        do {
            number = r.nextInt(9);
        } while(usedColors.contains(Integer.valueOf(number)));

        usedColors.add(Integer.valueOf(number));
        switch(number) {
            case 0:
                return -1;
            case 1:
                return -1;
            case 2:
                return -1;
            case 3:
                return -1;
            case 4:
                return -1;
            case 5:
                return -1;
            case 6:
                return -1;
            case 7:
                return -1;
            case 8:
                return -1;
            case 9:
                return -1;
            default:
                return -1;
        }
    }

    public Bitmap getImage() {
        return this.image;
    }

    public boolean checkAnswer(String ans) {
        return ans.equals(this.answer);
    }
}
