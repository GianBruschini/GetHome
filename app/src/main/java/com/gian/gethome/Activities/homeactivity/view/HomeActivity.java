package com.gian.gethome.Activities.homeactivity.view;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gian.gethome.Activities.openmap.view.OpenMapActivity;
import com.gian.gethome.Adapters.DrawerAdapter;
import com.gian.gethome.Adapters.DrawerItem;
import com.gian.gethome.Adapters.SimpleItem;
import com.gian.gethome.Adapters.SpaceItem;
import com.gian.gethome.BuildConfig;
import com.gian.gethome.Clases.CommonUtilsJava;
import com.gian.gethome.Clases.Constants;
import com.gian.gethome.Clases.LocationService;
import com.gian.gethome.Fragments.HomeFragment;
import com.gian.gethome.Fragments.likes.view.LikesFragment;
import com.gian.gethome.Fragments.mispublicaciones.view.MisPublicacionesFragment;
import com.gian.gethome.Fragments.perfil.view.PerfilFragment;
import com.gian.gethome.Fragments.PublicarAnimalFragment;
import com.gian.gethome.MainActivity;
import com.gian.gethome.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_HOME = 0;
    private static final int POS_LIKES = 1;
    private static final int POS_PERFIL = 2;
    private static final int POS_AGREGAR = 3;
    private static final int POS_MISPUBS = 4;
    private static final int POS_LOGOUT = 6;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;
    private CircleImageView fotoPerfil;
    private String imagenPerfil;
    private TextView provincia;
    private TextView pais;
    private ProgressDialog progressDialog;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;
    static HomeActivity instance;
    LocationRequest locationRequest;
    private Bundle savedInstance;

    public static HomeActivity getInstance() {
        return instance;
    }

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private DrawerAdapter adapter;
    private int seEjecuto = 0;
    private FirebaseAuth mFirebaseAuth;
    private Dialog loadingDialog;
    private String latitude;
    private String longitude;
    private TextView latitudeTxt;
    private TextView longitudeTxt;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstance = savedInstanceState;
        setContentView(R.layout.activity_home);
        instance = this;
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        loadingDialog = new Dialog(HomeActivity.this);
                        showProgressDialog();
                        startLocationService();
                        SharedPreferences.Editor editor = getSharedPreferences("prefCheckUser", MODE_PRIVATE).edit();
                        editor.putInt("code", 1);
                        editor.apply();
                        fotoPerfil = findViewById(R.id.fotoPerfil);
                        provincia = findViewById(R.id.provincia);
                        pais = findViewById(R.id.pais);
                        latitudeTxt = findViewById(R.id.latitudeTxt);
                        longitudeTxt = findViewById(R.id.longitudeTxt);
                        mFirebaseAuth = FirebaseAuth.getInstance();
                        Toolbar toolbar = findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayShowTitleEnabled(false);

                        //init();
                        //startLocation();
                        slidingRootNav = new SlidingRootNavBuilder(HomeActivity.this)
                                .withToolbarMenuToggle(toolbar)
                                .withDragDistance(180)
                                .withRootViewScale(0.75f)
                                .withRootViewElevation(25)
                                .withMenuOpened(false)
                                .withContentClickableWhenMenuOpened(false)
                                .withSavedState(savedInstanceState)
                                .withMenuLayout(R.layout.drawer_menu)
                                .inject();
                        screenIcons = loadScreenIcons();
                        screenTitles = loadScreenTitles();
                        adapter = new DrawerAdapter(Arrays.asList(
                                createItemFor(POS_HOME).setChecked(true),
                                createItemFor(POS_LIKES),
                                createItemFor(POS_PERFIL),
                                createItemFor(POS_AGREGAR),
                                createItemFor(POS_MISPUBS),
                                new SpaceItem(90),
                                createItemFor(POS_LOGOUT)));
                        adapter.setListener(HomeActivity.this);

                        RecyclerView list = findViewById(R.id.drawer_list);
                        list.setNestedScrollingEnabled(false);
                        list.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                        list.setAdapter(adapter);

                        TextView txt = slidingRootNav.getLayout().getChildAt(0).findViewById(R.id.nombrePerfilDrawer);
                        setUserName(txt);
                        //txt.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
                        setFotoPerfil();

                        adapter.setSelected(POS_HOME);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            openSettings();
                            // open device settings when the permission is
                            // denied permanently
                            //openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }


    public void setCountryAndProv(String countryName, String locality) {
        String chainProv = locality+",";
        provincia.setText(chainProv);
        pais.setText(countryName);
        hideProgressDialog();
    }


    public void setLatitudeAndLongitude(double latitude, double longitude) {
        latitudeTxt.setText(String.valueOf(latitude));
        longitudeTxt.setText(String.valueOf(longitude));
        setFragment(new HomeFragment());
        hideProgressDialog();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean isLocationServiceRunning(){
         ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            for (ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(service.service.getClassName())){
                    if(service.foreground){
                        return  true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction("startLocationService");
            startService(intent);
        }
    }

    private void stopLocationService(){
        if(isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(),LocationService.class);
            intent.setAction("stopLocationService");
            startService(intent);
        }
    }


    private void setUserName(TextView txt) {

        DatabaseReference profilePicture =
                FirebaseDatabase.getInstance().
                        getReference().
                        child("Users").
                        child("Person").
                        child(mFirebaseAuth.getCurrentUser().getUid())
                        .child("userName");
        profilePicture.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = (String) dataSnapshot.getValue();
                txt.setText(userName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showProgressDialog() {
        /*progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Por favor, espere");
        progressDialog.setCancelable(false);
        progressDialog.show();

         */
        hideProgressDialog();
        CommonUtilsJava commonUtils = new CommonUtilsJava();
        loadingDialog = commonUtils.showLoadingDialog(this);

    }

    private void hideProgressDialog(){

        if(loadingDialog.isShowing()){
            loadingDialog.cancel();
        }
    }


    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.grayText))
                .withTextTint(color(R.color.grayText))
                .withSelectedIconTint(color(R.color.white))
                .withSelectedTextTint(color(R.color.white));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }



    @Override
    public void onItemSelected(int position) {

        switch(position){
            case POS_HOME:
                setFragment(new HomeFragment());
                break;
            case POS_LIKES:
                setFragment(new LikesFragment());
                break;
            case POS_PERFIL:
                setFragment(new PerfilFragment());
                break;
            case POS_AGREGAR:
                Bundle bundle = new Bundle();
                bundle.putString("latitude",latitude);
                bundle.putString("longitude",longitude);
                PublicarAnimalFragment publicarAnimalFragment = new PublicarAnimalFragment();
                publicarAnimalFragment.putBundle(bundle);
                setFragment(publicarAnimalFragment);
                break;
            case POS_MISPUBS:
                setFragment(new MisPublicacionesFragment());
                break;
            case POS_LOGOUT:
                SharedPreferences.Editor editor = getSharedPreferences("prefCheckUser", MODE_PRIVATE).edit();
                editor.putInt("code", 0);
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }


        slidingRootNav.closeMenu();

    }

    @Override
    protected void onDestroy() {
        stopLocationService();
        instance = null;
        super.onDestroy();
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void setFotoPerfil() {
        fotoPerfil = findViewById(R.id.fotoPerfil);
        String profileImageURL= getIntent().getStringExtra("drawableProfile");
        Picasso.get().load(profileImageURL).placeholder(R.drawable.progress_animation).into(fotoPerfil);
        if(profileImageURL == null){
            DatabaseReference profilePicture = FirebaseDatabase.getInstance().getReference().child("Users").child("Person").child(mFirebaseAuth.getCurrentUser().getUid()).child("imageURL");
            profilePicture.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    imagenPerfil = (String) dataSnapshot.getValue();
                    Picasso.get().load(imagenPerfil).placeholder(R.drawable.progress_animation).into(fotoPerfil);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


}