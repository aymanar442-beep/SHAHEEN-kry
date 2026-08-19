package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.db.entity.MarketTrendEntity
import com.example.data.export.DataExportManager
import com.example.data.export.ExportFormat
import com.example.data.export.ExportResult
import com.example.data.export.ExportScope
import com.example.model.EngineStatus
import com.example.model.LogEntry
import com.example.model.ShaheenConfig
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.ConsoleCyan
import com.example.ui.theme.ConsoleYellow
import com.example.ui.theme.FalconBlue
import com.example.ui.theme.FalconCyan
import com.example.ui.theme.InactiveCrimson
import com.example.ui.theme.ShaheenBackground
import com.example.ui.theme.ShaheenMetallicBorder
import com.example.ui.theme.ShaheenMetallicBorderLight
import com.example.ui.theme.ShaheenSurfaceCard
import com.example.ui.theme.ShaheenSurfaceDark
import com.example.ui.theme.ShaheenSurfaceElevated
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.util.Locale

@Composable
fun DataExportDialog(
  logs: List<LogEntry>,
  marketTrends: List<MarketTrendEntity>,
  engineStatus: EngineStatus,
  config: ShaheenConfig,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  var selectedScope by remember { mutableStateOf(ExportScope.ALL_TELEMETRY) }
  var selectedFormat by remember { mutableStateOf(ExportFormat.CSV) }
  var isExporting by remember { mutableStateOf(false) }
  var showPreview by remember { mutableStateOf(false) }
  var lastExportedResult by remember { mutableStateOf<ExportResult?>(null) }

  // Generate payload string dynamically based on scope and format
  val exportedPayload = remember(selectedScope, selectedFormat, logs.size, marketTrends.size) {
    when (selectedFormat) {
      ExportFormat.CSV -> {
        when (selectedScope) {
          ExportScope.ALL_TELEMETRY -> DataExportManager.exportFullTelemetryPackageCsv(logs, marketTrends)
          ExportScope.LOGS_ONLY -> DataExportManager.exportLogsToCsv(logs)
          ExportScope.MARKET_TRENDS_ONLY -> DataExportManager.exportMarketTrendsToCsv(marketTrends)
        }
      }
      ExportFormat.JSON -> {
        when (selectedScope) {
          ExportScope.ALL_TELEMETRY -> DataExportManager.exportFullTelemetryPackageJson(logs, marketTrends, engineStatus, config)
          ExportScope.LOGS_ONLY -> DataExportManager.exportLogsToJson(logs)
          ExportScope.MARKET_TRENDS_ONLY -> DataExportManager.exportMarketTrendsToJson(marketTrends)
        }
      }
    }
  }

  val totalExportCount = when (selectedScope) {
    ExportScope.ALL_TELEMETRY -> logs.size + marketTrends.size
    ExportScope.LOGS_ONLY -> logs.size
    ExportScope.MARKET_TRENDS_ONLY -> marketTrends.size
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .padding(vertical = 16.dp)
        .testTag("data_export_dialog"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = ShaheenSurfaceDark),
      border = BorderStroke(1.5.dp, FalconBlue)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(FalconBlue.copy(alpha = 0.2f))
                .border(1.dp, FalconCyan, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.FileDownload,
                contentDescription = null,
                tint = FalconCyan,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "تصدير البيانات والتحليلات",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextWhite
              )
              Text(
                text = "Room Database Telemetry & Market Exporter",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontFamily = FontFamily.Monospace,
                  fontSize = 10.sp
                ),
                color = FalconCyan
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(32.dp).testTag("close_export_dialog_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextMuted,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = ShaheenMetallicBorder)
        Spacer(modifier = Modifier.height(14.dp))

        // Database Summary Cards
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Logs Count Box
          Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            color = ShaheenSurfaceCard,
            border = BorderStroke(1.dp, ShaheenMetallicBorder)
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Terminal,
                contentDescription = null,
                tint = ConsoleCyan,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "السجلات المخزنة",
                  style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                  color = TextMuted
                )
                Text(
                  text = "${logs.size} سجل",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                  ),
                  color = TextWhite
                )
              }
            }
          }

          // Trends Count Box
          Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            color = ShaheenSurfaceCard,
            border = BorderStroke(1.dp, ShaheenMetallicBorder)
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.QueryStats,
                contentDescription = null,
                tint = ActiveEmerald,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "اتجاهات السوق",
                  style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                  color = TextMuted
                )
                Text(
                  text = "${marketTrends.size} مؤشر",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                  ),
                  color = TextWhite
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Select Export Scope
        Text(
          text = "1. حدد نطاق البيانات المطلوب تصديرها:",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = FalconCyan
        )
        Spacer(modifier = Modifier.height(6.dp))

        ExportScope.values().forEach { scope ->
          val isSelected = selectedScope == scope
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) FalconBlue.copy(alpha = 0.25f) else ShaheenSurfaceElevated,
            border = BorderStroke(
              1.dp,
              if (isSelected) FalconCyan else ShaheenMetallicBorder
            ),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clickable { selectedScope = scope }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = isSelected,
                onClick = { selectedScope = scope },
                colors = RadioButtonDefaults.colors(
                  selectedColor = FalconCyan,
                  unselectedColor = TextMuted
                ),
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = scope.labelAr,
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                  color = if (isSelected) TextWhite else TextMuted
                )
                Text(
                  text = scope.labelEn,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                  ),
                  color = if (isSelected) FalconCyan else TextDim
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Select Export Format
        Text(
          text = "2. حدد صيغة التصدير (Format):",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = FalconCyan
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ExportFormat.values().forEach { format ->
            val isSelected = selectedFormat == format
            Surface(
              modifier = Modifier
                .weight(1f)
                .clickable { selectedFormat = format },
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) FalconBlue.copy(alpha = 0.3f) else ShaheenSurfaceCard,
              border = BorderStroke(
                1.5.dp,
                if (isSelected) ActiveEmerald else ShaheenMetallicBorder
              )
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = if (format == ExportFormat.CSV) Icons.Default.TableChart else Icons.Default.DataObject,
                  contentDescription = null,
                  tint = if (isSelected) ActiveEmerald else TextMuted,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = format.name,
                    style = MaterialTheme.typography.labelLarge.copy(
                      fontWeight = FontWeight.Bold,
                      fontFamily = FontFamily.Monospace
                    ),
                    color = if (isSelected) TextWhite else TextMuted
                  )
                  Text(
                    text = if (format == ExportFormat.CSV) "Excel / Sheets" else "JSON Data API",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = if (isSelected) ActiveEmerald else TextDim
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Payload Summary Stats & Toggle Preview
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = ShaheenSurfaceCard,
          border = BorderStroke(1.dp, ShaheenMetallicBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "الحجم التقديري: ~${String.format(Locale.US, "%.1f", exportedPayload.toByteArray().size / 1024.0)} KB",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontFamily = FontFamily.Monospace,
                  fontWeight = FontWeight.Bold
                ),
                color = TextWhite
              )
              Text(
                text = "العناصر: $totalExportCount عنصر جاهز للتصدير",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = FalconCyan
              )
            }

            OutlinedButton(
              onClick = { showPreview = !showPreview },
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
              modifier = Modifier.height(30.dp),
              border = BorderStroke(1.dp, FalconBlue)
            ) {
              Icon(
                imageVector = if (showPreview) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = null,
                tint = FalconCyan,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (showPreview) "إخفاء المعاينة" else "معاينة البيانات",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = FalconCyan
              )
            }
          }
        }

        // Accordion Live Preview
        AnimatedVisibility(visible = showPreview) {
          Column(modifier = Modifier.padding(top = 8.dp)) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = ShaheenBackground,
              border = BorderStroke(1.dp, ShaheenMetallicBorderLight),
              modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 160.dp)
            ) {
              Box(
                modifier = Modifier
                  .padding(8.dp)
                  .verticalScroll(rememberScrollState())
                  .horizontalScroll(rememberScrollState())
              ) {
                Text(
                  text = exportedPayload.take(3000) + if (exportedPayload.length > 3000) "\n... [truncated preview]" else "",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                  ),
                  color = ConsoleCyan
                )
              }
            }
          }
        }

        // Export Outcome Message
        lastExportedResult?.let { result ->
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (result.success) ActiveEmerald.copy(alpha = 0.15f) else InactiveCrimson.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, if (result.success) ActiveEmerald else InactiveCrimson),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (result.success) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = if (result.success) ActiveEmerald else InactiveCrimson,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = if (result.success) "تم تجهيز وتصدير الملف بنجاح!" else "فشل تصدير الملف",
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = if (result.success) ActiveEmerald else InactiveCrimson
                )
                Text(
                  text = result.fileName,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                  ),
                  color = TextWhite
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Copy to clipboard button
          OutlinedButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Shaheen Telemetry Export", exportedPayload)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "تم نسخ محتوى ${selectedFormat.name} إلى الحافظة بنجاح", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .testTag("copy_export_payload_button"),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, FalconBlue)
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = null,
              tint = FalconCyan,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "نسخ المحتوى",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = TextWhite
            )
          }

          // Save and Share File button
          Button(
            onClick = {
              isExporting = true
              val fileName = DataExportManager.generateExportFileName(selectedScope, selectedFormat)
              try {
                val savedFile = DataExportManager.saveExportToFile(context, fileName, exportedPayload)
                lastExportedResult = ExportResult(
                  success = true,
                  fileName = fileName,
                  filePath = savedFile.absolutePath,
                  totalItemsExported = totalExportCount,
                  fileSizeKb = savedFile.length() / 1024.0
                )

                // Trigger Share Sheet with the content and summary
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                  type = selectedFormat.mimeType
                  putExtra(Intent.EXTRA_SUBJECT, "SHAHEEN APEX AI - $fileName")
                  putExtra(
                    Intent.EXTRA_TEXT,
                    "ملف تصدير بيانات وتحليلات محرك شاهين ($fileName)\n" +
                    "النوع: ${selectedScope.labelAr}\n" +
                    "الصيغة: ${selectedFormat.name}\n\n" +
                    exportedPayload
                  )
                }
                context.startActivity(Intent.createChooser(shareIntent, "مشاركة وتصدير ملف شاهين ($fileName)"))
                Toast.makeText(context, "تم تصدير وحفظ: $fileName", Toast.LENGTH_LONG).show()
              } catch (e: Exception) {
                lastExportedResult = ExportResult(
                  success = false,
                  fileName = fileName,
                  errorMessage = e.localizedMessage
                )
                Toast.makeText(context, "خطأ أثناء التصدير: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
              } finally {
                isExporting = false
              }
            },
            modifier = Modifier
              .weight(1.3f)
              .height(44.dp)
              .testTag("save_and_share_export_button"),
            colors = ButtonDefaults.buttonColors(containerColor = FalconBlue),
            shape = RoundedCornerShape(10.dp)
          ) {
            if (isExporting) {
              CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = TextWhite,
                strokeWidth = 2.dp
              )
            } else {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = TextWhite,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "تصدير ومشاركة الملف",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TextWhite
              )
            }
          }
        }
      }
    }
  }
}
