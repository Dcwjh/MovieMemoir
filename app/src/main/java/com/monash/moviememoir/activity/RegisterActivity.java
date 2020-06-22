package com.monash.moviememoir.activity;

import androidx.annotation.RequiresApi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.monash.moviememoir.R;
import com.monash.moviememoir.entity.Credentials;
import com.monash.moviememoir.entity.Users;
import com.monash.moviememoir.md5.Encode;
import com.monash.moviememoir.networkconnect.NetworkConnection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText text_email;
    private EditText text_password;
    private EditText text_firstname;
    private EditText text_surname;
    private RadioGroup text_group_gender;
    private String gender;
    private EditText text_DoB;
    private EditText text_address;
    private Spinner text_spinner_state;
    private String text_state;
    private EditText text_postcode;
    private RadioButton male;
    private RadioButton female;
    private ImageView imageView;
    NetworkConnection networkConnection = null;
    private Button submit;
    private TextView results;
    private Calendar calendar; // 通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean isHideFirst = true;// Enter the box password is hidden, the default is true
    private static String result;
    Credentials credential;
    Users user;
    String[] arr = {"NSW", "QLD", "SA", "TAS", "VIC", "WA"};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initAction();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }
    }

    private void initView() {
        text_email = findViewById(R.id.re_email);
        text_password = findViewById(R.id.re_password);
        text_firstname = findViewById(R.id.re_firstname);
        text_surname = findViewById(R.id.re_surname);
        text_group_gender = findViewById(R.id.re_gender);
        text_address = findViewById(R.id.re_address);
        male = findViewById(R.id.gender_male);
        female = findViewById(R.id.gender_female);
        text_spinner_state = findViewById(R.id.re_spinner_state);
        text_DoB = findViewById(R.id.re_dob);
        text_postcode = findViewById(R.id.re_postcode);
        submit = findViewById(R.id.re_submit);
        imageView = findViewById(R.id.re_iv);
        results = findViewById(R.id.result);
        male = findViewById(R.id.gender_male);
        female = findViewById(R.id.gender_female);
    }


    private void initData() {
        networkConnection = new NetworkConnection();
        imageView.setImageResource(R.drawable.close);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
        text_spinner_state.setAdapter(adapter);

    }

    private void initAction() {
        imageView.setOnClickListener(this);
        text_DoB.setOnClickListener(this);
        submit.setOnClickListener(this);
        text_spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text_state = text_spinner_state.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                text_state = arr[0];
            }
        });

        gender = "male";
        text_group_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (male.getId() == checkedId) {
                    gender = "male";
                } else if (female.getId() == checkedId) {
                    gender = "female";
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_iv:
                if (isHideFirst) {
                    imageView.setImageResource(R.drawable.open);
                    HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                    text_password.setTransformationMethod(method1);
                    isHideFirst = false;
                } else {
                    imageView.setImageResource(R.drawable.close);
                    TransformationMethod method = PasswordTransformationMethod.getInstance();
                    text_password.setTransformationMethod(method);
                    isHideFirst = true;
                }
                int index = text_password.getText().toString().length();
                text_password.setSelection(index);
                break;
            case R.id.re_dob:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    new DatePickerDialog(RegisterActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    // TODO Auto-generated method stub
                                    mYear = year;
                                    mMonth = month;
                                    mDay = day;
                                    text_DoB.setText(new StringBuilder().append(mYear).append("-")
                                            .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                                            .append((mDay < 10) ? "0" + mDay : mDay));
                                }
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                break;

            case R.id.re_submit:
                if(text_email.getText()==null || text_email.getText().toString().length() == 0){
                    infoProvide(" Please input username");
                }else if (text_password.getText() == null || text_password.getText().toString().length() == 0) {
                    infoProvide(" Please input password");
                } else if (text_firstname.getText() == null ||  text_firstname.getText().toString().length() == 0) {
                   infoProvide(" Please input firstname");
                } else if (text_surname.getText() == null ||  text_surname.getText().toString().length() == 0) {
                   infoProvide( " Please input surname");
                } else if (!text_email.getText().toString().contains("@")) {
                   infoProvide(" Incorrect email format");
                    text_email.setText("");
                } else if (text_email.getText().toString().length() < 7) {
                    infoProvide(" Email is to short");
                    text_email.setText("");
                } else if (text_password.getText().toString().length() < 6){
                    infoProvide(" Password is to short");
                    text_password.setText("");
                } else {
                    credential = new Credentials();
                    user = new Users(text_firstname.getText().toString(),text_surname.getText().toString());
                    if(text_address.getText() == null || text_address.getText().toString().length() == 0)
                        user.setAddress("null");
                    else
                        user.setAddress(text_address.getText().toString());
                    if(text_postcode == null || text_postcode.getText().toString().length() == 0)
                        user.setPostcode("null");
                    else
                        user.setPostcode(text_postcode.getText().toString());
                    user.setState(text_state);
                    user.setDob(text_DoB.getText().toString());
                    user.setGender(gender);
                    credential.setUserid(user);
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String signupdate = df.format(date);
                    credential.setSingupdate(signupdate);
                    credential.setSingupdate(signupdate);
                    credential.setUsername(text_email.getText().toString());
                    credential.setPassword(Encode.encodeMD5(text_password.getText().toString()));
                    CheckEmail checkEmail = new CheckEmail();
                    checkEmail.execute(text_email.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    private void infoProvide(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        LinearLayout layout = (LinearLayout) toast.getView();
        TextView tv = (TextView) layout.getChildAt(0);
        tv.setTextSize(18);
        toast.setText(msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ImageView imageView= new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.warn);
        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.setOrientation(LinearLayout.HORIZONTAL);
        toastView.addView(imageView, 0);
        toast.show();
    }


    private class PostCredential extends AsyncTask<Void, Void, String> {
        private Credentials credential;

        public PostCredential(Credentials credential) {
            this.credential = credential;
        }

        @Override
        protected String doInBackground(Void... params) {
            return networkConnection.doRegister(credential);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == null)
                infoProvide(" connect database failed!");
            else
                results.setText(result);
        }
    }



    private class CheckEmail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.checkEmail(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if(result == null){
                    infoProvide(" Connect database failed!");
                }else if (result.contains(text_email.getText().toString())) {
                   infoProvide(" Email has already existed");
                    text_email.setText("");
                } else if ("[]".equals(result)) {
                    PostCredential postCredential = new PostCredential(credential);
                    postCredential.execute();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    intent.putExtra("email", text_email.getText().toString());
                    startActivity(intent);
                } else {
                   infoProvide("Unknow Error");
                    text_email.setText("");
                    text_password.setText("");
                    text_firstname.setText("");
                    text_surname.setText("");
                    text_address.setText("");
                    text_postcode.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
                infoProvide(" Unknow Error, input again");
                text_email.setText("");
                text_password.setText("");
                text_firstname.setText("");
                text_surname.setText("");
                text_address.setText("");
                text_postcode.setText("");
            }
            finally {
//                results.setText(result);
                RegisterActivity.this.onResume();
            }
        }
    }

}
