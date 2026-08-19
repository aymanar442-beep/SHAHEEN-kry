package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LogEntry
import com.example.model.LogLevel
import com.example.ui.theme.ActiveEmerald
import com.example.ui.theme.ConsoleCyan
import com.example.ui.theme.ConsoleGreen
import com.example.ui.theme.ConsolePurple
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Filter mode for log severity levels.
 */
enum class SeverityFilter(val label: String, val levelGroup: Set<LogLevel>?) {
  ALL("الكل (All)", null),
  INFO("معلومات (Info)", setOf(LogLevel.INFO, LogLevel.SYSTEM)),
  WARNING("تحذير (Warning)", setOf(LogLevel.WARNING)),
  CRITICAL("أخطاء حرجة (Critical)", setOf(LogLevel.ERROR)),
  SUCCESS("ناجحة (Success)", setOf(LogLevel.SUCCESS))
}

/**
 * Filter mode for time/date range.
 */
enum class TimeRangeFilter(val label: String, val minutesWindow: Int?) {
  ALL_TIME("كافة الأوقات", null),
  LAST_5_MIN("آخر 5 دقائق", 5),
  LAST_30_MIN("آخر 30 دقيقة", 30),
  LAST_1_HOUR("آخر ساعة", 60),
  TODAY("سجلات اليوم", 1440)
}

