package com.monash.moviememoir.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monash.moviememoir.R;
import com.monash.moviememoir.entity.Users;
import com.monash.moviememoir.fragment.HomeFragment;
import com.monash.moviememoir.networkconnect.NetworkConnection;

import static com.monash.moviememoir.md5.Encode.encodeMD5;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    NetworkConnection networkConnection = null;
    private boolean isHideFirst = true;// Enter the box password is hidden, the default is true
    private TextView login_register;  //Jump to the registration page
    private ImageView login_eye;
    private EditText login_email;
    private EditText login_password;
    private Button login_btn;
    private CheckBox login_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login in");
        initView();
        initData();
        initAction();
    }

    public void initView() {
        login_register = findViewById(R.id.login_register);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_eye = findViewById(R.id.login_eye);
        login_btn = findViewById(R.id.login_btn);
        login_remember = findViewById(R.id.login_remember);
    }

    public void initData() {
        login_register.setText(Html.fromHtml("<u>" + "REGISTER" + "</u>"));
        networkConnection = new NetworkConnection();
    }

    public void initAction() {
        login_register.setOnClickListener(this);
        login_eye.setOnClickListener(this);
        login_eye.setImageResource(R.drawable.close);
        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_eye:
                if (isHideFirst == true) {
                    login_eye.setImageResource(R.drawable.open);
                    HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                    login_password.setTransformationMethod(method1);
                    isHideFirst = false;
                } else {
                    login_eye.setImageResource(R.drawable.close);
                    TransformationMethod method = PasswordTransformationMethod.getInstance();
                    login_password.setTransformationMethod(method);
                    isHideFirst = true;
                }
                int index = login_password.getText().toString().length();
                login_password.setSelection(index);
                break;
            case R.id.login_btn:
                try {
                    if (login_email.getText() == null || login_email.getText().toString().length() == 0) {
                        inforProvide("Please input email");
                    } else if (!login_email.getText().toString().contains("@")) {
                        inforProvide("The email format is incorrect");
                    } else if (login_password.getText() == null || login_password.getText().toString().length() == 0) {
                        inforProvide(" Please input password");
                    } else {
                        PostLogin postLogin = new PostLogin(login_email.getText().toString(), encodeMD5(login_password.getText().toString()));
                        postLogin.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!login_remember.isChecked()) {
                        login_email.setText("");
                        login_password.setText("");
                    }
                }
                break;
            case R.id.login_register:
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            default:
                break;

        }
    }

    private void inforProvide(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        LinearLayout layout = (LinearLayout) toast.getView();
        TextView tv = (TextView) layout.getChildAt(0);
        tv.setTextSize(18);
        toast.setText(msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.setOrientation(LinearLayout.VERTICAL);

        toast.show();
    }


    private class PostLogin extends AsyncTask<Void, Void, String> {
        private String email;
        private String password;

        public PostLogin(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return networkConnection.doLogin(email, password);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Notice");
                builder.setMessage("Connect database failed! Check the network.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            } else if ("1".equals(result)) {
                GetUser getUser = new GetUser();
                getUser.execute(email);
            } else if ("0".equals(result)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Notice");
                builder.setMessage("Incorrect username or password!, Please input again");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        login_email.setText("");
                        login_password.setText("");
                    }
                });
                builder.show();
            }
        }
    }

    private class GetUser extends AsyncTask<String, Void, Users> {
        @Override
        protected Users doInBackground(String... strings) {
            return networkConnection.getUser(strings[0]);
        }

        @Override
        protected void onPostExecute(Users result) {
            System.out.println("############### MainActivityuser" + result);
            if (null != result) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("userid", result.getUserid());
                intent.putExtra("start",100);
                startActivity(intent);
            } else {
                inforProvide(" Connect database failed");
            }

        }
    }

}
