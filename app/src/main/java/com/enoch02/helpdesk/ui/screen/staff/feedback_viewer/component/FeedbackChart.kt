package com.enoch02.helpdesk.ui.screen.staff.feedback_viewer.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.component.TextComponent


@Composable
fun FeedbackBarChart(feedbackData: List<FeedbackData>) {
    val scrollState = rememberVicoScrollState()
    val zoomState = rememberVicoZoomState()
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries {
                series(x = feedbackData.map { it.rating }, y = feedbackData.map { it.count })
            }
        }
    }

    Card(
        modifier = Modifier.padding(4.dp),
        content = {
            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = rememberStartAxis(
                        title = "Count",
                        titleComponent = rememberTextComponent()
                    ),
                    bottomAxis = rememberBottomAxis(
                        title = "Ratings",
                        titleComponent = rememberTextComponent()
                    )
                ),
                scrollState = scrollState,
                zoomState = zoomState,
                modelProducer = modelProducer
            )
        }
    )
}
