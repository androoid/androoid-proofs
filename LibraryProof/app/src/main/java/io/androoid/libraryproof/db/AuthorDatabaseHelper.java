package io.androoid.libraryproof.db;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.math.BigDecimal;
import java.sql.SQLException;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.domain.Author;

/**
 * Created by Juan Carlos García on 8/3/15.
 */
public class AuthorDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "libraryproof.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Author, BigDecimal> authorDao = null;
    private RuntimeExceptionDao<Author, BigDecimal> runtimeExceptionAuthorDao = null;

    public AuthorDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * What to do when your database needs to be created. Usually this entails creating the tables and loading any
     * initial data.
     * <p/>
     * <p>
     * <b>NOTE:</b> You should use the connectionSource argument that is passed into this method call or the one
     * returned by getConnectionSource(). If you use your own, a recursive call or other unexpected results may result.
     * </p>
     *
     * @param database         Database being created.
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Author.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * What to do when your database needs to be updated. This could mean careful migration of old data to new data.
     * Maybe adding or deleting database columns, etc..
     * <p/>
     * <p>
     * <b>NOTE:</b> You should use the connectionSource argument that is passed into this method call or the one
     * returned by getConnectionSource(). If you use your own, a recursive call or other unexpected results may result.
     * </p>
     *
     * @param database         Database being upgraded.
     * @param connectionSource To use get connections to the database to be updated.
     * @param oldVersion       The version of the current database so we can know what to do to the database.
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Author.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Dao<Author, BigDecimal> getAuthorDao() throws SQLException{
        if(authorDao == null){
            authorDao = getDao(Author.class);
        }
        return authorDao;
    }

    public RuntimeExceptionDao<Author, BigDecimal> getRuntimeExceptionAuthorDao() throws SQLException{
        if(runtimeExceptionAuthorDao == null){
            runtimeExceptionAuthorDao = getRuntimeExceptionDao(Author.class);
        }
        return runtimeExceptionAuthorDao;
    }

    @Override
    public void close(){
        super.close();
        authorDao = null;
        runtimeExceptionAuthorDao = null;
    }

}
