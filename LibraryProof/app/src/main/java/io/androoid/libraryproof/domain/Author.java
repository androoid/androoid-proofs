package io.androoid.libraryproof.domain;


import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Carlos García on 8/3/15.
 */

@DatabaseTable
public class Author {

    @DatabaseField(id = true, generatedId = true)
    private BigDecimal id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String biography;


    public Author(){

    }

    public Author(String name, String biography){
        this.name = name;
        this.biography = biography;
    }

    public Author(BigDecimal id, String name, String biography){
        this.id = id;
        this.name = name;
        this.biography = biography;
    }

    public String toString(){
        return this.name;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }


    /**
     * This method will return single Author by Id
     *
     * @param id
     * @param db
     * @return
     */
    public Author findAuthor(BigDecimal id, SQLiteDatabase db) {
        return null;
    }

    /**
     * This method will list all Authors on DB
     */
    public static List<Author> findAll(SQLiteDatabase db){
        List<Author> authorList = new ArrayList<Author>();
        // return Author list
        return authorList;

    }
}
