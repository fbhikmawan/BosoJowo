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
		SQLiteDatabase db = this.getReadableDatabase();

		// Sets local variable
		String soal = null;
		String[] pilihan = new String[4];
		

		// Untuk ngambil pertanyaan
		Cursor cursor = db.query(TABLE_PERTANYAAN, new String[] { PERTANYAAN_ID, PERTANYAAN_NAME}, 
				PERTANYAAN_ID + "=?", new String[] { String.valueOf(id_pertanyaan) }, null, null, null);
		if (cursor != null) {
			totalsoal = cursor.getCount();
			cursor.moveToPosition(question_number - 1);
			soal = cursor.getString(1);
		}
		cursor.close();

		// Untuk ngambil pilihan2
		Cursor cursor2 = db.query(
				TABLE_ANSWER,
				new String[] { ANSWER_ID, ANSWER_ANS, ANSWER_STATUS,
						QUESTION_ID, PERTANYAAN_ID },
				PERTANYAAN_ID + "=? AND " + QUESTION_ID + "=?",
				new String[] { String.valueOf(id_pertanyaan),
						String.valueOf(question_number) }, null, null, null,
				null);
		i = 0;
		if (cursor2.moveToFirst()) {
			do {
				pilihan[i] = cursor2.getString(1);
				i++;
			} while (cursor2.moveToNext());
		}
		cursor2.close();

		// Untuk ngambil jawaban
		Cursor cursor3 = db.query(
				TABLE_ANSWER,
				new String[] { ANSWER_ID, ANSWER_ANS, ANSWER_STATUS,
						QUESTION_ID, PERTANYAAN_ID },
				PERTANYAAN_ID + "=? AND " + QUESTION_ID + "=?",
				new String[] { String.valueOf(id_pertanyaan),
						String.valueOf(question_number) }, null, null, null,
				null);
		i = 0;
		int status = 0;
		if (cursor3.moveToFirst()) {
			do {
				status = cursor3.getInt(2);
				if (status == 1) {
					jawaban = i;
					break;
				}
				i++;
			} while (cursor3.moveToNext());
		}
		cursor3.close();

		SoalKuis soalKuis = new SoalKuis(question_number, soal, pilihan,
				jawaban, totalsoal);
		db.close();

		// return Pertanyaan
		return soalKuis;
	}
	
	

	/**
	 * ** UNTUK PERTANYAAN **
	 */
	// Masukkan satu Pertanyaan dalam database
	public void addPertanyaan(String... arg0) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PERTANYAAN_NAME, arg0[0]);

		// Inserting Row
		db.insert(TABLE_PERTANYAAN, null, values);
		db.close(); // Closing database connection
	}
	
	// Ambil satu pertanyaan berdasatkan id
	public String getPertanyaan(int id_pertanyaan) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PERTANYAAN, new String[] { PERTANYAAN_ID,
				PERTANYAAN_NAME, }, PERTANYAAN_ID + "=?",
				new String[] { String.valueOf(id_pertanyaan) }, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		db.close();
		
		// return Pertanyaan
		return cursor.getString(1);
	}

	// Ambil Semua Pertanyaan
	public String[] getAllPertanyaan() {
		
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PERTANYAAN;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		String[] pertanyaanList = new String[cursor.getCount()];
		int i=0;
		if (cursor.moveToFirst()) {
			do {
				pertanyaanList[i] = cursor.getString(1);
				i++;
			} while (cursor.moveToNext());
		}
		db.close();
		// return Pertanyaan list
		return pertanyaanList;
	}
	/**
	 * ** SELESAI PERTANYAAN **
	 */

	/**
	 * ** UNTUK QUESTION **
	 */
	// Masukkan satu Question dalam database
	public void addQuestion(String... arg0) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(QUESTION_SOAL, arg0[0]);
		values.put(PERTANYAAN_ID, arg0[1]);

		// Inserting Row
		db.insert(TABLE_OPSI, null, values);
		db.close(); // Closing database connection
	}

	// Ambil Jumlah Pertanyaan
	public int getJumlahQuestions() {
		SQLiteDatabase db = this.getReadableDatabase();
		String countQuery = "SELECT  * FROM " + TABLE_OPSI;
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		db.close();

		// return count
		return count;
	}

	/**
	 * ** SELESAI QUESTION **
	 */

	/**
	 * ** UNTUK ANSWER **
	 */
	// Masukkan satu Question dalam database
	public void addAnswer(String... arg0) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ANSWER_ANS, arg0[0]);
		values.put(ANSWER_STATUS, arg0[1]);
		values.put(QUESTION_ID, arg0[2]);
		values.put(PERTANYAAN_ID, arg0[3]);

		// Inserting Row
		db.insert(TABLE_ANSWER, null, values);
		db.close(); // Closing database connection
	}

	// Ambil Jumlah Pertanyaan
	public int getJumlahAnswer() {
		SQLiteDatabase db = this.getReadableDatabase();
		String countQuery = "SELECT  * FROM " + TABLE_ANSWER;
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		db.close();

		// return count
		return count;
	}

	/**
	 * ** SELESAI QUESTION **
	 */

	
}
