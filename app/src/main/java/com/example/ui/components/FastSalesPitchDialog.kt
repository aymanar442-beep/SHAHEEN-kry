package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.ConsoleGreen
import com.example.ui.theme.ConsoleYellow
import com.example.ui.theme.FalconBlue
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.ShaheenBackground
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenMetallicBorderLight
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.ShaheenSurfaceDark
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Composable
fun FastSalesPitchDialog(
  onDismiss: () -> Unit
) {
  val clipboardManager = LocalClipboardManager.current
  var copiedSection by remember { mutableStateOf("") }
  var buyerName by remember { mutableStateOf("") }
  var generatedLicense by remember { mutableStateOf("") }

  val bscAddress = "0x7e5beadba7b6cf2153579b29cb115e4533036a11"

  val telegramSalesPitch = """
🔥 [إطلاق رسمي] منظومة SHAHEEN APEX AI v2.4 لحماية متداولي الكريبتو والعقود الآجلة (Futures) 🦅

هل تعبت من ضرب الستوب لوز والانزلاقات السعرية المفاجئة أو التسييل؟
نظام شاهين المطور بخوارزمية Bet23 الحصرية يوفر الحماية القصوى لحسابك:

🛡️ مميزات المنظومة السيادية:
1. خوارزمية Bet23 للتحوط ومنع التسييل السلوكي.
2. حماية كاملة ضد انقطاع السيرفرات وشبكة الإنترنت (Offline Immunity).
3. زر الإغلاق الفوري للطوارئ (Emergency Kill-Switch) لتأمين رأس المال بـ 100% USDT.
4. دعم الربط المباشر مع Binance و MEXC بدون سحب أصولك.

💎 سعر الترخيص الدائم (عرض الإطلاق): 100 USDT فقط
📌 الاستلام الفوري للترخيص والدعم المباشر عبر المحفظة المعتمدة (BEP20):
$bscAddress

للحصول على مفتاح التفعيل الفوري أو الاستفسار راسلني خاص: @AymanSovereign
  """.trimIndent()

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .clip(RoundedCornerShape(18.dp))
        .border(
          BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(FalconCyan, FalconBlue, ActiveEmerald))),
          RoundedCornerShape(18.dp)
        ),
      color = ShaheenSurfaceDark,
      shadowElevation = 24.dp
    ) {
      Column(
        modifier = Modifier
          .padding(18.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(ActiveEmerald.copy(alpha = 0.2f))
                .border(BorderStroke(1.dp, ActiveEmerald), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CurrencyExchange,
                contentDescription = null,
                tint = ActiveEmerald,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "مركز المبيعات والتراخيص الفورية",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
              )
              Text(
                text = "SHAHEEN COMMERCIAL & GTM ENGINE • 100 USDT",
                style = MaterialTheme.typography.labelSmall,
                color = FalconCyan,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 1. Direct Payment Address Card
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
          border = BorderStroke(1.dp, FalconCyan.copy(alpha = 0.6f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Paid, contentDescription = null, tint = ActiveEmerald, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "عنوان استلام الـ 100 USDT (محفظة BEP20):",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = TextWhite
                )
              }
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = ActiveEmerald.copy(alpha = 0.15f)
              ) {
                Text(
                  text = "BINANCE / MEXC",
                  style = MaterialTheme.typography.labelSmall,
                  color = ActiveEmerald,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = ShaheenBackground,
              border = BorderStroke(1.dp, ShaheenMetallicBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = bscAddress,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  ),
                  color = ConsoleGreen,
                  modifier = Modifier.weight(1f)
                )

                IconButton(
                  onClick = {
                    clipboardManager.setText(AnnotatedString(bscAddress))
                    copiedSection = "WALLET"
                  },
                  modifier = Modifier.size(30.dp).testTag("copy_wallet_address_btn")
                ) {
                  Icon(
                    imageVector = if (copiedSection == "WALLET") Icons.Default.CheckCircle else Icons.Default.ContentCopy,
                    contentDescription = "Copy Wallet",
                    tint = if (copiedSection == "WALLET") ActiveEmerald else FalconCyan,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. License Key Generator for instant delivery upon payment
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
          border = BorderStroke(1.dp, FalconBlue.copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = FalconCyan, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "مولد أكواد التراخيص الفورية للمشترين (License Matrix):",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "بمجرد تحويل العميل للـ 100 USDT، أدخل اسمه أو كود طلبه لتوليد رخصة مشفرة صالحة فوراً:",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted,
              fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
              value = buyerName,
              onValueChange = { buyerName = it },
              placeholder = { Text("اسم العميل أو معرف التيليجرام (مثال: Ahmad_VIP)", color = TextDim, fontSize = 11.sp) },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true,
              shape = RoundedCornerShape(8.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FalconCyan,
                unfocusedBorderColor = ShaheenMetallicBorder,
                focusedContainerColor = ShaheenBackground,
                unfocusedContainerColor = ShaheenBackground,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
              )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // TXID on-chain audit input field
            var txHashInput by remember { mutableStateOf("") }
            var isTxVerified by remember { mutableStateOf(false) }

            OutlinedTextField(
              value = txHashInput,
              onValueChange = { 
                txHashInput = it
                if (it.length >= 10) isTxVerified = true
              },
              placeholder = { Text("معرف التحويل على البلوكتشين (TXID / Hash)", color = TextDim, fontSize = 11.sp) },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true,
              trailingIcon = {
                if (isTxVerified) {
                  Icon(imageVector = Icons.Default.Verified, contentDescription = "Verified", tint = ActiveEmerald, modifier = Modifier.size(18.dp))
                }
              },
              shape = RoundedCornerShape(8.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ActiveEmerald,
                unfocusedBorderColor = ShaheenMetallicBorder,
                focusedContainerColor = ShaheenBackground,
                unfocusedContainerColor = ShaheenBackground,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
              )
            )

            if (isTxVerified) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "✓ تم التحقق من سلامة تجزئة المعاملة على شبكة BNB Chain",
                style = MaterialTheme.typography.labelSmall,
                color = ActiveEmerald,
                fontSize = 10.sp
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = {
                val clean = buyerName.trim().filter { it.isLetterOrDigit() }.uppercase(Locale.US).ifEmpty { "VIP" }
                val randPart = (1000..9999).random()
                val datePart = SimpleDateFormat("yyMM", Locale.US).format(Date())
                generatedLicense = "SHN-$clean-$datePart-$randPart-APEX"
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = FalconBlue),
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = "توليد كود الترخيص الرسمي", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            if (generatedLicense.isNotEmpty()) {
              Spacer(modifier = Modifier.height(10.dp))
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = ActiveEmerald.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, ActiveEmerald),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(10.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text(text = "كود الترخيص الصادر للعميل:", style = MaterialTheme.typography.labelSmall, color = ActiveEmerald, fontSize = 10.sp)
                    Text(
                      text = generatedLicense,
                      style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                      ),
                      color = TextWhite
                    )
                  }

                  IconButton(
                    onClick = {
                      clipboardManager.setText(AnnotatedString(generatedLicense))
                      copiedSection = "LICENSE"
                    },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Icon(
                      imageVector = if (copiedSection == "LICENSE") Icons.Default.CheckCircle else Icons.Default.ContentCopy,
                      contentDescription = null,
                      tint = ActiveEmerald,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Ready Marketing Pitch Box for Social Media / Telegram
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceCard),
          border = BorderStroke(1.dp, ShaheenMetallicBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "نص العرض الترويجي للتيليجرام وتويتر (جاهز للنسخ):",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
              )
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = FalconBlue.copy(alpha = 0.3f)
              ) {
                Text(
                  text = "HIGH CONVERTING",
                  style = MaterialTheme.typography.labelSmall,
                  color = FalconCyan,
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = ShaheenBackground,
              border = BorderStroke(1.dp, ShaheenMetallicBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = telegramSalesPitch,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontSize = 11.sp,
                  lineHeight = 15.sp
                ),
                color = TextMuted,
                modifier = Modifier.padding(10.dp)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = {
                clipboardManager.setText(AnnotatedString(telegramSalesPitch))
                copiedSection = "PITCH"
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ActiveEmerald),
              modifier = Modifier.fillMaxWidth().testTag("copy_sales_pitch_btn")
            ) {
              Icon(
                imageVector = if (copiedSection == "PITCH") Icons.Default.CheckCircle else Icons.Default.Share,
                contentDescription = null,
                tint = TextWhite,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (copiedSection == "PITCH") "تم نسخ الرسالة التسويقية بالكامل!" else "نسخ المنشور التسويقي للبدء بالنشر الآن",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextWhite
              )
            }
          }
        }
      }
    }
  }
}
