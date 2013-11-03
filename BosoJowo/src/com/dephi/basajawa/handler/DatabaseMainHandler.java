package com.dephi.basajawa.handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Digunakan untuk pemrosesan database keseluruhan pada applikasi ini.
 * Semua proses meliputi pemanggilan, penyimpanan atau pencarian ada dalam
 * class ini.
 * 
 * @author Dephi
 *
 */
public class DatabaseMainHandler extends SQLiteOpenHelper {
	private DatabaseMainHandler mDbHelper = null;
	private Context mContext;

	// The Android's default system path of your application database.
	private static String sDBpath;
	private static final String DB_NAME = "dbpepak";
	private static final String KEY_ID = "_id";
	private static final String KEY_SUBID = "subcatId";
	private static final String KEY_POSTONE = "postOne";	
	private static final String KEY_POSTTWO = "postTwo";
	private static final String KEY_POSTPICTURE= "picture";

	// Konstruktor
	public DatabaseMainHandler(Context context) {
		super(context, DB_NAME, null, 1);
		this.mContext = context;

		String destPath = context.getFilesDir().getPath();
		sDBpath = destPath.substring(0, destPath.lastIndexOf("/"))
				+ "/databases/";

		try {
			createDataBase();
		} catch (IOException e) {
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
			String myPath = sDBpath + DB_NAME;
			// Perintah pengecekan dengan cara membuka, jika tidak ada maka
			// masuk ke catch SQLiteException
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			e.printStackTrace();
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
		InputStream myInput = mContext.getAssets().open(DB_NAME + ".sqlite");

		// Path to the just created empty db
		String outFile = sDBpath + DB_NAME;

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

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS pepak");
		db.execSQL("DROP TABLE IF EXISTS category");
		db.execSQL("DROP TABLE IF EXISTS subcategory");
		db.execSQL("DROP TABLE IF EXISTS posts");
		onCreate(db);
	}

	public void close() {
		// NOTE: openHelper must now be a member of CallDataHelper;
		// you currently have it as a local in your constructor
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}
	
	// Menambah entry baru pada database untuk POST
    public void addPost(String... arg) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Set nilai yg akan dimasukkan ke DB
        ContentValues values = new ContentValues();
        values.put(KEY_SUBID, arg[0]); // set post id
        values.put(KEY_POSTONE, arg[1]); // set post one
        values.put(KEY_POSTTWO, arg[2]); // set post two
        values.put(KEY_POSTPICTURE, arg[3]); // set path gambar
 
        // Inserting Row
        db.insert("posts", null, values);
        db.close(); // Closing database connection
    }

	// Mengambil data Utama
	public ArrayList<HashMap<String, String>> getMain() {
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<HashMap<String, String>> mainCat = new ArrayList<HashMap<String, String>>();

		// Database Query
		String selectQuery = "SELECT _id, name FROM pepak";
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key =&gt; value
				map.put(KEY_ID, cursor.getString(0));
				map.put("name", cursor.getString(1));

				// adding HashList to ArrayList
				mainCat.add(map);
			} while (cursor.moveToNext());
		}

		db.close();
		return mainCat;
	}

	// Mengambil data Category
	public ArrayList<HashMap<String, String>> getCat(int id) {
		ArrayList<HashMap<String, String>> resultDB = new ArrayList<HashMap<String, String>>();

		// Database Query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT _id, pepakId, catname FROM category WHERE pepakId = ?",
				new String[] { "" + id });

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key =&gt; value
				map.put(KEY_ID, "" + cursor.getInt(0));
				map.put("pepakId", "" + cursor.getInt(1));
				// map.put("catname", cursor.getString(2));
				map.put("seekThis", cursor.getString(2));

				// adding HashList to ArrayList
				resultDB.add(map);
			} while (cursor.moveToNext());
		}

		db.close();
		return resultDB;
	}

	// Mengambil data SubCategory
	public ArrayList<HashMap<String, String>> getSubCat(int id) {
		ArrayList<HashMap<String, String>> resultDB = new ArrayList<HashMap<String, String>>();

		// Database Query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT _id, categoryId, subcatname FROM subcategory WHERE categoryId = ?",
						new String[] { "" + id });

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key =&gt; value
				map.put(KEY_ID, "" + cursor.getInt(0));
				map.put("categoryId", "" + cursor.getInt(1));
				// map.put("subcatname", cursor.getString(2));
				map.put("seekThis", cursor.getString(2));

				// adding HashList to ArrayList
				resultDB.add(map);
			} while (cursor.moveToNext());
		}

		db.close();
		return resultDB;
	}

	// Mengambil data Posts dari keyword id SubCat
	public ArrayList<HashMap<String, String>> getPostsBySubCat(int id) {
		ArrayList<HashMap<String, String>> resultDB = new ArrayList<HashMap<String, String>>();

		// Database Query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT _id, subcatId, postOne, postTwo, picture FROM posts WHERE subcatId = ?",
						new String[] { "" + id });

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key =&gt; value
				map.put(KEY_ID, "" + cursor.getInt(0));
				map.put("subcatId", "" + cursor.getInt(1));
				map.put("postOne", cursor.getString(2));
				map.put("postTwo", cursor.getString(3));
				map.put("picture", cursor.getString(4));

				// adding HashList to ArrayList
				resultDB.add(map);
			} while (cursor.moveToNext());
		}

		db.close();
		return resultDB;
	}

	// Mengambil data Posts by keyword id ID
	public ArrayList<HashMap<String, String>> getPostsByID(int id) {
		ArrayList<HashMap<String, String>> resultDB = new ArrayList<HashMap<String, String>>();

		// Database Query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT _id, postOne, postTwo, picture FROM posts WHERE _id = ?",
						new String[] { "" + id });

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key =&gt; value
				map.put(KEY_ID, "" + cursor.getInt(0));
				map.put(KEY_POSTONE, cursor.getString(1));
				map.put(KEY_POSTTWO, cursor.getString(2));
				map.put(KEY_POSTPICTURE, cursor.getString(3));

				// adding HashList to ArrayList
				resultDB.add(map);
			} while (cursor.moveToNext());
		}

		db.close();
		return resultDB;
	}
	
	/**
	 * Melakukan pencarian sesuai dengan keyword yang diberikan.
	 * 
	 * @param keyword String keyword pencarian.
	 * @return ArrayList pasangan data hasil pencarian
	 */
	public ArrayList<HashMap<String, String>> getSearchResult(String keyword) {
		ArrayList<HashMap<String, String>> resultDB = new ArrayList<HashMap<String, String>>();

		// || is the operasi bersamaan in SQLite
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT _id, postOne, postTwo, picture FROM posts WHERE postOne || ' ' || postTwo LIKE ?",
						new String[] { "%" + keyword + "%" });
		
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				// adding each child node to HashMap key =&gt; value
				map.put(KEY_ID, "" + cursor.getInt(0));
				map.put("postOne", cursor.getString(1));
				map.put("postTwo", cursor.getString(2));
				map.put("picture", cursor.getString(3));

				// adding HashList to ArrayList
				resultDB.add(map);
			} while (cursor.moveToNext());
		}
		
		db.close();
		return resultDB;
	}
}
