package io.androoid.libraryproof.domain;


import android.renderscript.Element;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.osmdroid.util.GeoPoint;

/**
 *
 * @author Juan Carlos García
 * @since 1.0
 */
@DatabaseTable
public class Library {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private String name;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private GeoPoint location;

    @DatabaseField
    private boolean publicLibrary;


    public Library(){

    }

    public Library(String name, GeoPoint location, boolean publicLibrary){
        this.name = name;
        this.location = location;
        this.publicLibrary = publicLibrary;
    }


    public String toString(){
        return this.name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public boolean isPublicLibrary() {
        return publicLibrary;
    }

    public void setPublicLibrary(boolean publicLibrary) {
        this.publicLibrary = publicLibrary;
    }
}
