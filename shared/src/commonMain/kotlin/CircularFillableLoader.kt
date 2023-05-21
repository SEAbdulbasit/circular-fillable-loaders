/**
 * Created by abdulbasit on 21/05/2023.
 */

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CircularFillableLoaders(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier.size(226.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.wrapContentSize().clip(RoundedCornerShape(100)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(226.dp)
            ) {
                drawCircle(
                    color = Color.Black,
                    radius = 100.dp.toPx(),
                )
                drawCircle(
                    brush = SolidColor(Color.LightGray),
                    radius = 100.dp.toPx(),
                    style = Stroke(width = 20.dp.toPx())
                )
            }
            Image(
                painter = painterResource("compose-multiplatform.xml"),
                contentDescription = null,
                modifier = modifier.size(100.dp).align(Alignment.Center)
            )

            WavesLoadingIndicator2(modifier = modifier, color = Color.DarkGray, 0.9f)
        }
    }
}

