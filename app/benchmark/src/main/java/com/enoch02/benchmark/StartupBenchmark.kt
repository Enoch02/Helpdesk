package com.enoch02.benchmark

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class StartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    /*@get:Rule
    val composeTestRule = createComposeRule()*/

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.enoch02.helpdesk",
        metrics = listOf(
            StartupTimingMetric(),
            /*MemoryUsageMetric(mode = MemoryUsageMetric.Mode.Max),
            FrameTimingMetric()*/
        ),
        startupMode = StartupMode.WARM,
        iterations = /*5*/2,
        setupBlock = {
            pressHome()
        },
        measureBlock = {
            startActivityAndWait()
            Thread.sleep(5000)
        }
    )

    /*@OptIn(ExperimentalMetricApi::class)
    @Test
    fun login() = benchmarkRule.measureRepeated(
        packageName = "com.enoch02.helpdesk",
        metrics = listOf(
            StartupTimingMetric(),
            MemoryUsageMetric(mode = MemoryUsageMetric.Mode.Max),
            FrameTimingMetric()
        ),
        startupMode = StartupMode.WARM,
        iterations = *//*5*//*1,
        setupBlock = {
            pressHome()
        },
        measureBlock = {
            startActivityAndWait()

            composeTestRule.onNodeWithTag("EmailField").performTextInput("adesanyaenoch@gmail.com")
            composeTestRule.onNodeWithTag("PasswordField").performTextInput("")
            composeTestRule.onNodeWithTag("SubmitButton").performClick()

            Thread.sleep(5000)
        }
    )*/
}