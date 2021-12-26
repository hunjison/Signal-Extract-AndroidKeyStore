package com.android.keystore.androidkeystoredemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {
    // Changed by hunjison
    private static final String ALIAS = "FAKE_SignalSecret";
    private Encrypt mEncrypt;
    private Decrypt mDecrypt;
    Button toEncryt,toDecrypt,toKeystoreDecrypt;
    EditText password;
    TextView decrypted;
    // Changed by hunjison
    TextView toPref1;
    TextView toPref2;
    TextView toPref3;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        password = (EditText) findViewById(R.id.editText);
        decrypted = (TextView) findViewById(R.id.textView2);
        // Changed by hunjison
        toPref1 = (TextView) findViewById(R.id.textView3);
        toPref2 = (TextView) findViewById(R.id.textView4);
        toPref3 = (TextView) findViewById(R.id.textView5);
        toEncryt = (Button) findViewById(R.id.button);
        toDecrypt = (Button) findViewById(R.id.button2);
        toKeystoreDecrypt = (Button) findViewById(R.id.button3);
        mEncrypt = new Encrypt();
        try {
            mDecrypt = new Decrypt();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        toEncryt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encryptText();
            }
        });
        toDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decryptText();
            }
        });
        toKeystoreDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decryptKeystore();
            }
        });
    }
    private void encryptText() {

        try {
            final byte[] encryptedText = mEncrypt.encryptText(ALIAS, password.getText().toString());
            Log.d("s@urac","s@urac Encrypted Text:"+encryptedText);
            decrypted.setText(bytesToHex(encryptedText));
            decrypted.setVisibility(View.VISIBLE);
        } catch (InvalidAlgorithmParameterException | SignatureException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void decryptText() {
        try {
            Log.d("s@urac","s@urac Decrypted data is:"+mDecrypt.decryptData(ALIAS, mEncrypt.getEncryption(), mEncrypt.getIv()));
            decrypted.setText(mDecrypt.decryptData(ALIAS, mEncrypt.getEncryption(), mEncrypt.getIv()));
            decrypted.setVisibility(View.VISIBLE);
            Log.d("hunjison", "decryption success : " +mDecrypt.decryptData(ALIAS, mEncrypt.getEncryption(), mEncrypt.getIv()));

        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                IOException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void decryptKeystore(){
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("org.thoughtcrime.securesms_preferences", Context.MODE_PRIVATE);
        this.mSharedPreferences = sharedPref;

        try{
            // Changed by hunjison
            String pref1 = this.mSharedPreferences.getString("pref_database_encrypted_secret","error");
            String pref2 = this.mSharedPreferences.getString("pref_attachment_encrypted_secret","error");
            String pref3 = this.mSharedPreferences.getString("pref_log_encrypted_secret","error");

            JSONObject json1 = new JSONObject(pref1);
            JSONObject json2 = new JSONObject(pref2);
            JSONObject json3 = new JSONObject(pref3);

            byte[] ciphertext1 = Base64.decode(json1.getString("data"), Base64.NO_WRAP);
            byte[] iv1 = Base64.decode(json1.getString("iv"), Base64.NO_WRAP);
            byte[] ciphertext2 = Base64.decode(json2.getString("data"), Base64.NO_WRAP);
            byte[] iv2 = Base64.decode(json2.getString("iv"), Base64.NO_WRAP);
            byte[] ciphertext3 = Base64.decode(json3.getString("data"), Base64.NO_WRAP);
            byte[] iv3 = Base64.decode(json3.getString("iv"), Base64.NO_WRAP);

            byte[] decryptedText1 = mDecrypt.AESGCM_decrypt(ciphertext1, mDecrypt.signalkey, iv1);
            byte[] decryptedText2 = mDecrypt.AESGCM_decrypt(ciphertext2, mDecrypt.signalkey, iv2);
            byte[] decryptedText3 = mDecrypt.AESGCM_decrypt(ciphertext3, mDecrypt.signalkey, iv3);

            Log.d("hunjison","decryptedText :"+ bytesToHex(decryptedText1));
            Log.d("hunjison","decryptedText :"+ bytesToHex(decryptedText2));
            Log.d("hunjison","decryptedText :"+ bytesToHex(decryptedText3));

            toPref1.setText(bytesToHex(decryptedText1));
            toPref2.setText(new String(decryptedText2));
            toPref3.setText(bytesToHex(decryptedText3));

            toPref1.setVisibility(View.VISIBLE);
            toPref2.setVisibility(View.VISIBLE);
            toPref3.setVisibility(View.VISIBLE);

            SharedPreferences.Editor editor = this.mSharedPreferences.edit();
            editor.putString("[decrypt]pref_database_encrypted_secret", bytesToHex(decryptedText1));
            editor.putString("[decrypt]pref_attachment_encrypted_secret", new String(decryptedText2));
            editor.putString("[decrypt]pref_log_encrypted_secret", bytesToHex(decryptedText3));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
