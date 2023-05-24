/**
 * Created by abdulbasit on 21/05/2023.
 */

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CircularFillableLoaders(
    modifier: Modifier,
    waterLevel: MutableState<Int>,
    value: ImageBitmap?,
    wavesAmplitude: Float,
    wavesColor: Color,
) {
    Box(
        modifier = Modifier.size(300.dp), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.wrapContentSize().clip(RoundedCornerShape(100)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawCircle(
                    color = Color.Black,
                    radius = 120.dp.toPx(),
                )
                drawCircle(
                    brush = SolidColor(Color.DarkGray),
                    radius = 110.dp.toPx(),
                    style = Stroke(width = 0.dp.toPx())
                )
            }
            value?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = modifier.fillMaxSize().align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
            }

            WavesLoadingIndicator(
                modifier = modifier,
                progress = waterLevel,
                wavesAmplitude = wavesAmplitude,
                wavesColor = wavesColor
            )
        }
    }
}

