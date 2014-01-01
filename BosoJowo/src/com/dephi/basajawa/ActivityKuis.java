package com.dephi.basajawa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.dephi.basajawa.handler.DatabaseMainHandler;
import com.dephi.basajawa.model.SoalKuis;
import com.dephi.bosojowo.R;
import com.dephi.bosojowo.R.layout;
import com.dephi.bosojowo.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ActivityKuis extends SherlockActivity  {
	private RadioGroup radioGroup;
	private TextView quizQuestion;
	private Button quizJawab;
	
	private ArrayList<HashMap<String, String>> tetawuhan;
	private ArrayList<HashMap<String, String>> tetawuhanArane;
	private ArrayList<SoalKuis> soalsoalKuis;
	private DatabaseMainHandler mDB;
	
	private int _benar = 0, nomorSoalSaatIni = 0;
	private int[] _jawaban;
	private String actionBar_title;
	private static final int MAX_SOAL = 10;
	private static final int MAX_PILIHAN_JAWABAN = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kuis);
		
		// Inisiasi view-view yang ada di layout
		radioGroup = (RadioGroup) findViewById(R.id.RadioGroup1);
		quizQuestion = (TextView) findViewById(R.id.quizQuestion);
		quizJawab = (Button) findViewById(R.id.quizJawab);
		
		// Set OnClickListener untuk view ini
		quizJawab.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ViewHome(v);
			}
		});

		// Inisiasi
		membuatSoalSoalKuisSecaraAcak();
		update(nomorSoalSaatIni);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 5) {
			setResult(5);
			finish();
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Pembuatan ActionBar ada di baris ini
    	new Utilities(this).createActionBarWholeApp(this, menu, actionBar_title);
        return super.onCreateOptionsMenu(menu);
    }
	
	public void ViewHome(View v) {
		// Intent intent = null;
		switch (v.getId()) {
		case R.id.quizJawab:
			String tag = (String) quizJawab.getTag();
			if (tag != null) {
				// Jika sudah selesai, maka penekanan Button ini akan menuju 
				// ke ActivityNilai untuk menampilkan proses berikutnya
				hitungBenar();
				
				Intent nilai = new Intent(ActivityKuis.this, ActivityNilai.class);
				nilai.putExtra("nilai", _benar);
				startActivityForResult(nilai, 0);
			} else {
				// Jika pilihan soal belum habis, maka terus akan memperbarui
				// pilihan jawaban dan menghitung jawaban yg benar
				
				if (hitungBenar()) {
					nomorSoalSaatIni++;
					update(nomorSoalSaatIni);
				} else {
					new Alert().showAlertDialog(this, "harus diisi dulu");
				}
			}
			break;
		}
	}
	
	private void membuatSoalSoalKuisSecaraAcak() {
		// result = mDB.getPostsBySubCat(mSourceID);
		mDB = new DatabaseMainHandler(this);

		/*
		 * Mengambil tema
		 * 
		 * Akan mendapati data seperti ini: 
		 * {_id=1, seekThis=Arane Kembang - Nama Bunga, categoryId=1}, 
		 * {_id=2, seekThis=Arane Pentil - Nama Pentil Buah, categoryId=1}, 
		 * {_id=3, seekThis=Arane Woh - Nama Buah, categoryId=1}, 
		 * {_id=4, seekThis=Arane Isi - Nama Biji, categoryId=1},
		 * {_id=5, seekThis=Arane Godhong - Nama Daun, categoryId=1}, 
		 * {_id=6, seekThis=Arane Wit - Nama Pohon, categoryId=1}
		 */
		int randomTetawuhan = getRandomInt(6);
		tetawuhan = mDB.getSubCat(1);
		Log.v("Jumlah Tetawuhan", "" + tetawuhan.size());
		Log.v("Ambil Random Tetawuhan tsb", "" + randomTetawuhan);
		HashMap<String, String> currentTetawuhan = new HashMap<String, String>();
		currentTetawuhan = tetawuhan.get(randomTetawuhan);
		actionBar_title ="Kuis " + currentTetawuhan.get("seekThis");
		
		/*
		 * Memproses arane-arane
		 * 
		 * Akan mendapati data seperti ini: {picture=asem, subcatId=2, _id=47,
		 * postTwo=Buah Asem yang masih kecil disebut Cempaluk, postOne=Pentil
		 * Asem arane Cempaluk}, {picture=, subcatId=2, _id=48, postTwo=Buah
		 * Jagung yang masih kecil disebut Janten, postOne=Pentil Jagung arane
		 * Janten}, {picture=, subcatId=2, _id=49, postTwo=Buah Jambe yang masih
		 * kecil disebut Bleber, postOne=Pentil Jambe arane Bleber},
		 * {picture=jambu, subcatId=2, _id=50, postTwo=Buah Jambu yang masih
		 * kecil disebut Karuk , postOne=Pentil Jambu arane Karuk}, {picture=,
		 * subcatId=2, _id=51, postTwo=Buah Kacang yang masih kecil disebut
		 * Besengut , postOne=Pentil Kacang arane Besengut}, dst
		 */
		tetawuhanArane = mDB.getPostsBySubCat(Integer.valueOf(currentTetawuhan
				.get("_id")));
		int lengthTetawuhanArane = tetawuhanArane.size();
		String[] statement = new String[lengthTetawuhanArane];
		for (int i = 0; i < lengthTetawuhanArane; i++) {
			HashMap<String, String> currentArane = new HashMap<String, String>();
			currentArane = tetawuhanArane.get(i);
			statement[i] = currentArane.get("postOne");
		}
		Log.v("Jumlah arane tetawuhan", "" + lengthTetawuhanArane);

		/*
		 * Memisahkan menjadi pertanyaan dan jawaban
		 */
		String[] kumpulanPertanyaan = new String[lengthTetawuhanArane];
		String[] kumpulanJawaban = new String[lengthTetawuhanArane];
		for (int i = 0; i < lengthTetawuhanArane; i++) {
			String kalimat = statement[i];
			int posisiKataArane = kalimat.lastIndexOf("arane");
			int panjangKalimat = kalimat.length();

			String kalimatPertanyaan = null;
			String kalimatJawaban = null;
			try {
				kalimatPertanyaan = kalimat.substring(0, posisiKataArane + 5);
				kalimatJawaban = kalimat.substring(posisiKataArane + 6,
						panjangKalimat);
			} catch (StringIndexOutOfBoundsException e) {
				posisiKataArane = -1;
			}

			Log.v("Kalimat", kalimat);
			Log.v("Arane ke-", "" + i);
			Log.v("Posisi Kata Arane", "" + posisiKataArane);
			if (posisiKataArane == -1) {
				kumpulanPertanyaan[i] = null;
				kumpulanJawaban[i] = "Apel";
			} else {
				kumpulanPertanyaan[i] = kalimatPertanyaan;
				kumpulanJawaban[i] = kalimatJawaban;
			}
		}
		Log.v("Jumlah Pertanyaan", "" + kumpulanPertanyaan.length);
		Log.v("Jumlah Jawaban", "" + kumpulanJawaban.length);

		/*
		 * Memulai membuat model kuis untuk 10 soal
		 */
		soalsoalKuis =  new ArrayList<SoalKuis>();
		for (int i = 0; i < MAX_SOAL; i++) {
			SoalKuis soal = new SoalKuis();
			Log.v("Soal Kuis ke-", "" + i);
			
			// Generate Soal
			boolean cariSelamaTidakNull = true;
			String pertanyaan;
			String jawaban;
			do {
				int randomArane = getRandomInt(lengthTetawuhanArane);
				pertanyaan = kumpulanPertanyaan[randomArane];
				jawaban = kumpulanJawaban[randomArane];
				if (pertanyaan != null && !jawaban.equals("Apel")){
					if(isSudahAdaSebelumnya(soalsoalKuis, pertanyaan)){
						cariSelamaTidakNull = true;
					} else {
						cariSelamaTidakNull = false;
					}
				} else {
					cariSelamaTidakNull = true;
				}
			} while (cariSelamaTidakNull);

			// Generate Opsi Jawaban
			String opsiJawaban[] = new String[MAX_PILIHAN_JAWABAN];
			int letakJawaban = getRandomInt(MAX_PILIHAN_JAWABAN);
			int[] tagJawaban = new int[MAX_PILIHAN_JAWABAN];
			for (int j = 0; j < MAX_PILIHAN_JAWABAN; j++) {
				Log.v("=====================", "=====================");
				Log.v("Opsi Jawaban", opsiJawaban[0] + "," + opsiJawaban[1] + "," + opsiJawaban[2] + "," + opsiJawaban[3]);
				boolean cariSelamaTidakSama = true;
				int randomUntukJawabanAcak;
				String jawabanAcak;
				do {
					randomUntukJawabanAcak = getRandomInt(lengthTetawuhanArane);
					jawabanAcak = kumpulanJawaban[randomUntukJawabanAcak];
					Log.v("Jawaban acak yg terpilih", jawabanAcak);
					if(isSudahAdaSebelumnya(opsiJawaban, jawabanAcak, jawaban)){
						cariSelamaTidakSama = true;
					} else {
						cariSelamaTidakSama = false;
					}
				} while (cariSelamaTidakSama);
				
				if (j == letakJawaban) {
					opsiJawaban[j] = jawaban;
					tagJawaban[j] = 1;
				} else {
					opsiJawaban[j] = jawabanAcak;
					tagJawaban[j] = 0;
				}
			}
			Log.v("=====================", "=====================");
			soal.setSoalNomor(i + 1);
			soal.setPertanyaan(pertanyaan);
			soal.setOpsi(opsiJawaban);
			soal.setJawaban(tagJawaban);

			soalsoalKuis.add(soal);
		}
	}
	
	private boolean isSudahAdaSebelumnya(ArrayList<SoalKuis> soalsoalKuis2,
			String pertanyaan) {
		int length = soalsoalKuis2.size();
		for(int i = 0; i < length; i++){
			SoalKuis soal = soalsoalKuis2.get(i);
			if(soal.getPertanyaan().equals(pertanyaan)) return true;
		}
		return false;
	}

	private boolean isSudahAdaSebelumnya(String[] opsiJawaban,
			String jawabanAcak, String jawaban) {
		int length = opsiJawaban.length;
		if(jawabanAcak.equalsIgnoreCase(jawaban)){
			Log.v("Sama dengan jawaban!!! ulangi lagi", "Sama dengan jawaban!!! ulangi lagi");
			return true;
		}
		for(int i = 0; i < length; i++){
			if(opsiJawaban[i] != null){
				if(opsiJawaban[i].equalsIgnoreCase(jawabanAcak)){
					Log.v("Sudah ada!!! ulangi lagi", "Sudah ada!!! ulangi lagi");
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Menghasilkan angka acak
	 * @param maxInt
	 * @return
	 */
	private int getRandomInt(int maxInt){
		//Log.v("Pengolahan angka random", "******************************************");
		//Log.v("Integer Maksimal", "" + maxInt);
		int faktorKali = 10;
		if(maxInt > 10 && maxInt <= 100) faktorKali = 100;
		//Log.v("Faktor Pengali", "" + faktorKali);
		int targetNumber = (int) (Math.round(Math.random()* faktorKali)); 
		//Log.v("Angka Acak yang tercipta", "" + targetNumber);
		if(targetNumber != 0) targetNumber = targetNumber - 1;
		if(targetNumber >= maxInt){
			targetNumber = (targetNumber % maxInt);
			//if(targetNumber >= maxInt) targetNumber = targetNumber - 1;
		}
		//Log.v("Angka Acak yang telah dimodif", "" + targetNumber);
		//Log.v("Selesai", "******************************************");
		return targetNumber;
	}
	
	/**
	 * Mengupdate isi dari tiap soal, terdiri dari Judul dan Isi Soal. Fungsi
	 * ini diupdate berdasarkan database lokal yang sudah diambil dari server.
	 */
	private void update(int currentQuiz) {
		// Mengupdate judul
		SoalKuis soal = soalsoalKuis.get(currentQuiz);

		// Mengupdate judul soal
		quizQuestion.setText(""+soal.getSoalNomor()+". "+soal.getPertanyaan());

		// Mengupdate pilihan soal
		setTextRadioButton(soal.getOpsi());

		// Mengupdate jawaban
		_jawaban = soal.getJawaban();

		// Mengupdate tombol
		if (soalsoalKuis.size() == currentQuiz+1) {
			quizJawab.setText("Finish");
			quizJawab.setTag("Finish");
		}
	}
	
	/**
	 * Fungsi yang digunakan untuk memperbarui text di pilihan jawaban
	 * @param pilihan String array dari pilihan jawaban
	 */
	private void setTextRadioButton(String[] pilihan) {
		int counts = pilihan.length;
		for (int i = 0; i < counts; i++) {
			((RadioButton) radioGroup.getChildAt(i)).setText(pilihan[i]);
		}
	}
	
	/**
	 * Menghitung jumlah jawaban yang benar
	 * @return
	 */
	private boolean hitungBenar() {
		boolean status;
		int id = radioGroup.getCheckedRadioButtonId();
		if (id == -1) {
			status = false;
		} else {
			switch (id) {
			case R.id.radioButton1:
				if (checkJawaban(0))
					_benar++;
				break;
			case R.id.radioButton2:
				if (checkJawaban(1))
					_benar++;
				break;
			case R.id.radioButton3:
				if (checkJawaban(2))
					_benar++;
				break;
			case R.id.radioButton4:
				if (checkJawaban(3))
					_benar++;
				break;
			default:
				break;
			}
			radioGroup.clearCheck();
			status = true;
		}
		return status;
	}
	
	private boolean checkJawaban(int posisiYangDipilih){
		int jawabanAdaDiPosisi = 0;
		for(int i = 0; i < _jawaban.length; i++){
			if(_jawaban[i] == 1){
				jawabanAdaDiPosisi = i;
				break;
			}
		}
		
		if(jawabanAdaDiPosisi == posisiYangDipilih) return true;
		else return false;
	}
}
