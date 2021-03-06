package br.edu.ifsp.controlefinancas.activity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class SQLiteHelper extends SQLiteOpenHelper {

    private static final String KEY_DB_CLASS = "CONTROLE_FINANCAS";

    private static final String DATABASE_NAME = "controle_financas.db";

    //Atributos (colunas) da tabela de contas
    static final String DB_TABLE_CONTA = "conta";
    static final String KEY_CONTA_ID = "id";
    static final String KEY_CONTA_DESCRICAO = "descricao";
    static final String KEY_CONTA_SALDO = "saldo";

    //Atributos (colunas) da tabela de transação
    static final String DB_TABLE_TRANSACOES = "transacao";
    static final String KEY_TRANSACAO_ID = "id";
    static final String KEY_TRANSACAO_ID_CONTA = "id_conta"; //FK(id) da tabela conta
    static final String KEY_TRANSACAO_CATEGORIA_ID = "categoria_id"; //FK(id) tabela categoria
    static final String KEY_TRANSACAO_DESCRICAO = "descricao"; //descricao da transacao
    static final String KEY_TRANSACAO_NATUREZA = "natureza"; //natureza (1-credito/receita ou 2-Debito/despesa)
    static final String KEY_TRANSACAO_DATE = "data"; //valor da transacao
    static final String KEY_TRANSACAO_VALOR = "valor"; //valor da transacao

    //Atributos (colunas) da tabela de categorias
    static final String DB_TABLE_CATEGORIAS = "categoria";
    static final String KEY_CATEGORIA_ID = "id";
    static final String KEY_CATEGORIA_DESCRICAO = "descricao";

    static final int NATUREZA_RECEITA = 1;
    static final int NATUREZA_DESPESA = 2;

    //Registro da última versão do Banco de Dados (utilizado para possíveis atualizações no banco, onde deve ser incrementado)
    private static final int DATABASE_VERSION = 1;

    //SQL de criação da Tabela Conta
    private static final String CREATE_TABLE_CONTA = "CREATE TABLE IF NOT EXISTS "+ DB_TABLE_CONTA + " ("+
            KEY_CONTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            KEY_CONTA_DESCRICAO + " TEXT NOT NULL, "+
            KEY_CONTA_SALDO + " REAL); ";

    //SQL de criação da Tabela Transações
    private static final String CREATE_TABLE_TRANSACAO = "CREATE TABLE IF NOT EXISTS "
            + DB_TABLE_TRANSACOES+ " ("
            +KEY_TRANSACAO_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +KEY_TRANSACAO_ID_CONTA + " INTEGER NOT NULL, "
            +KEY_TRANSACAO_CATEGORIA_ID + " INTEGER NOT NULL, "
            +KEY_TRANSACAO_DESCRICAO + " TEXT NOT NULL, "
            +KEY_TRANSACAO_NATUREZA + " INTEGER NOT NULL, " //natureza (1-credito/receita ou 2-Debito/despesa)
            +KEY_TRANSACAO_DATE + " INTEGER NOT NULL, " // (formato: yyyymmdd)
            +KEY_TRANSACAO_VALOR + " REAL NOT NULL, "
            +" FOREIGN KEY ("+KEY_TRANSACAO_ID_CONTA+") REFERENCES "+DB_TABLE_CONTA+"("+KEY_CONTA_ID+") ON DELETE CASCADE,"
            +" FOREIGN KEY ("+KEY_TRANSACAO_CATEGORIA_ID+") REFERENCES "+DB_TABLE_CATEGORIAS+"("+KEY_CATEGORIA_ID+")"
            +");";

    //SQL de criação da Tabela Categoria
    private static final String CREATE_TABLE_CATEGORIA = "CREATE TABLE "+ DB_TABLE_CATEGORIAS + " ("+
            KEY_CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            KEY_CATEGORIA_DESCRICAO + " TEXT NOT NULL); ";

    private static final String DB_INSERT_CATREGORIA = "INSERT INTO " + DB_TABLE_CATEGORIAS +
            "("+KEY_CATEGORIA_DESCRICAO+")" +
            " VALUES " +
                "('Água'), " +
                "('Alimentação')," +
                "('Educação'), " +
                "('Internet')," +
                "('Lazer'), " +
                "('Luz')," +
                "('Moradia')," +
                "('Saúde')," +
                "('Salário')," +
                "('Tarifas Bancárias')," +
                "('Telefone')," +
                "('Transporte')," +
                "('Outros')"+
                ";";

    public SQLiteHelper(Context context) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()){
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CONTA);
        Log.v(KEY_DB_CLASS, "Criado tabela Conta");

        db.execSQL(CREATE_TABLE_CATEGORIA);
        Log.v(KEY_DB_CLASS, "Criado tabela Categoria");

        db.execSQL(CREATE_TABLE_TRANSACAO);
        Log.v(KEY_DB_CLASS, "Criado tabela Transacao");

        //Popular tabela Categoria com dados iniciais (default)
        db.execSQL(DB_INSERT_CATREGORIA);
        Log.v(KEY_DB_CLASS, "Inseridas Categorias Iniciais");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE_CATEGORIAS);
        onCreate(db);

    }

}
