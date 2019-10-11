package com.prod.ademo.allezlesbleus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Acceuil extends Activity {

    private TextView mTextMessage;
    private Context mContext;
    private Context activity;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        mContext=mContext = getApplicationContext();
        activity=Acceuil.this;
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-4617289986501343~7980589429");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4617289986501343/9456612182");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded()
            {
                mInterstitialAd.show();
            }
            //Permet de charger un autre intertitial au moment de la fermeture du précédent
            /*@Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }*/

        });
    }

    public void regarder(View v){
        //Intent intent = new Intent(mContext,Map.class);
        Intent intent = new Intent(mContext,Map.class);
        startActivityForResult(intent, 0);
    }

    public void tv(View v){
        //Intent intent = new Intent(mContext,Map.class);
        Intent intent = new Intent(mContext,Tv.class);
        startActivityForResult(intent, 0);
    }

    public void palmares(View v){
        Intent intent = new Intent(mContext,Palmares.class);
        startActivityForResult(intent, 0);
    }

    public void histoires(View v){
        Intent intent = new Intent(mContext,Histoires.class);
        startActivityForResult(intent, 0);
    }

    public void chante(View v){
        MediaPlayer ring = MediaPlayer.create (activity, R.raw.bleu1);
        ring.start ();
    }

}
