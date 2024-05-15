package com.febro.nestedscrolldemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.febro.nestedscrolldemo.ui.theme.NestedSCrollDemoTheme

class MainActivity : ComponentActivity() {
    val maxHeight = 200f
    val minHeight = 60f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NestedSCrollDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val density = LocalDensity.current.density
                    val toolbarHeightPx = with(LocalDensity.current) {
                        maxHeight.dp.roundToPx().toFloat()
                    }
                    val toolbarMinHeightPx = with(LocalDensity.current) {
                        minHeight.dp.roundToPx().toFloat()
                    }
                    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
                    val nestedScrollConnection = remember {
                        object : NestedScrollConnection {
                            override fun onPreScroll(
                                available: Offset,
                                source: NestedScrollSource
                            ): Offset {
                                val delta = available.y
                                val newOffset = toolbarOffsetHeightPx.value + delta
                                toolbarOffsetHeightPx.value =
                                    newOffset.coerceIn(toolbarMinHeightPx - toolbarHeightPx, 0f)
                                return Offset.Zero
                            }
                        }
                    }
                    var progress by remember { mutableStateOf(0f) }

                    LaunchedEffect(key1 = toolbarOffsetHeightPx.value) {
                        progress =
                            ((toolbarHeightPx + toolbarOffsetHeightPx.value) / toolbarHeightPx - minHeight / maxHeight) / (1f - minHeight / maxHeight)
                    }
                    Box(
                        Modifier
                            .fillMaxSize()
                            .nestedScroll(nestedScrollConnection)
                    ) {
                        LazyColumn(contentPadding = PaddingValues(top = maxHeight.dp)) {
                            items(100) { index ->
                                Text(
                                    "I'm item $index", modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(((toolbarHeightPx + toolbarOffsetHeightPx.value) / density).dp)
                                .background(
                                    Color.Black
                                )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(24.dp))
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(progress + 0.001f)
                                )
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight().weight(1f)
                                            .padding(vertical = 10.dp)
                                            .aspectRatio(1f)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_launcher_background),
                                            contentDescription = "",
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    Text(
                                        "Hello World",
                                        color = Color.White,
                                        modifier = Modifier
                                            .alpha(progress)
                                            .padding((8 * (progress * progress * progress)).dp),
                                        fontSize = (24 * (progress)).sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Text(
                                    "Hello World",
                                    color = Color.White,
                                    modifier = Modifier
                                        .alpha(1f - progress)
                                        .weight(1.001f - progress)
                                        .padding(start = 20.dp),
                                    fontSize = (24 * (1f - progress)).sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                )
                                Spacer(modifier = Modifier.width(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    NestedSCrollDemoTheme {
//        Greeting("Android")
//    }
//}