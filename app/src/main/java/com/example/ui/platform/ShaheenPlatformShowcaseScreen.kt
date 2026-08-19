package com.example.ui.platform

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

val BackgroundDark = Color(0xFF0F1219)
val CardDark = Color(0xFF151923)

@Composable
fun ShaheenPlatformShowcaseScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            PlatformTopBar()
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Hero Section
            item {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(FalconCyan.copy(alpha = alpha * 0.2f))
                        .border(2.dp, FalconCyan.copy(alpha = alpha), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Shaheen Crest",
                        tint = FalconCyan,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "SHAHEEN APEX AI",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp
                    ),
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "DEFENCE & STRATEGIC INTELLIGENCE",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp),
                    color = ActiveEmerald,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "منظومة متكاملة لحماية الأصول، البيانات، والأرواح. مصممة باستخدام خوارزميات ذكاء اصطناعي سيادية تعمل في أصعب الظروف.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }

            // Product Modules
            item {
                PlatformModuleCard(
                    title = "Shaheen Trade Shield",
                    description = "نظام التداول الدفاعي. يطبق خوارزميات الـ Psycho-Temporal Locks لمنع الانهيارات العاطفية والتداول الانتقامي.",
                    icon = Icons.Default.Security,
                    accentColor = InactiveCrimson
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                PlatformModuleCard(
                    title = "Shaheen Bio-Metrics",
                    description = "محرك قراءة الإشارات الحيوية (نبض القلب، EDA) لتقييم مستويات التوتر واتخاذ قرارات التدخل الطارئ.",
                    icon = Icons.Default.Analytics,
                    accentColor = FalconCyan
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                PlatformModuleCard(
                    title = "Shaheen Cloud Fallback",
                    description = "أنظمة طوارئ تعمل بلا إنترنت (Offline Execution) لحماية الأنظمة الحرجة من الانقطاعات المفاجئة.",
                    icon = Icons.Default.Speed,
                    accentColor = ActiveEmerald
                )
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }

            // CTA
            item {
                Button(
                    onClick = { /* Navigate to contact/investment deck */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = FalconCyan)
                ) {
                    Text(
                        text = "طلب شراكة استراتيجية",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BackgroundDark
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun PlatformTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(CardDark)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "SHAHEEN CORP.",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
            color = TextWhite
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("المنتجات", style = MaterialTheme.typography.labelMedium, color = TextMuted)
            Text("المستثمرين", style = MaterialTheme.typography.labelMedium, color = TextMuted)
        }
    }
}

@Composable
fun PlatformModuleCard(title: String, description: String, icon: ImageVector, accentColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardDark, RoundedCornerShape(12.dp))
            .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(accentColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = accentColor)
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
    }
}
