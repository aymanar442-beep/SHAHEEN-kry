package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

@Composable
fun FounderStoryDialog(
  onDismiss: () -> Unit,
  onOpenPaymentHub: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.90f)
        .clip(RoundedCornerShape(20.dp))
        .border(
          BorderStroke(
            1.5.dp,
            Brush.linearGradient(listOf(Color(0xFF168FFF), Color(0xFFF002F5), Color(0xFF00E676)))
          ),
          RoundedCornerShape(20.dp)
        ),
      color = Color(0xFF101B20) // Official Deep Obsidian Black from blueprint
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(18.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFF168FFF).copy(alpha = 0.2f))
                .border(BorderStroke(1.dp, Color(0xFF168FFF)), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = Color(0xFF168FFF),
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "قصة شاهين ورؤية المؤسس",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF002F5)
              )
              Text(
                text = "Ayman Al-Araishi • Y Combinator Startup School",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF168FFF),
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_founder_story_button")) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextDim
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Core Philosophy Card
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1224)),
          border = BorderStroke(1.dp, Color(0xFF168FFF).copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "فلسفة شاهين وميثاق الأمان السيادي:",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF168FFF)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "«حياة الإنسان أولاً. التكنولوجيا هي وسيلتنا. والثقة هي مسؤوليتنا. والابتكار هو طريقنا. وكل منتج نبنيه يجب أن يجعل حياة الناس أكثر أماناً.»",
              style = MaterialTheme.typography.bodyMedium,
              color = TextWhite,
              fontWeight = FontWeight.Medium,
              fontSize = 12.5.sp,
              lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Motto: \"Silent Vigilance. Unlimited Protection.\"",
              style = MaterialTheme.typography.labelSmall,
              fontFamily = FontFamily.Monospace,
              color = FalconCyan,
              fontSize = 10.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Founder's Journey - From 6-inch phone to Y Combinator
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1B2A)),
          border = BorderStroke(1.dp, ShaheenMetallicBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = ActiveEmerald, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "من شاشة هاتف محمول في دمشق إلى العالمية:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                fontSize = 12.sp
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "بدأت رحلة ابتكار خوارزميات شاهين من قلب المعاناة والتحديات الصعبة، حيث تم تطوير هذه الأنظمة الحسابية الذكية على شاشة هاتف محمول 6 بوصات، لتحويل الألم إلى درع تقني يحمي المتداولين من تلاعب الأسواق، ويحمي الأرواح في الأزمات. تم توثيق المؤسس رسمياً في Y Combinator Startup School و Google Developers Program.",
              style = MaterialTheme.typography.bodySmall,
              color = TextDim,
              fontSize = 11.sp,
              lineHeight = 16.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Ecosystem Architecture Roadmap (From Trading Bot to SHAHEEN A1)
        Text(
          text = "خارطة طريق منظومة شاهين الشاملة (Multi-Project Ecosystem):",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = Color(0xFFF002F5)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Product 1: Shaheen Shield v2.4 (Current App)
        EcosystemStepCard(
          number = "1",
          title = "SHAHEEN Shield Core v2.4 (المنتج الحالي)",
          category = "FINANCIAL SECURITY & QUANT RADAR",
          badge = "LIVE / IN PRODUCTION",
          badgeColor = ActiveEmerald,
          description = "درع الأمان المالي ومكافحة الانزلاق والتداول الانتقامي مع قاطع الدائرة الفوري ساب-100ms وتفريغ الأرباح للمحفظة الفورية."
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Product 2: SHAHEEN A1 Wearable Bio-Sensor
        EcosystemStepCard(
          number = "2",
          title = "SHAHEEN A1 (كبسولة الأمان الحيوي القادمة)",
          category = "BIOMETRIC RESILIENCE & TRACKER",
          badge = "R&D ESTIMATED $750K",
          badgeColor = Color(0xFF168FFF),
          description = "سوار وقلادة ذكية بحساسات PPG و EDA و IMU ترصد حالات الهلع والأزمات بدون إنترنت عبر خوارزمية الصندوق الأسود المشفرة."
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Product 3: SHAHEEN WildCare & SafeLink
        EcosystemStepCard(
          number = "3",
          title = "SHAHEEN WildCare & SafeLink Mesh",
          category = "AUTONOMOUS MESH & WILDLIFE",
          badge = "GLOBAL INFRASTRUCTURE",
          badgeColor = WarningAmber,
          description = "شبكات اتصال لا سلكية ذاتية الشفاء تعمل في مناطق الكوارث وانقطاع الأبراج وحماية البيئة والثروة الحيوانية."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Investor & Supporter CTA
        Button(
          onClick = {
            onDismiss()
            onOpenPaymentHub()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF168FFF)),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
          Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null, tint = TextWhite)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "دعم المشروع وشراء التراخيص الرسمية (USDT)",
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            fontSize = 13.sp
          )
        }
      }
    }
  }
}

@Composable
private fun EcosystemStepCard(
  number: String,
  title: String,
  category: String,
  badge: String,
  badgeColor: Color,
  description: String
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = Color(0xFF0A1224),
    border = BorderStroke(1.dp, ShaheenMetallicBorder),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(badgeColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = number, color = badgeColor, fontWeight = FontWeight.Bold, fontSize = 11.sp)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(text = title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
            Text(text = category, color = TextDim, fontSize = 8.5.sp, fontFamily = FontFamily.Monospace)
          }
        }

        Surface(
          shape = RoundedCornerShape(4.dp),
          color = badgeColor.copy(alpha = 0.15f)
        ) {
          Text(
            text = badge,
            color = badgeColor,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = description,
        color = TextDim,
        fontSize = 10.5.sp,
        lineHeight = 14.sp
      )
    }
  }
}
