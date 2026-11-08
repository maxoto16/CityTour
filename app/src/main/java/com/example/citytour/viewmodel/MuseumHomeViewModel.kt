package com.example.citytour.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.citytour.R
import com.example.citytour.model.HistoricalMonument

class MuseumHomeViewModel : ViewModel() {
    var monuments by mutableStateOf<List<HistoricalMonument>>(emptyList())
        private set

    var selectedMonument by mutableStateOf<HistoricalMonument?>(null)
        private set

    init {
        loadMonuments()
    }

    private fun loadMonuments() {
        monuments = listOf(
            HistoricalMonument(
                id = "1",
                name = "Castillo de Chapultepec",
                location = "Ciudad de México",
                summary = "Único castillo real en América y Museo Nacional de Historia.",
                description = "El Castillo de Chapultepec se encuentra ubicado en el Bosque de Chapultepec. Su construcción comenzó en 1785 durante el virreinato. Fue colegio militar, residencia imperial de Maximiliano y Carlota, y testigo de batallas históricas clave para la nación.",
                imageResIds = listOf(
                    R.drawable.castillo1,
                    R.drawable.castillo2,
                    R.drawable.castillo3,
                    R.drawable.castillo4
                ),
                audioResId = R.raw.audio
            ),
            HistoricalMonument(
                id = "2",
                name = "Zona Arqueológica de Chichén Itzá",
                location = "Yucatán, México",
                summary = "Maravilla del mundo moderno y centro de la civilización maya.",
                description = "Chichén Itzá fue una ciudad maya de gran esplendor. Su imponente pirámide de Kukulcán demuestra el avanzado conocimiento astronómico, matemático y arquitectónico de esta gran cultura mesoamericana.",
                imageResIds = listOf(
                    R.drawable.castillo1,
                    R.drawable.castillo2,
                    R.drawable.castillo3,
                    R.drawable.castillo4
                ),
                audioResId = R.raw.audio
            )
        )
    }

    fun selectMonument(monument: HistoricalMonument?) {
        selectedMonument = monument
    }
}
