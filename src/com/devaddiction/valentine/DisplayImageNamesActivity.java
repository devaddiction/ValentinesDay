package com.devaddiction.valentine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayImageNamesActivity extends Activity {

	MediaPlayer mPlayer;
	static String imageUri = Environment.getExternalStorageDirectory()
			+ "/screenshot.png";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_image_names);

		this.populateDisplay();
	}

	private void populateDisplay() {
		Intent intent = getIntent();

		this.populateImage(intent);
		this.populateNames(intent);
		this.populateDate(intent);

		this.startMusic();
		this.displayToast();
	}

	private void displayToast() {
		String message = getResources().getString(R.string.toast_send);

		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
				.show();
	}

	private void startMusic() {
		mPlayer = MediaPlayer.create(DisplayImageNamesActivity.this,
				R.raw.sound_music);
		mPlayer.start();
	}

	private void populateDate(Intent intent) {
		String datingDate = intent.getStringExtra("datingDate");

		TextView datingDateView = (TextView) findViewById(R.id.datingDate);
		datingDateView.setText(datingDate);
	}

	public void onDestroy() {
		mPlayer.stop();
		File image = new File(imageUri);
		image.delete();
		
		super.onDestroy();

	}

	private void populateNames(Intent intent) {
		String yourName = intent.getStringExtra("yourName");
		String hisHerName = intent.getStringExtra("hisHerName");

		TextView yourNameView = (TextView) findViewById(R.id.yourName);
		yourNameView.setText(yourName);

		TextView hisHerNameView = (TextView) findViewById(R.id.hisHerName);
		hisHerNameView.setText(hisHerName);
	}

	private void populateImage(Intent intent) {
		String picturePath = intent.getStringExtra("picturePath");

		ImageView imageView = (ImageView) findViewById(R.id.image);
		imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

		imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Bitmap bitmap = takeScreenshot();
				saveBitmap(bitmap);
				sendViaEmail();
			}
		});
	}

	private void sendViaEmail() {
		File image = new File(imageUri);
		Uri imageuri = Uri.fromFile(image);

		Intent send_img = new Intent(Intent.ACTION_SEND);
		send_img.putExtra(Intent.EXTRA_EMAIL, "");
		send_img.putExtra(Intent.EXTRA_SUBJECT, "");
		send_img.putExtra(Intent.EXTRA_STREAM, imageuri);
		send_img.putExtra(Intent.EXTRA_TEXT, "message");
		send_img.setType("text/plain");
		send_img.setType("image/png");
		startActivity(Intent.createChooser(send_img, "Send Email"));
	}

	private Bitmap takeScreenshot() {
		View rootView = findViewById(android.R.id.content).getRootView();
		rootView.setDrawingCacheEnabled(true);
		return rootView.getDrawingCache();
	}

	public void saveBitmap(Bitmap bitmap) {
		File imagePath = new File(imageUri);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(imagePath);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e("GREC", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("GREC", e.getMessage(), e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_image_names, menu);
		return true;
	}

}
