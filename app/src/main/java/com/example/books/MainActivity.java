package com.example.books;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BOOK_LOADER_ID = 1;
    BookAdapter mBookAdapter;
    ListView mBookList;
    boolean isConnected;
    private String mUrlRequestGoogleBooks = "";
    private TextView mEmptyStateTextView;
    private ImageView mEmptyStateImageView;
    private View circleProgressBar;
    private SearchView mSearchViewField;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        checkConnection(connectivityManager);

        mBookList = findViewById(R.id.list);
        mBookAdapter = new BookAdapter(this, new ArrayList<>());
        mBookList.setAdapter(mBookAdapter);

        // Find a reference to the empty view
        mEmptyStateTextView = findViewById(R.id.empty_text);
        mEmptyStateImageView = findViewById(R.id.empty_image);
        // mBookList.setEmptyView(mEmptyStateTextView);

        // Circle progress
        circleProgressBar = findViewById(R.id.loading_spinner);

        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader.
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            circleProgressBar.setVisibility(View.GONE);
            // Set empty state text to display "No internet connection."
            mEmptyStateTextView.setText(R.string.no_internet);
            mEmptyStateImageView.setImageResource(R.drawable.no_internet);
            mEmptyStateImageView.setVisibility(View.VISIBLE);
        }

        mBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current book that was clicked on
                //Book currentBook = mBookAdapter.getItem(position);

                Book currentBook = (Book) mBookList.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("imageURL", currentBook.getImageUrl());
                intent.putExtra("title", currentBook.getTitle());
                intent.putExtra("authors", currentBook.getAuthor());
                intent.putExtra("description", currentBook.getDescription());
                intent.putExtra("infoURL", currentBook.getBookUrl());

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuitem = menu.findItem(R.id.search);

        mSearchViewField = (SearchView) menuitem.getActionView();
        mSearchViewField.setQueryHint("Search Here...");

        mSearchViewField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                checkConnection(connectivityManager);
                if (isConnected) {
                    // Update URL and restart loader to displaying new result of searching
                    updateUrl(mSearchViewField.getQuery().toString());
                    restartLoader();
                    Log.i(LOG_TAG, "Search value: " + mSearchViewField.getQuery().toString());
                } else {
                    // Clear the adapter of previous book data
                    mBookAdapter.clear();

                    // Set mEmptyStateTextView visible
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText(R.string.no_internet);
                    mEmptyStateImageView.setImageResource(R.drawable.no_internet);
                    mEmptyStateImageView.setVisibility(View.VISIBLE);

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void updateUrl(String searchValue) {
        if (searchValue.contains(" ")) {
            searchValue = searchValue.replace(" ", "+");
        }
        mUrlRequestGoogleBooks = "https://www.googleapis.com/books/v1/volumes?q=" + searchValue + "&filter=paid-ebooks&maxResults=40";
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        Log.d("Done", mUrlRequestGoogleBooks);
        return new BookLoader(this, mUrlRequestGoogleBooks);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Progress bar mapping
        View circleProgressBar = findViewById(R.id.loading_spinner);
        circleProgressBar.setVisibility(View.GONE);
        // Set empty state text to display "No books found."
        mEmptyStateImageView.setImageResource(R.drawable.search_icon);
        mEmptyStateTextView.setText(R.string.search_books);

        // Clear the adapter of previous book data
        mBookAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mBookAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mBookAdapter.clear();
    }

    public void restartLoader() {
        mEmptyStateTextView.setVisibility(View.GONE);
        mEmptyStateImageView.setVisibility(View.GONE);
        circleProgressBar.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
    }

    public void checkConnection(ConnectivityManager connectivityManager) {
        // Status of internet connection
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}