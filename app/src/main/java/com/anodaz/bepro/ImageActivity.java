package com.anodaz.bepro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Bundle b=getIntent().getExtras();
        System.out.println("id : "+b.getInt("id"));
        System.out.println("date : "+b.getString("date"));
        System.out.println("image : "+b.getString("image"));
        System.out.println("status : "+b.getString("status"));
        String image=b.getString("image");
        textView=(TextView) findViewById(R.id.textView);
        imageView=(ImageView)findViewById(R.id.image);
        textView.setText("id: "+b.getInt("id")+" date: "+b.getString("date")+" image: "+b.getString("image")+" status: "+b.getString("status"));
        Picasso.get().load("https://drive.google.com/uc?id="+image).into(imageView);
    }
}
