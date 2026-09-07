package com.song.scribe

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.song.core.domain.settings.SettingsRepo
import com.song.core.domain.settings.ThemeMode
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import org.koin.compose.koinInject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepo = koinInject<SettingsRepo>()
            val themeMode by settingsRepo.themeMode.collectAsStateWithLifecycle(
                if (isSystemInDarkTheme()) ThemeMode.DARK else ThemeMode.LIGHT
            )

            SongScribeTheme(darkTheme = themeMode == ThemeMode.DARK) {
                Surface(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
                    NavigationRoot(
                        navController = rememberNavController()
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SongScribeTheme {
        Greeting("Android")
    }
}