package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
// Nawa AI Core
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ShaheenAICoreComponent(
    modifier: Modifier = Modifier,
    onVoiceCommand: () -> Unit
) {
    var showAudioStudio by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    if (showAudioStudio) {
        NawaAudioStudioDialog(onDismiss = { showAudioStudio = false })
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Nawa AI Core Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "نواة AI (مساعد ذكي للجيل القادم)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // The Central AI Core (Pulsing Sphere)
        CentralAICoreVisualizer()

        Spacer(modifier = Modifier.height(32.dp))

        // Glassmorphism Action Cards
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            AIActionCard(
                title = "تحدث معي",
                icon = Icons.Default.Mic,
                color = NeonCyan,
                modifier = Modifier.weight(1f),
                onClick = onVoiceCommand
            )
            AIActionCard(
                title = "استوديو Nawa (صوت)",
                icon = Icons.Default.GraphicEq,
                color = NeonPurpleBright,
                modifier = Modifier.weight(1f),
                onClick = { showAudioStudio = true }
            )
        }
    }
}

@Composable
fun CentralAICoreVisualizer() {
    val infiniteTransition = rememberInfiniteTransition(label = "core_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "core_scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "core_alpha"
    )

    Box(
        modifier = Modifier
            .size(160.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        // Outer Glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NeonPurpleBright.copy(alpha = alpha * 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Middle Ring
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(GlassWhite)
                .border(2.dp, NeonCyan.copy(alpha = alpha), CircleShape)
        )

        // Inner Core
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NeonCyan,
                            NeonPurple
                        )
                    )
                )
        ) {
            Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = "Core Active",
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun AIActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = GlassWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = TextWhite,
                fontSize = 12.sp
            )
        }
    }
}
