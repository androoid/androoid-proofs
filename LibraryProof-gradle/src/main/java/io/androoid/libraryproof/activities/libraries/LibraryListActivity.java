package io.androoid.libraryproof.activities.libraries;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.domain.Library;
import io.androoid.libraryproof.utils.DatabaseHelper;

/**
 *
 * @author Juan Carlos García
 * @since 1.0
 */
public class LibraryListActivity extends OrmLiteBaseListActivity<DatabaseHelper> implements
        AbsListView.MultiChoiceModeListener, AdapterView.OnItemClickListener{

    private ArrayAdapter adapter;
    private List<Library> selectedLibraries = new ArrayList<Library>();
    private ArrayList<Library> libraryList;
    private Menu contextualMenu;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_list_activity);

        // Adding back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Adding selector
        this.getListView().setSelector(R.drawable.selector);

        // Creating short click listener
        getListView().setOnItemClickListener(this);

        // Creating multiple choice view
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(this);

        // Fill library list with Libraries from Database
        try {
            fillLibraryList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that fills library list with Libraries getted from Database
     *
     * @throws SQLException
     */

    private void fillLibraryList() throws SQLException{

        Dao<Library, Integer> libraryDao = getHelper().getLibraryDao();

        List<Library> libraries = libraryDao.queryForAll();

        // Creating library ArrayList
        libraryList = new ArrayList<Library>();
        for(Library library : libraries){
            libraryList.add(library);
        }

        // Creating array adapter
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, libraryList);


        getListView().setAdapter(adapter);
    }

    /**
     * Method that removes all selected libraries
     *
     * @throws SQLException
     */
    private void removeLibraries() throws SQLException{
        // Removing all selected libraries
        Dao<Library, Integer> libraryDao = getHelper().getLibraryDao();
        Integer deleted = libraryDao.delete(selectedLibraries);


        // Show message with total deleted items
        String message = String.format("%s %s deleted", deleted, deleted > 1 ? "libraries were" : "library was");
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();

        // Close action mode
        actionMode.finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add:
                Intent intent = new Intent(LibraryListActivity.this, LibraryFormActivity.class);
                LibraryListActivity.this.startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when an item is checked or unchecked during selection mode.
     *
     * @param mode     The {@link ActionMode} providing the selection mode
     * @param position Adapter position of the item that was checked or unchecked
     * @param id       Adapter ID of the item that was checked or unchecked
     * @param checked  <code>true</code> if the item is now checked, <code>false</code>
     */
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // Getting current item
        Library library = (Library) getListView().getItemAtPosition(position);
        View child = getListView().getChildAt(position);

        // Checking if current item was checked before
        if(selectedLibraries.indexOf(library) != -1){
            // Removing element from selected Libraries
            selectedLibraries.remove(selectedLibraries.indexOf(library));
            // Removing background
            if(child != null) {
                child.setSelected(false);
                child.setBackgroundColor(Color.WHITE);
            }
        }else{
            // Adding element to selected Libraries
            selectedLibraries.add(library);
            // Changing background
            if(child != null) {
                child.setSelected(true);
                child.setBackgroundColor(Color.parseColor("#6DCAEC"));
            }
        }

        int checkedItems = getListView().getCheckedItemCount();

        if(checkedItems > 0){
            mode.setSubtitle(String.format("%s librar%s selected", checkedItems, checkedItems > 1 ? "ies" : "y"));
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

    /**
     * Called when action mode is first created. The menu supplied will be used to
     * generate action buttons for the action mode.
     *
     * @param mode ActionMode being created
     * @param menu Menu used to populate action buttons
     * @return true if the action mode should be created, false if entering this
     * mode should be aborted.
     */
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Setting title
        mode.setTitle("Selected Libraries");
        // Inflate the menu for the CAB
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.contextual_menu, menu);
        contextualMenu = menu;
        actionMode = mode;
        return true;
    }

    /**
     * Called to refresh an action mode's action menu whenever it is invalidated.
     *
     * @param mode ActionMode being prepared
     * @param menu Menu used to populate action buttons
     * @return true if the menu or action mode was updated, false otherwise.
     */
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    /**
     * Called to report a user click on an action button.
     *
     * @param mode The current ActionMode
     * @param item The item that was clicked
     * @return true if this callback handled the event, false if the standard MenuItem
     * invocation should continue.
     */
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        // Respond to clicks on the actions in the CAB
        Intent intent = new Intent(LibraryListActivity.this, LibraryFormActivity.class);
        Bundle bundle = new Bundle();

        switch (item.getItemId()) {
            case R.id.item_show:
                // Show selected library
                bundle.putInt("libraryId", selectedLibraries.get(0).getId());
                bundle.putString("mode", "show");
                intent.putExtras(bundle);
                LibraryListActivity.this.startActivity(intent);
                break;
            case R.id.item_edit:
                // Edit selected library
                bundle.putInt("libraryId", selectedLibraries.get(0).getId());
                intent.putExtras(bundle);
                LibraryListActivity.this.startActivity(intent);
                break;
            case R.id.item_delete:
                // Remove all selected library
                try {
                    removeLibraries();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    /**
     * Called when an action mode is about to be exited and destroyed.
     *
     * @param mode The current ActionMode being destroyed
     */
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // Cleaning selected libraries
        selectedLibraries.clear();
        try {
            fillLibraryList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Show selected library
        Intent intent = new Intent(LibraryListActivity.this, LibraryFormActivity.class);
        Bundle bundle = new Bundle();
        Library library = (Library) getListView().getItemAtPosition(position);
        bundle.putInt("libraryId", library.getId());
        bundle.putString("mode", "show");
        intent.putExtras(bundle);
        LibraryListActivity.this.startActivity(intent);
    }
}