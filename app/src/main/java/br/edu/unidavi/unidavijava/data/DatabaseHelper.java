package br.edu.unidavi.unidavijava.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import br.edu.unidavi.unidavijava.model.Jogo;
import br.edu.unidavi.unidavijava.model.MeuJogo;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Games.db";
    private static final int DB_VERSION = 3;
    private static final String TABLE_GAME = "GAME";
    private static final String TABLE_MYGAME = "MYGAME";
    private Context ctx;

    public DatabaseHelper (Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.ctx = context;
        //Log.v("DatabaseHelper_CONSTRUT",Environment.getDataDirectory() + "/data/br.edu.unidavi.unidavijava/databases/" + DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v("DatabaseHelper_CREATE", "Criação da database");
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
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        } catch (Exception e) { }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Log.v("DB_UPGRADE", "Nada para fazer");
        Log.v("DatabaseHelper_UPDATE", "Atualização da database");
        Log.v("DatabaseHelper_UPDATE", oldVersion + " < " + newVersion);
        // Eliminação da tabela que já existia
        if (newVersion <= 3) {
            String CREATE_TABLE = "DROP TABLE IF EXISTS " + TABLE_GAME;
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
            } catch (Exception e) {
            }

            CREATE_TABLE = "CREATE TABLE "
                    + TABLE_GAME + "("
                    + "codigo INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nome TEXT,"
                    + "plataforma TEXT,"
                    + "lancamento INTEGER, "
                    + "genero TEXT,"
                    + "imageUrl TEXT,"
                    + "nota REAL,"
                    + "CONSTRAINT jogo_unico UNIQUE (nome, plataforma)"
                    + ")";
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
            } catch (Exception e) {
            }

            CREATE_TABLE = "CREATE TABLE "
                    + TABLE_MYGAME + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "codigo_game INTEGER,"
                    + "quero INTEGER DEFAULT 0,"
                    + "tenho INTEGER DEFAULT 0,"
                    + "joguei INTEGER DEFAULT 0,"
                    + "zerei INTEGER DEFAULT 0,"
                    + "fisico INTEGER DEFAULT 0,"
                    + "paguei REAL DEFAULT 0.0,"
                    + "notaPessoal REAL DEFAULT 0.0,"
                    + "CONSTRAINT meujogo_unico UNIQUE (codigo_game)"
                    + ")";
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
            } catch (Exception e) {
            }
        }
    }

    public void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = this.ctx.getDatabasePath(DB_NAME).toString();
        Log.v("DATABASE", currentDBPath);
        String backupDBPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + DB_NAME + ".bak";
        //String backupDBPath = "/sdcard/Download/" + DB_NAME + ".bak";

        Log.v("BACKUP", backupDBPath);
        File currentDB = new File(currentDBPath);
        File backupDB = new File(backupDBPath);
        boolean success = backupDB.mkdirs();
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this.ctx, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void backupDB() {
        final String inFileName = this.ctx.getDatabasePath(DB_NAME).toString();
        Log.v("DATABASE", inFileName);
        //final String outFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/br.edu.unidavi.unidavijava/" + DB_NAME;
        final String outFileName = "/sdcard/Download" + "/br.edu.unidavi.unidavijava/" + DB_NAME;
        Log.v("BACKUP", outFileName);

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            boolean success = new File("/sdcard/Download" + "/br.edu.unidavi.unidavijava/").mkdirs();
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(this.ctx, "Backup Criado", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this.ctx, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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

    public long createMeuJogo(MeuJogo jogo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("nome", jogo.getNome());
        //values.put("plataforma", jogo.getPlataforma());
        values.put("codigo_game", jogo.getId());
        values.put("quero", jogo.isQuero());
        values.put("tenho", jogo.isTenho());
        values.put("joguei", jogo.isJoguei());
        values.put("zerei", jogo.isZerei());
        values.put("fisico", jogo.isFisico());
        values.put("paguei", jogo.getPaguei());
        values.put("notaPessoal", jogo.getNotaPessoal());

        //int ret = db.update(TABLE_GAME, values, "nome = '" + jogo.getNome().replace("'", "''").trim() + "' AND plataforma = '"+jogo.getPlataforma().replace("'", "''").trim()+"'", null);
        //db.close();
        long ret = 0;
        try {
            ret = db.insertOrThrow(TABLE_MYGAME, null, values);
        } catch (SQLException e) {
            ret = db.update(TABLE_MYGAME, values, "codigo_game = " + jogo.getId(), null);
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return ret;
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

    private String getPeriodo(Integer anoInicio, Integer anoFinal){
        if (anoInicio > 0 && anoFinal > 0) {
            return "where lancamento between " + anoInicio + " and " + anoFinal;
        }
        if (anoInicio > 0) {
            return "where lancamento >= " + anoInicio;
        }
        if (anoFinal > 0) {
            return "where lancamento <= " + anoFinal;
        }

        return "";
    }

    public List<Jogo> getAllJogos(Ordenacao order, Integer anoInicio, Integer anoFinal) {
        List<Jogo> jogoList = new ArrayList<Jogo>();

        String ordenacao = getOrdem(order);
        String periodo = getPeriodo(anoInicio, anoFinal);
        String selectQuery = "SELECT * FROM " + TABLE_GAME + periodo + ordenacao;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Jogo jogo = new Jogo();
                jogo.setId(cursor.getInt(0));
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
                jogo.setId(cursor.getInt(0));
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
        String selectQuery = "SELECT a.codigo_game, b.nome, b.plataforma, b.lancamento, b.genero, "
                + " b.imageUrl, b.nota, a.quero, a.tenho, a.joguei, a.zerei, a.fisico, a.paguei, "
                + " a.notaPessoal FROM " + TABLE_MYGAME + " AS a "
                + " INNER JOIN " + TABLE_GAME + " AS b "
                + " ON a.codigo_game = b.codigo "
                + ordenacao;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                MeuJogo jogo = new MeuJogo();
                jogo.setId(cursor.getInt(0));
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
                jogo.setNotaPessoal(cursor.getFloat(13));

                jogoList.add(jogo);
            } while (cursor.moveToNext());
        }
        db.close();

        return jogoList;
    }

    public List<MeuJogo> getMeuJogo(Integer id) {
        List<MeuJogo> jogoList = new ArrayList<MeuJogo>();

        String selectQuery = "SELECT a.codigo_game, b.nome, b.plataforma, b.lancamento, b.genero, "
                + " b.imageUrl, b.nota, a.quero, a.tenho, a.joguei, a.zerei, a.fisico, a.paguei, "
                + " a.notaPessoal FROM " + TABLE_MYGAME + " AS a "
                + " INNER JOIN " + TABLE_GAME + " AS b "
                + " ON a.codigo_game = b.codigo "
                + " WHERE a.codigo_game = "
                + id.toString();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                MeuJogo jogo = new MeuJogo();
                jogo.setId(cursor.getInt(0));
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
                jogo.setNotaPessoal(cursor.getFloat(13));

                jogoList.add(jogo);
            } while (cursor.moveToNext());
        }
        db.close();

        return jogoList;
    }

    public int deleteMeuJogo(int gameId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affected = db.delete(TABLE_MYGAME, "codigo_game = " + gameId, null);
        db.close();
        return affected;
    }

    public List<Float> getInfoRanking(){
        List<Float> ranking = new ArrayList<Float>();

        String selectQuery = "SELECT sum(tenho) as tenho, sum(joguei) as joguei, sum(zerei) as zerei, sum(quero) as quero, sum(paguei) as paguei FROM " + TABLE_MYGAME;

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
