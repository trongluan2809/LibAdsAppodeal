package com.example.libads;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.BannerView;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeAdView;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.NativeIconView;
import com.appodeal.ads.NativeMediaView;
import com.example.libads.callback.InterCallback;
import com.example.libads.callback.NativeCallback;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;


public class AppODealAZ {
    private static final String TAG = "AppODealAZ";
    private static AppODealAZ instance;
    private PrepareLoadingAdsDialog dialog;
    private boolean isTimeout; // xử lý timeout show ads
    private Handler handlerTimeout;
    private Runnable rdTimeout;
    private boolean openActivityAfterShowInterAds = false;


    public static AppODealAZ getInstance() {
        if (instance == null) {
            instance = new AppODealAZ();
        }
        return instance;
    }

    public void init(Activity activity, String app_key, boolean consent) {
        String userId = Appodeal.getUserId();
        Appodeal.setUserId(userId);
        Appodeal.initialize(activity, app_key, Appodeal.NONE, consent);
    }

    /* Load and show inter splash */
    public void loadSplashInter(Activity activity, String app_key, boolean consent, InterCallback adListener, int timeOut) {
        isTimeout = false;
        if (timeOut > 0) {
            handlerTimeout = new Handler();
            rdTimeout = new Runnable() {
                @Override
                public void run() {
                    isTimeout = true;
                    if (Appodeal.isInitialized(Appodeal.INTERSTITIAL) && Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
                        showInter(activity, adListener);
                        return;
                    }
                    if (adListener != null) {
                        adListener.adClosed();
                    }
                }
            };
            handlerTimeout.postDelayed(rdTimeout, timeOut);
        }
        Appodeal.initialize(activity, app_key, Appodeal.INTERSTITIAL, consent);
        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean isPrecache) {
                if (!isTimeout)
                    showInter(activity, adListener);
                Log.e(TAG, "onInterstitialLoaded: " + isPrecache);
            }

            @Override
            public void onInterstitialFailedToLoad() {
                adListener.adFailedToLoad();
                Log.e(TAG, "onInterstitialFailedToLoad: ");
            }

            @Override
            public void onInterstitialShown() {
                adListener.adShown();
                Log.e(TAG, "onInterstitialShown: ");
            }

            @Override
            public void onInterstitialShowFailed() {
                adListener.adShowFailed();
                Log.e(TAG, "onInterstitialShowFailed: ");
            }

            @Override
            public void onInterstitialClicked() {
                adListener.adClicked();
                Log.e(TAG, "onInterstitialClicked: ");
            }

            @Override
            public void onInterstitialClosed() {
                adListener.adClosed();
                Log.e(TAG, "onInterstitialClosed: ");
            }

