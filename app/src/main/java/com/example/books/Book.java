package com.example.books;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private final String title;
    private final String author;
    private final String imageUrl;
    private final String description;
    private final String bookUrl;

    public Book(String title, String author, String imageUrl, String bookUrl, String description) {
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.bookUrl = bookUrl;
        this.description = description;
    }

    protected Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        bookUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeString(bookUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBookUrl() {
        return bookUrl;
    }

}
