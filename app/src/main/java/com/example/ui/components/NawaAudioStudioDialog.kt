package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun NawaAudioStudioDialog(
    onDismiss: () -> Unit
) {
    var isGenerating by remember { mutableStateOf(false) }
    var generationProgress by remember { mutableFloatStateOf(0f) }
    var resultsReady by remember { mutableStateOf(false) }

    // Settings State
    var legalPerturbation by remember { mutableFloatStateOf(8f) } // 8% default
    var isFemaleVoice by remember { mutableStateOf(false) }
    var uploadedFileName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            generationProgress = 0f
            while (generationProgress < 1f) {
                delay(100)
                generationProgress += 0.02f
            }
            isGenerating = false
            resultsReady = true
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    BorderStroke(1.5.dp, Brush.linearGradient(listOf(NeonCyan, NeonPurpleBright))),
                    RoundedCornerShape(24.dp)
                ),
            color = ShaheenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Nawa Audio Studio",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "استوديو الذكاء الاصطناعي التوليدي",
                                color = NeonPurpleBright,
                                fontSize = 12.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (resultsReady) {
                        // View 4 Generated Tracks
                        AudioResultsView(
                            onReset = {
                                resultsReady = false
                                generationProgress = 0f
                            }
                        )
                    } else if (isGenerating) {
                        // Generating View
                        GeneratingAudioView(progress = generationProgress)
                    } else {
                        // Configuration View
                        AudioConfigView(
                            legalPerturbation = legalPerturbation,
                            onPerturbationChange = { legalPerturbation = it },
                            isFemaleVoice = isFemaleVoice,
                            onGenderSwap = { isFemaleVoice = !isFemaleVoice },
                            uploadedFileName = uploadedFileName,
                            onUploadClick = { uploadedFileName = "Ayed_Sample_Voice.mp3" },
                            onGenerateClick = {
                                if (uploadedFileName != null) isGenerating = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioConfigView(
    legalPerturbation: Float,
    onPerturbationChange: (Float) -> Unit,
    isFemaleVoice: Boolean,
    onGenderSwap: () -> Unit,
    uploadedFileName: String?,
    onUploadClick: () -> Unit,
    onGenerateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Step 1: Upload
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
            border = BorderStroke(1.dp, GlassBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. استخراج البصمة الصوتية والفنية", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                
                if (uploadedFileName == null) {
                    OutlinedButton(
                        onClick = onUploadClick,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderStroke(1.dp, NeonPurpleBright)
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("رفع عينة للفنان (مثال: المطرب عايض)")
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ActiveEmerald.copy(alpha = 0.2f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ActiveEmerald)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(uploadedFileName, color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        }

        // Step 2: Legal & Gender Configuration
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
            border = BorderStroke(1.dp, GlassBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. المحاكاة والتعديل القانوني للترددات", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Text("نسبة التحوير القانوني لتجنب حقوق الملكية: ${legalPerturbation.toInt()}%", color = Color.White, fontSize = 12.sp)
                Slider(
                    value = legalPerturbation,
                    onValueChange = onPerturbationChange,
                    valueRange = 1f..15f,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonCyan,
                        activeTrackColor = NeonCyan,
                        inactiveTrackColor = ShaheenMetallicBorder
                    )
                )
                Text(
                    "تنويه: نسبة 5% - 8% تجعل الصوت رائعاً ومشابهاً للأسلوب مع الحفاظ على شرعية الحقوق.",
                    color = TextMuted, fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = ShaheenMetallicBorder)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("عكس الطابع الجندري (Gender Swap)", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("توليد الأغنية بصوت أنثوي", color = TextMuted, fontSize = 11.sp)
                    }
                    Switch(
                        checked = isFemaleVoice,
                        onCheckedChange = { onGenderSwap() },
                        colors = SwitchDefaults.colors(checkedThumbColor = NeonPurpleBright, checkedTrackColor = NeonPurpleBright.copy(alpha = 0.5f))
                    )
                }
            }
        }

        // Generate Button
        Button(
            onClick = onGenerateClick,
            enabled = uploadedFileName != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonPurpleBright, disabledContainerColor = ShaheenMetallicBorder),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("توليد 4 تحف فنية (Studio Quality)", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
        }
    }
}

@Composable
private fun GeneratingAudioView(progress: Float) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val pulse by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse)
        )

        Icon(
            imageVector = Icons.Default.GraphicEq,
            contentDescription = null,
            tint = NeonCyan,
            modifier = Modifier
                .size(80.dp)
                .scale(pulse)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "جاري معالجة البصمة الصوتية سحابياً...",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "نواة AI تقوم بتركيب الإيقاعات وتطبيق التحوير القانوني",
            color = TextMuted,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = NeonPurpleBright,
            trackColor = ShaheenMetallicBorder
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("${(progress * 100).toInt()}%", color = NeonCyan, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AudioResultsView(onReset: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("تم توليد 4 تحف فنية حصرية لك:", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        
        val tracks = listOf(
            "Track 1: المزيج الأصلي (Original Rhythm)",
            "Track 2: التوزيع السينمائي (Cinematic)",
            "Track 3: الإيقاع السريع (Upbeat Pop)",
            "Track 4: النسخة الهادئة (Acoustic Chill)"
        )

        tracks.forEach { trackName ->
            Card(
                modifier = Modifier.fillMaxWidth().height(70.dp),
                colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
                border = BorderStroke(1.dp, GlassBorder)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(NeonPurpleBright.copy(alpha = 0.2f))
                                .clickable { /* Play audio */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = NeonPurpleBright)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(trackName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("3:42 • 320kbps Studio", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                    IconButton(onClick = { /* Download */ }) {
                        Icon(Icons.Default.Download, contentDescription = "Download", tint = NeonCyan)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, NeonCyan)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("بدء جلسة توليد جديدة")
        }
    }
}
