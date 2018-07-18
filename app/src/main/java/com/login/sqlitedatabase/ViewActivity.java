package com.login.sqlitedatabase;

import java.io.ByteArrayOutputStream;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewActivity extends Activity
{
	ListView listView;
	MyDatabase md;
	int height,width;
	int CAMERA_REQUEST=145;
	int GALLERY_REQUEST=457;
	Bitmap imageBitmap;
	ImageView editImage;
	ImageView floating;
	//Data d;
	
@Override
protected void onCreate(Bundle savedInstanceState)
{	
	super.onCreate(savedInstanceState);
	setContentView(R.layout.view_activity);
	height=getResources().getDisplayMetrics().heightPixels;
	width=getResources().getDisplayMetrics().widthPixels;
	listView=(ListView)findViewById(R.id.listView);
	floating=(ImageView)findViewById(R.id.fab);
	floating.setOnClickListener(new OnClickListener() 
	{
		
		@Override
		public void onClick(View v)
		{
			startActivity(new Intent(ViewActivity.this,MainActivity.class));	
			finish();
			
		}
	});
	md=new MyDatabase(this);
	List<Data> alldata=md.getListData();
	
	SqliteAdapter adapter=new SqliteAdapter(this,alldata);
	listView.setAdapter(adapter);
}
@Override
public void onBackPressed() 
{
	
	
	AlertDialog.Builder alert=new AlertDialog.Builder(ViewActivity.this);
	alert.setTitle("Are you sure you want to exit..?");
	alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			ViewActivity.this.finish();
			startActivity(new Intent(ViewActivity.this,MainActivity.class));	
			
		}
	} );
   alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) 
		{
			dialog.cancel();
			
		}
	} );
   AlertDialog alertDialog = alert.create();
   alertDialog.show();

}
public class SqliteAdapter extends BaseAdapter
{
	Context context;
	List<Data> alldata;

	public SqliteAdapter(Context context, List<Data> alldata) 
	{
   this.context=context;
   this.alldata=alldata;

	}

	@Override
	public int getCount() 
	{
		
		return alldata.size();
	}

