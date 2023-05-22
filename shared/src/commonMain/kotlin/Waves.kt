import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WavesLoadingIndicator2(modifier: Modifier, color: Color, progress: Float) {
    BoxWithConstraints(modifier = modifier.offset(y = 16.dp), contentAlignment = Alignment.Center) {
        AnimatedPathTranslation()
    }
}

@Composable
fun AnimatedPathTranslation() {
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
        initialValue = 0.35f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = WavesAmplitudeAnimationDurationInMillis,
                easing = FastOutLinearInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val wavePath = createPath(
            canvasWidth.toInt(),
            canvasHeight.toInt(),
            waveShiftRatio.value,
            amplitudeRatio.value
        )
        drawPath(
            color = (Color.Red.copy(alpha = 0.5f)),
            path = wavePath[0],
        )

        translate(0f, 20f) {
            drawPath(
                color = (Color.Red),
                path = wavePath[1]
            )
        }
    }
}

private fun createPath(
    width: Int,
    height: Int,
    waveShiftRatio: Float,
    amplitudeRatio: Float
): List<Path> {
    val angularFrequency = 2f * PI / width
    val amplitude = height * AmplitudeRatio * amplitudeRatio
    val waterLevel = height * WaterLevelRatio

    val originalPath = Path().apply {
        val startX = 0f
        val startY = waterLevel

        moveTo(startX, startY)

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
        val startY = waterLevel

        moveTo(startX, startY)

        for (x in 0..width) {
            val wx = x * angularFrequency + waveShiftRatio
            val y = waterLevel + amplitude * sin(wx + waveShiftRatio * 2 * PI).toFloat()
            lineTo(x.toFloat(), y)
        }
        lineTo(width.toFloat(), height.toFloat())
        lineTo(startX, height.toFloat())
        close()
    }



    return listOf(originalPath, path2)
}


private const val AmplitudeRatio = 0.05f
private const val WaterLevelRatio = 0.5f
private const val WavesShiftAnimationDurationInMillis = 2500
private const val WavesAmplitudeAnimationDurationInMillis = 3000