package ko.kufr.m77m77.chatandroidclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {

    private EditText nameField;
    private Button button;
    private TextView crtAcc;
    private TextView error;

    private boolean inRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        this.nameField = findViewById(R.id.name);
        this.button = this.findViewById(R.id.loginButton);
        this.crtAcc = this.findViewById(R.id.crt_account);
        this.error = this.findViewById(R.id.error_message);

        this.inRegister = false;
        this.nameField.setVisibility(View.GONE);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 121);
        }
    }

    public  void onClickCreateNewAccount(View view) {
        if(view == this.crtAcc) {
            if(this.inRegister) {
                this.nameField.setVisibility(View.GONE);
                this.button.setText(R.string.login_button_signIn);
                this.crtAcc.setText(R.string.login_text_crt);

            }else {
                this.nameField.setVisibility(View.VISIBLE);
                this.nameField.setText("");

                this.button.setText(R.string.login_button_register);

                this.crtAcc.setText(R.string.login_text_already);
            }

            this.inRegister = !this.inRegister;
        }
    }

    public void onClickButton(View view) {

        if(view == this.button) {
            try {
                String res = new RequestManager().execute("").get();

                this.error.setText(res);
            } catch (Exception e) {
                this.error.setText(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
