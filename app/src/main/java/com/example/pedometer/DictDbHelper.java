package com.example.pedometer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DictDbHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;


  private static final String DATABASE_NAME = "pedometer.bd";


  private static final String AJUSTES_TABLE = "CREATE TABLE AJUSTES (CM_ZANCADA INTEGER, PASOS_OBJETIVOS INTEGER) ";
  private static final String REGISTO_DIARIO_TABLE = "CREATE TABLE REGISTROS_DIA (DIA TEXT, MES TEXT, PASOS_DADOS INTEGER) ";
  private static final String CREAR_DEFAULT = "INSERT INTO AJUSTES VALUES(72, 10000)";


  public DictDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // Este método será invocado al establecer la conexión con la BD
    // en al caso de que la creación de la base de datos sea necesaria
    db.execSQL(DictDbHelper.AJUSTES_TABLE);
    db.execSQL(DictDbHelper.REGISTO_DIARIO_TABLE);
    db.execSQL(CREAR_DEFAULT);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Este método será invocado al establecer la conexión con la BD
    // en el caso de que la versión de la BD almacenada sea inferior a
    // la versión de la BD que queremos abrir (especificada por
    // DATABASE_VERSION proporcionada en el contructor de la clase)
    //
    // una política de actualización simple: eliminar los datos almacenados
    // y comenzar de nuevo con una BD vacía
    db.execSQL("");
    onCreate(db);
  }

  @Override
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Este método será invocado al establecer la conexión con la BD
    // en el caso de que la versión de la BD almacenada sea superior a
    // la versión de la BD que queremos abrir (especificada por
    // DATABASE_VERSION proporcionada en el contructor de la clase)
    //
    // una política de actualización simple: eliminar los datos almacenados
    // y comenzar de nuevo con una BD vacía
    db.execSQL("");
    onCreate(db);
  }

  public void agregarDia(String dia, String mes, int pasos) {

    SQLiteDatabase db = getWritableDatabase();
    if (db != null) {
      db.execSQL(String.format("INSERT INTO REGISTROS_DIA VALUES('%s', '%s', '%d')", dia, mes, pasos));
    }
  }

  public void agregarAjustes(int cm_zancada, int pasos_objetivos) {

    SQLiteDatabase db = getWritableDatabase();
    if (db != null) {
      db.execSQL(String.format("INSERT INTO AJUSTES VALUES('%d', '%s')", cm_zancada, pasos_objetivos));
    }
  }

  public void eliminarAjustes() {

    SQLiteDatabase db = getWritableDatabase();
    if (db != null) {
      db.execSQL("DELETE FROM AJUSTES");
    }
  }

  public List<Pair<String, String>> getAjustes(){

    SQLiteDatabase db = getReadableDatabase();
    List<Pair<String, String>> lista = new ArrayList<>();
    String columnas;
    Cursor cursor = db.rawQuery("SELECT * FROM AJUSTES", null);
    try{
      while(cursor.moveToNext()){
        String columnaZancada = cursor.getString(cursor.getColumnIndex("CM_ZANCADA"));
        String columnaObjetivo = cursor.getString(cursor.getColumnIndex("PASOS_OBJETIVOS"));

        lista.add(new Pair<String, String>(columnaZancada, columnaObjetivo));
      }
    } finally{
      cursor.close();
    }
    return lista;
  }
}