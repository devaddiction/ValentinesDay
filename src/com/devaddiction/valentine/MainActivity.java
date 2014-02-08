package com.devaddiction.valentine;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class MainActivity extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.setImageSelector();
	}

	private void setImageSelector() {
		Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
		buttonLoadImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			Intent intent = new Intent(this, DisplayImageNamesActivity.class);

			EditText yourNameText = (EditText) findViewById(R.id.yourName);
			EditText hisHerNameText = (EditText) findViewById(R.id.hisHerName);

			DatePicker datingDate = (DatePicker) findViewById(R.id.dateMeet);

			intent.putExtra("yourName", yourNameText.getText().toString());
			intent.putExtra("hisHerName", hisHerNameText.getText().toString());
			intent.putExtra("datingDate", getDateFromDatePicker(datingDate));
			intent.putExtra("picturePath", picturePath);

			startActivity(intent);
		}
	}

	public static String getDateFromDatePicker(DatePicker datePicker) {
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		SimpleDateFormat formattedDate = new SimpleDateFormat("MM-dd-yyyy");
		return formattedDate.format(calendar.getTime());
	}
}