package com.wifin.kachingme.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Constant;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NynmIcons extends Drawable {

    private final static int    TEXT_PADDING           = 3;
    private final static int    ROUNDED_RECT_RADIUS    = 5;

    private final String    text;
    private final Paint textPaint;
    private final Rect textBounds;
    private final Paint     bgPaint;
    private final RectF bgBounds;

    public NynmIcons(String text, String backgroundColor, int textHeight) {

        this.text = text;

        // Text
        this.textPaint = new Paint();
        this.textBounds = new Rect();
        textPaint.setColor(Color.WHITE);
        textPaint.setARGB(255, 255, 255, 255);
        textPaint.setAntiAlias(true);
        textPaint.setSubpixelText(true);
        textPaint.setTextAlign(Paint.Align.CENTER); // Important to centre horizontally in the background RectF
        textPaint.setTextSize(textHeight);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        // Map textPaint to a Rect in order to get its true height
        // ... a bit long-winded I know but unfortunately getTextSize does not seem to give a true height!
        textPaint.getTextBounds(text, 0, text.length(), textBounds);

        // Background
        this.bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.parseColor(backgroundColor));
        float rectHeight  = TEXT_PADDING * 2 + textHeight;
        float rectWidth   = TEXT_PADDING * 2 + textPaint.measureText(text);
        //float rectWidth   = TEXT_PADDING * 2 + textHeight;  // Square (alternative)
        // Create the background - use negative start x/y coordinates to centre align the icon
        this.bgBounds = new RectF(rectWidth / -2, rectHeight / -2, rectWidth / 2, rectHeight / 2);
    }



    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(bgBounds, ROUNDED_RECT_RADIUS, ROUNDED_RECT_RADIUS, bgPaint);
        // Position the text in the horizontal/vertical centre of the background RectF
        canvas.drawText(text, 0, (textBounds.bottom - textBounds.top)/2, textPaint);

    }

    public void customIcon(final String text)throws IOException {
        final Paint textPaint = new Paint() {
            {
                setColor(Color.WHITE);
                setTextAlign(Paint.Align.LEFT);
                setTextSize(20f);
                setAntiAlias(true);
            }
        };
        final Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        final Bitmap bmp = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888); //use ARGB_8888 for better quality
        final Canvas canvas = new Canvas(bmp);
        canvas.drawText(text, 0, 20f, textPaint);
        String iconsStoragePath = String.valueOf(
                Constant.local_image_dir + text+".png");

     //   new File(iconsStoragePath).mkdirs();
      //  File sdIconStorageDir = new File(iconsStoragePath);
        FileOutputStream stream = new FileOutputStream(iconsStoragePath); //create your FileOutputStream here
//        BufferedOutputStream bos = new BufferedOutputStream();
//       boolean doneIcon =  bmp.compress(Bitmap.CompressFormat.PNG, 85, bos);
//        Constant.printMsg("IIIIIIIIII  :" +doneIcon);
//        bmp.recycle();
//        stream.close();


        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        boolean doneIcon = bmp.compress(Bitmap.CompressFormat.PNG,
                85, outstream);
        Constant.printMsg("IIIIIIIIII  :" +doneIcon);
        byte[] byteArray = outstream.toByteArray();

        stream.write(byteArray);
        stream.close();
    }

    @Override
    public void setAlpha(int alpha) {
        bgPaint.setAlpha(alpha);
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        bgPaint.setColorFilter(cf);
        textPaint.setColorFilter(cf);
    }



    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}