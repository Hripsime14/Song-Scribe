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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.song.core.domain.settings.SettingsRepo
import com.song.core.domain.settings.ThemeMode
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
class MainActivity : AppCompatActivity() {

    private var isThemeReady = false
    private val splashDismissed = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash up until the app's own saved theme has loaded from DataStore, so a
        // user with saved Dark mode doesn't see a flash of the light windowBackground (which is
        // only aware of the SYSTEM's dark mode) before Compose draws with the real theme.
        splashScreen.setKeepOnScreenCondition { !isThemeReady }

        // Removing the splash screen's view hands focus back to the window, and Android's
        // default "focus the first focusable view" behavior can land it on the first text field
        // on screen (e.g. the My Demos search bar). Track the removal so we can explicitly clear
        // whatever focus that lands, once it's actually possible to do so.
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            splashScreenViewProvider.remove()
            splashDismissed.value = true
        }

        setContent {
            val settingsRepo = koinInject<SettingsRepo>()

            LaunchedEffect(Unit) {
                settingsRepo.themeMode.first()
                isThemeReady = true
            }

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
                val isSplashDismissed by splashDismissed

                LaunchedEffect(isSplashDismissed) {
                    if (isSplashDismissed) {
                        focusManager.clearFocus(force = true)
                    }
                }

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