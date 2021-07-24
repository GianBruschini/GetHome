package com.gian.gethome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gian.gethome.Activities.elegirfotodeperfil.view.ElegirFotoDePerfilActivity;
import com.gian.gethome.Activities.homeactivity.view.HomeActivity;
import com.gian.gethome.Adapters.SliderPagerAdapter;
import com.gian.gethome.Clases.SlideFirstScreen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {

    private List<SlideFirstScreen> lstSlides;
    private TextView[] mDots;
    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private int code;
    private LoginButton loginButton;

    private CallbackManager mCallBackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private Button facebookButton;



    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sliderPager)ViewPager sliderPager;
    @SuppressLint("NonConstantResourceId")
    @BindView (R.id.dotsLayout)LinearLayout mDotLinearLayout;
    @Override

    protected void onStart() {
        super.onStart();
        /*System.out.println("Entre en el onStart");
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
        }
         */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSharedPreference();
        mAuth = FirebaseAuth.getInstance();
        //checkIfUserWasCreatedOnDatabase();
        createRequestGmail();
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        ButterKnife.bind(this);
        setSliderPage();

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallBackManager = CallbackManager.Factory.create();


        facebookButton = findViewById(R.id.facebook_button);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    updateUI();
                    System.out.println("Se autentico!");
                }else{

                }

            }
        };



    }

    private void updateUI() {
        Intent intent = new Intent(this,ElegirFotoDePerfilActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleFacebookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    System.out.println("Es exitoso!");
                    updateUI();
                }else{
                    System.out.println("NO Es exitoso!");
                }
            }
        });
    }

    private void checkSharedPreference() {
        SharedPreferences prefs = getSharedPreferences("prefCheckUser", MODE_PRIVATE);
        code = prefs.getInt("code", 0);
        System.out.println("El codigo es " + " " + code);
        if(code!=0){
            System.out.println("Entre por el code");
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void checkIfUserWasCreatedOnDatabase() {

        if(mAuth.getCurrentUser()!=null){

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                    child("Users").child("Person").
                    child(mAuth.getCurrentUser().getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        if(data.exists()){
                            Intent intent = new Intent (MainActivity.this, HomeActivity.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Intent intent= new Intent(MainActivity.this,ElegirFotoDePerfilActivity.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        MainActivity.this.finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }

    }

    private void setSliderPage() {
        lstSlides = new ArrayList<>();
        lstSlides.add(new SlideFirstScreen(R.drawable.background_pet1,null));
        lstSlides.add(new SlideFirstScreen(R.drawable.background_pet2,null));
        lstSlides.add(new SlideFirstScreen(R.drawable.background_pet3,null));
        lstSlides.add(new SlideFirstScreen(R.drawable.background_pet4,null));
        SliderPagerAdapter adapter = new SliderPagerAdapter(this,lstSlides);
        sliderPager.setAdapter(adapter);
        addDotsIndicator(0);
        sliderPager.addOnPageChangeListener(viewListener);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MainActivity.sliderTimer(),4000,6000);
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void addDotsIndicator(int position){
        mDots = new TextView[4];
        mDotLinearLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){
            mDots[i]= new TextView (this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.gray));
            mDotLinearLayout.addView(mDots[i]);
        }

        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }


    }



    class sliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderPager.getCurrentItem() < lstSlides.size() - 1) {
                        sliderPager.setCurrentItem(sliderPager.getCurrentItem() + 1);
                    } else {
                        sliderPager.setCurrentItem(0);
                    }


                }
            });
        }

    }

    private void createRequestGmail() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        //aca se lanza la ventanita con todas las cuentas de gmail para que selecciones una
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallBackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent= new Intent(MainActivity.this,ElegirFotoDePerfilActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Error al loguearse", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }
}