            @Override
            public void onInterstitialExpired() {
                adListener.adExpired();
                Log.e(TAG, "onInterstitialExpired: ");
            }
        });
    }


    /* Ads Inter */
    public void loadInter(Activity activity, String app_key, boolean consent, InterCallback adListener) {
        String userId = Appodeal.getUserId();
        Appodeal.setUserId(userId);
        Appodeal.initialize(activity, app_key, Appodeal.INTERSTITIAL, consent);
        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean isPrecache) {
                adListener.adLoaded(isPrecache);
                Log.e(TAG, "onInterstitialLoaded: " + isPrecache);
            }

            @Override
            public void onInterstitialFailedToLoad() {
                adListener.adFailedToLoad();
                Log.e(TAG, "onInterstitialFailedToLoad: ");
            }

            @Override
            public void onInterstitialShown() {
                adListener.adShown();
                Log.e(TAG, "onInterstitialShown: ");
            }

            @Override
            public void onInterstitialShowFailed() {
                adListener.adShowFailed();
                Log.e(TAG, "onInterstitialShowFailed: ");
            }

            @Override
            public void onInterstitialClicked() {
                adListener.adClicked();
                Log.e(TAG, "onInterstitialClicked: ");
            }

            @Override
            public void onInterstitialClosed() {
                adListener.adClosed();
                Log.e(TAG, "onInterstitialClosed: ");
            }

            @Override
            public void onInterstitialExpired() {
                adListener.adExpired();
                Log.e(TAG, "onInterstitialExpired: ");
            }
        });
    }

    public void showInter(Activity activity, InterCallback adListener) {
        if (handlerTimeout != null && rdTimeout != null) {
            handlerTimeout.removeCallbacks(rdTimeout);
        }

        if (Appodeal.isInitialized(Appodeal.INTERSTITIAL) && Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
            Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                @Override
                public void onInterstitialLoaded(boolean isPrecache) {
                    adListener.adLoaded(isPrecache);
                    Log.e(TAG, "onInterstitialLoaded: " + isPrecache);
                }

                @Override
                public void onInterstitialFailedToLoad() {
                    adListener.adFailedToLoad();
                    Log.e(TAG, "onInterstitialFailedToLoad: ");
                }

                @Override
                public void onInterstitialShown() {
                    adListener.adShown();
                    Log.e(TAG, "onInterstitialShown: ");
                }

                @Override
                public void onInterstitialShowFailed() {
                    adListener.adShowFailed();
                    Log.e(TAG, "onInterstitialShowFailed: ");
                }

                @Override
                public void onInterstitialClicked() {
                    adListener.adClicked();
                    Log.e(TAG, "onInterstitialClicked: ");
                }

                @Override
                public void onInterstitialClosed() {
                    try {
                        if (dialog != null && !activity.isDestroyed())
                            dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (!openActivityAfterShowInterAds) {
                        adListener.adClosed();
                    }
                    Log.e(TAG, "onInterstitialClosed: ");
                }

                @Override
                public void onInterstitialExpired() {
                    adListener.adExpired();
                    Log.e(TAG, "onInterstitialExpired: ");
                }
            });
            if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                try {
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    dialog = new PrepareLoadingAdsDialog(activity);
                    try {
                        dialog.show();
                    } catch (Exception e) {
                        adListener.adClosed();
                        return;
                    }
                } catch (Exception e) {
                    dialog = null;
                    e.printStackTrace();
                }
                new Handler().postDelayed(() -> {
                    if (openActivityAfterShowInterAds && adListener != null) {
                        adListener.adClosed();
                    }
                    Appodeal.show(activity, Appodeal.INTERSTITIAL);
                }, 800);

            }
        } else {
            adListener.adFailedToLoad();
        }

    }

    /* Ads Banner */
    public void loadBanner(final Activity activity, String app_key, int adTypes, boolean consent) {
        String userId = Appodeal.getUserId();
        Appodeal.setUserId(userId);
        Appodeal.initialize(activity, app_key, adTypes, consent);
        final FrameLayout adContainer = activity.findViewById(R.id.banner_container);
        final ShimmerFrameLayout containerShimmer = activity.findViewById(R.id.shimmer_container_banner);
        loadBanner(activity, adContainer, adTypes, containerShimmer);
    }

    public void loadBanner(Activity activity, FrameLayout frameLayoutBanner, int adTypes, final ShimmerFrameLayout containerShimmer) {
        //show shimmer loading
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        BannerView bannerView = activity.findViewById(R.id.banner_view);
        if (Appodeal.isInitialized(adTypes)) {
            if (!Appodeal.isLoaded(adTypes)) {
                Appodeal.setBannerCallbacks(new BannerCallbacks() {
                    @Override
                    public void onBannerLoaded(int height, boolean isPrecache) {
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);
                        frameLayoutBanner.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onBannerFailedToLoad() {
                        containerShimmer.stopShimmer();
                        containerShimmer.setVisibility(View.GONE);
                        frameLayoutBanner.setVisibility(View.GONE);
                    }

                    @Override
                    public void onBannerShown() {

                    }

                    @Override
                    public void onBannerShowFailed() {

                    }

                    @Override
                    public void onBannerClicked() {

                    }

                    @Override
                    public void onBannerExpired() {

                    }
                });
                Appodeal.setBannerViewId(bannerView.getId());
            } else {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
                frameLayoutBanner.setVisibility(View.VISIBLE);
            }
            Appodeal.show(activity, adTypes);
        } else {
            containerShimmer.stopShimmer();
            containerShimmer.setVisibility(View.GONE);
            frameLayoutBanner.setVisibility(View.VISIBLE);
        }
    }

    /* Ads Native */
    public void loadAndShowNative(Activity activity, String app_key, int adTypes, boolean consent, NativeCallback callback) {
        String userId = Appodeal.getUserId();
        Appodeal.setUserId(userId);
        Appodeal.initialize(activity, app_key, adTypes, consent);
        FrameLayout frameLayout = activity.findViewById(R.id.fl_placeholder);
        ShimmerFrameLayout containerShimmer = activity.findViewById(R.id.shimmer_container_native);
        loadAndShowNative(frameLayout, containerShimmer, R.layout.native_ads, callback);
    }

    public void loadAndShowNative(FrameLayout frameLayout, ShimmerFrameLayout containerShimmer, int layout, NativeCallback adListener) {
        frameLayout.removeAllViews();
        frameLayout.setVisibility(View.GONE);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        final List<NativeAd>[] nativeAds = new List[]{new ArrayList()};
        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                Log.e(TAG, "onNativeLoaded: ");
                frameLayout.setVisibility(View.VISIBLE);
                containerShimmer.setVisibility(View.GONE);
                containerShimmer.stopShimmer();
                frameLayout.removeAllViews();

                nativeAds[0] = Appodeal.getNativeAds(1);
                frameLayout.addView(fillCustomNativeAdView(nativeAds[0].get(0), layout, frameLayout));
            }

            @Override
            public void onNativeFailedToLoad() {
                adListener.onNativeFailedToLoad();
                frameLayout.setVisibility(View.GONE);
                containerShimmer.setVisibility(View.GONE);
                containerShimmer.stopShimmer();
                Log.e(TAG, "onNativeFailedToLoad: ");
            }

            @Override
            public void onNativeShown(NativeAd nativeAd) {
                Log.e(TAG, "onNativeShown: ");
                adListener.onNativeShown(nativeAd);
            }

            @Override
            public void onNativeShowFailed(NativeAd nativeAd) {
                frameLayout.setVisibility(View.GONE);
                containerShimmer.setVisibility(View.GONE);
                containerShimmer.stopShimmer();

                adListener.onNativeFailedToLoad();
                Log.e(TAG, "onNativeShowFailed: ");
            }

            @Override
            public void onNativeClicked(NativeAd nativeAd) {
                adListener.onNativeClicked(nativeAd);
                Log.e(TAG, "onNativeClicked: ");
            }

            @Override
            public void onNativeExpired() {
                adListener.onNativeExpired();
                Log.e(TAG, "onNativeExpired: ");
            }
        });


    }

    private NativeAdView fillCustomNativeAdView(NativeAd nativeAd, int layout, FrameLayout nativeListView) {
        NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(nativeListView.getContext()).inflate(layout,
                nativeListView, false);
        TextView tvTitle = nativeAdView.findViewById(R.id.tv_title);
        tvTitle.setText(nativeAd.getTitle());
        nativeAdView.setTitleView(tvTitle);
        TextView tvDescription = nativeAdView.findViewById(R.id.tv_description);
        tvDescription.setText(nativeAd.getDescription());
        nativeAdView.setDescriptionView(tvDescription);
        RatingBar ratingBar = nativeAdView.findViewById(R.id.rb_rating);
        if (nativeAd.getRating() == 0) {
            ratingBar.setVisibility(View.INVISIBLE);
        } else {
            ratingBar.setVisibility(View.VISIBLE);
            ratingBar.setRating(nativeAd.getRating());
            ratingBar.setStepSize(0.1f);
        }
        nativeAdView.setRatingView(ratingBar);
        Button ctaButton = nativeAdView.findViewById(R.id.b_cta);
        ctaButton.setText(nativeAd.getCallToAction());
        nativeAdView.setCallToActionView(ctaButton);
        NativeIconView nativeIconView = nativeAdView.findViewById(R.id.icon);
        nativeAdView.setNativeIconView(nativeIconView);
        View providerView = nativeAd.getProviderView(nativeListView.getContext());
        if (providerView != null) {
            if (providerView.getParent() != null && providerView.getParent() instanceof ViewGroup) {
                ((ViewGroup) providerView.getParent()).removeView(providerView);
            }
            FrameLayout providerViewContainer = nativeAdView.findViewById(R.id.provider_view);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            providerViewContainer.addView(providerView, layoutParams);
        }
        nativeAdView.setProviderView(providerView);
        TextView tvAgeRestrictions = nativeAdView.findViewById(R.id.tv_age_restriction);
        if (nativeAd.getAgeRestrictions() != null) {
            tvAgeRestrictions.setText(nativeAd.getAgeRestrictions());
            tvAgeRestrictions.setVisibility(View.VISIBLE);
        } else {
            tvAgeRestrictions.setVisibility(View.GONE);
        }
        NativeMediaView nativeMediaView = nativeAdView.findViewById(R.id.appodeal_media_view_content);
        nativeAdView.setNativeMediaView(nativeMediaView);
        nativeAdView.registerView(nativeAd, "default");
        nativeAdView.setVisibility(View.VISIBLE);
        return nativeAdView;
    }

}
