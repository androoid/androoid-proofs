package io.androoid.libraryproof.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.activities.authors.AuthorListActivity;

/**
 * Created by Juan Carlos García on 4/3/15.
 */
public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Getting buttons
        Button listAuthorsButton = (Button) findViewById(R.id.list_authors_button);
        listAuthorsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AuthorListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
