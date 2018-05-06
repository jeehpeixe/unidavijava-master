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
    private static final int DB_VERSION = 3;
    private static final String TABLE_GAME = "GAME";
    private static final String TABLE_MYGAME = "MYGAME";
    private Context ctx;
    private SessionConfig session;

    public DatabaseHelper (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.ctx = context;
        session = new SessionConfig(context);
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
        onUpgrade(sqLiteDatabase, 1, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.v("DatabaseHelper_UPDATE", "Atualização da database");
        Log.v("DatabaseHelper_UPDATE", oldVersion + " < " + newVersion);

        // Eliminação da tabela que já existia
        if (newVersion <= 3) {
            String CREATE_TABLE = "DROP TABLE IF EXISTS " + TABLE_GAME;
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
            } catch (Exception e) {}

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
            } catch (Exception e) {}

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
            } catch (Exception e) {}
        }
    }

    public void createJogo(Jogo jogo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        if (jogo.getPlataforma() == null)
            jogo.setPlataforma("");

        if (jogo.getGenero() == null)
            jogo.setGenero("");

        values.put("nome", jogo.getNome());
        values.put("plataforma", jogo.getPlataforma());
        values.put("lancamento", jogo.getLancamento());
        values.put("genero", jogo.getGenero());
        values.put("imageUrl", jogo.getImageUrl());
        values.put("nota", jogo.getNota());

        try {
            db.insertOrThrow(TABLE_GAME, null, values);
        }
        catch (SQLException e) {
            db.update(TABLE_GAME, values, "nome = '" + jogo.getNome().replace("'", "''").trim() + "' AND plataforma = '"+jogo.getPlataforma().replace("'", "''").trim()+"'", null);
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public long createMeuJogo(MeuJogo jogo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("codigo_game", jogo.getId());
        values.put("quero", jogo.isQuero());
        values.put("tenho", jogo.isTenho());
        values.put("joguei", jogo.isJoguei());
        values.put("zerei", jogo.isZerei());
        values.put("fisico", jogo.isFisico());
        values.put("paguei", jogo.getPaguei());
        values.put("notaPessoal", jogo.getNotaPessoal());

        long ret = 0;
        try {
            ret = db.insertOrThrow(TABLE_MYGAME, null, values);
        }
        catch (SQLException e) {
            ret = db.update(TABLE_MYGAME, values, "codigo_game = " + jogo.getId(), null);
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
        return ret;
    }

    private String getOrdem() {

        String ordenacao = " ORDER BY ";

        switch (getOrdenacaoJogos()) {
            case DATA:
                ordenacao += "lancamento";
                break;
            case GENERO:
                ordenacao += "genero";
                break;
            default:
                ordenacao += "nome";
                break;
        }

        return ordenacao;
    }

    private String getCondicaoPeriodo(){

        Integer anoInicio = getAnoInicioFiltro();
        Integer anoFinal  = getAnoFinalFiltro();

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

    private Integer getAnoInicioFiltro(){
        Integer ano = 0;

        try {
            ano = Integer.parseInt(session.getAnoInicioInSession());
        } catch (Exception e) {}

        return ano;
    }

    private Integer getAnoFinalFiltro(){
        Integer ano = 0;

        try {
            ano = Integer.parseInt(session.getAnoFinalInSession());
        } catch (Exception e) {}

        return ano;
    }

    private Ordenacao getOrdenacaoJogos(){
        if (session.getOrdemCategoriaInSession()) {
            return Ordenacao.GENERO;
        }
        if (session.getOrdemDataInSession()) {
            return Ordenacao.DATA;
        }
        return Ordenacao.NOME;
    }

    private List<Jogo> getListaJogosFromSql(String selectQuery){
        List<Jogo> jogoList = new ArrayList<Jogo>();
        SQLiteDatabase db   = this.getReadableDatabase();
        Cursor cursor       = db.rawQuery(selectQuery, null);

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

    public List<Jogo> getAllJogos() {
        String selectQuery  = "SELECT * FROM " + TABLE_GAME + " " + getCondicaoPeriodo() + getOrdem();

        return getListaJogosFromSql(selectQuery);
    }

    public List<Jogo> getJogo(Integer id) {
        String selectQuery  = "SELECT * FROM " + TABLE_GAME + " WHERE codigo = " + id.toString();

        return getListaJogosFromSql(selectQuery);
    }

    private List<MeuJogo> getListaMeusJogosFromSql(String selectQuery){
        List<MeuJogo> jogoList = new ArrayList<MeuJogo>();
        SQLiteDatabase db      = this.getReadableDatabase();
        Cursor cursor          = db.rawQuery(selectQuery, null);

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
                jogo.setQuero(cursor.getInt(7) == 1);
                jogo.setTenho(cursor.getInt(8) == 1);
                jogo.setJoguei(cursor.getInt(9) == 1);
                jogo.setZerei(cursor.getInt(10) == 1);
                jogo.setFisico(cursor.getInt(11) == 1);
                jogo.setPaguei(cursor.getFloat(12));
                jogo.setNotaPessoal(cursor.getFloat(13));

                jogoList.add(jogo);
            } while (cursor.moveToNext());
        }
        db.close();

        return jogoList;
    }

    public List<MeuJogo> getAllMeusJogos() {
        String selectQuery = "SELECT a.codigo_game, b.nome, b.plataforma, b.lancamento, b.genero, "
                + " b.imageUrl, b.nota, a.quero, a.tenho, a.joguei, a.zerei, a.fisico, a.paguei, "
                + " a.notaPessoal FROM " + TABLE_MYGAME + " AS a "
                + " INNER JOIN " + TABLE_GAME + " AS b "
                + " ON a.codigo_game = b.codigo "
                + getOrdem();

        return getListaMeusJogosFromSql(selectQuery);
    }

    public List<MeuJogo> getMeuJogo(Integer id) {
        String selectQuery = "SELECT a.codigo_game, b.nome, b.plataforma, b.lancamento, b.genero, "
                + " b.imageUrl, b.nota, a.quero, a.tenho, a.joguei, a.zerei, a.fisico, a.paguei, "
                + " a.notaPessoal FROM " + TABLE_MYGAME + " AS a "
                + " INNER JOIN " + TABLE_GAME + " AS b "
                + " ON a.codigo_game = b.codigo "
                + " WHERE a.codigo_game = "
                + id.toString();

        return getListaMeusJogosFromSql(selectQuery);
    }

    public int deleteMeuJogo(int gameId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affected      = db.delete(TABLE_MYGAME, "codigo_game = " + gameId, null);
        db.close();
        return affected;
    }

    public List<Float> getInfoRanking(){
        List<Float> ranking = new ArrayList<Float>();
        String selectQuery  = "SELECT sum(tenho) as tenho, sum(joguei) as joguei, sum(zerei) as zerei, sum(quero) as quero, sum(paguei) as paguei FROM " + TABLE_MYGAME;
        SQLiteDatabase db   = this.getReadableDatabase();
        Cursor cursor       = db.rawQuery(selectQuery, null);

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
