package io.androoid.libraryproof.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.db.AuthorDatabaseHelper;
import io.androoid.libraryproof.domain.Author;

public class AuthorListActivity extends OrmLiteBaseListActivity<AuthorDatabaseHelper> {

    private ArrayAdapter adapter;

    private View currentViewSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_list_activity);

        // Adding back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Only one item could be selected
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Getting ListView
        // Getting author list
        ListView authorListView = (ListView)  findViewById(android.R.id.list);

        // Registering item click
        registerItemClick(authorListView);

        // Fill author list with Authors from Database
        try {
            fillAuthorList(authorListView);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that fills author list with authors getted from Database
     *
     * @param authorListView
     * @throws SQLException
     */

    private void fillAuthorList(ListView authorListView) throws SQLException{
        Dao<Author, BigDecimal> authorDao = getHelper().getAuthorDao();
        List<Author> authors = authorDao.queryForAll();

        // Creating author ArrayList
        final ArrayList<String> authorList = new ArrayList<String>();
        for(Author author : authors){
            authorList.add(author.toString());
        }

        // Creating array adapter
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, authorList);


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


    /**
     * Method that register item click listener on each element
     */
    private void registerItemClick(ListView authorListView){

        authorListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                // Getting selected element
                if(currentViewSelected != null){
                    currentViewSelected.setBackgroundResource(android.R.drawable.list_selector_background);
                    currentViewSelected.setSelected(false);
                }

                // Select current item
                AuthorListActivity.this.startActionMode(new AuthorActionBarCallback());
                view.setBackgroundColor(getResources().getColor(R.color.selected));
                view.setSelected(true);

                // Saving current view selected
                currentViewSelected = view;
            }
        });

    }

    class AuthorActionBarCallback implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

    }

}
