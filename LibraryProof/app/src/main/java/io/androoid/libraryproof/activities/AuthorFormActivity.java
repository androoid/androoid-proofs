package io.androoid.libraryproof.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.math.BigDecimal;
import java.sql.SQLException;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.db.AuthorDatabaseHelper;
import io.androoid.libraryproof.domain.Author;

public class AuthorFormActivity extends OrmLiteBaseActivity<AuthorDatabaseHelper> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_form_activity);

        // Adding back button
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Method to create new Author item
     */
    private void createAuthor() {

        try {
            Dao<Author, BigDecimal> authorDao = getHelper().getAuthorDao();

            // Getting authorName
            EditText authorNameEditText = (EditText) findViewById(R.id.author_name_text);
            String authorName = authorNameEditText.getText().toString();

            // Getting biography
            EditText authorBiographyEditText = (EditText) findViewById(R.id.author_biography_text);
            String authorBiography = authorBiographyEditText.getText().toString();

            Author author = new Author(authorName,authorBiography);
            authorDao.create(author);

            Integer id = author.getId();
            String test = "";

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_author_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_save_author:
                // Create new Author
                createAuthor();
                // Return to list
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
