package com.example.epublibtest;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.io.InputStream;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.context = this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEpub();
            }
        });
    }

    private void openEpub() {
        try {
            AssetManager am = context.getAssets();
            InputStream stream = am.open("Session1.epub");

            EpubReader epubReader = new EpubReader();
            Book book = epubReader.readEpub(stream);

            String title = book.getTitle();

            Log.d("epub", title + " read successfully");

            List<TOCReference> ref = book.getTableOfContents().getTocReferences();
            Resource res = ref.get(0).getResource();

            Log.d("epub", "Resource: " + res.getHref());

            openWebView(res.getData());

        } catch (Exception e) {
            Log.d("epub", "error reading epub");
        }
    }

    private void openWebView(byte[] data) {
        WebView view = findViewById(R.id.web_view);

        view.loadData(new String(data), "text/html", "utf-8");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
