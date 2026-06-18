package com.christhperalta.donext.features.home.presentation.stats


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christhperalta.donext.core.presentation.CustomText
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart


@Composable
fun StatsScreen() {

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries { series(13, 8, 7, 12, 0, 1, 15, 14, 0, 11, 6, 12, 0, 11, 12, 11) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {StatsTopBar()}
    ) { innerPadding ->

        LazyColumn (
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(top = 30.dp)) {
                    CustomText(
                        text = "This week.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Productivity",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Tarjeta de Weekly Completion
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "WEEKLY\nCOMPLETION",
                        value = "84",
                        unit = "%",
                        valueColor = Color(0xFF60DF20) // Un verde similar al de la imagen
                    )

                    // Tarjeta de Longest Streak
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "LONGEST STREAK",
                        value = "12",
                        unit = "days",
                        valueColor = Color.Black
                    )
                }
            }

            item {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomText(
                        text = "Activity Graph",
                        fontWeight = FontWeight.Bold
                    )

                    Row (
                        modifier = Modifier.padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF6A9153)))
                        Spacer(modifier = Modifier.width(5.dp))
                        CustomText(
                            text = "Task completed",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                CartesianChartHost(
                    rememberCartesianChart(
                        rememberLineCartesianLayer(),
                        startAxis = VerticalAxis.rememberStart(),
                        bottomAxis = HorizontalAxis.rememberBottom(),
                    ),
                    modelProducer,
                )
            }

            item{
                CustomText(
                    text = "Top Catefories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                TaskProgressCard(
                    title = "Deep Work",
                    taskCount = 18,
                    progress = 0.7f,
                )
                Spacer(modifier = Modifier.height(10.dp))
                TaskProgressCard(
                    title = "Deep Work",
                    taskCount = 10,
                    progress = 0.4f,
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsTopBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = "Activity", fontWeight = FontWeight.Bold) },
    )
}


@Composable
fun StatCard(
    title: String,
    value: String,
    unit: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.aspectRatio(1.2f),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9BA5B4),
                    letterSpacing = 1.sp
                )
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = value,
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = valueColor
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = unit,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF9BA5B4)
                    )
                )
            }
        }
    }
}


@Composable
fun TaskProgressCard(
    title: String,
    taskCount: Int,
    progress: Float, // 0.0f a 1.0f
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier.size(48.dp).background(Color(0xFFF1F5EE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF6A9153)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("$taskCount tasks", color = Color(0xFF9BA5B4), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Barra de progreso personalizada
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = Color(0xFF6A9153),
                    trackColor = Color(0xFFF0F0F0)
                )
            }
        }
    }
}