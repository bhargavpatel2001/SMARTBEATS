//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA
package ca.shalominc.it.smartbeats;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ca.shalominc.it.smartbeats.ui.settings.SettingsFragment;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/privacyPolicy.html");

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
    }
}


