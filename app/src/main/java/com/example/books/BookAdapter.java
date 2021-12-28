package com.example.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book current_book = getItem(position);

        // Book Title
        TextView title = listItemView.findViewById(R.id.title);
        title.setText(current_book.getTitle());

        // Author Name
        TextView author = listItemView.findViewById(R.id.author);
        author.setText(current_book.getAuthor());

        // Book Cover
        ImageView imageView = listItemView.findViewById(R.id.book_cover);
        Picasso.get().load(current_book.getImageUrl()).into(imageView);

        return listItemView;
    }
}
