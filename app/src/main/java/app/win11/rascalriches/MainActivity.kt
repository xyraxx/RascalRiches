package app.win11.rascalriches

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import app.win11.rascalriches.databinding.ActivityMainBinding
import com.adjust.sdk.webbridge.AdjustBridge
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var adView: AdView


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(1024,1024)

       /* val gameContent = binding.gameContent
        gameContent.webViewClient = WebViewClient()
        gameContent.settings.javaScriptEnabled = true
        binding.gameContent.loadUrl(GlobalConfig.gameURL)*/

        adView = binding.adView
        /*AdjustBridge.registerAndGetInstance(getApplication(), gameContent);

        adView = binding.adView

        try {
            binding.gameContent.loadUrl(GlobalConfig.gameURL)
        } catch (e: Exception) {
            e.printStackTrace()
        }

*/
        binding.gameContent.loadUrl(GlobalConfig.gameURL)

        if(GlobalConfig.gameURL.contains("WB107817")){
            Log.d("appCode", "true")
            MobileAds.initialize(this) {
                Log.d(
                    "AdMobs",
                    "Initialized Complete."
                )
            }

            val adRequest: AdRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
        else{
            Log.d("appCode", "false")
        }

    }


}