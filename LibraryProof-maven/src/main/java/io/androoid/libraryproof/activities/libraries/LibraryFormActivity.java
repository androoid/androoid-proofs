package io.androoid.libraryproof.activities.libraries;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.sql.SQLException;
import java.util.ArrayList;

import io.androoid.libraryproof.R;
import io.androoid.libraryproof.domain.Library;
import io.androoid.libraryproof.utils.AsyncGeoResponse;
import io.androoid.libraryproof.utils.DatabaseHelper;
import io.androoid.libraryproof.utils.GeoSearchHelper;

/**
 *
 * @author Juan Carlos García
 * @since 1.0
 */
public class LibraryFormActivity extends OrmLiteBaseActivity<DatabaseHelper> implements AsyncGeoResponse {

    private Library library;
    private EditText libraryNameEditText;
    private MapView libraryLocationMap;
    private EditText libraryLocationText;
    private Switch libraryPublicSwitch;
    private String mode;
    private GeoSearchHelper geoSearchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_form_activity);

        // Adding back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // initializes Map element
        initializeMapElement();

        // Getting form items
        libraryNameEditText = (EditText) findViewById(R.id.library_name_text);
        libraryPublicSwitch =(Switch) findViewById(R.id.library_public_switch);

        // Checking if is create view, update view or show view
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            // Getting library id
            int libraryId = bundle.getInt("libraryId");
            // Getting library by id
            populateForm(libraryId);
            // Disabling elements if is show view
            mode = bundle.getString("mode");
            if("show".equals(mode)){
                // Updating title to show
                setTitle(R.string.title_activity_library_form_show);
                disableLibraryFormElements();
            }else{
                // Updating title to edit
                setTitle(R.string.title_activity_library_form_update);
            }

        }
    }

    /**
     * Method that initializes map element
     */
    private void initializeMapElement() {
        // Getting street element
        libraryLocationText = (EditText) findViewById(R.id.library_location_text);
        // Getting map element
        libraryLocationMap = (MapView) findViewById(R.id.library_location_map);
        libraryLocationMap.setTileSource(TileSourceFactory.MAPNIK);
        libraryLocationMap.setMultiTouchControls(true);
        libraryLocationMap.setClickable(true);
        // Initial map position
        IMapController mapController = libraryLocationMap.getController();
        mapController.setZoom(5);
        GeoPoint startPoint = new GeoPoint(45.416775400000000000, -7.703790199999957600);
        mapController.setCenter(startPoint);

        // Adding event on street input
        final Handler mHandler = new Handler();
        libraryLocationText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                mHandler.removeCallbacks(mFilterTask);
                mHandler.postDelayed(mFilterTask, 1000);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            Runnable mFilterTask = new Runnable() {

                @Override
                public void run() {
                    // Creating AsyncTask to allow address search in async mode
                    geoSearchHelper = new GeoSearchHelper();
                    geoSearchHelper.delegate = LibraryFormActivity.this;

                    // Update map with address location
                    String address = libraryLocationText.getText().toString();
                    if (address != null) {
                        geoSearchHelper.execute(address);
                    }
                }
            };
        });

    }

    /**
     * Method that disables all form items.
     *
     */
    private void disableLibraryFormElements() {
        // Disabling all form fields
        libraryNameEditText.setEnabled(false);
        libraryPublicSwitch.setEnabled(false);
        // Disabling Map elements
        libraryLocationText.setEnabled(false);
    }

    /**
     * Method that populate Library form with selected library
     * id
     *
     * @param libraryId
     */
    private void populateForm(int libraryId) {
        // Getting library object
        try {
            Dao<Library, Integer> libraryDao = getHelper().getLibraryDao();
            library = libraryDao.queryForId(libraryId);

            // Getting values
            String name = library.getName();
            GeoPoint libraryLocation = library.getLocation();
            boolean isPublic = library.isPublicLibrary();

            libraryNameEditText.setText(name);

            // Populate map elements if exists
            if(libraryLocation != null){
                ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
                // Adding items
                items.add(new OverlayItem(library.toString(), "", libraryLocation));

                /* OnTapListener for the Markers, shows a simple Toast. */
                ItemizedOverlay<OverlayItem> mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                        new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                            @Override
                            public boolean onItemSingleTapUp(final int index,
                                                             final OverlayItem item) {
                                return true; // We 'handled' this event.
                            }
                            @Override
                            public boolean onItemLongPress(final int index,
                                                           final OverlayItem item) {
                                return false;
                            }
                        }, new DefaultResourceProxyImpl(getApplicationContext()));

                libraryLocationMap.getOverlays().add(mMyLocationOverlay);
                libraryLocationMap.invalidate();

                // Initial map position
                IMapController mapController = libraryLocationMap.getController();
                mapController.setZoom(15);
                mapController.setCenter(libraryLocation);
            }

            libraryPublicSwitch.setChecked(isPublic);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to create new Library item
     */
    private void createLibrary() {

        try {
            Dao<Library, Integer> libraryDao = getHelper().getLibraryDao();

            // Getting library name
            String libraryName = libraryNameEditText.getText().toString();
            // Getting library location
            GeoPoint libraryPosition = null;
            if(libraryLocationMap.getOverlays().size() > 0){
                libraryPosition = (GeoPoint) libraryLocationMap.getMapCenter();
            }

            // Getting library isPublic
            boolean isPublic = (boolean) libraryPublicSwitch.isChecked();

            // Creating new Library
            Library library = new Library(libraryName, libraryPosition, isPublic);
            libraryDao.create(library);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to update selected Library
     */
    private void updateLibrary() {

        try {
            Dao<Library, Integer> libraryDao = getHelper().getLibraryDao();

            // Getting library name
            String libraryName = libraryNameEditText.getText().toString();
            // Getting library location
            GeoPoint libraryPosition = null;
            if(libraryLocationMap.getOverlays().size() > 0){
                libraryPosition = (GeoPoint) libraryLocationMap.getMapCenter();
            }
            // Getting library isPublic
            boolean isPublic = (boolean) libraryPublicSwitch.isChecked();

            library.setName(libraryName);
            library.setLocation(libraryPosition);
            library.setPublicLibrary(isPublic);

            libraryDao.update(library);

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
                // Checks if there is a selected library or is necessary to create a new one
                if(library != null){
                    // Update existing Library
                    updateLibrary();
                }else{
                    // Create new Library
                    createLibrary();
                }
                // Return to list
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method that will be executed before AsyncGeoTasks
     *
     * @param output
     */
    @Override
    public void processFinish(GeoPoint output) {

        libraryLocationMap.getOverlays().clear();

        if(output == null){
            libraryLocationText.setBackgroundColor(Color.parseColor("#ff9090"));
            return;
        }else{
            libraryLocationText.setBackgroundColor(Color.WHITE);
        }


        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        // Adding items
        items.add(new OverlayItem("", "", output));

        /* OnTapListener for the Markers, shows a simple Toast. */
        ItemizedOverlay<OverlayItem> mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index,
                                                     final OverlayItem item) {
                        return true; // We 'handled' this event.
                    }
                    @Override
                    public boolean onItemLongPress(final int index,
                                                   final OverlayItem item) {
                        return false;
                    }
                }, new DefaultResourceProxyImpl(getApplicationContext()));


        libraryLocationMap.getOverlays().add(mMyLocationOverlay);
        libraryLocationMap.invalidate();

        // Initial map position
        IMapController mapController = libraryLocationMap.getController();
        mapController.setZoom(15);
        mapController.setCenter(output);
    }
}
