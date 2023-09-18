package com.example.da4_trangquangchien;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThongtinND extends AppCompatActivity {
    private ProgressDialog doimking;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin_nd);
        TextView changepass = findViewById(R.id.changepass);
        dialog = new Dialog(ThongtinND.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.changepass);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doimk(Gravity.CENTER);
            }
        });
    }

        private void doimk(int gravity) {
            final Dialog dialog = new Dialog(ThongtinND.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.changepass);
            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = gravity;
            window.setAttributes(windowAttributes);

            if (Gravity.BOTTOM == gravity) {
                dialog.setCancelable(true);
            } else {
                dialog.setCancelable(false);
            }
            EditText mkcu = dialog.findViewById(R.id.passold);
            EditText mkmoi = dialog.findViewById(R.id.passnew);
            EditText xacnhanmkmoi = dialog.findViewById(R.id.comfimpass);
            Button btnno = dialog.findViewById(R.id.no);
            Button btnyes = dialog.findViewById(R.id.yes);
            DatabaseReference mkdatabase = FirebaseDatabase.getInstance().getReference().child("user/pass");
            DatabaseReference finalMdatabasemk = FirebaseDatabase.getInstance().getReference().child("user/pass");
            doimking= new ProgressDialog(ThongtinND.this);
            EditText finalEditTextmkcu = mkcu;
            EditText finalEditTextmkmoi = mkmoi;
            EditText finalEditTextxacnhanmkmoi = xacnhanmkmoi;
            btnyes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String datamkcu = finalEditTextmkcu.getText().toString();
                    String datamkmoi = finalEditTextmkmoi.getText().toString();
                    String dataxacnhanmkmoi = finalEditTextxacnhanmkmoi.getText().toString();

                    mkdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String mk = dataSnapshot.getValue().toString();
                            if (datamkcu.isEmpty() || datamkmoi.isEmpty() || dataxacnhanmkmoi.isEmpty()) {
                                Toast.makeText(ThongtinND.this, "Không được bỏ trống", Toast.LENGTH_LONG).show();
                                return;
                            } else if (mk != null && !mk.equals(datamkcu)) {
                                Toast.makeText(ThongtinND.this, "Mật khẩu cũ không chính xác", Toast.LENGTH_LONG).show();
                                return;
                            } else if (datamkmoi.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?âăôêơưáảãạà ếểễệ].*")) {
                                Toast.makeText(ThongtinND.this, "Mật khẩu chứa ký tự không hợp lệ", Toast.LENGTH_LONG).show();
                                return;
                            }else if (!datamkmoi.equals(dataxacnhanmkmoi)) {
                                Toast.makeText(ThongtinND.this, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_LONG).show();
                                return;

                            } else if(datamkmoi.length() < 8){
                                Toast.makeText(ThongtinND.this, "Mật khẩu có ít nhất 8 kí tự.", Toast.LENGTH_LONG).show();
                                return;
                            }else if(datamkmoi.length() > 32){
                                Toast.makeText(ThongtinND.this, "Mật khẩu phải ít hơn 32 kí tự.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else if (datamkcu.equals(datamkmoi)){
                                Toast.makeText(ThongtinND.this, "Mật Khẩu Mới Phải Khác Mật Khẩu Cũ", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                finalMdatabasemk.setValue(datamkmoi).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        doimking.setMessage("Đang Đổi Mật Khẩu");
                                        doimking.show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                doimking.dismiss();
                                            }
                                        }, 1000); //tắt dialog sau 3 giây (3000ms)
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ThongtinND.this, "Đổi Mật Khẩu Thành Công", Toast.LENGTH_LONG).show();
                                            }
                                        }, 1000);
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xử lý lỗi nếu có
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Xử lý lỗi nếu có
                        }
                    });
                }
            });
            btnno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
