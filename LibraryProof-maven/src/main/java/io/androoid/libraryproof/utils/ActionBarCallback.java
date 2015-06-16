package io.androoid.libraryproof.utils;

import android.app.ActionBar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.androoid.libraryproof.R;

/**
 *
 * @author Juan Carlos García
 * @since 1.0
 */
public class ActionBarCallback implements ActionMode.Callback {

    private int contextualMenu;

    public ActionBarCallback(int menu){
        this.contextualMenu = menu;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(contextualMenu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }


    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }



}
