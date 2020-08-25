package com.example.aesencryption.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aesencryption.Functions.Encrypt;
import com.example.aesencryption.Functions.Utils;
import com.example.aesencryption.R;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Encryption extends Activity {

    @BindView(R.id.etOrjText)
    EditText etOrjText;
    @BindView(R.id.secretkey)
    TextView tvSecretKey;
    @BindView(R.id.sonuc)
    TextView tvSonuc;
    @BindView(R.id.anahtar)
    TextView tvAnahtar;
    @BindView(R.id.encBtn)
    Button encBtn;

    KeyGenerator keyGenerator;
    SecretKey secretKey;
    byte[] secretKeyen;
    String strSecretKey;
    byte[] IV = new byte[16];
    byte[] cipherText;
    SecureRandom random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encryption);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.encBtn)
    public void btnEncodeClick() {
        if (TextUtils.isEmpty(etOrjText.getText())) {
            Toast t = Toast.makeText(this, "Fill empty field.", Toast.LENGTH_SHORT);
            t.show();
        } else {
            try {
//                initialise keyGenerator
                keyGenerator = KeyGenerator.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            keyGenerator.init(256);
            secretKey = keyGenerator.generateKey();
            secretKeyen=secretKey.getEncoded();
            strSecretKey = encoderfun(secretKeyen);
            tvSecretKey.setText(strSecretKey);

//           here random initialisation Vector generated
            random = new SecureRandom();
            random.nextBytes(IV);
            try {
                cipherText = Encrypt.encrypt(etOrjText.getText().toString().trim().getBytes(), secretKey, IV);

                String sonuc = encoderfun(cipherText);
                tvSonuc.setText(sonuc);


                String tvIV = encoderfun(IV);
                tvAnahtar.setText(tvIV);

                Utils.saveData(this, sonuc, strSecretKey, tvIV);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static String encoderfun(byte[] decval) {
        String conVal= Base64.encodeToString(decval,Base64.DEFAULT);
        return conVal;
    }
}