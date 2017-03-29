package com.wifin.kachingme.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class encry_decry {

    private String encryptedFileName = "Enc_File2.db";
    private static String algorithm = "AES";
    static SecretKey yourKey = null;
    static Context context;
    public static byte[] key;
    public static String has = "Maheshmori";

    public encry_decry(Context c) {
        this.context = c;
        File direct = new File(Constant.local_database_dir);

        if (!direct.exists()) {
            if (direct.mkdir())
                ; // directory is created;
        }

        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        String date = date_format.format(new Date());

        encryptedFileName = "Kachingme-" + date + ".1.db";

        File f = new File(direct, encryptedFileName);
        if (f.exists()) {

            int ver = Integer.parseInt(encryptedFileName.split("[-.]")[4]);
            ver++;
            encryptedFileName = "Kachingme-" + date + "." + ver + ".db";
            File f1 = new File(direct, encryptedFileName);

            while (f1.exists()) {

                ver++;
                encryptedFileName = "Kachingme-" + date + "." + ver + ".db";
                f1 = new File(direct, encryptedFileName);

            }

        }

    }

    public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should <span id="IL_AD3" class="IL_AD">select
        // a</span> value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations,
                outputKeyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return secretKey;
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = 256;
        SecureRandom secureRandom = new SecureRandom();

        // Do *not* seed secureRandom! Automatically seeded from system entropy.

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        yourKey = keyGenerator.generateKey();

		/*
		 * SharedPreferences settings
		 * =context.getSharedPreferences(KachingMeApplication
		 * .getPereference_label(), 0); String stringArray =
		 * settings.getString("has", null);
		 */

        String stringArray = "[53, 85, -12, 104, 32, -57, -29, -7, -42, -53, 51, 77, -66, -19, 76, 6, 73, -52, -55, -14, -52, 11, 107, 122, -3, -31, 10, -110, 84, -58, -95, 89]";

        if (stringArray != null) {
            String[] split = stringArray.substring(1, stringArray.length() - 1)
                    .split(", ");
            byte[] array = new byte[split.length];
            for (int i = 0; i < split.length; i++) {
                array[i] = Byte.parseByte(split[i]);
            }

            key = array;
        }
		/*
		 * else { SharedPreferences.Editor editor = settings.edit();
		 * editor.putString("has", Arrays.toString(yourKey.getEncoded()));
		 * editor.commit(); key=yourKey.getEncoded(); }
		 */

        // key=has.getBytes();

        return yourKey;
    }

    public static byte[] encodeFile(SecretKey yourKey, byte[] fileData)
            throws Exception {
        byte[] encrypted = null;
        byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(key, 0, key.length,
                algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));
        encrypted = cipher.doFinal(fileData);
        return encrypted;

    }

    public static byte[] decodeFile(SecretKey yourKey, byte[] fileData)
            throws Exception {

        byte[] decrypted = null;
        Cipher cipher = Cipher.getInstance(algorithm);
        byte[] data = yourKey.getEncoded();
        Key skeySpec = new SecretKeySpec(key, 0, key.length, algorithm);

        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));
        decrypted = cipher.doFinal(fileData);
        return decrypted;
    }

    public void saveFile() {
        try {

            byte[] stringToSave = convertFileToByteArray(new File("/data/data/"
                    + context.getApplicationContext().getPackageName()
                    + "/databases/kachingme.db"));

            File file = new File(Constant.local_database_dir,
                    encryptedFileName);
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            yourKey = generateKey();
            byte[] filesBytes = encodeFile(yourKey, stringToSave);
            bos.write(filesBytes);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // decodeFile();
    }

    public File decodeFile(File f) {
        File file = null;
        try {
            yourKey = generateKey();

            Log.d("encry_decry",
                    "Is File Exist::" + f.exists() + " Name::" + f.getName());

            byte[] decodedData = decodeFile(yourKey, readFile(f));
            // String str = new String(decodedData);
            // //Constant.printMsg("DECODED FILE CONTENTS: " + str);

            try {
                file = new File("/data/data/"
                        + context.getApplicationContext().getPackageName()
                        + "/databases/", "kachingme_backup.db");

                if (file.exists())
                {
                    long size = file.length();

                    Log.e("Size ", size + " ");
                    Constant.printMsg("DECODED FILE CONTENTS: " + size);
                    Toast.makeText(context, size + " ", Toast.LENGTH_SHORT).show();
                }
				/*
				 * if (file.exists()) file.delete();
				 */

                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(file));

                bos.write(decodedData);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public byte[] readFile(File f) {
        byte[] contents = null;

		/*
		 * File file = new File(Environment.getExternalStorageDirectory() +
		 * File.separator, encryptedFileName);
		 */
        File file = f;
        int size = (int) file.length();
        contents = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(
                    new FileInputStream(file));
            try {
                buf.read(contents);
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contents;
    }

    public static byte[] convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
