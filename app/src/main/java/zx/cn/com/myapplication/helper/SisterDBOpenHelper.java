package zx.cn.com.myapplication.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import zx.cn.com.myapplication.db.TabImageDefine;

/**
 * Description:
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/6/20 21:40
 */

public class SisterDBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "sisterImg.db";  //数据库名
    private static final int DB_VERSION = 1;    //数据库版本号

    public SisterDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public SisterDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + TabImageDefine.TABLE_FULI + " ("
                + TabImageDefine.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TabImageDefine.COLUMN_FULI_ID + " TEXT, "
                + TabImageDefine.COLUMN_FULI_CREATEAT + " TEXT, "
                + TabImageDefine.COLUMN_FULI_DESC + " TEXT, "
                + TabImageDefine.COLUMN_FULI_PUBLISHEDAT + " TEXT, "
                + TabImageDefine.COLUMN_FULI_SOURCE + " TEXT, "
                + TabImageDefine.COLUMN_FULI_TYPE + " TEXT, "
                + TabImageDefine.COLUMN_FULI_URL + " TEXT, "
                + TabImageDefine.COLUMN_FULI_USED + " BOOLEAN, "
                + TabImageDefine.COLUMN_FULI_WHO + " TEXT"
                + ")";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
