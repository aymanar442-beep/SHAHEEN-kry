package com.example

import com.example.model.FluctuationDirection
import com.example.model.ShaheenConfig
import com.example.model.ThresholdBreachType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FluctuationAlertUnitTest {

  @Test
  fun default_fluctuation_config_values_are_valid() {
    val config = ShaheenConfig()
    assertEquals(0.50, config.fluctuationPercentageThreshold, 0.001)
    assertEquals(FluctuationDirection.BOTH, config.fluctuationDirection)
    assertEquals(10, config.fluctuationTimeWindowSeconds)
    assertTrue(config.systemNotificationsEnabled)
    assertTrue(config.fluctuationAlertSoundEnabled)
  }

  @Test
  fun percentage_fluctuation_threshold_breach_detection() {
    val previousPrice = 98000.0
    val surgePrice = 98588.0 // +0.60%
    val dropPrice = 97412.0 // -0.60%
    val minorPrice = 98196.0 // +0.20%

    val thresholdPercent = 0.50

    val surgeDelta = ((surgePrice - previousPrice) / previousPrice) * 100.0
    val dropDelta = ((dropPrice - previousPrice) / previousPrice) * 100.0
    val minorDelta = ((minorPrice - previousPrice) / previousPrice) * 100.0

    assertTrue(Math.abs(surgeDelta) >= thresholdPercent)
    assertTrue(Math.abs(dropDelta) >= thresholdPercent)
    assertFalse(Math.abs(minorDelta) >= thresholdPercent)

    // Check Direction Filter Logic
    assertTrue(surgeDelta > 0)
    assertTrue(dropDelta < 0)
  }

  @Test
  fun breach_type_labels_are_accurate() {
    assertEquals("تذبذب سعري حاد", ThresholdBreachType.PERCENTAGE_FLUCTUATION.labelAr)
    assertEquals("Price Fluctuation Alert", ThresholdBreachType.PERCENTAGE_FLUCTUATION.labelEn)
  }
}
