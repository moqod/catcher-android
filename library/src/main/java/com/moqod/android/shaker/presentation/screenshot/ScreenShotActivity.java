package com.moqod.android.shaker.presentation.screenshot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import com.moqod.android.shaker.R;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 23/08/2017
 * Time: 13:56
 */

public class ScreenShotActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_URI = "image_uri";

    public static void run(Context context, Uri uri) {
        Intent intent = new Intent(context, ScreenShotActivity.class);
        intent.putExtra(EXTRA_IMAGE_URI, uri);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Uri imageUri = getIntent().getParcelableExtra(EXTRA_IMAGE_URI);
        ((ImageView) findViewById(R.id.screen_shot_image)).setImageURI(imageUri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