	@Override
	public Object getItem(int position) 
	{
		
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		Holder holder;
		LayoutInflater inflater;
		if(convertView==null)
		{
		    inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.name_activity,null);
			holder=new Holder();
			holder.nameView=(TextView)convertView.findViewById(R.id.textView);
			holder.fulldetails=(Button)convertView.findViewById(R.id.fulldetails);
			
			holder.fulldetails.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					
					final Data d1=alldata.get(position);
					final Dialog customDialog = new Dialog(ViewActivity.this);
					customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					customDialog.setContentView(R.layout.full_details);
					customDialog.show();
					customDialog.setCancelable(false);
					LinearLayout  linear=(LinearLayout)customDialog.findViewById(R.id.linear);
					linear.getLayoutParams().width=width;
					linear.getLayoutParams().height=height/3;
					
					TextView idView=(TextView)customDialog.findViewById(R.id.idtext);
					TextView nameView=(TextView)customDialog.findViewById(R.id.nametext);
					TextView courseView=(TextView)customDialog.findViewById(R.id.coursetext);
					TextView mobileView=(TextView)customDialog.findViewById(R.id.mobiletext);
					ImageView saveImage=(ImageView)customDialog.findViewById(R.id.saveimage);
					idView.setText(d1.getId());
					nameView.setText(d1.getName());
					courseView.setText(d1.getCourse());
					mobileView.setText(d1.getNumber());
					saveImage.setImageBitmap(BitmapFactory.decodeByteArray(d1.getImage(), 0, d1.getImage().length));
					Button dataedit=(Button)customDialog.findViewById(R.id.update);
					Button datadelete=(Button)customDialog.findViewById(R.id.delete);
					Button cancel=(Button)customDialog.findViewById(R.id.cancel);
					cancel.setOnClickListener(new OnClickListener() 
					{
						
						@Override
						public void onClick(View v)
						{
						customDialog.cancel();	
							
						}
					});
					
					datadelete.setOnClickListener(new OnClickListener() 
					{
						
						@Override
						public void onClick(View v) 
						{	
							 AlertDialog.Builder builder = new AlertDialog.Builder(context);
					            builder.setTitle("Delete this item??");
					            builder.create();
					            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
					            {
									
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										md.deleteData(d1.getId());
										((Activity)context).finish();
				                        context.startActivity(((Activity)context).getIntent());										
									}
								} );
					            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
					            {
									
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										dialog.cancel();
										
									}
								});
					            builder.show();
							
						   
						}
					});
					dataedit.setOnClickListener(new OnClickListener() 
					{
						
						
						@Override
						public void onClick(View v)
						{
							customDialog.dismiss();					
						
						    LayoutInflater LF=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						    View subView =LF.inflate(R.layout.temp,null);
//						    LinearLayout  linear1=(LinearLayout)customDialog1.findViewById(R.id.linearedit);
//							linear1.getLayoutParams().width=width;
//							linear1.getLayoutParams().height=height/3;
							final EditText editname=(EditText)subView.findViewById(R.id.nameedit1);
							final EditText editcourse=(EditText)subView.findViewById(R.id.courseedit1);
						    final  EditText editnum=(EditText)subView.findViewById(R.id.numberedit1);
						    final ImageView editcamera=(ImageView)subView.findViewById(R.id.editcamera);
						    final ImageView editgallery=(ImageView)subView.findViewById(R.id.editgallery);
						    editImage=(ImageView)subView.findViewById(R.id.editimageView);
						    editcamera.getLayoutParams().width=width/7;
						    editcamera.getLayoutParams().height=width/7;
						    editgallery.getLayoutParams().width=width/7;
						    editgallery.getLayoutParams().height=width/7;
						    editImage.getLayoutParams().width=width/3;
						    editImage.getLayoutParams().height=height/6;
						    editcamera.setOnClickListener(new OnClickListener() 
						    {
								
								@Override
								public void onClick(View v)
								{
									
									Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					                startActivityForResult(cameraIntent, CAMERA_REQUEST);
								}
							});
						    editgallery.setOnClickListener(new OnClickListener() 
						    {
								
								@Override
								public void onClick(View v) 
								{
								startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
											GALLERY_REQUEST);									
								}
							});
						    
						
							if(d1!=null)
							{
							editname.setText(d1.getName());
							editcourse.setText(d1.getCourse());
							editnum.setText(d1.getNumber());
							editImage.setImageBitmap(BitmapFactory.decodeByteArray(d1.getImage(), 0, d1.getImage().length));
							}
							else
							{
								Toast.makeText(ViewActivity.this, "No Data", Toast.LENGTH_SHORT).show();
							}
							AlertDialog.Builder builder = new AlertDialog.Builder(ViewActivity.this);
						    builder.setTitle("Edit Student Data");
						    builder.setView(subView);
						    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() 
						    {
								
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
									String editname1=editname.getText().toString();
									String editcourse1=editcourse.getText().toString();
									String editnum1=editnum.getText().toString();	
									if(imageBitmap!=null)
									{
									ByteArrayOutputStream out = new ByteArrayOutputStream();
									imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);								
								    byte[] buffer=out.toByteArray();
									md.updateData(new Data(d1.getId(),editname1,editcourse1,editnum1,buffer));
									((Activity)context).finish();
				                    context.startActivity(((Activity)context).getIntent());
									}
									else
									{
										ByteArrayOutputStream out = new ByteArrayOutputStream();
										Bitmap tmp=BitmapFactory.decodeByteArray(d1.getImage(), 0, d1.getImage().length);
										tmp.compress(Bitmap.CompressFormat.PNG, 100, out);								
									    byte[] buffer=out.toByteArray();
										md.updateData(new Data(d1.getId(),editname1,editcourse1,editnum1,buffer));
										((Activity)context).finish();
					                    context.startActivity(((Activity)context).getIntent());
								    }
									
								}
							});
						    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
						    {
						        @Override
						        public void onClick(DialogInterface dialog, int which)
						        {
						            dialog.cancel();
						        }
						    });
						    
						    AlertDialog alertDialog = builder.create();
						    alertDialog.show();
							//Editdetails();
						}
					});
					
				
					
					
				}
			});
			
	        convertView.setTag(holder);

		}
		else
		{
			holder=(Holder)convertView.getTag();
		}
		
		final Data d=alldata.get(position);
		holder.nameView.setText(d.getName());
	
		
		return convertView;
	}
	
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
        editImage.setImageBitmap(imageBitmap);
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
			editImage.setImageBitmap(imageBitmap);
		}
		catch (Exception e)
		{				
			e.printStackTrace();
			Log.e("Image  Error", "****" + e.getMessage());
		}
	}
}
/*private void Editdetails()
{
	
	
	
}*/
public class Holder
{
	TextView nameView;
	Button fulldetails;
	
}
}


