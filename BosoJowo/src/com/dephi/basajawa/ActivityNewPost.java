package com.dephi.basajawa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.dephi.bosojowo.R;

/**
 * Acivity ini akan membentuk entry baru pada database sesuai dengan isian
 * yang disediakan. Mengenai data isian tersebut, lihat layout 
 * "activity_new_post.xml". <br> <br>
 * 
 * @author Dephi
 */
public class ActivityNewPost extends SherlockActivity {
	private int mIDtoEdit;
	private static final int REQ_CODE_CAMERA = 777;
	
	/**
     * Pengeksekusian selalu dilakukan disini terlebih dulu
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_post);
		
		// Ambil Intent Ektra dari pemanggilan sebelumnya
		mIDtoEdit = getIntent().getIntExtra("ID", 0);
		
		// Inisiasi elemen widget pada layout
		Button buttonAmbilGambar = (Button) findViewById(R.id.btnPilih);
		Button buttonSave = (Button) findViewById(R.id.btnSave);
		
		// Memberikan action pada tombol "Ambil Gambar"
		buttonAmbilGambar.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/png");
				startActivityForResult(photoPickerIntent, REQ_CODE_CAMERA);
			}
		});
		
		// Memberikan action pada tombol "Save"
		buttonSave.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				prosesMenyimpanEntryKeDatabase();
				Toast.makeText(ActivityNewPost.this, "New Entry has been saved", 
						Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Mengambil resources judul yang nanti digunakan pada action bar
		Resources res = getResources();
		String title = res.getString(R.string.title_new_post);
		
		// Pembuatan ActionBar ada di baris ini
		new Utilities(this).createActionBarEmpty(this, menu, title);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Menyimpan entry baru hasil masukan ke dalam database
	 */
	private void prosesMenyimpanEntryKeDatabase() {
		// Inisiasi elemen widget pada layout yang diperlukan untuk diambil
		// datanya sebagai entry baru.
		EditText etJudul = (EditText) this.findViewById(R.id.etJudul);
		EditText etIsi = (EditText) this.findViewById(R.id.etIsi);
		EditText etAmbilGambar = (EditText) this.findViewById(R.id.etAmbilGambar);
		
		// Mengambil string dari edittext yang ada di layar
		String stringJudul = etJudul.getText().toString();
		String stringIsi = etIsi.getText().toString();
		String stringAmbilGambar = etAmbilGambar.getText().toString();
		
		if(!stringJudul.equals("")){
			// Deteksi file gambar, jika ya ada gambar maka simpan
			if (!stringAmbilGambar.equals("")) {
				simpanGambarKeSDCard(stringAmbilGambar);
			}

			// Simpan data ke database
			new DatabaseHelper(this).addPost("" + mIDtoEdit, stringJudul,
					stringIsi, stringAmbilGambar);
			finish();
		} else {
			Toast.makeText(this, "Baris Jowo harus diisi", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	/**
	 * Menyimpan gambar yang telah dipilih ke dalam SDCard.
	 * Path yang dipakai bergantung pada konfigurasi OS Android yg dipakai,
	 * namun pada umumnya adalah: <br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<i>\sdcard\KamusBasaJawa\</i> <br>
	 * dengan format gambar PNG.
	 */
	private void simpanGambarKeSDCard(String stringAmbilGambar) {
		// Load gambar ke memory, parameter-parameter berikut untuk
		// optimasi memory sehingga tidak terjadi overload.
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inDither = false;
		opt.inScaled = false;
		opt.inDensity = 0;
		opt.inJustDecodeBounds = false;
		opt.inPurgeable = false;
		opt.inSampleSize = 1;
		opt.inScreenDensity = 0;
		opt.inTargetDensity = 0;
		Bitmap gambarAkanDisimpan = BitmapFactory.decodeFile(stringAmbilGambar,
				opt);

		// Simpan Gambar ke SDCard sesuai path yang dipilih
		String sdcardState = android.os.Environment.getExternalStorageState();
		String destPath = null;
		int indexSepar = stringAmbilGambar.lastIndexOf(File.separator);
		int indexPoint = stringAmbilGambar.lastIndexOf(".");
		if (indexPoint <= 1) {
			indexPoint = stringAmbilGambar.length();
		}
		String fileNameDest = stringAmbilGambar.substring(indexSepar + 1,
				indexPoint);
		if (sdcardState.contentEquals(android.os.Environment.MEDIA_MOUNTED)) {
			// Set path tempat gambar tersimpan
			destPath = android.os.Environment.getExternalStorageDirectory()
					+ File.separator + "KamusBasaJawa" + File.separator
					+ fileNameDest + ".png";
		}
		OutputStream fout = null;
		try {
			fout = new FileOutputStream(destPath);
			gambarAkanDisimpan.compress(Bitmap.CompressFormat.PNG, 100, fout);
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.parse("file://" + destPath)));
			fout.flush();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gambarAkanDisimpan.recycle();
	}
	
	/**
	 * Bagian ini hanya dipanggil saat user memilih untuk mengambil gambar 
	 * entry baru. Bagian yang diambil dari proses ini adalah string path
	 * dari gambar tersebut.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case (REQ_CODE_CAMERA):
			if (resultCode == RESULT_OK) {
				Uri photoUri = intent.getData();
				if (photoUri != null) {
					try {
						EditText etAmbilGambar = (EditText) this
								.findViewById(R.id.etAmbilGambar);

						Cursor cursor = getContentResolver().query(photoUri,
								null, null, null, null);
						cursor.moveToFirst();

						int idx = cursor
								.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
						etAmbilGambar.setText(cursor.getString(idx));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			break;
		}
	}
}