@Composable
fun TelemetryConsole(
  logs: List<LogEntry>,
  isRunning: Boolean,
  onClearLogs: () -> Unit,
  onOpenExport: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val listState = rememberLazyListState()
  val context = LocalContext.current

  var searchQuery by remember { mutableStateOf("") }
  var selectedSeverity by remember { mutableStateOf(SeverityFilter.ALL) }
  var selectedTimeRange by remember { mutableStateOf(TimeRangeFilter.ALL_TIME) }
  var showFilterBar by remember { mutableStateOf(true) }

  // Filter logs based on search query, severity, and date/time range
  val filteredLogs by remember(logs, searchQuery, selectedSeverity, selectedTimeRange) {
    derivedStateOf {
      val now = Calendar.getInstance()
      val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
      val currentMidnightMillis = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
      }.timeInMillis

      logs.filter { log ->
        // 1. Severity filter
        val matchesSeverity = when (val levels = selectedSeverity.levelGroup) {
          null -> true
          else -> levels.contains(log.level)
        }

        // 2. Search query filter (matches message, tag, or timestamp)
        val matchesSearch = if (searchQuery.isBlank()) {
          true
        } else {
          val query = searchQuery.trim().lowercase(Locale.ROOT)
          log.message.lowercase(Locale.ROOT).contains(query) ||
              log.tag.lowercase(Locale.ROOT).contains(query) ||
              log.timestamp.lowercase(Locale.ROOT).contains(query)
        }

        // 3. Time/Date range filter
        val matchesTime = when (val minutes = selectedTimeRange.minutesWindow) {
          null -> true
          else -> {
            try {
              // Parse time string e.g. "14:25:32.100" or "14:25:32"
              val cleanedTime = log.timestamp.split(".").firstOrNull() ?: log.timestamp
              val parsedDate = timeFormat.parse(cleanedTime)
              if (parsedDate != null) {
                val logCalendar = Calendar.getInstance().apply {
                  time = parsedDate
                  // Inherit year, month, and day from current date
                  set(Calendar.YEAR, now.get(Calendar.YEAR))
                  set(Calendar.MONTH, now.get(Calendar.MONTH))
                  set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
                }
                val diffMinutes = (now.timeInMillis - logCalendar.timeInMillis) / (1000 * 60)
                diffMinutes in 0..minutes
              } else {
                true
              }
            } catch (e: Exception) {
              true
            }
          }
        }

        matchesSeverity && matchesSearch && matchesTime
      }
    }
  }

  // Count per severity category for badge numbers
  val countCritical = remember(logs) { logs.count { it.level == LogLevel.ERROR } }
  val countWarning = remember(logs) { logs.count { it.level == LogLevel.WARNING } }
  val countInfo = remember(logs) { logs.count { it.level == LogLevel.INFO || it.level == LogLevel.SYSTEM } }
  val countSuccess = remember(logs) { logs.count { it.level == LogLevel.SUCCESS } }

  // Auto-scroll when new filtered logs arrive
  LaunchedEffect(filteredLogs.size) {
    if (filteredLogs.isNotEmpty()) {
      listState.scrollToItem(filteredLogs.size - 1)
    }
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(ShaheenSurfaceDark)
      .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(16.dp))
      .testTag("telemetry_console")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
    ) {
      // Console Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(ShaheenSurfaceCard)
          .border(
            width = 1.dp,
            color = ShaheenMetallicBorder,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
          )
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Terminal indicator dot
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(if (isRunning) ActiveEmerald else InactiveCrimson)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.Default.Terminal,
            contentDescription = null,
            tint = FalconBlue,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "LIVE TELEMETRY CONSOLE",
            style = MaterialTheme.typography.labelSmall.copy(
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            ),
            color = TextWhite
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Filter toggle button
          IconButton(
            onClick = { showFilterBar = !showFilterBar },
            modifier = Modifier.size(32.dp).testTag("toggle_filters_button")
          ) {
            Icon(
              imageVector = Icons.Default.FilterList,
              contentDescription = "Toggle filters",
              tint = if (showFilterBar) FalconCyan else TextMuted,
              modifier = Modifier.size(16.dp)
            )
          }

          // Copy logs action
          IconButton(
            onClick = {
              val textToCopy = filteredLogs.joinToString("\n") { "[${it.timestamp}] [${it.tag}] [${it.level}] ${it.message}" }
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Shaheen Telemetry Logs", textToCopy)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "${filteredLogs.size} logs copied to clipboard", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.size(32.dp).testTag("copy_logs_button")
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "Copy logs",
              tint = TextMuted,
              modifier = Modifier.size(16.dp)
            )
          }

          // Export data action
          IconButton(
            onClick = onOpenExport,
            modifier = Modifier.size(32.dp).testTag("export_telemetry_button")
          ) {
            Icon(
              imageVector = Icons.Default.FileDownload,
              contentDescription = "Export telemetry and trends",
              tint = FalconCyan,
              modifier = Modifier.size(16.dp)
            )
          }

          // Clear logs action
          IconButton(
            onClick = onClearLogs,
            modifier = Modifier.size(32.dp).testTag("clear_logs_button")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteSweep,
              contentDescription = "Clear logs",
              tint = TextMuted,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // Search & Filter Panel
      AnimatedVisibility(
        visible = showFilterBar,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(ShaheenSurfaceElevated.copy(alpha = 0.5f))
            .border(1.dp, ShaheenMetallicBorderLight.copy(alpha = 0.3f))
            .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
          // Search Bar
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(38.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(ShaheenSurfaceDark)
              .border(1.dp, ShaheenMetallicBorder, RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = FalconCyan,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            BasicTextField(
              value = searchQuery,
              onValueChange = { searchQuery = it },
              modifier = Modifier
                .weight(1f)
                .testTag("log_search_input"),
              textStyle = TextStyle(
                color = TextWhite,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
              ),
              cursorBrush = SolidColor(FalconCyan),
              singleLine = true,
              decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                  Text(
                    text = "بحث في السجلات (Search tag, keyword, time)...",
                    color = TextDim,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                  )
                }
                innerTextField()
              }
            )
            if (searchQuery.isNotEmpty()) {
              IconButton(
                onClick = { searchQuery = "" },
                modifier = Modifier.size(24.dp).testTag("clear_search_button")
              ) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Clear search",
                  tint = TextDim,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Severity Level Filter Chips Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "المستوى:",
              color = TextDim,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(end = 2.dp)
            )

            // ALL
            LogFilterChip(
              text = "الكل (${logs.size})",
              isSelected = selectedSeverity == SeverityFilter.ALL,
              onClick = { selectedSeverity = SeverityFilter.ALL },
              icon = Icons.Default.FilterList,
              activeColor = FalconCyan,
              tag = "filter_chip_all"
            )

            // INFO
            LogFilterChip(
              text = "Info ($countInfo)",
              isSelected = selectedSeverity == SeverityFilter.INFO,
              onClick = { selectedSeverity = SeverityFilter.INFO },
              icon = Icons.Default.Info,
              activeColor = FalconCyan,
              tag = "filter_chip_info"
            )

            // WARNING
            LogFilterChip(
              text = "Warning ($countWarning)",
              isSelected = selectedSeverity == SeverityFilter.WARNING,
              onClick = { selectedSeverity = SeverityFilter.WARNING },
              icon = Icons.Default.WarningAmber,
              activeColor = ConsoleYellow,
              tag = "filter_chip_warning"
            )

            // CRITICAL
            LogFilterChip(
              text = "Critical ($countCritical)",
              isSelected = selectedSeverity == SeverityFilter.CRITICAL,
              onClick = { selectedSeverity = SeverityFilter.CRITICAL },
              icon = Icons.Default.ErrorOutline,
              activeColor = InactiveCrimson,
              tag = "filter_chip_critical"
            )

            // SUCCESS
            LogFilterChip(
              text = "Success ($countSuccess)",
              isSelected = selectedSeverity == SeverityFilter.SUCCESS,
              onClick = { selectedSeverity = SeverityFilter.SUCCESS },
              icon = Icons.Default.CheckCircle,
              activeColor = ActiveEmerald,
              tag = "filter_chip_success"
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          // Date / Time Range Filter Chips Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "النطاق:",
              color = TextDim,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(end = 2.dp)
            )

            TimeRangeFilter.values().forEach { timeFilter ->
              TimeFilterChip(
                text = timeFilter.label,
                isSelected = selectedTimeRange == timeFilter,
                onClick = { selectedTimeRange = timeFilter },
                tag = "time_filter_${timeFilter.name.lowercase(Locale.ROOT)}"
              )
            }
          }

          // Active filter results counter
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "عرض ${filteredLogs.size} من أصل ${logs.size} سجل",
              color = TextMuted,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace
            )

            if (selectedSeverity != SeverityFilter.ALL || selectedTimeRange != TimeRangeFilter.ALL_TIME || searchQuery.isNotEmpty()) {
              Text(
                text = "إعادة ضبط التصفية ↺",
                color = FalconCyan,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                  .clickable {
                    searchQuery = ""
                    selectedSeverity = SeverityFilter.ALL
                    selectedTimeRange = TimeRangeFilter.ALL_TIME
                  }
                  .testTag("reset_filters_button")
              )
            }
          }
        }
      }

      // Console Body / Log List
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
          .background(ShaheenBackground)
          .padding(8.dp)
      ) {
        if (filteredLogs.isEmpty()) {
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = if (logs.isEmpty()) {
                "Console idle. Click VPN switch to start monitoring loop."
              } else {
                "لا توجد سجلات تطابق معايير التصفية والبحث المحددة."
              },
              style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
              color = TextDim
            )
          }
        } else {
          LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            items(filteredLogs, key = { it.id }) { log ->
              LogLine(log = log)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun LogFilterChip(
  text: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  icon: ImageVector,
  activeColor: Color,
  tag: String
) {
  Surface(
    modifier = Modifier
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .testTag(tag),
    shape = RoundedCornerShape(14.dp),
    color = if (isSelected) activeColor.copy(alpha = 0.2f) else ShaheenSurfaceDark,
    border = BorderStroke(
      1.dp,
      if (isSelected) activeColor else ShaheenMetallicBorder
    )
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isSelected) activeColor else TextDim,
        modifier = Modifier.size(12.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = text,
        color = if (isSelected) TextWhite else TextMuted,
        fontSize = 10.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        fontFamily = FontFamily.Monospace
      )
    }
  }
}

