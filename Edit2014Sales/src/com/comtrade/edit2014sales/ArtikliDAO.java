package com.comtrade.edit2014sales;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ArtikliDAO {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			  MySQLiteHelper.COLUMN_BARKOD,
			  MySQLiteHelper.COLUMN_NAZIV,
			  MySQLiteHelper.COLUMN_CIJENA };
	
	public ArtikliDAO(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void otvoriBazu() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void zatvoriBazu() {
		dbHelper.close();
	}
	
	public Artikal kreirajArtikal(long id, int barkod, String naziv, float cijena) {
		ContentValues vrijednosti = new ContentValues();
		vrijednosti.put(MySQLiteHelper.COLUMN_BARKOD, barkod);
		vrijednosti.put(MySQLiteHelper.COLUMN_NAZIV, naziv);
		vrijednosti.put(MySQLiteHelper.COLUMN_CIJENA, cijena);
		long idUnosa = database.insert(MySQLiteHelper.TABLE_ARTIKLI, null, vrijednosti);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ARTIKLI,
		        allColumns, MySQLiteHelper.COLUMN_ID + " = " + idUnosa, null,
		        null, null, null);
		cursor.moveToFirst();
		Artikal noviArtikal = izKursoraUArtikal(cursor);
		cursor.close();
		return noviArtikal;
	}
	
	private Artikal izKursoraUArtikal(Cursor cursor) {
		Artikal artikal = new Artikal();
		artikal.setId(cursor.getLong(0));
		artikal.setBarkod(cursor.getInt(1));
		artikal.setNaziv(cursor.getString(2));
		artikal.setCijena(cursor.getFloat(3));
		return artikal;
	}
	
	public void izbrisiArtikal(Artikal artikal) {
		long id = artikal.getId();
	    System.out.println("Comment deleted with id: " + id);
	    database.delete(MySQLiteHelper.TABLE_ARTIKLI, MySQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	}
	
	public List<Artikal> uzmiListuSvihArtikala() {
		List<Artikal> artikli = new ArrayList<Artikal>();
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ARTIKLI, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Artikal artikal = izKursoraUArtikal(cursor);
			artikli.add(artikal);
			cursor.moveToNext();
		}
		
		cursor.close();
		return artikli;
	}
	
	public Artikal uzmiArtikalSaId(long id) {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ARTIKLI,
		        allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
		        null, null, null);
		cursor.moveToFirst();
		Artikal artikal = izKursoraUArtikal(cursor);
		cursor.close();
		return artikal;
	}
	
	public Artikal izmijeniArtikal(Artikal artikal) {
		long id = artikal.getId();
		ContentValues vrijednosti = new ContentValues();
		vrijednosti.put(MySQLiteHelper.COLUMN_BARKOD, artikal.getBarkod());
		vrijednosti.put(MySQLiteHelper.COLUMN_NAZIV, artikal.getNaziv());
		vrijednosti.put(MySQLiteHelper.COLUMN_CIJENA, artikal.getCijena());
		database.update(MySQLiteHelper.TABLE_ARTIKLI, vrijednosti, MySQLiteHelper.COLUMN_ID + " = " + id, null);
		return uzmiArtikalSaId(id);
	}
}
