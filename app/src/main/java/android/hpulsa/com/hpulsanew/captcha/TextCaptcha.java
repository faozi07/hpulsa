package android.hpulsa.com.hpulsanew.captcha;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.hpulsa.com.hpulsanew.R;

import java.io.CharArrayWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ozi on 09/10/2017.
 */

public class TextCaptcha extends Captcha {
    protected TextCaptcha.TextOptions options;
    private int wordLength;

    public TextCaptcha(int width, int height, int wordLength, TextCaptcha.TextOptions opt) {
        this.height = height;
        this.width = width;
        this.options = opt;
        usedColors = new ArrayList();
        this.wordLength = wordLength;
        this.image = this.image();
    }

    protected Bitmap image() {
        LinearGradient gradient = new LinearGradient(0.0F, 0.0F, 0.0F, 0.0F, color2(), color(), Shader.TileMode.CLAMP);
        Paint p = new Paint();
        p.setDither(true);
        p.setShader(gradient);
//        p.setColor(R.color.blueDark);
        Bitmap bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
//        c.drawRect(0.0F, 0.0F, (float)this.width, (float)this.height, p);
        c.drawRect(0.0F, 0.0F, (float)this.width*2, (float)this.height, p);
        Paint tp = new Paint();
        tp.setDither(true);
        tp.setTextSize((float)(this.width / this.height * 20));
        Random r = new Random(System.currentTimeMillis());
        CharArrayWriter cab = new CharArrayWriter();
        this.answer = "";

        int i;
        for(int data = 0; data < this.wordLength; ++data) {
            i = r.nextInt(3);
            char cc = 32;
            switch(i) {
                case 0:
//                    cc = (char)(r.nextInt(26) + 65);
                    cc = (char)(r.nextInt(9)+49);
                    break;
                case 1:
//                    cc = (char)(r.nextInt(26) + 97);
                    cc = (char)(r.nextInt(9) + 49);
                    break;
                case 2:
                    cc = (char)(r.nextInt(9) + 49);
            }

            cab.append(cc);
            this.answer = this.answer + cc;
        }

        char[] var11 = cab.toCharArray();

        for(i = 0; i < var11.length; ++i) {
            this.x = (int)((double)this.x + (double)(60 - 3 * this.wordLength) + (double)Math.abs(r.nextInt()) % (65.0D - 1.2D * (double)this.wordLength));
            this.y = 100 + Math.abs(r.nextInt()) % 50;
            Canvas var12 = new Canvas(bitmap);
            tp.setTextSkewX(r.nextFloat() - r.nextFloat());
            tp.setColor(color());
//            var12.drawText(var11, i, 1, (float)this.x, (float)this.y, tp);
            var12.drawText(var11, i, 1, (float)this.x, (float)this.y, tp);
            tp.setTextSkewX(0.0F);
        }

        return bitmap;
    }

    public static enum TextOptions {
        NUMBERS_ONLY;

        private TextOptions() {
        }
    }
}