@Composable
private fun TimeFilterChip(
  text: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  tag: String
) {
  Surface(
    modifier = Modifier
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .testTag(tag),
    shape = RoundedCornerShape(14.dp),
    color = if (isSelected) FalconBlue.copy(alpha = 0.25f) else ShaheenSurfaceDark,
    border = BorderStroke(
      1.dp,
      if (isSelected) FalconBlue else ShaheenMetallicBorder
    )
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.AccessTime,
        contentDescription = null,
        tint = if (isSelected) FalconCyan else TextDim,
        modifier = Modifier.size(11.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = text,
        color = if (isSelected) TextWhite else TextMuted,
        fontSize = 9.5.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
      )
    }
  }
}

@Composable
private fun LogLine(log: LogEntry) {
  val (tagColor, textColor) = when (log.level) {
    LogLevel.SUCCESS -> ActiveEmerald to ConsoleGreen
    LogLevel.ERROR -> InactiveCrimson to InactiveCrimson
    LogLevel.WARNING -> ConsoleYellow to ConsoleYellow
    LogLevel.SYSTEM -> ConsolePurple to TextWhite
    LogLevel.INFO -> FalconCyan to TextWhite
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 1.dp),
    verticalAlignment = Alignment.Top
  ) {
    Text(
      text = log.timestamp,
      style = MaterialTheme.typography.labelSmall.copy(
        fontFamily = FontFamily.Monospace,
        fontSize = 10.sp
      ),
      color = TextDim,
      modifier = Modifier.padding(end = 6.dp)
    )

    Text(
      text = "[${log.tag}]",
      style = MaterialTheme.typography.labelSmall.copy(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp
      ),
      color = tagColor,
      modifier = Modifier.padding(end = 6.dp)
    )

    Text(
      text = log.message,
      style = MaterialTheme.typography.labelSmall.copy(
        fontFamily = FontFamily.Monospace,
        fontSize = 10.sp,
        lineHeight = 14.sp
      ),
      color = textColor
    )
  }
}
