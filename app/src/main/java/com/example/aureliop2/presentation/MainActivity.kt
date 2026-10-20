package com.example.aureliop2.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.aureliop2.presentation.theme.Aureliop2Theme
import com.example.aureliop2.presentation.theme.PinkBackground
import com.example.aureliop2.presentation.theme.PinkDark
import com.example.aureliop2.presentation.theme.PinkLight
import com.example.aureliop2.presentation.theme.PinkMedium
import com.example.aureliop2.presentation.theme.PinkOperator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)
        setContent { CalculatorApp() }
    }
}

private val KEY_ROWS = listOf(
    listOf("C", "⌫", "%", "÷"),
    listOf("7", "8", "9", "×"),
    listOf("4", "5", "6", "−"),
    listOf("1", "2", "3", "+"),
    listOf("0", ".", "="),
)

private enum class KeyKind { Digit, Operator, Function, Equals }

private fun kindOf(key: String) = when (key) {
    "÷", "×", "−", "+" -> KeyKind.Operator
    "C", "⌫", "%" -> KeyKind.Function
    "=" -> KeyKind.Equals
    else -> KeyKind.Digit
}

@Composable
fun CalculatorApp() {
    Aureliop2Theme {
        var state by remember { mutableStateOf(CalcState()) }
        val listState = rememberScalingLazyListState(initialCenterItemIndex = 1)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PinkBackground),
            contentAlignment = Alignment.Center,
        ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                anchorType = ScalingLazyListAnchorType.ItemStart,
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
            ) {
                item { Display(state.display) }
                KEY_ROWS.forEach { row ->
                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            row.forEach { key ->
                                CalcKey(key) { state = state.press(key) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Display(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .height(44.dp)
            .clip(CircleShape)
            .background(PinkLight),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(
            text = text,
            color = PinkDark,
            fontSize = if (text.length > 8) 16.sp else 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(end = 14.dp),
        )
    }
}

@Composable
private fun CalcKey(key: String, onClick: () -> Unit) {
    val kind = kindOf(key)
    val background = when (kind) {
        KeyKind.Digit -> PinkLight
        KeyKind.Function -> PinkMedium
        KeyKind.Operator -> PinkOperator
        KeyKind.Equals -> PinkDark
    }
    val foreground = when (kind) {
        KeyKind.Digit, KeyKind.Function -> PinkDark
        KeyKind.Operator, KeyKind.Equals -> Color.White
    }
    Box(
        modifier = Modifier
            .size(if (key == "=") 70.dp else 34.dp, 34.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = key,
            color = foreground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun SmallRoundPreview() {
    CalculatorApp()
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
@Composable
fun LargeRoundPreview() {
    CalculatorApp()
}
