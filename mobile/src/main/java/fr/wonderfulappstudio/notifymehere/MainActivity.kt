package fr.wonderfulappstudio.notifymehere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import fr.wonderfulappstudio.notifymehere.theme.NotifyMeHereTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotifyMeHereTheme {
                Text(text = "Hello World!")
            }
        }
    }
}