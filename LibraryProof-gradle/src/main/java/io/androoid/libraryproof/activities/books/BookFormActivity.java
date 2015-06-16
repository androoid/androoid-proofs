package io.androoid.libraryproof.activities.books;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.domain.Author;
import io.androoid.libraryproof.domain.Book;
import io.androoid.libraryproof.utils.DatabaseHelper;

/**
 *
 * @author Juan Carlos García
 * @since 1.0
 */
public class BookFormActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private Book book;
    private EditText bookTitleEditText;
    private EditText bookIsbnEditText;
    private EditText bookYearEditText;
    private Spinner bookAuthorSpinner;
    private String mode;
    private ArrayList<Author> authorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_form_activity);

        // Adding back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Getting form items
        bookTitleEditText = (EditText) findViewById(R.id.book_title_text);
        bookIsbnEditText = (EditText) findViewById(R.id.book_isbn_text);
        bookYearEditText = (EditText) findViewById(R.id.book_year_text);
        bookAuthorSpinner = (Spinner) findViewById(R.id.book_author_text);

        // Populate spinners
        populateSpinners();

        // Checking if is create view, update view or show view
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            // Getting book id
            int bookId = bundle.getInt("bookId");
            // Getting book by id
            populateForm(bookId);
            // Disabling elements if is show view
            mode = bundle.getString("mode");
            if("show".equals(mode)){
                // Updating title to show
                setTitle(R.string.title_activity_book_form_show);
                disableBookFormElements();
            }else{
                // Updating title to edit
                setTitle(R.string.title_activity_book_form_update);
            }

        }
    }

    /**
     * This methos will populate all form Spinners
     */
    private void populateSpinners() {

        ArrayAdapter adapter;

        // Populate author spinner
        try{

            Dao<Author, Integer> authorDao = getHelper().getAuthorDao();
            List<Author> authors = authorDao.queryForAll();

            // Creating author ArrayList
            authorList = new ArrayList<Author>();
            for(Author author : authors){
                authorList.add(author);
            }

            // Creating array adapter
            adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, authorList);

            // Setting adapter on spinner
            bookAuthorSpinner.setAdapter(adapter);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method that disables all form items.
     *
     */
    private void disableBookFormElements() {
        // Disabling all form fields
        bookTitleEditText.setEnabled(false);
        bookIsbnEditText.setEnabled(false);
        bookYearEditText.setEnabled(false);
        bookAuthorSpinner.setEnabled(false);
    }

    /**
     * Method that populate Book form with selected book
     * id
     *
     * @param bookId
     */
    private void populateForm(int bookId) {
        // Getting book object
        try {
            Dao<Book, Integer> bookDao = getHelper().getBookDao();
            book = bookDao.queryForId(bookId);

            // Getting values
            String title = book.getTitle();
            String isbn = book.getIsbn();
            Integer year = book.getYear();
            Author author = book.getAuthor();


            bookTitleEditText.setText(title);
            bookIsbnEditText.setText(isbn);
            bookYearEditText.setText(year.toString());
            ArrayAdapter adapter = (ArrayAdapter) bookAuthorSpinner.getAdapter();
            int authorPosition = -1;
            for(int i = 0; i < adapter.getCount(); i++){
                Author item = (Author) adapter.getItem(i);
                if(item.getId().equals(author.getId())){
                    authorPosition = i;
                    break;
                }
            }

            bookAuthorSpinner.setSelection(authorPosition);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to create new Book item
     */
    private void createBook() {

        try {
            Dao<Book, Integer> bookDao = getHelper().getBookDao();
            Dao<Author, Integer> authorDao = getHelper().getAuthorDao();

            // Getting book title
            String bookTitle = bookTitleEditText.getText().toString();
            // Getting book ISBN
            String bookIsbn = bookIsbnEditText.getText().toString();
            // Getting book year
            String bookYear = bookYearEditText.getText().toString();
            // Getting book author
            Author author = (Author) bookAuthorSpinner.getSelectedItem();

            book = new Book(bookTitle, bookIsbn, Integer.parseInt(bookYear), author);
            bookDao.create(book);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to update selected Book
     */
    private void updateBook() {

        try {
            Dao<Book, Integer> bookDao = getHelper().getBookDao();

            // Getting book title
            String bookTitle = bookTitleEditText.getText().toString();
            // Getting book ISBN
            String bookIsbn = bookIsbnEditText.getText().toString();
            // Getting book year
            String bookYear = bookYearEditText.getText().toString();
            // Getting book author
            Author author = (Author) bookAuthorSpinner.getSelectedItem();

            book.setTitle(bookTitle);
            book.setIsbn(bookIsbn);
            book.setYear(Integer.parseInt(bookYear));
            book.setAuthor(author);

            bookDao.update(book);

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
                // Checks if there is a selected book or is necessary to create a new one
                if(book != null){
                    // Update existing Book
                    updateBook();
                }else{
                    // Create new Book
                    createBook();
                }
                // Return to list
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
