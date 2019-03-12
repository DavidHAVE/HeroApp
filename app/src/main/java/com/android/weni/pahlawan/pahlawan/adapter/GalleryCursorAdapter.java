package com.android.weni.pahlawan.pahlawan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.weni.pahlawan.pahlawan.R;
import com.android.weni.pahlawan.pahlawan.data.HeroesContract;

import java.util.Locale;

/**
 * Created by David on 16/11/2017.
 */

public class GalleryCursorAdapter extends CursorAdapter {

    public GalleryCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView mHeroImageView = (ImageView) view.findViewById(R.id.hero_image_view);
        TextView mHeroNameTextView = (TextView) view.findViewById(R.id.hero_name_text_view);

        int imageColumnIndex = cursor.getColumnIndex(HeroesContract.HeroesEntry.COLUMN_HEROES_IMAGE);
        int nameColumnIndex = cursor.getColumnIndex(HeroesContract.HeroesEntry.COLUMN_HEROES_NAME);

        String heroImage = cursor.getString(imageColumnIndex);
        String heroName = cursor.getString(nameColumnIndex);


        Bitmap imageBitmap = decodeBase64(heroImage);
        mHeroImageView.setImageBitmap(imageBitmap);
        mHeroNameTextView.setText(heroName);

    }


}
