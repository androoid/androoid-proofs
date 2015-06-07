package io.androoid.libraryproof.domain;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author Juan Carlos García
 * @since 1.0
 */
@DatabaseTable
public class Book {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String isbn;

    @DatabaseField
    private Integer year;

    @DatabaseField
    private Author author;


    public Book(){

    }

    public Book(String title, String isbn, Integer year, Author author){
        this.title = title;
        this.isbn = isbn;
        this.year = year;
        this.author = author;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
