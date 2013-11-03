package com.dephi.basajawa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
	private static final String TAG = "ActivityNewPost";
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
			String destPath = null;
			
			// Deteksi file gambar, jika ya ada gambar maka simpan
			if (!stringAmbilGambar.equals("")) {
				destPath = simpanGambarKeSDCard(stringAmbilGambar);
			}

			// Simpan data ke database
			new DatabaseHelper(this).addPost("" + mIDtoEdit, stringJudul,
					stringIsi, destPath);
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
	private String simpanGambarKeSDCard(String stringAmbilGambar) {
		// Load gambar ke memory
		File gambarAkanDisimpan = new File(stringAmbilGambar);
		
		// Simpan Gambar ke SDCard sesuai path yang dipilih
		String sdcardState = android.os.Environment.getExternalStorageState();
		String destFile = null;
		int indexSepar = stringAmbilGambar.lastIndexOf(File.separator);
		int indexPoint = stringAmbilGambar.lastIndexOf(".");
		if (indexPoint <= 1) {
			indexPoint = stringAmbilGambar.length();
		}
		String fileNameDest = stringAmbilGambar.substring(indexSepar + 1,
				indexPoint);
		String pathDest = null;
		if (sdcardState.contentEquals(android.os.Environment.MEDIA_MOUNTED)) {
			// Set path tempat gambar tersimpan
			pathDest =  android.os.Environment.getExternalStorageDirectory()
					+ File.separator + "KamusBasaJawa";
			new File(pathDest).mkdirs();
			
			destFile = pathDest + File.separator+ fileNameDest + ".png";
		}
		
		File savedImage = new File(destFile);
    	try {
    		if (savedImage.exists()) 
    			savedImage.delete();
    		copyFile(gambarAkanDisimpan, savedImage);
    		
    		Log.d(TAG, "Image saved at: " + destFile);
    		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, 
    				Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
		
    	return destFile;
	}
	
	/**
	 * Copies the sourceFile to destFile
	 * @param sourceFile The source file that contains the 
	 * @param destFile
	 * @throws IOException
	 */
	private void copyFile(File sourceFile, File destFile) throws IOException {
		if (!sourceFile.exists()) {
            return;
        }
		destFile.createNewFile();
		FileChannel source = null;
		FileChannel destination = null;		
		source = new FileInputStream(sourceFile).getChannel();
		destination = new FileOutputStream(destFile).getChannel();
		if (destination != null && source != null) {
			destination.transferFrom(source, 0, source.size());
		}
		if (source != null) {
			source.close();
		}
		if (destination != null) {
			destination.close();
		}
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
