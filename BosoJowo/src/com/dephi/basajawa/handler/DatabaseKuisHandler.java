package com.dephi.basajawa.handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dephi.basajawa.model.SoalKuis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Digunakan untuk pemrosesan database keseluruhan pada applikasi ini. Semua
 * proses meliputi pemanggilan, penyimpanan atau pencarian ada dalam class ini.
 * 
 * @author Dephi
 * 
 */
public class DatabaseKuisHandler extends SQLiteOpenHelper {

	// Parameters for this database
	private static final int DATABASE_VERSION = 1; // Database Version
	private static final String DATABASE_NAME = "Kuis"; // Database Name
	private static final String TABLE_PERTANYAAN = "pertanyaan"; // Database Table
	private static final String TABLE_OPSI = "opsi"; // Database Table

	// Coloumns untuk Tabel Pertanyaan
	private static final String PERTANYAAN_ID = "id_pertanyaan";
	private static final String PERTANYAAN_NAME = "nama_pertanyaan";

	// Coloumns untuk Tabel Opsi
	private static final String OPSI_ID = "id_opsi";
	private static final String OPSI_SOAL = "opsi";
	private static final String OPSI_TAG = "TAG";

	public DatabaseKuisHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_PERTANYAAN = "CREATE TABLE " + TABLE_PERTANYAAN + "("
				+ PERTANYAAN_ID + " INTEGER PRIMARY KEY," 
				+ PERTANYAAN_NAME+ " TEXT" + ")";
		
		String CREATE_TABLE_OPSI = "CREATE TABLE " + TABLE_OPSI + "("
				+ OPSI_ID + " INTEGER PRIMARY KEY," 
				+ PERTANYAAN_ID + " INTEGER," 
				+ OPSI_SOAL + " TEXT," 
				+ OPSI_TAG + " INTEGER" + ")";

		db.execSQL(CREATE_TABLE_PERTANYAAN);
		db.execSQL(CREATE_TABLE_OPSI);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERTANYAAN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPSI);

		// Create tables again
		onCreate(db);
	}

	private void createPertanyaanTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		String CREATE_TABLE_PERTANYAAN = "CREATE TABLE " + TABLE_PERTANYAAN + "("
				+ PERTANYAAN_ID + " INTEGER PRIMARY KEY," + PERTANYAAN_NAME
				+ " TEXT" + ")";

		db.execSQL(CREATE_TABLE_PERTANYAAN);
		db.close();
	}

	private void createOpsiTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		String CREATE_TABLE_OPSI = "CREATE TABLE " + TABLE_OPSI + "("
				+ OPSI_ID + " INTEGER PRIMARY KEY," 
				+ PERTANYAAN_ID + " INTEGER," 
				+ OPSI_SOAL + " TEXT," 
				+ OPSI_TAG + " INTEGER" + ")";

		db.execSQL(CREATE_TABLE_OPSI);
		db.close();
	}

	public void dropPertanyaanTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERTANYAAN);
		db.close();

		createPertanyaanTable();
	}
	
	public void dropOpsiTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPSI);
		db.close();

		createOpsiTable();
	}
	
	/**
	 * Mengambil semua soal dari database
	 */
	public List<SoalKuis> getAllSoalKuis() {
		ArrayList<SoalKuis> AllSoalKuis = new ArrayList<SoalKuis>();
		
		// Sets local variable
		int jumlahSoal;
		String soal = null;
		String[] pilihan = new String[4];
		int[] jawaban = new int[4];
		
		// Untuk ngambil semua pertanyaan
		jumlahSoal = getJumlahSoalKeseluruhan();
		for(int i = 0; i < jumlahSoal; i++){
			SoalKuis satuSoalKuis = new SoalKuis();
			
			// Ambil pertanyaan nomer ke-i kuis
			soal = getPertanyaanKe(i);
			
			// Ambil pilihan jawaban dari pertanyaan nomer ke-i kuis
			pilihan = getOpsiJawabanDariPertanyaanKe(i);
			
			// Ambil tag jawaban dari pertanyaan nomer ke-1 kuis
			jawaban = getTagJawaban(i);
			
			// Set ke dalam model SoalKuis
			satuSoalKuis.setSoalNomor(i);
			satuSoalKuis.setPertanyaan(soal);
			satuSoalKuis.setOpsi(pilihan);
			satuSoalKuis.setJawaban(jawaban);
			
			AllSoalKuis.add(satuSoalKuis);
		}
		return AllSoalKuis;
	}
	
	private int getJumlahSoalKeseluruhan() {
		SQLiteDatabase db = this.getReadableDatabase();
		String countQuery = "SELECT  * FROM " + TABLE_PERTANYAAN;
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		db.close();
		return count;
	}
	
	private String getPertanyaanKe(int i) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PERTANYAAN, new String[] { PERTANYAAN_ID,
				PERTANYAAN_NAME, }, PERTANYAAN_ID + "=?",
				new String[] { String.valueOf(i) }, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		db.close();
		
		// return Pertanyaan
		return cursor.getString(1);
	}
	
	private String[] getOpsiJawabanDariPertanyaanKe(int id) {
		Cursor cursor = getCursorForOPSItable(id);
		
		// looping through all rows and adding to list
		String[] opsi = new String[cursor.getCount()];
		int i = 0;
		if (cursor.moveToFirst()) {
			do {
				opsi[i] = cursor.getString(2);
				i++;
			} while (cursor.moveToNext());
		}
		
		// return tagJawaban
		return opsi;
	}
	
	private int[] getTagJawaban(int id) {
		Cursor cursor = getCursorForOPSItable(id);
		
		// looping through all rows and adding to list
		int[] tags = new int[cursor.getCount()];
		int i = 0;
		if (cursor.moveToFirst()) {
			do {
				tags[i] = cursor.getInt(3);
				i++;
			} while (cursor.moveToNext());
		}
		
		// return tagJawaban
		return tags;
	}

	private Cursor getCursorForOPSItable(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_OPSI, new String[] { OPSI_ID, PERTANYAAN_ID,
				OPSI_SOAL, OPSI_TAG}, PERTANYAAN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		db.close();
		
		return cursor;
	}
}
