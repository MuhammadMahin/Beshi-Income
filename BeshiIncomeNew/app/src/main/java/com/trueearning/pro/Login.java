package com.trueearning.pro;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trueearning.pro.Utility.NetworkChangeListener;

import java.util.Objects;

@Keep
public class Login extends AppCompatActivity {

    private static final String FILE_NAME = "myFile";
    Button callSignUp;
    AppCompatButton login_btn, skipLogin;
    ImageView image;
    TextView logoText, sloganText, forgotText;
    TextInputLayout username, password;
    LottieAnimationView loginProgress;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callSignUp = findViewById(R.id.callSignUp);
        loginProgress = findViewById(R.id.lottie_login);
        image = findViewById(R.id.logo_Image);
        logoText = findViewById(R.id.logo_text);
        sloganText = findViewById(R.id.slogan);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        forgotText = findViewById(R.id.forgotPass);
        skipLogin = findViewById(R.id.skip_login);


        FirebaseApp.initializeApp(this);
        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String userUsername = sharedPreferences.getString("userEnteredUsername", "");
        String userPassword = sharedPreferences.getString("userEnteredPassword", "");

        if (userUsername.isEmpty() && userPassword.isEmpty()) {
            Toast.makeText(this, "REGISTRATION First", Toast.LENGTH_SHORT).show();
        }
        Objects.requireNonNull(username.getEditText()).setText(userUsername);
        Objects.requireNonNull(password.getEditText()).setText(userPassword);


        login_btn.setOnClickListener(view -> {
            loginProgress.setVisibility(View.VISIBLE);
            loginProgress.playAnimation();

            if (Username() && Password()) {

                boolean isFirst = sharedPreferences.getBoolean("firstLogin", true);

                String userEnteredUsername = Objects.requireNonNull(username.getEditText()).getText().toString().toLowerCase().trim();
                String userEnteredPassword = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

                if (isFirst) {


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                    Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                username.setError(null);
                                username.setErrorEnabled(false);

                                String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                                assert passwordFromDB != null;
                                if (passwordFromDB.equals(userEnteredPassword)) {
                                    Toast.makeText(Login.this, "Welcome to Beshi Income!", Toast.LENGTH_SHORT).show();
                                    password.setError(null);
                                    password.setErrorEnabled(false);
                                    String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                                    String phoneFromDB = snapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                                    String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                                    Integer pointFromDB = snapshot.child(userEnteredUsername).child("points").getValue(Integer.class);


                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("secondTime", false);
                                    editor.putBoolean("firstLogin", false);
                                    editor.apply();

                                    StoreData(userEnteredUsername, userEnteredPassword, nameFromDB, phoneFromDB, emailFromDB, pointFromDB);


                                    Intent i = new Intent(getApplicationContext(), Home.class);
                                    startActivity(i);
                                } else {
                                    password.setError("Wrong password");
                                    password.requestFocus();
                                }
                            } else {
                                username.setError("No such User exist!");
                                username.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Login.this, "Database Error, Contact us", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    if ((userEnteredUsername.equals(userUsername)) && (userEnteredPassword.equals(userPassword))) {
                        username.setError(null);
                        password.setError(null);
                        startActivity(new Intent(getApplicationContext(), Home.class));

                    } else {
                        username.setError("Username or Password is Wrong");
                        password.setError("Username or Password is Wrong");
                        Toast.makeText(this, "Username or Password is Wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }


        });

        skipLogin.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Home.class));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("secondTime", false);
            editor.apply();
        });

        callSignUp.setOnClickListener(view -> {
            Intent i = new Intent(Login.this, SignUp.class);
            Pair[] pairs = new Pair[8];
            pairs[0] = new Pair<View, String>(image, "logo_image");
            pairs[1] = new Pair<View, String>(logoText, "logo_text");
            pairs[2] = new Pair<View, String>(sloganText, "slogan_text");
            pairs[3] = new Pair<View, String>(username, "username_tran");
            pairs[4] = new Pair<View, String>(password, "password_tran");
            pairs[5] = new Pair<View, String>(forgotText, "forgot_tran");
            pairs[6] = new Pair<View, String>(login_btn, "button_tran");
            pairs[7] = new Pair<View, String>(callSignUp, "login_signup_tran");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(i, options.toBundle());

        });


    }


    private void StoreData(String userEnteredUsername, String userEnteredPassword, String nameFromDB, String phoneFromDB, String emailFromDB, Integer pointFromDB) {
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString("userEnteredUsername", userEnteredUsername);
        editor.putString("userEnteredPassword", userEnteredPassword);
        editor.putString("nameFromDB", nameFromDB);
        editor.putString("phoneFromDB", phoneFromDB);
        editor.putString("emailFromDB", emailFromDB);
        int lPoints = pointFromDB;
        editor.putInt("point", lPoints);
        editor.apply();
    }

    private boolean Username() {
        String val = Objects.requireNonNull(username.getEditText()).getText().toString().toLowerCase().trim();
        String noWhiteSpace = "[^-\\s]";
        if (val.isEmpty()) {
            username.setError("Username cannot be empty");
            return false;
        } else if (val.length() < 4) {
            username.setError("Username is too short");
            return false;
        } else if (val.matches(noWhiteSpace)) {
            username.setError("White space are not allowed");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }

    private boolean Password() {
        String val = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press again to exit Login",
                    Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }


    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
