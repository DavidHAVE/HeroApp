package com.android.weni.pahlawan.pahlawan.ui.gallery;

import android.Manifest;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.weni.pahlawan.pahlawan.object.Heroes;
import com.android.weni.pahlawan.pahlawan.R;
import com.android.weni.pahlawan.pahlawan.adapter.GalleryCursorAdapter;
import com.android.weni.pahlawan.pahlawan.data.HeroesContract;
import com.android.weni.pahlawan.pahlawan.helper.FirebaseHandler;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.R.attr.data;
import static android.R.attr.filter;
import static android.media.CamcorderProfile.get;

public class GalleryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SearchView.OnQueryTextListener {

    private static final int HERO_LOADER = 0;
    GalleryCursorAdapter mGalleryCursorAdapter;

    List<Heroes> heroesLists;
    ListView mGalleryListView;
    GridView mGalleryGridView;
    LinearLayout mEmptyView;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    private SearchView mSearchView;
    private Toolbar toolbar;
    private URL imageUrl;
    private ImageView mTestImageView;
    private String base;
    private String title, information;
    private ContentValues contentValues;
    private List<String> baseList;
    private boolean filterHeroes = false;
    private String heroesName;
    // Firebase instance variables
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private String userId;
    private FirebaseHandler firebaseHandler;

    private ProgressDialog loadingDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("GalleryActivity", "Handler");
            saveTips();
            //mFoodAdapter.notifyDataSetChanged();
        }
    };

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_gallery);

        mSearchView = (SearchView) findViewById(R.id.search_view);
//        mGalleryListView = (ListView) findViewById(R.id.galleryListView);
        mGalleryGridView = (GridView) findViewById(R.id.galleryGridView);
        mEmptyView = (LinearLayout) findViewById(R.id.emptyView);

        if (isNetworkConnected()) {
            //Initialize Firebase Components
            storage = FirebaseStorage.getInstance();
            mAuth = FirebaseAuth.getInstance();
            firebaseHandler = new FirebaseHandler();
        }

        heroesLists = new ArrayList<>();
        baseList = new ArrayList<>();

        mGalleryCursorAdapter = new GalleryCursorAdapter(this, null);
//        mGalleryListView.setEmptyView(mEmptyView);
//        mGalleryListView.setAdapter(mGalleryCursorAdapter);
        mGalleryGridView.setEmptyView(mEmptyView);
        mGalleryGridView.setNumColumns(3);

        mGalleryGridView.setAdapter(mGalleryCursorAdapter);
        mGalleryGridView.setTextFilterEnabled(true);

