package com.song.scribe

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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

        setContent {
            val settingsRepo = koinInject<SettingsRepo>()

            val themeMode by settingsRepo.themeMode.collectAsStateWithLifecycle(
                if (isSystemInDarkTheme()) {
                    ThemeMode.DARK
                } else {
                    ThemeMode.LIGHT
                }
            )

            val darkTheme = themeMode == ThemeMode.DARK

            // enableEdgeToEdge()'s default style derives bar-icon appearance from the SYSTEM's
            // dark mode and keeps re-applying it on every inset dispatch, which fights with the
            // app's own theme toggle (SongScribeTheme's SystemBarsEffect). Passing a
            // detectDarkMode lambda ties both to the same darkTheme flag instead.
            SideEffect {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme }
                )
            }

            SongScribeTheme(
                darkTheme = darkTheme
            ) {
                val focusManager = LocalFocusManager.current
                val keyboardController = LocalSoftwareKeyboardController.current

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        },
                    color = MaterialTheme.colorScheme.background
                ) {
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