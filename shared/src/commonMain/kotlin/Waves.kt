import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WavesLoadingIndicator2(modifier: Modifier, color: Color, progress: Float) {
    BoxWithConstraints(modifier = modifier.offset(y = 16.dp), contentAlignment = Alignment.Center) {
        val constraintsWidth = maxWidth
        val constraintsHeight = maxHeight
        val density = LocalDensity.current

        val wavesShader by produceState<Path?>(
            initialValue = null,
            constraintsHeight,
            constraintsWidth,
            color,
            density
        ) {
            value = withContext(Dispatchers.Default) {
                createWavesPath(
                    width = with(density) { constraintsWidth.roundToPx() },
                    height = with(density) { constraintsHeight.roundToPx() },
                )
            }
        }

        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = WavesShiftAnimationDurationInMillis),
                repeatMode = RepeatMode.Restart
            )
        )

        if (animatedProgress > 0f && wavesShader != null) {
            //WavesOnCanvas(wavePath = wavesShader!!, progress = animatedProgress.coerceAtMost(0.99f))
            //AnimatedPath(modifier = Modifier.fillMaxSize(), path = wavesShader!!, amplitude = 0.0f,)
            AnimatedPath(
                modifier = Modifier.fillMaxSize(),
                path = wavesShader!!,
                amplitude = 10f,
                durationMillis = 2000
            )

        }
    }
}

private fun calculateWaveOffset(animatedProgress: Float, canvasHeight: Float): Float {
    val amplitude = 50f // Adjust the wave amplitude as needed
    val frequency = 2f // Adjust the wave frequency as needed
    val progressOffset = (animatedProgress * 2 * PI).toFloat()
    return amplitude * sin(frequency * progressOffset) + (canvasHeight / 2f)
}

@Composable
fun AnimatedPath(modifier: Modifier = Modifier, path: Path, amplitude: Float, durationMillis: Int) {
    var animationProgress by remember { mutableStateOf(0f) }
    val transition = rememberInfiniteTransition()
    val animatedProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(animatedProgress) {
        launch {
            animationProgress = animatedProgress
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        drawPathWithOffset(
            path, size,
            calculateWaveOffset(animatedProgress, amplitude).toDouble()
        )
    }
}

private fun DrawScope.drawPathWithOffset(path: Path, size: Size, offsetY: Double) {
    val centerX = size.width / 2f
    val centerY = size.height / 2f + offsetY

    val paint = Paint().apply {
        color = Color.Blue // Set the desired color for the path
        style = androidx.compose.ui.graphics.PaintingStyle.Stroke
        strokeWidth = 2f
    }


    translate(0f, offsetY.toFloat()) {
        drawPath(
            color = (Color.Red),
            path = path,
        )
    }

}

private class WavesTransition(
    val waveShiftRatio: State<Float>,
    val amplitudeRatio: State<Float>
)

@Composable
private fun rememberWavesTransition(): WavesTransition {
    val transition = rememberInfiniteTransition()

    val waveShiftRatio = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = WavesShiftAnimationDurationInMillis,
                easing = LinearEasing
            )
        )
    )

    val amplitudeRatio = transition.animateFloat(
        initialValue = 0.005f,
        targetValue = 0.015f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = WavesAmplitudeAnimationDurationInMillis,
                easing = FastOutLinearInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    return remember(transition) {
        WavesTransition(waveShiftRatio = waveShiftRatio, amplitudeRatio = amplitudeRatio)
    }
}

@Stable
private fun createWavesPath(width: Int, height: Int): Path {
    val angularFrequency = 2f * PI / width
    val amplitude = height * AmplitudeRatio
    val waterLevel = height * WaterLevelRatio

    val wavePath = Path().apply {
        val startX = 0f
        val startY = waterLevel

        moveTo(startX, startY)

        for (x in 0..width) {
            val wx = x * angularFrequency
            val y = waterLevel + amplitude * sin(wx).toFloat()
            lineTo(x.toFloat(), y)
        }
        lineTo(width.toFloat(), height.toFloat())
        lineTo(startX, height.toFloat())
        close()
    }

    return wavePath
}


private const val AmplitudeRatio = 0.02f
private const val WaterLevelRatio = 0.5f
private const val WavesShiftAnimationDurationInMillis = 2500
private const val WavesAmplitudeAnimationDurationInMillis = 3000