package com.example.demoappodeal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.NativeAd;
import com.example.demoappodeal.R;
import com.example.libads.AppODealAZ;
import com.example.libads.callback.InterCallback;
import com.example.libads.callback.NativeCallback;
import com.explorestack.consent.Consent;
import com.explorestack.consent.ConsentManager;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static final String CONSENT = "consent";
    private String APP_KEY = "a212b1d9954d7811616c72c1600a5a7050ab552e06483bf7";
    boolean consent;

    private List<NativeAd> nativeAds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (savedInstanceState == null) {
            consent = getIntent().getBooleanExtra(CONSENT, false);
        } else {
            Consent.Status consentStatus = ConsentManager.getInstance(this).getConsentStatus();
            consent = consentStatus == Consent.Status.PERSONALIZED
                    || consentStatus == Consent.Status.PARTLY_PERSONALIZED;
        }

        AppODealAZ.getInstance().init(this, APP_KEY, false);
        AppODealAZ.getInstance().loadBanner(this, APP_KEY, Appodeal.BANNER_VIEW, false);

        AppODealAZ.getInstance().loadSplashInter(this, APP_KEY, false, new InterCallback() {
            @Override
            public void adClosed() {
                super.adClosed();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 30000);
        AppODealAZ.getInstance().loadAndShowNative(this, APP_KEY, Appodeal.NATIVE, false, new NativeCallback());
    }

    /* Inter */
    public void initInterSdk(View v) {
        AppODealAZ.getInstance().loadInter(this, APP_KEY, false, new InterCallback());
    }

    public void showInterSdk(View v) {
        AppODealAZ.getInstance().showInter(this, new InterCallback() {
            @Override
            public void adClosed() {
                super.adClosed();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

            @Override
            public void adFailedToLoad() {
                super.adFailedToLoad();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

            }
        });
    }

    /* Native */
//    public void initNativeSdk() {
//        Appodeal.initialize(this, APP_KEY, Appodeal.NATIVE, consent);
//        Appodeal.setNativeCallbacks(new AdNativeCallback(this));
//    }

//    public void showNativeSdk(View v) {
//        Appodeal.cache(this, Appodeal.NATIVE, 1);
//        nativeAds = Appodeal.getNativeAds(1);
//        LinearLayout nativeAdsListView = findViewById(R.id.nativeAdsListView);
//        fillCustomNativeAdView(nativeAds.get(0), nativeAdsListView);
//        nativeAdsListView.addView(fillCustomNativeAdView(nativeAds.get(0), nativeAdsListView));
//    }
//
//
//    private NativeAdView fillCustomNativeAdView(NativeAd nativeAd, LinearLayout nativeListView) {
//        NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(nativeListView.getContext()).inflate(R.layout.native_ads,
//                nativeListView, false);
//        TextView tvTitle = nativeAdView.findViewById(R.id.tv_title);
//        tvTitle.setText(nativeAd.getTitle());
//        nativeAdView.setTitleView(tvTitle);
//        TextView tvDescription = nativeAdView.findViewById(R.id.tv_description);
//        tvDescription.setText(nativeAd.getDescription());
//        nativeAdView.setDescriptionView(tvDescription);
//        RatingBar ratingBar = nativeAdView.findViewById(R.id.rb_rating);
//        if (nativeAd.getRating() == 0) {
//            ratingBar.setVisibility(View.INVISIBLE);
//        } else {
//            ratingBar.setVisibility(View.VISIBLE);
//            ratingBar.setRating(nativeAd.getRating());
//            ratingBar.setStepSize(0.1f);
//        }
//        nativeAdView.setRatingView(ratingBar);
//        Button ctaButton = nativeAdView.findViewById(R.id.b_cta);
//        ctaButton.setText(nativeAd.getCallToAction());
//        nativeAdView.setCallToActionView(ctaButton);
//        NativeIconView nativeIconView = nativeAdView.findViewById(R.id.icon);
//        nativeAdView.setNativeIconView(nativeIconView);
//        View providerView = nativeAd.getProviderView(nativeListView.getContext());
//        if (providerView != null) {
//            if (providerView.getParent() != null && providerView.getParent() instanceof ViewGroup) {
//                ((ViewGroup) providerView.getParent()).removeView(providerView);
//            }
//            FrameLayout providerViewContainer = nativeAdView.findViewById(R.id.provider_view);
//            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            providerViewContainer.addView(providerView, layoutParams);
//        }
//        nativeAdView.setProviderView(providerView);
//        TextView tvAgeRestrictions = nativeAdView.findViewById(R.id.tv_age_restriction);
//        if (nativeAd.getAgeRestrictions() != null) {
//            tvAgeRestrictions.setText(nativeAd.getAgeRestrictions());
//            tvAgeRestrictions.setVisibility(View.VISIBLE);
//        } else {
//            tvAgeRestrictions.setVisibility(View.GONE);
//        }
//        NativeMediaView nativeMediaView = nativeAdView.findViewById(R.id.appodeal_media_view_content);
//        nativeAdView.setNativeMediaView(nativeMediaView);
//        nativeAdView.registerView(nativeAd, "default");
//        nativeAdView.setVisibility(View.VISIBLE);
//        return nativeAdView;
//    }

}