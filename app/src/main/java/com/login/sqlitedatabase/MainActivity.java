package com.login.sqlitedatabase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity
{
    EditText id,name,course,number;
    Button save;
    MyDatabase databse;
    ImageView camera,gallery,imageView1;
    int height,width;
    int GALLERY_REQUEST=234;
    int CAMERA_REQUEST=254;
    Bitmap imageBitmap;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews();
    }
    public  void initviews()
    {
        height=getResources().getDisplayMetrics().heightPixels;
        width=getResources().getDisplayMetrics().widthPixels;
        id=(EditText)findViewById(R.id.id);
        name=(EditText)findViewById(R.id.name);
        course=(EditText)findViewById(R.id.course);
        number=(EditText)findViewById(R.id.number);
        camera=(ImageView)findViewById(R.id.camera);
        gallery=(ImageView)findViewById(R.id.gallery);
        imageView1=(ImageView)findViewById(R.id.imageView1);
        camera.getLayoutParams().height=width/8;
        camera.getLayoutParams().width=width/8;
        gallery.getLayoutParams().height=width/8;
        gallery.getLayoutParams().width=width/8;
        databse=new MyDatabase(this);

        gallery.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                        GALLERY_REQUEST);
            }
        });
        camera.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST && resultCode==RESULT_OK)
        {
            try
            {
			/*imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
			final float densityMultiplier = this.getResources().getDisplayMetrics().density;
			float newHeight = 300;
			int h= (int) (newHeight*densityMultiplier);
			int w= (int) (h * imageBitmap.getWidth()/((double) imageBitmap.getHeight()));
			imageBitmap=Bitmap.createScaledBitmap(imageBitmap, w, h, true);	*/


                imageBitmap = (Bitmap) data.getExtras().get("data");
                imageView1.setImageBitmap(imageBitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e("Image  Error", "****" + e.getMessage());
            }

        }
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                final float densityMultiplier = this.getResources().getDisplayMetrics().density;
                float newHeight = 150;
                int h= (int) (newHeight*densityMultiplier);
                int w= (int) (h * imageBitmap.getWidth()/((double) imageBitmap.getHeight()));

                imageBitmap=Bitmap.createScaledBitmap(imageBitmap, w, h, true);
                imageView1.setImageBitmap(imageBitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e("Image  Error", "****" + e.getMessage());
            }
        }
    }

    public void save(View v)
    {
        //int personid=Integer.parseInt(id.getText().toString());
        String personid=id.getText().toString();
        String personname=name.getText().toString();
        String personcourse=course.getText().toString();
        //int personnum=Integer.parseInt(number.getText().toString());
        String personnum=number.getText().toString();

        if(personname.equals("") || personcourse.equals("") || personid.equals("") || personnum.equals("") || imageBitmap==null)
        {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer=out.toByteArray();
            Data data=new Data(personid, personname, personcourse, personnum,buffer);
            databse.addData(data);

            id.setText("");
            name.setText("");
            course.setText("");
            number.setText("");
            imageView1.setImageBitmap(null);
//Toast.makeText()

        }


    }
    public void view(View v)
    {
        startActivity(new Intent(this,ViewActivity.class));
        MainActivity.this.finish();
    }
    private static long back_pressed_time;
    private static long PERIOD = 2000;
    @Override
    public void onBackPressed()
    {
        if (back_pressed_time + PERIOD > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed_time = System.currentTimeMillis();
    }
}
