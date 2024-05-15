package com.example.composepulseanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composepulseanimation.ui.theme.ComposePulseAnimationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePulseAnimationTheme {
                var openBottomSheet by remember { mutableStateOf(false) }
                ComposePulseAnimation(openBottomSheet) { openBottomSheet = it }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposePulseAnimation(openBottomSheet: Boolean, onBottomSheetOpen: (Boolean) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PulseAnimation {
            onBottomSheetOpen(true)
        }
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onBottomSheetOpen(false) }
        ) {
            Column {
                repeat(10) {
                    Text(text = "Text $it", Modifier.padding(15.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposePulseAnimationTheme {
        PulseAnimation {}
    }
}

@Composable
fun PulseAnimation(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val activeAnimationDuration = 2000
    val activeAnimationCount = 3
    val activeAnimationProgress = List(activeAnimationCount) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(durationMillis = activeAnimationDuration, easing = LinearEasing),
                initialStartOffset = StartOffset(it * activeAnimationDuration / activeAnimationCount),
                repeatMode = RepeatMode.Restart,
            ), label = ""
        )
    }

    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .clickable(
                indication = null, // Assign null to disable the ripple effect
                interactionSource = interactionSource,
                onClick = onClick
            )
            .height(68.dp)
            .padding(horizontal = 20.dp)
            .drawBehind {
                val circlePosition = Offset(0.dp.toPx(), size.height / 2f)
                drawCircle(
                    color = Color.Red,
                    radius = 6.dp.toPx(),
                    center = circlePosition,
                )
                activeAnimationProgress.forEachIndexed { _, progress ->
                    drawCircle(
                        color = Color.Red.copy(alpha = 1f - progress.value),
                        radius = 20.dp.toPx() * progress.value,
                        center = circlePosition,
                    )
                }
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Hello String", modifier = Modifier.padding(20.dp), color = Color.Red)
    }
}