package com.example.basedatosneco1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private EditText edName, edSecName, edEmail;
    private DatabaseReference mDataBase;
    private String USER_KEY="User";
    private ImageView imImage;
    private StorageReference mStorageReferance;
    private Uri uploadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


    }

    private void init()
    {
        mStorageReferance= FirebaseStorage.getInstance().getReference("ImageDB");
        edName=findViewById(R.id.edName);
        edSecName=findViewById(R.id.edSecName);
        edEmail=findViewById(R.id.edEmail);
        mDataBase= FirebaseDatabase.getInstance().getReference(USER_KEY);
        imImage=findViewById(R.id.imImage);
    }

    private void SaveUser()
    {
        String id =mDataBase.push().getKey();
        String name=edName.getText().toString();
        String sec_name=edSecName.getText().toString();
        String email=edEmail.getText().toString();
        User newUser=new User(id, name, sec_name, email, uploadUri.toString());

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(sec_name) && !TextUtils.isEmpty(email))
        {
            if(id!=null)mDataBase.child(id).setValue(newUser);
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Пустое поле", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSave(View view)
    {
        uploadImage();
    }
    public void onClickRead(View view)
    {
        Intent i=new Intent(MainActivity.this, ReadActivity.class);
        startActivity(i);
    }

    public void onClickChooseImage(View view)
    {
        getImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && data!=null && data.getData()!=null)
        {
            if(resultCode==RESULT_OK)
            {
                Log.d("Mylog", "Image URI: "+data.getData());
                imImage.setImageURI(data.getData());

            }
        }
    }

    private void uploadImage()
    {
        Bitmap bitMap=((BitmapDrawable) imImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray=baos.toByteArray();
        final StorageReference mRef=mStorageReferance.child(System.currentTimeMillis()+"my_Image");
        UploadTask up=mRef.putBytes(byteArray);
        Task<Uri> task=up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri=task.getResult();
                Toast.makeText(MainActivity.this, "Изображение загружено!", Toast.LENGTH_SHORT).show();
                SaveUser();
            }
        });

    }

    private void getImage(){
        Intent intentChooser=new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, 1);
    }

}