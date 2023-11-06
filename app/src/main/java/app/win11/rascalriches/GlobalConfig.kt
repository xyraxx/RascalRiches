package app.win11.rascalriches

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.fullyInitialize
import com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled
import com.facebook.FacebookSdk.setAutoInitEnabled
import com.facebook.FacebookSdk.setAutoLogAppEventsEnabled
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings


class GlobalConfig : Application() {

    private var hasUserConsent : Boolean = false
    private lateinit var sharedPref : SharedPreferences

    companion object {
        private const val urlCode = "WB107817"
        var gameURL = ""
        private var userConsent = "userConsent"
        var policyURL = ""
    }

    override fun onCreate() {
        super.onCreate()

        sharedPref = getSharedPreferences(urlCode, Context.MODE_PRIVATE)
        hasUserConsent = sharedPref.getBoolean(userConsent, false)

        val appToken = "sxb6qsgwvhfk"
        val environment = AdjustConfig.ENVIRONMENT_SANDBOX
        val config = AdjustConfig(this, appToken, environment)
        config.setLogLevel(LogLevel.WARN)
        Adjust.onCreate(config)

        registerActivityLifecycleCallbacks(AdjustLifecycleCallbacks())

        FacebookSdk.fullyInitialize()
        AppEventsLogger.activateApp(this)

        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
    }

    private class AdjustLifecycleCallbacks : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {
            Adjust.onResume()
        }

        override fun onActivityPaused(activity: Activity) {
            Adjust.onPause()
        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {

        }
    }



     fun fbrcSetup(context: Context, activity: Activity, hasFirebase: Boolean) {

        if(hasFirebase){
            FirebaseApp.initializeApp(context)
            val fbrc = FirebaseRemoteConfig.getInstance()
            val fbrcSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build()
            fbrc.setConfigSettingsAsync(fbrcSettings)
            fbrc.fetchAndActivate().addOnCompleteListener(activity
            ) { task: Task<Boolean?> ->
                if (task.isSuccessful) {
                    gameURL = fbrc.getString("apiURL") + "?appid=" + urlCode
                    Log.d("gameURL",gameURL)
                    policyURL = fbrc.getString("policyURL")
                    Log.d("policyURL", policyURL)
                }
            }
        }
    }

    private fun showConsentDialog(context: Context, activity: Activity) {
        val builder = AlertDialog.Builder(context)
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.consent_dialog, null)
        val consent = dialogView.findViewById<WebView>(R.id.consentWV)
        val privacyPolicyURL = "https://win11apps.site/policy"
        consent.webViewClient = WebViewClient()
        consent.loadUrl(privacyPolicyURL)
        builder.setTitle("Data Privacy Policy")
        builder.setView(dialogView)
        builder.setPositiveButton("Accept"
        ) { _: DialogInterface?, _: Int ->
            setConsentValue(true)
            loadActivity(activity)
        }
        builder.setNegativeButton(
            "Decline",
            (DialogInterface.OnClickListener { _: DialogInterface?, _: Int -> activity.finishAffinity() })
        )
        builder.show()
    }

    private fun setConsentValue(userChoice: Boolean) {
        hasUserConsent = userChoice
        setAutoInitEnabled(userChoice);
        fullyInitialize();
        setAutoLogAppEventsEnabled(userChoice);
        setAdvertiserIDCollectionEnabled(userChoice);

        //FACEBOOK,ADJUST,APPSFLYER
        //FacebookSdk.setAutoInitEnabled(userChoice);
        //FacebookSdk.fullyInitialize();
        //setAutoLogAppEventsEnabled(userChoice);
        //setAdvertiserIDCollectionEnabled(userChoice);
        val sp = getSharedPreferences(urlCode, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(userConsent, userChoice)
        editor.apply()
        editor.commit()

    }

    fun getUserConsent(): Boolean {
        val sp = getSharedPreferences(urlCode, MODE_PRIVATE)
        hasUserConsent = sp.getBoolean(userConsent, false)
        return hasUserConsent
    }

    fun checkUC(context: Context, activity: Activity, hasPolicy: Boolean) {

        fbrcSetup(context, activity, true)

        if (!hasUserConsent && java.lang.Boolean.TRUE == hasPolicy) {
            showConsentDialog(context, activity)
        }
    }

    private fun loadActivity(activity: Activity) {
        val newActivity = Intent(activity, LoadingActivity::class.java)
        newActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(newActivity)
    }


}