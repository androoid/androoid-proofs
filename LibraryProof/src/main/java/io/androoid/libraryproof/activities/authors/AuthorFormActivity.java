package io.androoid.libraryproof.activities.authors;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.db.AuthorDatabaseHelper;
import io.androoid.libraryproof.domain.Author;

/**
 *
 * @author Juan Carlos García
 * @since 1.0
 */
public class AuthorFormActivity extends OrmLiteBaseActivity<AuthorDatabaseHelper> {

    private Author author;
    private EditText authorNameEditText;
    private EditText authorBiographyEditText;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_form_activity);

        // Adding back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Getting form items
        authorNameEditText = (EditText) findViewById(R.id.author_name_text);
        authorBiographyEditText = (EditText) findViewById(R.id.author_biography_text);

        // Checking if is create view, update view or show view
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            // Getting author id
            int authorId = bundle.getInt("authorId");
            // Getting author by id
            populateForm(authorId);
            // Disabling elements if is show view
            mode = bundle.getString("mode");
            if("show".equals(mode)){
                // Updating title to show
                setTitle(R.string.title_activity_author_form_show);
                disableAuthorFormElements();
            }else{
                // Updating title to edit
                setTitle(R.string.title_activity_author_form_update);
            }

        }
    }

    /**
     * Method that disables all form items.
     *
     */
    private void disableAuthorFormElements() {
        // Disabling all form fields
        authorNameEditText.setEnabled(false);
        authorBiographyEditText.setEnabled(false);
    }

    /**
     * Method that populate Author form with selected author
     * id
     *
     * @param authorId
     */
    private void populateForm(int authorId) {
        // Getting author object
        try {
            Dao<Author, Integer> authorDao = getHelper().getAuthorDao();
            author = authorDao.queryForId(authorId);

            authorNameEditText.setText(author.getName());
            authorBiographyEditText.setText(author.getBiography());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to create new Author item
     */
    private void createAuthor() {

        try {
            Dao<Author, Integer> authorDao = getHelper().getAuthorDao();

            // Getting authorName
            String authorName = authorNameEditText.getText().toString();

            // Getting biography
            String authorBiography = authorBiographyEditText.getText().toString();

            author = new Author(authorName,authorBiography);
            authorDao.create(author);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to update selected Author
     */
    private void updateAuthor() {

        try {
            Dao<Author, Integer> authorDao = getHelper().getAuthorDao();

            // Getting authorName
            String authorName = authorNameEditText.getText().toString();

            // Getting biography
            String authorBiography = authorBiographyEditText.getText().toString();

            author.setName(authorName);
            author.setBiography(authorBiography);

            authorDao.update(author);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(mode == null){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_save:
                // Checks if there is a selected author or is necessary to create a new one
                if(author != null){
                    // Update existing Author
                    updateAuthor();
                }else{
                    // Create new Author
                    createAuthor();
                }
                // Return to list
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
