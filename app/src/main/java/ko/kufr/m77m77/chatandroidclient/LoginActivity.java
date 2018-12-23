package ko.kufr.m77m77.chatandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText nameField;
    private Button button;
    private TextView crtAcc;

    private boolean inRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        this.nameField = findViewById(R.id.name);
        this.button = this.findViewById(R.id.loginButton);
        this.crtAcc = this.findViewById(R.id.crt_account);

        this.inRegister = false;
        this.nameField.setVisibility(View.GONE);
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
}
