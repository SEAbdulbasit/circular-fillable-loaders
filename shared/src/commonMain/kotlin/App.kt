import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        val waterLevel = remember { mutableStateOf(50) }
        val wavesAmplitude = remember { mutableStateOf<Float>(0.03f) }

        val image = remember { mutableStateOf<ImageBitmap?>(null) }

        LaunchedEffect(Unit) {
            image.value = resource("5.jpg").readBytes().toImageBitmap()
        }


        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                CircularFillableLoaders(
                    modifier = Modifier,
                    waterLevel = waterLevel,
                    value = image.value,
                    wavesAmplitude = wavesAmplitude.value,
                )
                Text(text = "Progress")
                Slider(
                    modifier = Modifier.widthIn(200.dp, max = 200.dp),
                    value = waterLevel.value.toFloat(),
                    onValueChange = {
                        waterLevel.value = it.toInt()
                    },
                    valueRange = 1f.rangeTo(99f),
                )

                Text(text = "Waves Amplitude")
                Slider(
                    modifier = Modifier.widthIn(200.dp, max = 200.dp),
                    value = wavesAmplitude.value,
                    onValueChange = {
                        wavesAmplitude.value = it
                    },
                    valueRange = 0.03f.rangeTo(0.10f),
                )
            }
        }

    }
}
