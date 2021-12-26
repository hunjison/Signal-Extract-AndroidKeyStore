package com.android.keystore.androidkeystoredemo;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Created by saurav.p on 8/23/2017.
 */
// Changed by hunjison

class Decrypt {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final int GCM_TAG_LENGTH = 16;

    private KeyStore keyStore;
    // Changed by hunjison
    public SecretKey signalkey;

    Decrypt() throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
            IOException {
        initKeyStore();
    }

    private void initKeyStore() throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
    }

    String decryptData(final String alias, final byte[] encryptedData, final byte[] encryptionIv)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        final GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIv);

        SecretKey tmp = getSecretKey(alias);
        this.signalkey = tmp;
        cipher.init(Cipher.DECRYPT_MODE, tmp, spec);

        return new String(cipher.doFinal(encryptedData), "UTF-8");
    }

    public SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            UnrecoverableEntryException, KeyStoreException {
        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
    }

    // Changed by hunjison
    public static byte[] AESGCM_decrypt(byte[] cipherText, SecretKey key, byte[] IV) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        byte[] decryptedText = cipher.doFinal(cipherText);
        return decryptedText;
    }
}