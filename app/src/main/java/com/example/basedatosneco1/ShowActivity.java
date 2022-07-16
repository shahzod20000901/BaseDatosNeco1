package com.example.basedatosneco1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ShowActivity extends AppCompatActivity {
    private TextView tvName, tvSecName, tvEmail;
    private ImageView imBD;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        init();
        getIntentMain();
    }

    private void init(){
        tvName=findViewById(R.id.tvName);
        tvSecName=findViewById(R.id.tvSecName);
        tvEmail=findViewById(R.id.tvEmail);
        imBD=findViewById(R.id.im_read);

    }

    private void getIntentMain(){
        Intent i =getIntent();
        if (i !=null)
        {
            Picasso.get().load(i.getStringExtra(Constant.USER_IMAGE_ID)).into(imBD);
            tvName.setText(i.getStringExtra(Constant.USER_NAME));
            tvSecName.setText(i.getStringExtra(Constant.USER_SEC_NAME));
            tvEmail.setText(i.getStringExtra(Constant.USER_EMAIL));
        }
    }
}
