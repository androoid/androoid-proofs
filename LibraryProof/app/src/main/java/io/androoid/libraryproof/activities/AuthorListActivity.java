package io.androoid.libraryproof.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.db.AuthorDatabaseHelper;
import io.androoid.libraryproof.domain.Author;

public class AuthorListActivity extends OrmLiteBaseListActivity<AuthorDatabaseHelper> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_activity);

        // Adding back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Fill author list with Authors from Database
        try {
            fillAuthorList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that fills author list with authors getted from Database
     *
     * @throws SQLException
     */
    private void fillAuthorList() throws SQLException{
        Dao<Author, BigDecimal> authorDao = getHelper().getAuthorDao();
        List<Author> authors = authorDao.queryForAll();

        // Creating author ArrayList
        final ArrayList<String> authorList = new ArrayList<String>();
        for(Author author : authors){
            authorList.add(author.toString());
        }

        // Creating array adapter
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, authorList);

        // Getting author list
        ListView authorListView = (ListView)  findViewById(android.R.id.list);
        authorListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_author, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add_author:
                Intent intent = new Intent(AuthorListActivity.this, AuthorFormActivity.class);
                AuthorListActivity.this.startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
