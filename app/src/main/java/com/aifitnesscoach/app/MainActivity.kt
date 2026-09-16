package com.aifitnesscoach.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aifitnesscoach.app.ui.navigation.AppNavGraph
import com.aifitnesscoach.app.ui.theme.AiFitnessCoachTheme
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = (application as AiFitnessApp).repository
        setContent {
            AiFitnessCoachTheme {
                AppRoot(repository)
            }
        }
    }
}

@Composable
private fun AppRoot(repository: com.aifitnesscoach.app.data.repository.FitnessRepository) {
    var hasProfile by remember { mutableStateOf<Boolean?>(null) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        hasProfile = repository.observeProfile().first() != null
    }

    val resolved = hasProfile
    if (resolved == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        AppNavGraph(repository = repository, hasProfile = resolved)
    }
}
