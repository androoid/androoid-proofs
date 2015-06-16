package io.androoid.libraryproof.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.activities.authors.AuthorListActivity;
import io.androoid.libraryproof.activities.books.BookListActivity;
import io.androoid.libraryproof.activities.libraries.LibraryListActivity;

/**
 *
 * @author Juan Carlos García
 * @since 1.0
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // List Authors Button event
        Button listAuthorsButton = (Button) findViewById(R.id.list_authors_button);
        listAuthorsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AuthorListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        // List Books Button event
        Button listBooksButton = (Button) findViewById(R.id.list_books_button);
        listBooksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        // List Libraries Button event
        Button listLibrariesButton = (Button) findViewById(R.id.list_libraries_button);
        listLibrariesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LibraryListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
