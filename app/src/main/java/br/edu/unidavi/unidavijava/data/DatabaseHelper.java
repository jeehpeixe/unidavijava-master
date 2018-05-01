package br.edu.unidavi.unidavijava.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.edu.unidavi.unidavijava.model.Jogo;
import br.edu.unidavi.unidavijava.model.MeuJogo;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Games.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_GAME = "GAME";

    public DatabaseHelper (Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE "
                + TABLE_GAME + "("
                + "codigo INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT,"
                + "plataforma TEXT,"
                + "lancamento INTEGER, "
                + "genero TEXT,"
                + "imageUrl TEXT,"
                + "nota REAL,"
                + "quero INTEGER DEFAULT 0,"
                + "tenho INTEGER DEFAULT 0,"
                + "joguei INTEGER DEFAULT 0,"
                + "zerei INTEGER DEFAULT 0,"
                + "fisico INTEGER DEFAULT 0,"
                + "paguei REAL DEFAULT 0.0,"
                + "notaPessoal REAL DEFAULT 0.0," +
                "CONSTRAINT jogo_unico UNIQUE (nome, plataforma)"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.v("DB_UPGRADE", "Nada para fazer");
    }

    public void createJogo(Jogo jogo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", jogo.getNome());
        values.put("plataforma", jogo.getPlataforma());
        values.put("lancamento", jogo.getLancamento());
        values.put("genero", jogo.getGenero());
        values.put("imageUrl", jogo.getImageUrl());
        values.put("nota", jogo.getNota());

        //db.insert(TABLE_GAME, null, values);
        try {
            db.insertOrThrow(TABLE_GAME, null, values);
        } catch (SQLException e) {
            db.update(TABLE_GAME, values, "nome = '" + jogo.getNome().replace("'", "''").trim() + "' AND plataforma = '"+jogo.getPlataforma().replace("'", "''").trim()+"'", null);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void createMeuJogo(MeuJogo jogo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("nome", jogo.getNome());
        //values.put("plataforma", jogo.getPlataforma());
        if (jogo.getLancamento() != 0)
            values.put("lancamento", jogo.getLancamento());
        if (!jogo.getGenero().equals(""))
            values.put("genero", jogo.getGenero());
        if (!jogo.getImageUrl().equals(""))
            values.put("imageUrl", jogo.getImageUrl());
        if (jogo.getNota() != 0)
            values.put("nota", jogo.getNota());
        values.put("quero", jogo.isQuero());
        values.put("tenho", jogo.isTenho());
        values.put("joguei", jogo.isJoguei());
        values.put("zerei", jogo.isZerei());
        values.put("fisico", jogo.isFisico());
        values.put("paguei", jogo.getPaguei());
        values.put("notaPessoal", jogo.getNotaPessoal());

        db.update(TABLE_GAME, values, "nome = '" + jogo.getNome().replace("'", "''").trim() + "' AND plataforma = '"+jogo.getPlataforma().replace("'", "''").trim()+"'", null);
        db.close();
    }

    private String getOrdem(Ordenacao order) {
        String ordenacao = " ORDER BY ";
        switch (order) {
            case NOME:
                ordenacao += "nome";
                break;
            case NOME_DESC:
                ordenacao += "nome DESC";
                break;
            case DATA:
                ordenacao += "lancamento";
                break;
            case DATA_DESC:
                ordenacao += "lancamento DESC";
                break;
            case GENERO:
                ordenacao += "genero";
                break;
            case GENERO_DESC:
                ordenacao += "genero DESC";
                break;
            case NOTA:
                ordenacao += "nota";
                break;
            case NOTA_DESC:
                ordenacao += "nota DESC";
                break;
            default:
                ordenacao += "nome";
                break;
        }
        return ordenacao;
    }

    public List<Jogo> getAllJogos(Ordenacao order) {
        List<Jogo> jogoList = new ArrayList<Jogo>();

        String ordenacao = getOrdem(order);
        String selectQuery = "SELECT * FROM " + TABLE_GAME + ordenacao;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Jogo jogo = new Jogo();
                jogo.setCodigo(cursor.getInt(0));
                jogo.setNome(cursor.getString(1));
                jogo.setPlataforma(cursor.getString(2));
                jogo.setLancamento(cursor.getInt(3));
                jogo.setGenero(cursor.getString(4));
                jogo.setImageUrl(cursor.getString(5));
                jogo.setNota(cursor.getDouble(6));

                jogoList.add(jogo);
            } while (cursor.moveToNext());
        }
        db.close();

        return jogoList;
    }

    public List<Jogo> getJogo(Integer id) {
        List<Jogo> jogoList = new ArrayList<Jogo>();

        String selectQuery = "SELECT * FROM " + TABLE_GAME + " WHERE codigo = " + id.toString();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Jogo jogo = new Jogo();
                jogo.setCodigo(cursor.getInt(0));
                jogo.setNome(cursor.getString(1));
                jogo.setPlataforma(cursor.getString(2));
                jogo.setLancamento(cursor.getInt(3));
                jogo.setGenero(cursor.getString(4));
                jogo.setImageUrl(cursor.getString(5));
                jogo.setNota(cursor.getDouble(6));

                jogoList.add(jogo);
            } while (cursor.moveToNext());
        }
        db.close();

        return jogoList;
    }

    public List<MeuJogo> getAllMeusJogos(Ordenacao order) {
        List<MeuJogo> jogoList = new ArrayList<MeuJogo>();

        String ordenacao = getOrdem(order);
        String selectQuery = "SELECT * FROM " + TABLE_GAME
                + " WHERE quero = 1 OR tenho = 1 OR joguei = 1 OR zerei = 1 OR fisico = 1 "
                + ordenacao;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                MeuJogo jogo = new MeuJogo();
                jogo.setCodigo(cursor.getInt(0));
                jogo.setNome(cursor.getString(1));
                jogo.setPlataforma(cursor.getString(2));
                jogo.setLancamento(cursor.getInt(3));
                jogo.setGenero(cursor.getString(4));
                jogo.setImageUrl(cursor.getString(5));
                jogo.setNota(cursor.getDouble(6));
                if (cursor.getInt(7) == 1)
                    jogo.setQuero(true);
                else
                    jogo.setQuero(false);
                if (cursor.getInt(8) == 1)
                    jogo.setTenho(true);
                else
                    jogo.setTenho(false);
                if (cursor.getInt(9) == 1)
                    jogo.setJoguei(true);
                else
                    jogo.setJoguei(false);
                if (cursor.getInt(10) == 1)
                    jogo.setZerei(true);
                else
                    jogo.setZerei(false);
                if (cursor.getInt(11) == 1)
                    jogo.setFisico(true);
                else
                    jogo.setFisico(false);
                jogo.setPaguei(cursor.getFloat(12));
                jogo.setNotaPessoal(cursor.getDouble(13));

                jogoList.add(jogo);
            } while (cursor.moveToNext());
        }
        db.close();

        return jogoList;
    }

    public List<MeuJogo> getMeuJogo(Integer id) {
        List<MeuJogo> jogoList = new ArrayList<MeuJogo>();

        String selectQuery = "SELECT * FROM " + TABLE_GAME
                + " WHERE codigo = "
                + id.toString();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                MeuJogo jogo = new MeuJogo();
                jogo.setCodigo(cursor.getInt(0));
                jogo.setNome(cursor.getString(1));
                jogo.setPlataforma(cursor.getString(2));
                jogo.setLancamento(cursor.getInt(3));
                jogo.setGenero(cursor.getString(4));
                jogo.setImageUrl(cursor.getString(5));
                jogo.setNota(cursor.getDouble(6));
                if (cursor.getInt(7) == 1)
                    jogo.setQuero(true);
                else
                    jogo.setQuero(false);
                if (cursor.getInt(8) == 1)
                    jogo.setTenho(true);
                else
                    jogo.setTenho(false);
                if (cursor.getInt(9) == 1)
                    jogo.setJoguei(true);
                else
                    jogo.setJoguei(false);
                if (cursor.getInt(10) == 1)
                    jogo.setZerei(true);
                else
                    jogo.setZerei(false);
                if (cursor.getInt(11) == 1)
                    jogo.setFisico(true);
                else
                    jogo.setFisico(false);
                jogo.setPaguei(cursor.getFloat(12));
                jogo.setNotaPessoal(cursor.getDouble(13));

                jogoList.add(jogo);
            } while (cursor.moveToNext());
        }
        db.close();

        return jogoList;
    }

    public List<Float> getInfoRanking(){
        List<Float> ranking = new ArrayList<Float>();

        String selectQuery = "SELECT sum(tenho) as tenho, sum(joguei) as joguei, sum(zerei) as zerei, sum(quero) as quero, sum(paguei) as paguei FROM " + TABLE_GAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                ranking.add(cursor.getFloat(0));
                ranking.add(cursor.getFloat(1));
                ranking.add(cursor.getFloat(2));
                ranking.add(cursor.getFloat(3));
                ranking.add(cursor.getFloat(4));

            } while (cursor.moveToNext());
        }
        db.close();

        return ranking;
    }
}
