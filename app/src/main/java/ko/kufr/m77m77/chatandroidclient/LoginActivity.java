package ko.kufr.m77m77.chatandroidclient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import java.util.concurrent.Callable;

import ko.kufr.m77m77.chatandroidclient.fragments.ChatFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.GroupFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.GroupsFragment;
import ko.kufr.m77m77.chatandroidclient.models.Request;
import ko.kufr.m77m77.chatandroidclient.models.Response;
import ko.kufr.m77m77.chatandroidclient.models.enums.StatusCode;

public class LoginActivity extends AppCompatActivity implements GroupsFragment.OnFragmentInteractionListener,GroupFragment.OnFragmentInteractionListener {

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private Button button;
    private TextView crtAcc;

    private TextView errorName;
    private TextView errorEmail;
    private TextView errorPassword;

    private boolean inRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        this.nameField = findViewById(R.id.name);
        this.emailField = this.findViewById(R.id.email);
        this.passwordField = this.findViewById(R.id.password);
        this.button = this.findViewById(R.id.loginButton);
        this.crtAcc = this.findViewById(R.id.crt_account);

        this.errorName = this.findViewById(R.id.error_message_name);
        this.errorEmail = this.findViewById(R.id.error_message_email);
        this.errorPassword = this.findViewById(R.id.error_message);

        this.inRegister = false;
        this.nameField.setVisibility(View.GONE);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 121);
        }


        /*FragmentManager man = this.getSupportFragmentManager();
        FragmentTransaction transaction = man.beginTransaction();

        GroupsFragment frag = GroupsFragment.newInstance("a","a");
        transaction.add(R.id.fragmentContainer,frag);
        transaction.commit();*/
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
            String authMethod;

            if(this.inRegister) {
                authMethod = "register";
            }else
                authMethod = "login";

            sendRequest(authMethod,this.nameField.getText().toString(),this.emailField.getText().toString(),this.passwordField.getText().toString());
        }
    }

    public void onClickDebug(View view) {
        this.openMainActivity();
    }

    private void sendRequest(final String authMethod,final String name,final String email, final String password) {
        new RequestManager().execute(new Request("api/auth/" + authMethod, "POST","", "Email="+email+"&Password="+password+"&Name=" + name, new RequestCallback() {
            public void call(Response response) {
                //error.setText(response.statusCode + response.data.toString());
                errorName.setVisibility(View.GONE);
                errorEmail.setVisibility(View.GONE);
                errorPassword.setVisibility(View.GONE);

                if(response.statusCode == StatusCode.OK) {
                    if(authMethod == "login") {
                        SharedPreferences sp = getSharedPreferences("global", Context.MODE_PRIVATE);
                        sp.edit().putString("Token", response.data.toString()).commit();
                        openMainActivity();
                    }else if(authMethod == "register") {
                        sendRequest("login",name,email,password);
                    }
                }else {

                    switch (response.statusCode) {
                        case INVALID_REQUEST:
                            errorPassword.setVisibility(View.VISIBLE);
                            errorPassword.setText(getString(R.string.error_invalid_request));
                            break;
                        case DATABASE_ERROR:
                            errorPassword.setVisibility(View.VISIBLE);
                            errorPassword.setText(R.string.error_database_error);
                            break;
                        case INVALID_EMAIL:
                            errorEmail.setVisibility(View.VISIBLE);
                            errorEmail.setText(R.string.error_invalid_email);
                            break;
                        case INVALID_PASSWORD:
                            errorPassword.setVisibility(View.VISIBLE);
                            errorPassword.setText(R.string.error_invalid_password);
                            break;
                        case EMAIL_ALREADY_EXISTS:
                            errorEmail.setVisibility(View.VISIBLE);
                            errorEmail.setText(R.string.error_email_already_exists);
                            break;
                        case EMPTY_EMAIL:
                            errorEmail.setVisibility(View.VISIBLE);
                            errorEmail.setText(R.string.error_empty_email);
                            break;
                        case EMPTY_PASSWORD:
                            errorPassword.setVisibility(View.VISIBLE);
                            errorPassword.setText(R.string.error_empty_password);
                            break;
                        case EMPTY_NAME:
                            errorName.setVisibility(View.VISIBLE);
                            errorName.setText(R.string.error_empty_name);
                            break;
                        case NETWORK_ERROR:
                            errorPassword.setVisibility(View.VISIBLE);
                            errorPassword.setText(R.string.error_network);
                            break;
                    }
                }
            }
        }));
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(View view) {

    }
}
