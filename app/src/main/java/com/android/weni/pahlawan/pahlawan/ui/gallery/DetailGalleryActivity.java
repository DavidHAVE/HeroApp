package com.android.weni.pahlawan.pahlawan.ui.gallery;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.weni.pahlawan.pahlawan.R;
import com.android.weni.pahlawan.pahlawan.data.HeroesContract;

public class DetailGalleryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DIET_LOADER = 0;
    TextView mHeroesNameTextView, mHeroesDescriptionTextView;
    ImageView mHeroesImageView;
    private Uri mCurrentActivityUri;

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gallery);

        mHeroesNameTextView = (TextView) findViewById(R.id.heroes_name_text_view);
        mHeroesDescriptionTextView = (TextView) findViewById(R.id.heroes_description_textView);
        mHeroesImageView = (ImageView) findViewById(R.id.heroes_image_view);

        Intent i = getIntent();
        if (i.getData() != null) {
            mCurrentActivityUri = i.getData();
            getLoaderManager().initLoader(DIET_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                HeroesContract.HeroesEntry._ID,
                HeroesContract.HeroesEntry.COLUMN_HEROES_NAME,
                HeroesContract.HeroesEntry.COLUMN_HEROES_DESCRIPTION,
                HeroesContract.HeroesEntry.COLUMN_HEROES_IMAGE};
        return new CursorLoader(this,
                mCurrentActivityUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(HeroesContract.HeroesEntry.COLUMN_HEROES_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(HeroesContract.HeroesEntry.COLUMN_HEROES_DESCRIPTION);
            int imageColumnIndex = cursor.getColumnIndex(HeroesContract.HeroesEntry.COLUMN_HEROES_IMAGE);

            String heroesName = cursor.getString(nameColumnIndex);
            String heroesDescription = cursor.getString(descriptionColumnIndex);
            String heroesImage = cursor.getString(imageColumnIndex);

            mHeroesNameTextView.setText(heroesName);
            mHeroesDescriptionTextView.setText(heroesDescription);
            Bitmap imageBitmap = decodeBase64(heroesImage);
            mHeroesImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mHeroesNameTextView.setText("");
        mHeroesDescriptionTextView.setText("");
    }
}
