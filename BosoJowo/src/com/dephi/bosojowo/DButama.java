package com.dephi.bosojowo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DButama extends SQLiteOpenHelper {

	private static final String TAG = DButama.class.getSimpleName();

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/com.satya.SPISEVEN/databases/";
	private static String DB_NAME = "SPI7";
	private SQLiteDatabase db;

	static String TABLE_NAME = "operator";
	static String ROW_IDOP = "id_Op";
	static String ROW_NAMEOP = "name_Op";
	static String ROW_CODEOP = "code_Op";
	static String ROW_KARTUOP = "kartu_Op";

	static String TABLE_NAME1 = "nominal";
	static String ROW_IDNOM = "id_nom";
	static String ROW_NAMENOM = "name_nom";
	static String ROW_CODENOM = "code_nom";

	static String TABLE_NAME2 = "waktu";
	static String ROW_IDTGL = "id_waktu";
	static String ROW_TGL = "tanggal";
	static String ROW_NOCENTER = "nocenter";
	static String ROW_ISIPESAN = "isipesan";

	static String TABLE_NAME3 = "creden";
	static String ROW_CENTER = "center";
	static String ROW_APPPASS = "appPass";
	static String ROW_TRANSPASS = "transPass";

	static String TABLE_NAME4 = "token";
	static String ROW_IDTOKEN = "id_token";
	static String ROW_NAMETOKEN = "name_token";
	static String ROW_CODETOKEN = "nom_token";

	private final Context myContext;

	// Constructor dengan parameter context untuk meng-access assets and
	// resources.
	public DButama(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		try {
			createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Creates new database from the assets, but check the before
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase(); // Cek dulu apakah DB sudah ada?
		if (dbExist) {
			// Database sudah ada, tidak perlu dilakukan apa2
		} else {
			// Database TIDAK ADA, melakukan copy DB dari asset ke folder app
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error(e);
			}
		}
	}

	/**
	 * Memeriksa keberadaan database
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			// Perintah pengecekan dengan cara membuka, jika tidak ada maka
			// masuk ke catch SQLiteException
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			// tidak perlu dilakukan apa2
		}
		if (checkDB != null) {
			System.out.println("Database already exist");
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open("SPI7.sqlite");

		// Path to the just created empty db
		String outFile = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFile);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		System.out.println("Database opened successfull");
	}

	/**
	 * Gets the Operator Code based on the Operator
	 * 
	 * @param name_Op
	 *            Operator name
	 * @param code_Op
	 * @return
	 */
	public String getKodeOperator(String name_Op) {
		openDataBase();
		String code_Op = null;
		Cursor c = db.rawQuery("SELECT * FROM operator WHERE name_Op='"
				+ name_Op + "'", null);

		if (c.moveToFirst()) {
			code_Op = c.getString(2);
			System.out.println("nama operator: " + name_Op + ", kode: "
					+ code_Op);
		}
		db.close();
		c.close();
		return code_Op;
	}

	public String getKartuOperator(String name_Op) {
		openDataBase();
		String kartu_Op = null;
		Cursor c = db.rawQuery("SELECT * FROM operator WHERE name_Op='"
				+ name_Op + "'", null);

		if (c.moveToFirst()) {
			kartu_Op = c.getString(3);
			System.out.println("nama operator: " + name_Op + ", kode: "
					+ kartu_Op);
		}
		db.close();
		c.close();
		return kartu_Op;
	}

	public String getKodeNominal(String name_nom) {
		openDataBase();
		String code_nom = null;
		Cursor c = db.rawQuery("select * from nominal where name_nom='"
				+ name_nom + "'", null);

		if (c.moveToFirst()) {
			code_nom = c.getString(2);
			System.out.println("nominal: " + code_nom);
		}

		db.close();
		c.close();
		return code_nom;
	}

	public String getToken(String name_token) {
		openDataBase();
		String nom_token = null;
		Cursor c = db.rawQuery("select * from token where name_token='"
				+ name_token + "'", null);

		if (c.moveToFirst()) {
			nom_token = c.getString(2);
			System.out.println("nominal: " + nom_token);
		}

		db.close();
		c.close();
		return nom_token;
	}

	public String[] getCredential() {
		openDataBase();
		String[] credential = new String[3];
		Cursor c = db.rawQuery("SELECT * FROM creden", null);

		if (c.moveToFirst()) {
			credential[0] = c.getString(1);
			credential[1] = c.getString(2);
			credential[2] = c.getString(3);
			System.out.println("NoCenter: " + credential[0]
					+ ", App Password: " + credential[1]
					+ ", Transaksi Password" + credential[2]);
		}
		db.close();
		c.close();
		return credential;
	}

	// Gets the list of all operator
	public String[] getStringAllOperator() {
		openDataBase();
		String[] opName = null;
		Cursor c;
		try {
			c = db.query(TABLE_NAME, new String[] { ROW_IDOP, ROW_NAMEOP,
					ROW_CODEOP }, null, null, null, null, null);
			opName = new String[c.getCount()];
			c.moveToFirst();
			if (!c.isAfterLast()) {
				for (int i = 0; i < c.getCount(); i++) {
					opName[i] = c.getString(1);
					c.moveToNext();
					db.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
			Toast.makeText(myContext,
					"gagal ambil semua baris:" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}

		db.close();
		return opName;
	}

	// Gets the list of all nominal
	public String[] getStringAllNom() {
		openDataBase();
		String[] nominal = null;
		Cursor c;
		try {
			c = db.query(TABLE_NAME1, new String[] { ROW_IDNOM, ROW_NAMENOM,
					ROW_CODENOM }, null, null, null, null, null);
			nominal = new String[c.getCount()];
			c.moveToFirst();
			if (!c.isAfterLast()) {
				for (int i = 0; i < c.getCount(); i++) {
					nominal[i] = c.getString(1);
					c.moveToNext();
					db.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
		db.close();
		return nominal;
	}

	public String[] getStringAllToken() {
		openDataBase();
		String[] token = null;
		Cursor c;
		try {
			c = db.query(TABLE_NAME4, new String[] { ROW_IDTOKEN,
					ROW_NAMETOKEN, ROW_CODETOKEN }, null, null, null, null,
					null);
			token = new String[c.getCount()];
			c.moveToFirst();
			if (!c.isAfterLast()) {
				for (int i = 0; i < c.getCount(); i++) {
					token[i] = c.getString(1);
					c.moveToNext();
					c.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
		db.close();
		return token;
	}

	// Update Credential
	public void updateCredential(String[] credential) {
		openDataBase();
		try {
			String updatesql = "UPDATE creden SET center='" + credential[0]
					+ "', appPass='" + credential[1] + "', transPass='"
					+ credential[2] + "'";
			db.execSQL(updatesql);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
		db.close();
	}

	// Update Kode Operator
	public void updateKodeOperator(String[] operator) {
		openDataBase();
		try {
			String updatesql = "UPDATE creden SET code_Op='" + operator[1];
			db.execSQL(updatesql);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
		db.close();
	}

	// menambah data ditiap baris
	public long addRow(int id_Op, String name_Op, String code_Op) {
		openDataBase();
		ContentValues cv = new ContentValues();
		cv.put(ROW_IDOP, id_Op);
		cv.put(ROW_NAMEOP, name_Op);
		cv.put(ROW_CODEOP, code_Op);

		try {
			db.insert("operator", null, cv);
		} catch (Exception e) {
			Log.e("error", e.toString());
			e.printStackTrace();
		}
		db.close();
		return 0;
	}

	// proses menambah data
	public void insert_data(String name_Op, String code_Op) {
		openDataBase();
		String insertsql = "INSERT INTO operator (name_Op, code_Op) "
				+ "VALUES ('" + name_Op + "' , '" + code_Op + "')";
		System.out.println("cccccccccc-" + insertsql);
		db.execSQL(insertsql);
		db.close();
	}

	// proses menghapus data

	public void delete_data(String name_Op) {
		openDataBase();
		String deletesql = "DELETE FROM operator WHERE name_Op='" + name_Op
				+ "'";
		db.execSQL(deletesql);
		db.close();
	}

	// proses meng-edit data
	public void update_data(String name_Op, String code_Op) {
		openDataBase();
		String checkData = "select * from operator where name_Op='" + name_Op
				+ "'";
		Cursor cursor = db.rawQuery(checkData, null);
		if (cursor.moveToFirst()) {
			String updatesql = "UPDATE operator SET code_Op='" + code_Op
					+ "' WHERE name_Op='" + name_Op + "'";
			db.execSQL(updatesql);
		} else {
			String insertsql = "INSERT INTO operator (name_Op, code_Op) "
					+ "VALUES ( '" + name_Op + "' , '" + code_Op + "')";
			db.execSQL(insertsql);
		}
		db.close();
	}

	// Gets the list of all operator
	public ArrayList<ArrayList<Object>> getAllOperator() {
		ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
		Cursor c;
		try {
			c = db.query(TABLE_NAME, new String[] { ROW_IDOP, ROW_NAMEOP,
					ROW_CODEOP }, null, null, null, null, null);
			c.moveToFirst();
			if (!c.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();
					dataList.add(c.getLong(0));
					dataList.add(c.getString(1));
					dataList.add(c.getString(2));
					dataArray.add(dataList);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
			Toast.makeText(myContext,
					"gagal ambil semua baris:" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}
		return dataArray;
	}

	// ambil sebuah baris
	public ArrayList<Object> ambilBaris() {
		// TODO Auto-generated method stub

		ArrayList<Object> arrbaris = new ArrayList<Object>();
		Cursor c;
		try {
			c = db.query(TABLE_NAME, new String[] { ROW_IDOP, ROW_NAMEOP,
					ROW_CODEOP }, ROW_IDOP + "=" + ROW_IDOP, null, null, null,
					null, null);

			c.moveToFirst();
			if (!c.isAfterLast()) {
				do {
					arrbaris.add(c.getLong(0));
					arrbaris.add(c.getString(1));
					arrbaris.add(c.getString(2));
					System.out.println("data: " + arrbaris);
				} while (c.moveToNext());
				String r = String.valueOf(arrbaris);
				Toast.makeText(myContext, "haha" + r, Toast.LENGTH_SHORT)
						.show();
			}
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("error", e.toString());
			Toast.makeText(myContext, "hhii" + e.toString(), Toast.LENGTH_SHORT)
					.show();
		}

		return arrbaris;
	}

	// *END PROSES FOR OPERATOR*\\

	// menambah data ditiap baris
	public long addRownom(int id_nom, String name_nom, String code_nom) {

		openDataBase();
		ContentValues cv = new ContentValues();
		cv.put(ROW_IDNOM, id_nom);
		cv.put(ROW_NAMENOM, name_nom);
		cv.put(ROW_CODENOM, code_nom);

		try {
			db.insert("nominal", null, cv);
		} catch (Exception e) {
			Log.e("error", e.toString());
			e.printStackTrace();
		}
		db.close();
		return 0;
	}

	// proses menambah data
	public void insert_datanom(String name_nom, String code_nom) {
		openDataBase();
		String insertsql = "INSERT INTO nominal (name_nom, code_nom) "
				+ "VALUES ('" + name_nom + "' , '" + code_nom + "')";
		System.out.println("cccccccccc-" + insertsql);
		db.execSQL(insertsql);
		db.close();

	}

	// proses menghapus data

	public void delete_datanom(String name_nom) {
		openDataBase();
		String deletesql = "DELETE FROM nominal WHERE name_nom='" + name_nom
				+ "'";
		db.execSQL(deletesql);
		db.close();
	}

	// proses meng-edit data

	public void update_datanom(String name_nom, String code_nom) {
		openDataBase();
		String checkData = "select * from nominal where name_nom='" + code_nom
				+ "'";
		Cursor cursor = db.rawQuery(checkData, null);
		if (cursor.moveToFirst()) {
			String updatesql = "UPDATE nominal SET code_nom='" + code_nom
					+ "' WHERE name_nom='" + name_nom + "'";
			db.execSQL(updatesql);
		} else {
			String insertsql = "INSERT INTO nominal (name_nom, code_nom) "
					+ "VALUES ( '" + name_nom + "' , '" + code_nom + "')";
			db.execSQL(insertsql);
		}
		db.close();
	}

	// ambil semua baris
	public ArrayList<ArrayList<Object>> ambilSemuaBarisnom() {

		ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
		Cursor c;
		try {
			c = db.query(TABLE_NAME1, new String[] { ROW_IDNOM, ROW_NAMENOM,
					ROW_CODENOM }, null, null, null, null, null);
			c.moveToFirst();
			if (!c.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();
					dataList.add(c.getLong(0));
					dataList.add(c.getString(1));
					dataList.add(c.getString(2));
					dataArray.add(dataList);
				} while (c.moveToNext());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("DEBE ERROR", e.toString());
			Toast.makeText(myContext,
					"gagal ambil semua baris:" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}
		return dataArray;
	}

	// ambil sebuah baris
	public ArrayList<Object> ambilBarisnom() {
		// TODO Auto-generated method stub

		ArrayList<Object> arrbaris = new ArrayList<Object>();
		Cursor c;
		try {
			c = db.query(TABLE_NAME1, new String[] { ROW_IDNOM, ROW_NAMENOM,
					ROW_CODENOM }, ROW_IDNOM + "=" + ROW_IDNOM, null, null,
					null, null, null);

			c.moveToFirst();
			if (!c.isAfterLast()) {
				do {
					arrbaris.add(c.getLong(0));
					arrbaris.add(c.getString(1));
					arrbaris.add(c.getString(2));
					System.out.println("data: " + arrbaris);
				} while (c.moveToNext());
				String r = String.valueOf(arrbaris);
				Toast.makeText(myContext, "haha" + r, Toast.LENGTH_SHORT)
						.show();
			}
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("error", e.toString());
			Toast.makeText(myContext, "hhii" + e.toString(), Toast.LENGTH_SHORT)
					.show();
		}
		return arrbaris;
	}

	// *END PROSES FOR NOMINAL*\\

	// menambah data ditiap baris
	public long addpesan(String tanggal, String nocenter, String isipesan) {
		openDataBase();
		ContentValues cv = new ContentValues();
		cv.put(ROW_TGL, tanggal);
		cv.put(ROW_NOCENTER, nocenter);
		cv.put(ROW_ISIPESAN, isipesan);
		// return db.insert(TABLE_NAME2, null, cv);
		try {
			db.insert("waktu", null, cv);
		} catch (Exception e) {
			Log.e("error", e.toString());
			e.printStackTrace();
		}
		db.close();
		return 0;
		// return tanggal;
	}

	// ambil semua baris
	public ArrayList<ArrayList<Object>> ambilSemuapesan() {
		openDataBase();
		ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
		Cursor c;
		try {
			c = db.query(TABLE_NAME2, new String[] { ROW_IDTGL, ROW_TGL,
					ROW_NOCENTER, ROW_ISIPESAN }, null, null, null, null, null);
			c.moveToFirst();
			if (!c.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();
					dataList.add(c.getLong(0));
					dataList.add(c.getString(1));
					dataList.add(c.getString(2));
					dataList.add(c.getString(3));
					dataArray.add(dataList);

				} while (c.moveToNext());
				c.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("DEBE ERROR", e.toString());
			Toast.makeText(myContext,
					"gagal ambil semua baris:" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}
		db.close();
		return dataArray;
	}
	
	public void eraseSemuaPesan(){
		openDataBase();
		db.delete(TABLE_NAME2, null, null);
		db.close();
	}

	@Override
	public synchronized void close() {
		if (db != null)
			db.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
