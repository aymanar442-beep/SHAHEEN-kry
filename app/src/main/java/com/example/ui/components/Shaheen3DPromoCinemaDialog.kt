package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.ui.theme.*
import com.example.util.FalconAudioEngine

@Composable
fun Shaheen3DPromoCinemaDialog(
  onDismiss: () -> Unit,
  onOpenPricingHub: () -> Unit
) {
  var isFeaturesExpanded by remember { mutableStateOf(true) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .fillMaxHeight(0.93f)
        .clip(RoundedCornerShape(24.dp))
        .border(
          BorderStroke(
            2.dp,
            Brush.linearGradient(listOf(FalconGold, FalconCyan, FalconBlue, FalconGold))
          ),
          RoundedCornerShape(24.dp)
        ),
      color = Color(0xFF070D12)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(FalconGold, Color(0xFFB45309))))
                .border(1.5.dp, FalconGold, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Diamond,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
              )
            }
            Column {
              Text(
                text = "المعرض السينمائي 3D • العرض الترويجي الشامل",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
              )
              Text(
                text = "SHAHEEN APEX • 3D Cinematic Commercial Blueprint",
                color = FalconCyan,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(Color(0xFF1E293B))
              .testTag("close_3d_promo_dialog")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3D Cinematic Hero Card with Generated Image
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.5.dp, Brush.linearGradient(listOf(FalconCyan, FalconGold)), RoundedCornerShape(18.dp))
        ) {
          Image(
            painter = painterResource(id = R.drawable.shaheen_cinematic_3d_promo_1786884400292),
            contentDescription = "Shaheen 3D Cinematic Hero Render",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )

          // Dark Gradient Overlay for Typography Clarity
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(Color.Transparent, Color(0x99000000), Color(0xF0070D12)),
                  startY = 80f
                )
              )
          )

          // Play Sound Overlay Button
          Row(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(10.dp)
          ) {
            Button(
              onClick = { FalconAudioEngine.playFalconStartupChime() },
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xCC000000),
                contentColor = FalconGold
              ),
              shape = RoundedCornerShape(20.dp),
              border = BorderStroke(1.dp, FalconGold),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
              modifier = Modifier.testTag("play_3d_cinema_sound")
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("صوت 528Hz السيادي", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
            }
          }

          // Bottom Caption
          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(12.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(FalconGold)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text("النسخة التأسيسية الفاخرة • Genesis V1.0", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "محرك شاهين APEX: قوة حيتان وول ستريت بين يديك",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Value Statement Card (Commercial Pitch)
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF101922)),
          border = BorderStroke(1.dp, FalconCyan.copy(alpha = 0.4f))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "لماذا يدفع المتداول المحترف $100 لاقتناء شاهين فوراً؟",
              color = FalconGold,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "إذا عزّيت صنفك.. صنفك بيعزك! شاهين ليس مجرد تطبيق، بل هو ترخيص تكنولوجي طرفي (Edge HFT) يمنحك امتياز الصقر: سرعة تنفيذ دون 10ms، تحكيم مثلثي خالي من مخاطر السوق، وحصانة عتادية مقفلة باسمك تضمن عدم نسخ أو مشاركة نسختك.",
              color = Color(0xFFCBD5E1),
              fontSize = 12.sp,
              lineHeight = 19.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pricing Architecture & Grand Offer (100$ + 25$ / Exemption for 6+ months)
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF15222E)),
          border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(FalconGold, FalconCyan)))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "👑 هيكل التسعير التأسيسي المعتمد:",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp
              )
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(ActiveEmerald)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text("عرض الرؤساء والمؤسسين", fontWeight = FontWeight.Bold, fontSize = 9.5.sp, color = Color.Black)
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Point 1: $100 Genesis License
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(FalconGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Text("1", color = FalconGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "رخصة التملك الأولى (Genesis Entry): $100 لمرة واحدة",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.5.sp
                )
                Text(
                  text = "تملك مدى الحياة للمحرك الأساسي، ربط البصمة العتادية الحصرية، والكتابين التوثيقيين.",
                  color = Color(0xFF94A3B8),
                  fontSize = 11.sp
                )
              }
            }

            Divider(color = Color(0xFF1E2D3D), modifier = Modifier.padding(vertical = 6.dp))

            // Point 2: $25 Annual for standard
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(FalconCyan.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Text("2", color = FalconCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "صيانة وتحديثات سنوية للمستخدمين العاديين: $25 / سنة فقط",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.5.sp
                )
                Text(
                  text = "تغطية تكاليف التحديثات المستمرة ودرع الأمان السنوي لحسابات الحماية الأساسية.",
                  color = Color(0xFF94A3B8),
                  fontSize = 11.sp
                )
              }
            }

            Divider(color = Color(0xFF1E2D3D), modifier = Modifier.padding(vertical = 6.dp))

            // Point 3: Exemption for 6+ months subscription
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(ActiveEmerald.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = ActiveEmerald, modifier = Modifier.size(14.dp))
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "🔥 ميزة الإعفاء الملكي للمشتركين التكتيكيين:",
                  color = ActiveEmerald,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.5.sp
                )
                Text(
                  text = "عند الاشتراك في الباقات (Growth Pro أو Elite Apex) لمدة 6 أشهر أو أكثر، يُعفى المشترك تماماً من رسم الـ $25 السنوي ويكتفي بسعر الباقة فقط!",
                  color = Color(0xFFE2E8F0),
                  fontSize = 11.sp,
                  lineHeight = 16.sp
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Super Features Breakdown
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0D161F)),
          border = BorderStroke(1.dp, Color(0xFF203344))
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { isFeaturesExpanded = !isFeaturesExpanded },
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FlashOn, contentDescription = null, tint = FalconGold, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "الميزات الخارقة التي تجعل الشراء قراراً بديهياً:",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.5.sp
                )
              }
              Icon(
                imageVector = if (isFeaturesExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = Color(0xFF94A3B8)
              )
            }

            AnimatedVisibility(visible = isFeaturesExpanded) {
              Column(modifier = Modifier.padding(top = 10.dp)) {
                val superFeatures = listOf(
                  "⚡ سرعة تنفيذ دون 10ms بفضل المعالجة الطرفية على الهاتف مباشرة.",
                  "🔄 تحكيم مثلثي متزامن بين 3 أزواج عملات يقتنص ربح 0.8% إلى 2% بدون مخاطرة السوق.",
                  "🛡️ قاطع خسارة فوري مشفر يحمي رأس مالك بنسبة 100% مجاناً للأبد.",
                  "💳 نظام الاقتطاع والتحويل الذكي بين العملات دون توقف التداول إطلاقاً.",
                  "🔒 قفل عتادي صارم يمنع تداول أو استنساخ نسختك لجهاز آخر."
                )

                superFeatures.forEach { feat ->
                  Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                    Text("• ", color = FalconCyan, fontWeight = FontWeight.Bold)
                    Text(text = feat, color = Color(0xFFCBD5E1), fontSize = 11.5.sp, lineHeight = 17.sp)
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action CTA Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier
              .weight(1f)
              .height(46.dp)
              .testTag("dismiss_3d_promo"),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF475569)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
          ) {
            Text("إغلاق العرض", fontSize = 12.sp)
          }

          Button(
            onClick = {
              onDismiss()
              onOpenPricingHub()
            },
            modifier = Modifier
              .weight(1.6f)
              .height(46.dp)
              .testTag("proceed_to_buy_genesis_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = FalconGold,
              contentColor = Color.Black
            )
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Diamond, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("شراء رخصة شاهين ($100)", fontSize = 12.5.sp, fontWeight = FontWeight.Black)
            }
          }
        }
      }
    }
  }
}
