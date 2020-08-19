package com.boreas.plainlife.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.boreas.plainlife.mvp.models.LoginModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LOGIN_MODEL".
*/
public class LoginModelDao extends AbstractDao<LoginModel, Long> {

    public static final String TABLENAME = "LOGIN_MODEL";

    /**
     * Properties of entity LoginModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
    }


    public LoginModelDao(DaoConfig config) {
        super(config);
    }
    
    public LoginModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LOGIN_MODEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL );"); // 0: id
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LOGIN_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, LoginModel entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LoginModel entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public LoginModel readEntity(Cursor cursor, int offset) {
        LoginModel entity = new LoginModel( //
            cursor.getLong(offset + 0) // id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LoginModel entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(LoginModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(LoginModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(LoginModel entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}