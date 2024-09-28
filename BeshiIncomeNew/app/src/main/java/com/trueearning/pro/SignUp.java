package com.trueearning.pro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Objects;




@Keep
public class SignUp extends AppCompatActivity {

    private static final String FILE_NAME = "myFile";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    LottieAnimationView lottie;
    Button regBtn, callLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        regBtn = findViewById(R.id.reg_btn);
        lottie = findViewById(R.id.lottie_reg);
        callLogIn = findViewById(R.id.callLogIn);
        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPhoneNo = findViewById(R.id.reg_phone);
        regPassword = findViewById(R.id.reg_password);

         FirebaseApp.initializeApp(SignUp.this);

        regBtn.setOnClickListener(view -> {
            lottie.setVisibility(View.VISIBLE);
            lottie.playAnimation();
            String userEnteredUsername = Objects.requireNonNull(regUsername.getEditText()).getText().toString().toLowerCase().trim();
            Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        regUsername.setError("User already Exist, Try new");
                        regUsername.requestFocus();
                    } else {
                        regUsername.setError(null);
                        regUsername.setErrorEnabled(false);
                        if (validateName() && validateUsername() && validateEmail() && validatePhoneNo() && validatePassword()) {
                            String name = Objects.requireNonNull(regName.getEditText()).getText().toString().trim();
                            String username = Objects.requireNonNull(regUsername.getEditText()).getText().toString().toLowerCase().trim();
                            String email = Objects.requireNonNull(regEmail.getEditText()).getText().toString().trim();
                            String phone = Objects.requireNonNull(regPhoneNo.getEditText()).getText().toString().trim();
                            String password = Objects.requireNonNull(regPassword.getEditText()).getText().toString().trim();
                            String withdraw = "";
                            int points = 50;

                            Calendar calendar = Calendar.getInstance();
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int d = calendar.get(Calendar.DAY_OF_MONTH);
                            int day = d + 1;
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);
                            String date = day  + "-" + month + "-" + year + "; " + hour + ":"+ minute;

                            String address = getDeviceIpAddress();
                            String secreate = "create";

                            try {
                                Users users = new Users(name, username, email, phone, password, withdraw, date, address, secreate, points);
                                reference.child(username).setValue(users);
                                Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                                StoreNamePass(username, password, date, address, points);

                                Intent i = new Intent(SignUp.this, Login.class);
                                startActivity(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SignUp.this, "Database Linking Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

        callLogIn.setOnClickListener(view -> SignUp.super.onBackPressed());
    }


    @NonNull
    private String getDeviceIpAddress() {
        String actualConnectedToNetwork = null;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi.isConnected()) {
                actualConnectedToNetwork = getWifiIp();
            }
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = getNetworkInterfaceIpAddress();
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = "not found";
        }
        return actualConnectedToNetwork;
    }

    @Nullable
    private String getWifiIp() {
        final WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null && mWifiManager.isWifiEnabled()) {
            int ip = mWifiManager.getConnectionInfo().getIpAddress();
            return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                    + ((ip >> 24) & 0xFF);
        }
        return null;
    }


    @Nullable
    public String getNetworkInterfaceIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String host = inetAddress.getHostAddress();
                        if (!TextUtils.isEmpty(host)) {
                            return host;
                        }
                    }
                }

            }
        } catch (Exception ex) {
            Log.e("IP Address", "getLocalIpAddress", ex);
        }
        return null;
    }





    private void StoreNamePass(String username, String password, String date, String address, Integer points) {
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString("userEnteredUsername", username);
        editor.putString("userEnteredPassword", password);
        editor.putString("date", date);
        editor.putString("address", address);

        int coins = points;
        editor.putInt("point", coins);
        editor.apply();
    }

    /***********On_Create Ends Here***********/
    private boolean validateName() {
        String val = Objects.requireNonNull(regName.getEditText()).getText().toString();

        if (val.isEmpty()) {
            regName.setError("Full Name cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername() {
        String val = Objects.requireNonNull(regUsername.getEditText()).getText().toString().toLowerCase().trim();
        String noWhiteSpace = "^[a-zA-Z0-9]{4,15}$";

        if (val.isEmpty()) {
            regUsername.setError("Username cannot be empty");
            return false;
        } else if (val.length() < 4) {
            regUsername.setError("Username is too short");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("শুধুমাত্র 1 টি WORD হতে পারবে, 2 টি নয়!");
            AlertDialog dialog = new AlertDialog.Builder(SignUp.this).setTitle("Username ফরম্যাট ভুল হয়েছে")
                    .setMessage("ইউজারনেম শুধুমাত্র একটি Name অথবা Name1234 হতে পারবে, কিন্তু দুই শব্দের মাঝখানে কোনো গ্যাপ(Gap বা শুন্যস্থান) রাখা যাবে না এবং @#৳%ঁ! এইগুলা রাখা যাবে না। ঊদাহরনস্বরুপঃ- messi,   messi10,  neymar,   neymar420,   YourName1234 এইরকম হতে পারবে। অন্যথায়, Error দেখাবে!")
                    .setPositiveButton("বুঝতে পেরেছি", (dialogInterface, i) -> dialogInterface.dismiss()).show();
            dialog.setCancelable(false);
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateEmail() {
        String val = Objects.requireNonNull(regEmail.getEditText()).getText().toString();
        String emailPattern = "^[a-z0-9+_.-]+@[a-z0-9.-]+$";

        if (val.isEmpty()) {
            regEmail.setError("Email cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Enter valid Email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhoneNo() {
        String val = Objects.requireNonNull(regPhoneNo.getEditText()).getText().toString();
        String phoneValidate = "(?=\\S+$)" + "(?:\\+88|88)?(01[3-9]\\d{8})";

        if (val.isEmpty()) {
            regPhoneNo.setError("Must enter a valid phoneNumber");
            return false;
        } else if (!val.matches(phoneValidate)) {
            regPhoneNo.setError("Invalid, Only BD phone No are accepted");
            return false;
        } else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {
        String val = Objects.requireNonNull(regPassword.getEditText()).getText().toString();
        String passwordValidate = "(?=\\S+$)" + ".{4,}";

        if (val.isEmpty()) {
            regPassword.setError("Password cannot be empty");
            return false;
        } else if (!val.matches(passwordValidate)) {
            regPassword.setError("Password is too short");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }
}
