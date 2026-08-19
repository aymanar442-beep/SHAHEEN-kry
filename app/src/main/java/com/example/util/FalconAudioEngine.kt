package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.exp

/**
 * Procedural Audio Synthesizer for Shaheen:
 * Generates an inspiring, majestic Falcon cry / soaring sound wave
 * followed by a futuristic ascending crystal chord on app boot,
 * delivering high-frequency positive energy to the trader.
 */
object FalconAudioEngine {

  private const val SAMPLE_RATE = 44100
  private var isMuted = false

  fun setMuted(muted: Boolean) {
    isMuted = muted
  }

  fun isMuted(): Boolean = isMuted

  fun playFalconStartupChime(scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) {
    if (isMuted) return

    scope.launch(Dispatchers.Default) {
      try {
        val durationSeconds = 2.4
        val totalSamples = (SAMPLE_RATE * durationSeconds).toInt()
        val pcmBuffer = ShortArray(totalSamples)

        // Generate synthetic acoustic profile
        for (i in 0 until totalSamples) {
          val t = i.toDouble() / SAMPLE_RATE

          var sample = 0.0

          // Phase 1: Majestic Falcon Screech & Soaring Frequency Sweep (0.0s -> 1.1s)
          if (t < 1.1) {
            val falconEnv = if (t < 0.15) {
              t / 0.15
            } else {
              exp(-(t - 0.15) * 3.5)
            }

            // Frequency sweep: 2400Hz soaring up to 3800Hz with vibrato
            val baseFreq = 2400.0 + 1400.0 * (1.0 - exp(-t * 6.0))
            val vibrato = sin(2.0 * PI * 18.0 * t) * 120.0
            val subHarmonic = sin(2.0 * PI * (baseFreq * 0.5 + vibrato) * t) * 0.35
            val mainFalconWave = sin(2.0 * PI * (baseFreq + vibrato) * t) +
                0.45 * sin(2.0 * PI * (baseFreq * 1.5 + vibrato) * t) +
                0.25 * sin(2.0 * PI * (baseFreq * 2.1) * t)

            // Rasp/Texture
            val grit = (sin(2.0 * PI * 340.0 * t) * 0.25)

            sample += (mainFalconWave + subHarmonic + grit) * falconEnv * 0.48
          }

          // Phase 2: Ascending Futuristic Golden Chime & Solfeggio 528Hz Miracle Wave (0.4s -> 2.4s)
          if (t >= 0.4) {
            val chordT = t - 0.4
            val chimeEnv = exp(-chordT * 1.8) * (if (chordT < 0.06) chordT / 0.06 else 1.0)

            // 528Hz (Transformation/Prosperity frequency) + Harmonics (C-sharp / F-sharp / A-sharp / C# Apex)
            val freq1 = 528.0   // Golden ratio / Prosperity tone
            val freq2 = 660.0   // Major third
            val freq3 = 792.0   // Fifth
            val freq4 = 1056.0  // Octave Apex
            val freq5 = 1584.0  // Shimmering High Bell

            val chord = sin(2.0 * PI * freq1 * chordT) * 0.4 +
                sin(2.0 * PI * freq2 * chordT) * 0.3 +
                sin(2.0 * PI * freq3 * chordT) * 0.25 +
                sin(2.0 * PI * freq4 * chordT) * 0.2 +
                sin(2.0 * PI * freq5 * chordT) * 0.15

            sample += chord * chimeEnv * 0.52
          }

          // Master volume limiter / soft-clip
          sample = sample.coerceIn(-0.95, 0.95)
          pcmBuffer[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }

        // Initialize AudioTrack and stream the buffer
        val bufferSize = totalSamples * 2
        val audioTrack = AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(SAMPLE_RATE)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(bufferSize)
          .setTransferMode(AudioTrack.MODE_STATIC)
          .build()

        audioTrack.write(pcmBuffer, 0, totalSamples)
        audioTrack.play()

        // Release after finish
        kotlinx.coroutines.delay((durationSeconds * 1000).toLong() + 300)
        audioTrack.stop()
        audioTrack.release()
      } catch (e: Exception) {
        Log.e("FalconAudioEngine", "Audio synthesis non-blocking fallback: ${e.message}")
      }
    }
  }
}
