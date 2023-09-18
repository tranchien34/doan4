package com.example.da4_trangquangchien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText user = findViewById(R.id.user);
        EditText pass = findViewById(R.id.pass);
        Button login = findViewById(R.id.login);
        DatabaseReference userdata = database.getInstance().getReference().child("user/user");
        DatabaseReference passdata = database.getInstance().getReference().child("user/pass");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tk = user.getText().toString();
                String mk = pass.getText().toString();
                userdata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String datauser = snapshot.getValue().toString();
                        passdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String datapass = snapshot.getValue().toString();
                                if(tk.isEmpty() || mk.isEmpty()){
                                    Toast.makeText(Login.this,"Không được bỏ trống",Toast.LENGTH_SHORT).show();
                                }
                                else if(tk.equals(datauser) && mk.equals(datapass)) {
                                    Intent i = new Intent(Login.this,MainActivity.class);
                                    startActivity(i);
//                                    finishAffinity();
                                }
                                else {
                                        Toast.makeText(Login.this,"Thông tin tài khoản mật khẩu không chính xác !",Toast.LENGTH_LONG).show();
                                    }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}