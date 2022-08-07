package com.zp.tech.deleted.messages.status.saver.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.zp.tech.deleted.messages.status.saver.R;
import org.jetbrains.annotations.NotNull;

public class AdsManager {

    private Activity context;
    private MaxInterstitialAd maxInterstitialAd;

    private MaxNativeAdLoader maxNativeAdLoader;
    private MaxAd maxNativeAd;

    public AdsManager(Activity context) {
        this.context = context;
    }


    public void loadNativeBannerMax(RelativeLayout relativeLayout) {
        maxNativeAdLoader = new MaxNativeAdLoader(new PreferenceManager(context).getMaxNativeSmall(), context);
        maxNativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                // Clean up any pre-existing native ad to prevent memory leaks.
                if (maxNativeAd != null) {
                    maxNativeAdLoader.destroy(maxNativeAd);
                }

                // Save ad for cleanup.
                maxNativeAd = ad;

                // Add ad view to view.
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout.removeAllViews();
                relativeLayout.addView(nativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                loadBannerMAxMediation(relativeLayout);
                // We recommend retrying with exponentially higher delays up to a maximum delay
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
                // Optional click callback
            }
        });

        maxNativeAdLoader.loadAd(createNativeAdView());

    }

    private MaxNativeAdView createNativeAdView() {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.max_native_small)
                .setTitleTextViewId(R.id.title_text_view)
                .setBodyTextViewId(R.id.body_text_view)
//                .setAdvertiserTextViewId( R.id.advertiser_textView )
                .setIconImageViewId(R.id.icon_image_view)
//                .setMediaContentViewGroupId( R.id.media_view_container )
                .setOptionsContentViewGroupId(R.id.options_view)
                .setCallToActionButtonId(R.id.cta_button)
                .build();

        return new MaxNativeAdView(binder, context);
    }


    public void showInterstitial() {
        if (maxInterstitialAd.isReady()) {
            maxInterstitialAd.showAd();
        } else {
            loadMaxInterstitial();
        }
    }

    public void loadMaxInterstitial() {

        maxInterstitialAd = new MaxInterstitialAd(new PreferenceManager(context).getMaxInterstitial(), context);
        maxInterstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d("TAG", "onAdLoaded: ");
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {
                maxInterstitialAd.loadAd();
                Log.d("TAG", "onAdHidden: ");
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                Log.d("TAG", "onAdClicked: ");
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.d("TAG", "onAdLoadFailed: ");
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        });
        maxInterstitialAd.loadAd();
    }

    public void loadBannerMAxMediation(@NotNull RelativeLayout relTopBanner) {
        MaxAdView adView = new MaxAdView(new PreferenceManager(context).getMaxBanner(), context);
        adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                relTopBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        });

        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;

        // Banner height on phones and tablets is 50 and 90, respectively
        int heightPx = context.getResources().getDimensionPixelSize(R.dimen.banner_height);

        adView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));

        relTopBanner.addView(adView);

        // Load the ad
        adView.loadAd();
    }
}