//        mGalleryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(GalleryActivity.this, DetailGalleryActivity.class);
//
//                Uri currentHeroUri = ContentUris.withAppendedId(HeroesContract.HeroesEntry.CONTENT_URI, id);
//                i.setData(currentHeroUri);
//                startActivity(i);
//            }
//        });

        mGalleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(GalleryActivity.this, DetailGalleryActivity.class);

                Uri currentHeroUri = ContentUris.withAppendedId(HeroesContract.HeroesEntry.CONTENT_URI, id);
                i.setData(currentHeroUri);
                startActivity(i);
            }
        });

        isStoragePermissionGranted();
        setupSearchView();

        getLoaderManager().initLoader(HERO_LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isNetworkConnected()) {
            signInAnonymously();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //attachTipDatabaseReadListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachTipDatabaseReadListener();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isNetworkConnected()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_gallery, menu);
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.refreshGallery:
                // saveTips();
                startTread();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startTread() {
        loadingDialog = new ProgressDialog(GalleryActivity.this);
        loadingDialog.setMessage("Please Wait...");
        loadingDialog.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    baseList.clear();
                    heroesLists.clear();
                    attachHeroDatabaseReadListener();
                    Log.e("GalleryActivity", "startTread");
                    //readTip();
                    sleep(3000);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("GalleryActivity", "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.e("DietInfomrationAct", "Permission is granted");
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            finish();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("GalleryActivity", "heroesName4 :" + query.toString());


        filterHeroes = true;
        Heroes heroes = new Heroes();
        getLoaderManager().restartLoader(HERO_LOADER, null, this);
//            mGalleryGridView.setFilterText(query.toString());
        mGalleryCursorAdapter.notifyDataSetChanged();
        Log.e("GalleryActivity", "heroesName2 :" + query.toString());

        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
//        String text = query;
//        mGalleryCursorAdapter.getFilter().equals(text);
        heroesName = query;
        Log.e("GalleryActivity", "heroesName :" + heroesName);


//        mGalleryGridView.notifyAll();

        if (TextUtils.isEmpty(query)) {
            filterHeroes = false;
            getLoaderManager().restartLoader(HERO_LOADER, null, this);
            mGalleryCursorAdapter.notifyDataSetChanged();
//            mGalleryGridView.clearTextFilter();
            Log.e("GalleryActivity", "heroesName3 :" + query.toString());
        }
        return false;
    }


    // Filter Class
//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        .clear();
//        if (charText.length() == 0) {
//            animalNamesList.addAll(arraylist);
//        } else {
//            for (AnimalNames wp : arraylist) {
//                if (wp.getAnimalName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    animalNamesList.add(wp);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    private void attachHeroDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Heroes heroesList = dataSnapshot.getValue(Heroes.class);
                    //    String uid = newWeight.getUid();

                    heroesLists.add(heroesList);

                    Log.e("GalleryActivity", "HeroesListSize :" + heroesLists.size());
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.e("ProgGoalActivity", "onChanged");
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.e("ProgGoalActivity", "onRemoved");
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.e("ProgGoalActivity", "onMoved");
                }

                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ProgGoalActivity", "onCancel");
                }
            };
            firebaseHandler.getRefHeroes().addChildEventListener(mChildEventListener);
        }
    }

    private void detachTipDatabaseReadListener() {
        if (mChildEventListener != null) {
            firebaseHandler.getRefHeroes().removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void saveTips() {
//        ArrayList<TipList> tipLists = new ArrayList<>();
//        tipLists.add(new TipList("Title1",R.drawable.about_24,"Infromation1"));
//        tipLists.add(new TipList("Title2",R.drawable.age_48,"Infromation2"));
//        tipLists.add(new TipList("Title3",R.drawable.boy_48,"Infromation3"));
//        tipLists.add(new TipList("Title4",R.drawable.calendar_50,"Infromation4"));

        title = null;
        base = null;
        information = null;

        contentValues = new ContentValues();

        int rowsDeleted = getContentResolver().delete(HeroesContract.HeroesEntry.CONTENT_URI,
                null, null);
        Log.v("CatalogAcitivy", rowsDeleted + " rows deleted from Tip database");

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://heroesapp-96b6c.appspot.com/");
//        gs://weightcontrol-f645f.appspot.com/admin/-KgJJ0ocz6nB-TOp5Nad/arduino.jpg

        Log.e("GalleryActivity", "Save Tips");
        if (heroesLists != null && !heroesLists.isEmpty()) {
            for (int i = 0; i < heroesLists.size(); i++) {
                Heroes useDict = heroesLists.get(i);

                String imageUri = useDict.getImageUri();

                Log.e("TipActivity", "tipList1 :" + title);

                String LOC_SEPARATOR = "admin";
                String offsetLocation;
                String primaryLocation = null;

                if (imageUri.contains(LOC_SEPARATOR)) {
                    String[] location = imageUri.split(LOC_SEPARATOR);
                    offsetLocation = location[0];
                    primaryLocation = LOC_SEPARATOR + location[1];
                    Log.e("TipActivity", "primaryLocation : " + primaryLocation);
                }
                Log.e("TipActivity", "primaryLocation2 : " + primaryLocation);
                Log.e("TipActivity", "imageUri : " + imageUri);

                final URL[] imageUrl = new URL[1];
                storageRef.child(primaryLocation).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        gs:
//sdadasdasdasdasdasd/
                        Log.e("TipActivity", "Uri : " + uri);
                        try {
                            imageUrl[0] = new URL(uri.toString());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        new GetImageTask().execute(imageUrl[0]);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Log.e("TipActivity", "gagal");
                    }
                });
            }
        } else {
            Toast.makeText(this, "Item Pahlawan Belum Ada", Toast.LENGTH_SHORT).show();
            detachTipDatabaseReadListener();
            loadingDialog.dismiss();
        }
    }

    private void save() {

        Log.e("TipActivity", "baseListS: " + baseList.size() + ", tipListsS : " + heroesLists.size());
        if (baseList != null && !baseList.isEmpty()) {
            for (int i = 0; i < baseList.size(); i++) {
                Heroes useDict = heroesLists.get(i);

                String heroesName = useDict.getHeroesName();

                String description = useDict.getDescription();

                contentValues.put(HeroesContract.HeroesEntry.COLUMN_HEROES_NAME, heroesName);
                contentValues.put(HeroesContract.HeroesEntry.COLUMN_HEROES_DESCRIPTION, description);

                contentValues.put(HeroesContract.HeroesEntry.COLUMN_HEROES_IMAGE, baseList.get(i));

                //  int newuri = getContentResolver().insert(DietContract.TipEntry.CONTENT_URI, contentValues);
                Uri newuri = getContentResolver().insert(HeroesContract.HeroesEntry.CONTENT_URI, contentValues);
                if (newuri == null) {
                    Toast.makeText(this, "Gagal menyimpan menu", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Menu disimpan", Toast.LENGTH_SHORT).show();
                }

                detachTipDatabaseReadListener();
                loadingDialog.dismiss();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                HeroesContract.HeroesEntry._ID,
                HeroesContract.HeroesEntry.COLUMN_HEROES_NAME,
                HeroesContract.HeroesEntry.COLUMN_HEROES_DESCRIPTION,
                HeroesContract.HeroesEntry.COLUMN_HEROES_IMAGE};
        String selection = HeroesContract.HeroesEntry.COLUMN_HEROES_NAME + " LIKE ?";
//        CacheContract.COLUMN_TITLE + " LIKE ?",
        String[] selectionArgs = new String[]{
                '%' + String.valueOf(heroesName) + '%'
        };

        if (filterHeroes == true) {
            return new CursorLoader(this,
                    HeroesContract.HeroesEntry.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null);
        } else {
            Log.e("GalleryActivity", "loader = false");
            return new CursorLoader(this,
                    HeroesContract.HeroesEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mGalleryCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGalleryCursorAdapter.swapCursor(null);
    }


    public class GetImageTask extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... params) {
            URL imgUrl = params[0];
            Log.e("TipActivity", " imgUrl : " + imgUrl);
            Bitmap bitmap = null;
            try {
                bitmap = Glide.with(getApplicationContext()).
                        load(imgUrl).
                        asBitmap().
                        into(100, 100). // Width and height
                        get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap == null) {
                Log.e("TipActivity", "Bitmap tidak ada");
            } else {
                Log.e("TipActivity", "Bitmap ada");
                base = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
                Log.e("TipActivity", "base64 : " + base);
                baseList.add(base);
                if (baseList.size() == heroesLists.size()) {
                    save();
                }
            }
        }
    }
}
