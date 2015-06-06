package io.androoid.libraryproof.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


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
    private List<Author> selectedAuthors = new ArrayList<Author>();
    private ArrayList<Author> authorList;
    private Menu contextualMenu;
    private Dao<Author, BigDecimal> authorDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_list_activity);

        // Creating Author DAO
        try{
            authorDao = getHelper().getAuthorDao();
        }catch(Exception e){
            e.printStackTrace();
        }


        // Adding back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Adding selector
        this.getListView().setSelector(R.drawable.selector);

        // Creating multiple choice view
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {

                // Getting current item
                Author author = (Author) getListView().getItemAtPosition(position);
                View child = getListView().getChildAt(position);

                // Checking if current item was checked before
                if(selectedAuthors.indexOf(author) != -1){
                    // Removing element from selected Authors
                    selectedAuthors.remove(selectedAuthors.indexOf(author));
                    // Removing background
                    if(child != null) {
                        child.setSelected(false);
                        child.setBackgroundColor(Color.WHITE);
                    }
                }else{
                    // Adding element to selected Authors
                    selectedAuthors.add(author);
                    // Changing background
                    if(child != null) {
                        child.setSelected(true);
                        child.setBackgroundColor(Color.parseColor("#6DCAEC"));
                    }
                }

                int checkedItems = getListView().getCheckedItemCount();

                if(checkedItems > 0){
                    mode.setSubtitle(String.format("%s author%s selected", checkedItems, checkedItems > 1 ? "s" : ""));
                }

                // If there are more than one selected item, is not possible edit or show elements
                if(contextualMenu != null){
                    MenuItem showMenuItem = contextualMenu.getItem(0);
                    MenuItem editMenuItem = contextualMenu.getItem(1);

                    boolean toShow = true;

                    if(checkedItems > 1){
                        toShow = false;
                    }

                    showMenuItem.setVisible(toShow);
                    editMenuItem.setVisible(toShow);
                }

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                if(item.getTitle().equals("Show")){
                    return true;
                }else if(item.getTitle().equals("Edit")){
                    return true;
                }else if(item.getTitle().equals("Delete")){
                    removeAuthors();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Setting title
                mode.setTitle("Selected Authors");
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                contextualMenu = menu;
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });

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
        List<Author> authors = authorDao.queryForAll();

        // Creating author ArrayList
        authorList = new ArrayList<Author>();
        for(Author author : authors){
            authorList.add(author);
        }

        // Creating array adapter
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, authorList);


        getListView().setAdapter(adapter);
    }

    /**
     * Method that removes all selected authors
     */
    private void removeAuthors(){
        try {
            // Removing all selected authors
            Integer deleted = authorDao.delete(selectedAuthors);

            // Show message with total deleted items
            String message = String.format("%s %s deleted", deleted, deleted > 1 ? "authors were" : "author was");
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();

            // Refresh activity
            finish();
            startActivity(getIntent());

        } catch (SQLException e) {
            e.printStackTrace();
        }
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