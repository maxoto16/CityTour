package com.example.citytour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.citytour.ui.MonumentDetailScreen
import com.example.citytour.ui.MuseumHomeScreen
import com.example.citytour.ui.theme.CityTourTheme
import com.example.citytour.viewmodel.MonumentDetailViewModel
import com.example.citytour.viewmodel.MuseumHomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CityTourTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VirtualMuseumApp()
                }
            }
        }
    }
}

@Composable
fun VirtualMuseumApp(
    homeViewModel: MuseumHomeViewModel = viewModel(),
    detailViewModel: MonumentDetailViewModel = viewModel()
) {
    val selectedMonument = homeViewModel.selectedMonument

    if (selectedMonument == null) {
        MuseumHomeScreen(
            viewModel = homeViewModel,
            onMonumentClick = { monument ->
                homeViewModel.selectMonument(monument)
            }
        )
    } else {
        MonumentDetailScreen(
            monument = selectedMonument,
            viewModel = detailViewModel,
            onBackClick = {
                detailViewModel.stop()
                homeViewModel.selectMonument(null)
            }
        )
    }
}
