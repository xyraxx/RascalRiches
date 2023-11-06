package app.win11.rascalriches

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig


class LoadingActivity : AppCompatActivity() {
    private var hasUserConsent = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        window.setFlags(1024,1024)

        val globalConfig = application as GlobalConfig

        globalConfig.checkUC(this, this, true)
        hasUserConsent = globalConfig.getUserConsent()

        if(hasUserConsent){
            Handler(this.mainLooper).postDelayed({
                val appContent = Intent(this, MainActivity::class.java)
                appContent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(appContent)
            }, 1800)
        }
    }

   /* private fun permissionChecker(): Boolean {
        val location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val media =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        return location == PackageManager.PERMISSION_GRANTED
                && camera == PackageManager.PERMISSION_GRANTED
                && media == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val permissions =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val locationGranted = grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED
            val cameraGranted = grantResults.getOrNull(1) == PackageManager.PERMISSION_GRANTED
            val mediaGranted = grantResults.getOrNull(2) == PackageManager.PERMISSION_GRANTED

            sharedPref.edit {
                putBoolean("locationGranted", locationGranted)
                putBoolean("cameraGranted", cameraGranted)
                putBoolean("mediaGranted", mediaGranted)
                putBoolean("runOnce", locationGranted && cameraGranted && mediaGranted)
                apply()
            }
        }

        openActivity()
    }

    private fun openActivity() {
        if(!sharedPref.getBoolean("permitSendData", false)){
            val policyIntent = Intent(this, PolicyActivity::class.java)
            policyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(policyIntent)
            finish()
        }else{
            val gameIntent = Intent(this, MainActivity::class.java)
            gameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(gameIntent)
            finish()
        }

    }*/

}