package com.paril.mlaclientapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

/**
 * Created by paril on 02-Oct-17.
 */

public class CommonUtils {

 public static String MlaBaseURL = "https://ec2-54-67-108-156.us-west-1.compute.amazonaws.com/MlaWebApi/";



    public static String EXTRA_USER_ADMIN_DATA="extra_user_admin_data";

    public static String EXTRA_IS_TO_ADD="extra_is_to_add";

    public static String EXTRA_EDIT_MODE="extra_edit_mode";

    public static boolean checkInternetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static boolean isValidMobile(String phone) {
//        String phoneRegexStr = "\\d{3}-\\d{3}-\\d{4}";
//        Pattern pattern = Pattern.compile(phoneRegexStr);
        return phone.length()==10;
    }

    public static boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }




    ///////////////////////////////////
    public static String encryptString(String text,String alias,KeyStore keyStore) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            String initialText = text;
            if(initialText.isEmpty()) {
               // Toast.makeText(this, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
                return "no text";
            }

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(initialText.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte [] vals = outputStream.toByteArray();
              String encryptedText = Base64.encodeToString(vals, Base64.DEFAULT);
              return encryptedText;
        } catch (Exception e) {
           System.out.println(e);
        }

     return "no key";
    }




    public static String decryptString(String encryptedText, String alias,KeyStore keyStore) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            String cipherText = encryptedText;
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte)nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            String decryptedText = finalText;

            System.out.println(decryptedText);
            return decryptedText;

        } catch (Exception e) {
          //  Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
          //  Log.e(TAG, Log.getStackTraceString(e));
        }
        return"no text";
    }
}


