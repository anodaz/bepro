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
        System.out.println("id : "+b.getString("id"));
        String id=b.getString("id");
        System.out.println("id : "+b.getString("title"));
        String title=b.getString("title");
        textView=(TextView) findViewById(R.id.textView);
        imageView=(ImageView)findViewById(R.id.image);
        textView.setText("anodaz: "+title);
        Picasso.get().load("https://drive.google.com/uc?id="+id).into(imageView);
    }
}
