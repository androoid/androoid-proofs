package io.androoid.libraryproof.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.math.BigDecimal;
import java.sql.SQLException;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.domain.Author;
import io.androoid.libraryproof.domain.Book;

/**
 *
 * @author Juan Carlos García
 * @since 1.0
 */
public class BookDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "libraryproof.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Book, Integer> bookDao = null;
    private RuntimeExceptionDao<Book, Integer> runtimeExceptionBookDao = null;

    public BookDatabaseHelper(Context context){
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
            TableUtils.createTable(connectionSource, Book.class);
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
            TableUtils.dropTable(connectionSource, Book.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Dao<Book, Integer> getBookDao() throws SQLException{
        if(bookDao == null){
            bookDao = getDao(Book.class);
        }
        return bookDao;
    }

    public RuntimeExceptionDao<Book, Integer> getRuntimeExceptionAuthorDao() throws SQLException{
        if(runtimeExceptionBookDao == null){
            runtimeExceptionBookDao = getRuntimeExceptionDao(Book.class);
        }
        return runtimeExceptionBookDao;
    }

    @Override
    public void close(){
        super.close();
        bookDao = null;
        runtimeExceptionBookDao = null;
    }

}
