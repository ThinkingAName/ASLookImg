package zx.cn.com.myapplication.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import zx.cn.com.myapplication.DrySisterApp;
import zx.cn.com.myapplication.beans.Sister;
import zx.cn.com.myapplication.db.TabImageDefine;

/**
 * Description:   数据库操作类
 * Name:$name$
 * Author by:zx
 * Email:
 * Created:2019/6/20 21:54
 */

public class SisterDBOperateHelper {
    private static final String TAG = "SisterDBOperateHelper";

    private static SisterDBOperateHelper dbHelper;
    private SisterDBOpenHelper sqlHelper;
    private SQLiteDatabase db;

    private SisterDBOperateHelper() {
        sqlHelper = new SisterDBOpenHelper(DrySisterApp.getContext());
    }

    /** 单例 */
    public static SisterDBOperateHelper getInstance() {
        if(dbHelper == null) {
            synchronized (SisterDBOperateHelper.class) {
                if(dbHelper == null) {
                    dbHelper = new SisterDBOperateHelper();
                    System.out.println("dbHelper" + dbHelper);
                }
            }
        }
        return dbHelper;
    }

    /** 插入一个妹子 */
    public void insertSister(Sister sister) {
        db = getWritableDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TabImageDefine.COLUMN_FULI_ID,sister.get_id());
        contentValues.put(TabImageDefine.COLUMN_FULI_CREATEAT,sister.getCreateAt());
        contentValues.put(TabImageDefine.COLUMN_FULI_DESC,sister.getDesc());
        contentValues.put(TabImageDefine.COLUMN_FULI_PUBLISHEDAT,sister.getPublishedAt());
        contentValues.put(TabImageDefine.COLUMN_FULI_SOURCE,sister.getSource());
        contentValues.put(TabImageDefine.COLUMN_FULI_TYPE,sister.getType());
        contentValues.put(TabImageDefine.COLUMN_FULI_URL,sister.getUrl());
        contentValues.put(TabImageDefine.COLUMN_FULI_USED,sister.getUsed());
        contentValues.put(TabImageDefine.COLUMN_FULI_WHO,sister.getWho());
        db.insert(TabImageDefine.TABLE_FULI,null,contentValues);
        closeIO(null);
    }

    /** 插入一堆妹子(使用事务) */
    public void insertSisters(ArrayList<Sister> sisters) {
        db = getWritableDB();
        db.beginTransaction();
        try{
            for (Sister sister: sisters) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TabImageDefine.COLUMN_FULI_ID,sister.get_id());
                contentValues.put(TabImageDefine.COLUMN_FULI_CREATEAT,sister.getCreateAt());
                contentValues.put(TabImageDefine.COLUMN_FULI_DESC,sister.getDesc());
                contentValues.put(TabImageDefine.COLUMN_FULI_PUBLISHEDAT,sister.getPublishedAt());
                contentValues.put(TabImageDefine.COLUMN_FULI_SOURCE,sister.getSource());
                contentValues.put(TabImageDefine.COLUMN_FULI_TYPE,sister.getType());
                contentValues.put(TabImageDefine.COLUMN_FULI_URL,sister.getUrl());
                contentValues.put(TabImageDefine.COLUMN_FULI_USED,sister.getUsed());
                contentValues.put(TabImageDefine.COLUMN_FULI_WHO,sister.getWho());
                db.insert(TabImageDefine.TABLE_FULI,null,contentValues);
            }
            db.setTransactionSuccessful();
        } finally {
            if(db != null && db.isOpen()) {
                db.endTransaction();
                closeIO(null);
            }
        }
    }
    /** 删除妹子(根据_id) */
    public void deleteSister(String _id) {
        db = getWritableDB();
        db.delete(TabImageDefine.TABLE_FULI,"_id =?",new String[]{_id});
        closeIO(null);
    }
    /** 删除所有妹子 */
    public void deleteAllSisters() {
        db = getWritableDB();
        db.delete(TabImageDefine.TABLE_FULI,null,null);
        System.out.println("数据库以清除"+getSistersCount());

        closeIO(null);
    }

    /** 更新妹子信息(根据_id) */
    public void updateSister(String _id,Sister sister) {
        db = getWritableDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TabImageDefine.COLUMN_FULI_ID,sister.get_id());
        contentValues.put(TabImageDefine.COLUMN_FULI_CREATEAT,sister.getCreateAt());
        contentValues.put(TabImageDefine.COLUMN_FULI_DESC,sister.getDesc());
        contentValues.put(TabImageDefine.COLUMN_FULI_PUBLISHEDAT,sister.getPublishedAt());
        contentValues.put(TabImageDefine.COLUMN_FULI_SOURCE,sister.getSource());
        contentValues.put(TabImageDefine.COLUMN_FULI_TYPE,sister.getType());
        contentValues.put(TabImageDefine.COLUMN_FULI_URL,sister.getUrl());
        contentValues.put(TabImageDefine.COLUMN_FULI_USED,sister.getUsed());
        contentValues.put(TabImageDefine.COLUMN_FULI_WHO,sister.getWho());
        db.update(TabImageDefine.TABLE_FULI,contentValues,"_id =?",new String[]{_id});
        closeIO(null);
    }

    /** 查询当前表中有多少个妹子 */
    public int getSistersCount() {
        db = getReadableDB();
        Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM " + TabImageDefine.TABLE_FULI,null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        Log.v(TAG,"count：" + count);
        closeIO(cursor);
        return count;
    }

    /** 分页查询妹子，参数为当前页和每一个的数量，页数从0开始算 */
    public List<Sister> getSistersLimit(int curPage, int limit) {
        db =  getReadableDB();
        List<Sister> sisters = new ArrayList<>();
        String startPos = String.valueOf(curPage * limit);  //数据开始位置
        if(db != null) {
            Cursor cursor = db.query(TabImageDefine.TABLE_FULI,new String[] {
                    TabImageDefine.COLUMN_FULI_ID, TabImageDefine.COLUMN_FULI_CREATEAT,
                    TabImageDefine.COLUMN_FULI_DESC, TabImageDefine.COLUMN_FULI_PUBLISHEDAT,
                    TabImageDefine.COLUMN_FULI_SOURCE, TabImageDefine.COLUMN_FULI_TYPE,
                    TabImageDefine.COLUMN_FULI_URL, TabImageDefine.COLUMN_FULI_USED,
                    TabImageDefine.COLUMN_FULI_WHO,
            },null,null,null,null,TabImageDefine.COLUMN_ID,startPos + "," + limit);
            while (cursor.moveToNext()) {
                Sister sister = new Sister();
                sister.set_id(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_ID)));
                sister.setCreateAt(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_CREATEAT)));
                sister.setDesc(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_DESC)));
                sister.setPublishedAt(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_PUBLISHEDAT)));
                sister.setSource(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_SOURCE)));
                sister.setType(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_TYPE)));
                sister.setUrl(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_URL)));
                sister.setUsed(cursor.getInt(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_USED)));
                sisters.add(sister);
            }
            closeIO(cursor);
        }
        return sisters;

        
    }

    /** 查询所有妹子 */
    public List<Sister> getAllSisters() {
        db = getReadableDB();
        List<Sister> sisters = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TabImageDefine.TABLE_FULI,null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Sister sister = new Sister();
            sister.set_id(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_ID)));
            sister.setCreateAt(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_CREATEAT)));
            sister.setDesc(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_DESC)));
            sister.setPublishedAt(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_PUBLISHEDAT)));
            sister.setSource(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_SOURCE)));
            sister.setType(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_TYPE)));
            sister.setUrl(cursor.getString(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_URL)));
            sister.setUsed(cursor.getInt(cursor.getColumnIndex(TabImageDefine.COLUMN_FULI_USED)));
            sisters.add(sister);
        }
        closeIO(cursor);
        return sisters;
    }
    
    /** 获得可写数据库的方法 */
    private SQLiteDatabase getWritableDB() {
        return sqlHelper.getWritableDatabase();
    }

    /** 获得可读数据库的方法 */
    private SQLiteDatabase getReadableDB() {
        return sqlHelper.getReadableDatabase();
    }
    /** 关闭cursor和数据库的方法 */
    private void closeIO(Cursor cursor) {
        if(cursor != null) {
            cursor.close();
        }
        if(db != null) {
            db.close();
        }
    }
}
