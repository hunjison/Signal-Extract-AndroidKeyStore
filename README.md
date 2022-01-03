# Signal-Extract-AndroidKeyStore
Demo app for extracting Android Keystore in Signal messenger

This repository is used to decrypt Signal messenger, more information can be found below.
[Messenger-Forensics](https://github.com/hunjison/Messenger-Forensics)

---
## Prerequisite
Install Android Studio or Android SDK Build tools. (I recommend Android Studio)
Set ANDROID_SDK_HOME environment variable.
Install Gradle version 6.5

## Build
For Linux, Mac
```
gradle build
```

For Windows
```
gradlew.bat build
```

If the build is successful, you can find Android application in the `build/outputs/apk/` folder.

## How to use
Usage for MY Application is as below.

### 1. copy SharedPreferences to Rogue App.
```
cd /data/data/com.android.keystore.androidkeystoredemo
mkdir shared_prefs
cd shared_prefs/
cp /data/data/org.thoughtcrime.securesms/shared_prefs/* ./
chmod 777 -R /data/data/com.android.keystore.androidkeystoredemo/shared_prefs/
```

### 2. copy Android Keystore SecretKey to Rogue App
```
cd /data/misc/keystore/user_0
ls | grep Signal
cp <UID>_USRPKEY_SignalSecret <UID>_USRPKEY_FAKE_SignalSecret
```

### 3. Open the Application and Check the extracted Keys
You can get 3 keys, `pref_database_encrypted_secret`, `pref_attachment_encrypted_secret`, `pref_log_encrypted_secret`. Use these values ​​in the subsequent decryption process.

![Signal_AndroidKeystore](https://user-images.githubusercontent.com/96677057/147407715-ce7f596c-d313-43d9-8a5c-c1d00ea6fb34.png)

---

We made this repository by cloning and editing some codes for Signal Messenger.

Original codes from : https://github.com/sauravpradhan/AndroidKeyStore

