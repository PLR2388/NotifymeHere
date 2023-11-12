package fr.wonderfulappstudio.notifymehere.ui.map


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : ComponentActivity() {

    companion object {
        const val LATITUDE_EXTRA_KEY: String = "latitude"
        const val LONGITUDE_EXTRA_KEY: String = "longitude"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val latitude = intent.getFloatExtra(LATITUDE_EXTRA_KEY, 0.0f)
        val longitude = intent.getFloatExtra(LONGITUDE_EXTRA_KEY, 0.0f)
        setContent {
            MapScreen(
                selectedPosition = Pair(latitude, longitude),
                onNavigateBack = { finish() },
                onValidatePicker = {
                    val data = Intent().apply {
                        putExtra(LATITUDE_EXTRA_KEY, it.first)
                        putExtra(LONGITUDE_EXTRA_KEY, it.second)
                    }
                    setResult(Activity.RESULT_OK, data)
                    finish()
                }
            )
        }
    }
}

fun Context.startMapActivityForResult(
    launcher: ActivityResultLauncher<Intent>,
    latitude: Float,
    longitude: Float
) {
    val intent = Intent(this, MapActivity::class.java).apply {
        putExtra(MapActivity.LATITUDE_EXTRA_KEY, latitude)
        putExtra(MapActivity.LONGITUDE_EXTRA_KEY, longitude)
    }
    launcher.launch(intent)
}