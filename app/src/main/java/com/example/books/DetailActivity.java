package com.example.books;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String mBookTitle = getIntent().getStringExtra("title");
        String mImageURL = getIntent().getStringExtra("imageURL");
        String mAuthors = getIntent().getStringExtra("authors");
        String mBookDescription = getIntent().getStringExtra("description");
        String mInfoURL = getIntent().getStringExtra("infoURL");

        ImageView mImageView = findViewById(R.id.detail_image);
        TextView mTitleView = findViewById(R.id.book_title);
        TextView mAuthorView = findViewById(R.id.book_author);
        TextView mDescriptionView = findViewById(R.id.book_description);
        Button mInfoButton = findViewById(R.id.more_info);

        Picasso.get()
                .load(mImageURL)
                .fit()
                .centerInside()
                .into(mImageView);

        mTitleView.setText(mBookTitle);
        mAuthorView.setText(mAuthors);
        mDescriptionView.setText(mBookDescription);

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(mInfoURL);

                // Create a new intent to view buy the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }
}