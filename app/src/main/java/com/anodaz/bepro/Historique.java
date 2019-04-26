package com.anodaz.bepro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.anodaz.bepro.Service.Constants;
import com.anodaz.bepro.Service.DBSqlite;
import com.anodaz.bepro.Service.Item;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Historique extends AppCompatActivity {
    private ArrayAdapter adapters;
    private ListView listView;
    public int lastshownitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        listView=(ListView) findViewById(R.id.item);
        DBSqlite db = new DBSqlite(this);
        ArrayList<Item> allp=db.getallp();
        adapters = new Historique.propertyArrayAdapter(this, 0, allp);
        listView.setAdapter(adapters);


    }
    class propertyArrayAdapter extends ArrayAdapter<Item> {

        private Context context;
        private List<Item> rentalProperties;

        //constructor, call on creation
        public propertyArrayAdapter(Context context, int resource, ArrayList<Item> objects) {
            super(context, resource, objects);

            this.context = context;
            this.rentalProperties = objects;
        }
        //called when rendering the list

        public View getView(final int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            final Item item = rentalProperties.get(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //conditionally inflate either standard or special template
            View view;
            view = inflater.inflate(R.layout.item, null);
            TextView id = (TextView) view.findViewById(R.id.id);
            TextView date = (TextView) view.findViewById(R.id.date);
            //TextView price = (TextView) view.findViewById(R.id.price);
            final ImageView image = (ImageView) view.findViewById(R.id.image);
            Switch status = (Switch) view.findViewById(R.id.status);
            /*URL newurl = null;
            try {
                newurl = new URL(Constants.URL+"producer/Images/"+producer.getImage()+"/");
                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
                image.setImageBitmap(mIcon_val);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
*/

            if(item.getImage()!="")
                try {
                    Picasso.get().load(Constants.URL+"producer/Images/"+item.getId()).into(image);
                }catch (Exception e){
                    image.setImageBitmap(loadImageBitmap(getApplicationContext(), item.getImage()));
                }

            status.setChecked(item.getStatus());
            date.setText(item.getDate());
            id.setText(item.getId()+"");
            //description.setText(producer.getDescription());
            //FloatingActionButton edite= view.findViewById(R.id.edite);

            FloatingActionButton view1= view.findViewById(R.id.view);
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item item=rentalProperties.get(position);
                    System.out.println("Possition : "+position);
                    Intent i = new Intent(Historique.this, ImageActivity.class);
                    Bundle b=new Bundle();
                    b.putInt("id", item.getId());
                    b.putString("date", item.getDate());
                    b.putString("image", item.getImage());
                    b.putString("status", ""+item.getStatus());
                    i.putExtras(b);
                    startActivity(i);
                }
            });
            return view;
        }
    }
    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }
}
