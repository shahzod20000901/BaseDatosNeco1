package com.example.basedatosneco1;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private DatabaseReference mDataBase;
    private String USER_KEY="User";
    private List<User> listTemp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_layout);
        init();
        getDataFromDB();
        setOnClickItem();
    }

    private void init(){
        listView=findViewById(R.id.listView);
        listData=new ArrayList<>();
        listTemp=new ArrayList<>();
        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
        mDataBase= FirebaseDatabase.getInstance().getReference(USER_KEY);
    }

    private void getDataFromDB(){
        ValueEventListener vListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size()>0)listData.clear();
                if(listTemp.size()>0)listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    User user=ds.getValue(User.class);
                    assert user!=null;
                    listData.add(user.name);
                    listTemp.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBase.addValueEventListener(vListener);
    }

    private void setOnClickItem()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user=listTemp.get(i);
                Intent intent =new Intent(ReadActivity.this, ShowActivity.class);
                intent.putExtra(Constant.USER_NAME, user.name);
                intent.putExtra(Constant.USER_SEC_NAME, user.sec_name);
                intent.putExtra(Constant.USER_EMAIL, user.email);
                intent.putExtra(Constant.USER_IMAGE_ID, user.image_id);
                startActivity(intent);
            }
        });
    }
}
