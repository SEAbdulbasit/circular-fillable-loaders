import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WavesLoadingIndicator(
    modifier: Modifier,
    color: Color,
    progress: MutableState<Int>,
    wavesAmplitude: Float
) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        AnimatedPathTranslation(color, progress, wavesAmplitude)
    }
}

@Composable
fun AnimatedPathTranslation(color: Color, progress: MutableState<Int>, wavesAmplitude: Float) {
    val transition = rememberInfiniteTransition()

    val waveShiftRatio = transition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            tween(
                durationMillis = WavesShiftAnimationDurationInMillis, easing = LinearEasing
            )
        )
    )


    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val wavePath = createPath(
            canvasWidth.toInt(),
            canvasHeight.toInt(),
            waveShiftRatio.value,
            progress.value,
            wavesAmplitude
        )
        drawPath(
            color = (color.copy(alpha = 0.5f)),
            path = wavePath[0],
        )

        translate(0f, 20f) {
            drawPath(
                color = (color), path = wavePath[1]
            )
        }
    }
}

private fun createPath(
    width: Int,
    height: Int,
    waveShiftRatio: Float,
    progress: Int,
    wavesAmplitude: Float,
): List<Path> {
    val angularFrequency = 2f * PI / width
    val amplitude = height * wavesAmplitude
    val waterLevel = (height / progress * 10).toFloat()

    val originalPath = Path().apply {
        val startX = 0f

        moveTo(startX, waterLevel)

        for (x in 0..width) {
            val wx = x * angularFrequency
            val y = waterLevel + amplitude * sin(wx + waveShiftRatio * 2 * PI).toFloat()
            lineTo(x.toFloat(), y)
        }
        lineTo(width.toFloat(), height.toFloat())
        lineTo(startX, height.toFloat())
        close()
    }

    val path2 = Path().apply {
        val startX = 0f

        moveTo(startX, waterLevel)

        for (x in 0..width) {
            val wx = x * angularFrequency + 0.8
            val y = waterLevel + amplitude * sin(wx + waveShiftRatio * 2 * PI).toFloat()
            lineTo(x.toFloat(), y)
        }
        lineTo(width.toFloat(), height.toFloat())
        lineTo(startX, height.toFloat())
        close()
    }

    return listOf(originalPath, path2)
}

private const val WavesShiftAnimationDurationInMillis = 2500
