package com.example.da4_trangquangchien;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;
public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final boolean[] batdieuhoa = {false};
        final boolean[] batnonglanh = {false};
        final boolean[] batquat = {false};
        Button dieuhoa=findViewById(R.id.dieuhoa);
        Button quat=findViewById(R.id.quat);
        Button binhnonglanh=findViewById(R.id.binhnonglanh);
        TextView nhietdo = findViewById(R.id.nhietdo);
        TextView hon =findViewById(R.id.hon);
        TextView hoff =findViewById(R.id.hoff);
        TextView doam =findViewById(R.id.doam);
        TextView giothuc =findViewById(R.id.gio);
        Button ok = findViewById(R.id.ok);
        EditText sethon=findViewById(R.id.sethon);
        EditText setmon=findViewById(R.id.setmon);
        EditText sethoff=findViewById(R.id.sethoff);
        EditText setmoff=findViewById(R.id.setmoff);
        DatabaseReference dieuhoadatabase = database.getInstance().getReference().child("dieukhien/dieuhoa");
        DatabaseReference quatdatabase = database.getInstance().getReference().child("dieukhien/quat");
        DatabaseReference nonglanhdatabase = database.getInstance().getReference().child("dieukhien/binhnonglanh");
        DatabaseReference nhietdodatabase = database.getInstance().getReference().child("giamsat/nhietdo");
        DatabaseReference doamdatabase = database.getInstance().getReference().child("giamsat/doam");
        DatabaseReference giodatabase = database.getInstance().getReference().child("thoigian/gio");
        DatabaseReference phutdatabase = database.getInstance().getReference().child("thoigian/phut");
        DatabaseReference hondatabase = database.getInstance().getReference().child("thoigian/hon");
        DatabaseReference mondatabase = database.getInstance().getReference().child("thoigian/mon");
        DatabaseReference hoffdatabase = database.getInstance().getReference().child("thoigian/hoff");
        DatabaseReference moffdatabase = database.getInstance().getReference().child("thoigian/moff");



        // bật bình nóng lạnh
        binhnonglanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(batnonglanh[0]==false){
                    nonglanhdatabase.setValue(1);
                }
                else{
                    nonglanhdatabase.setValue(0);
                }
            }
        });
        // cập nhật trạng thái nút nhấn nóng lạnh.
        nonglanhdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int data = Integer.parseInt(snapshot.getValue().toString());
                if(data==1){
                    binhnonglanh.setText("Đang Bật");
                    batnonglanh[0] =true;
                }
                else{
                    binhnonglanh.setText("Đang Tắt");
                    batnonglanh[0] =false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        // bật điều hòa
        dieuhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(batdieuhoa[0] ==false){
                    dieuhoadatabase.setValue(1);
                }
                else{
                    dieuhoadatabase.setValue(0);
                }
            }
        });
// bật quạt
        quat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(batquat[0] ==false){
                    quatdatabase.setValue(1);
                }
                else{
                    quatdatabase.setValue(0);
                }
            }
        });
        // cập nhật trạng thái nút nhấn điều hòa.
        dieuhoadatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            int data = Integer.parseInt(snapshot.getValue().toString());
            if(data==1){
                dieuhoa.setText("Đang Bật");
                batdieuhoa[0] =true;
            }
            else{
                dieuhoa.setText("Đang Tắt");
                batdieuhoa[0] =false;
            }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
// cập nhật trạng thái quạt
        quatdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int data = Integer.parseInt(snapshot.getValue().toString());
                if(data==1){
                    quat.setText("Đang Bật");
                    batquat[0] =true;
                }
                else{
                    quat.setText("Đang Tắt");
                    batquat[0] =false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //hiển thị nhiệt độ.
        nhietdodatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data= snapshot.getValue().toString();
                nhietdo.setText(data+" độ C");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //hiển thị độ ẩm
        doamdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data = snapshot.getValue().toString();
                doam.setText(data+" %");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        // cập nhật giờ on
        hondatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int gio = Integer.parseInt(snapshot.getValue().toString());
                mondatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int phut = Integer.parseInt(snapshot.getValue().toString());
                        hon.setText(gio+" : "+phut);
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

        Button profileactiviti = findViewById(R.id.pro);
        profileactiviti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ThongtinND.class);
                startActivity(i);
            }
        });





        // cập nhật giờ off
        hoffdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int gio = Integer.parseInt(snapshot.getValue().toString());
                moffdatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int phut = Integer.parseInt(snapshot.getValue().toString());
                        hoff.setText(gio+" : "+phut);
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

        //set giờ on off cho điều hòa
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gioon= sethon.getText().toString();
                String phuton= setmon.getText().toString();
                String giooff= sethoff.getText().toString();
                String phutoff= setmoff.getText().toString();
                if (gioon.isEmpty() || phuton.isEmpty()||giooff.isEmpty()||phutoff.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    int datahon =Integer.parseInt(gioon);
                    int datamon =Integer.parseInt(phuton);
                    int datahoff =Integer.parseInt(giooff);
                    int datamoff =Integer.parseInt(phutoff);
                    if(datahon >23 || datahoff >23 || datamoff >59 || datamon>59){
                        Toast.makeText(MainActivity.this, "Nhập thời gian chưa chính xác", Toast.LENGTH_LONG).show();
                    }
                    else if(datahon+datamon == datahoff+datamoff){
                        Toast.makeText(MainActivity.this, "Không thể đặt giờ bật và tắt cùng 1 thời điểm", Toast.LENGTH_LONG).show();
                    }
                    else{
                        hondatabase.setValue(datahon);
                        mondatabase.setValue(datamon);
                        hoffdatabase.setValue(datahoff);
                        moffdatabase.setValue(datamoff);
                        Toast.makeText(MainActivity.this, "Cài đặt thành công !!!", Toast.LENGTH_LONG).show();

                    }
                }
                catch (NumberFormatException e){
                    Toast.makeText(MainActivity.this, "Dữ liệu nhập sai", Toast.LENGTH_LONG).show();
                }
            }
        });


        phutdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int phut=Integer.parseInt(snapshot.getValue().toString());
                hondatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int giobat = Integer.parseInt(snapshot.getValue().toString());
                        hoffdatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int giotat = Integer.parseInt(snapshot.getValue().toString());
                                mondatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int phutbat = Integer.parseInt(snapshot.getValue().toString());
                                        moffdatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int phuttat = Integer.parseInt(snapshot.getValue().toString());
                                                giodatabase.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        int gio = Integer.parseInt(snapshot.getValue().toString());
                                                        giothuc.setText(gio+" : "+phut);
                                                        if(phut==phutbat && gio==giobat){
                                                            nonglanhdatabase.setValue(1);
                                                        }
                                                        if(phut==phuttat&&gio==giotat){
                                                            nonglanhdatabase.setValue(0);
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
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi lắng nghe dữ liệu bị hủy bỏ hoặc gặp lỗi
            }
        });
    }
}