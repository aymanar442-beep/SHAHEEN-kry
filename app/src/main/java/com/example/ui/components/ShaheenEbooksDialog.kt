package com.example.ui.components

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.EbookChapter
import com.example.data.ShaheenEbook
import com.example.data.ShaheenEbooksRepository
import com.example.ui.theme.*
import com.example.util.FalconAudioEngine

@Composable
fun ShaheenEbooksDialog(
  onDismiss: () -> Unit,
  onOpenSubscriptionHub: () -> Unit
) {
  val books = remember { ShaheenEbooksRepository.getAllBooks() }
  var selectedBookIndex by remember { mutableIntStateOf(0) }
  val activeBook = books[selectedBookIndex]
  var selectedChapterIndex by remember { mutableIntStateOf(0) }
  val activeChapter = activeBook.chapters.getOrNull(selectedChapterIndex) ?: activeBook.chapters.first()

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .fillMaxHeight(0.93f)
        .clip(RoundedCornerShape(22.dp))
        .border(
          BorderStroke(
            1.5.dp,
            Brush.linearGradient(listOf(FalconCyan, FalconGold, FalconGreen))
          ),
          RoundedCornerShape(22.dp)
        ),
      color = Color(0xFF0C141B)
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
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(FalconBlue, FalconGold)))
                .border(1.dp, FalconCyan, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = "Shaheen Master Books",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }
            Column {
              Text(
                text = "المكتبة التوثيقية والدستور العالمي",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
              )
              Text(
                text = "SHAHEEN APEX • Official Enterprise E-Books",
                color = FalconCyan,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }

          Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            // Play Falcon Sound button
            IconButton(
              onClick = { FalconAudioEngine.playFalconStartupChime() },
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(FalconGold.copy(alpha = 0.2f))
                .border(1.dp, FalconGold, CircleShape)
                .testTag("falcon_sound_chime_button")
            ) {
              Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Play Falcon Cry & Harmonic Sound",
                tint = FalconGold,
                modifier = Modifier.size(18.dp)
              )
            }

            IconButton(
              onClick = onDismiss,
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E293B))
                .testTag("close_ebooks_dialog_button")
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Book Selector Tabs
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF141F28))
            .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          books.forEachIndexed { index, book ->
            val isSelected = selectedBookIndex == index
            Button(
              onClick = {
                selectedBookIndex = index
                selectedChapterIndex = 0
              },
              modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .testTag("ebook_tab_${book.id}"),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) FalconBlue else Color.Transparent,
                contentColor = if (isSelected) Color.White else Color(0xFF94A3B8)
              ),
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = if (index == 0) Icons.Default.Speed else Icons.Default.Gavel,
                  contentDescription = null,
                  tint = if (isSelected) FalconCyan else Color(0xFF94A3B8),
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (index == 0) "📘 دليل التشغيل التكتيكي" else "📗 دستور شاهين والمعايير العالمية",
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 11.sp,
                  maxLines = 1
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Book Banner Metadata
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF12222E)),
          border = BorderStroke(1.dp, FalconCyan.copy(alpha = 0.35f))
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Badge(
                containerColor = FalconGold.copy(alpha = 0.2f),
                contentColor = FalconGold
              ) {
                Text(activeBook.badgeAr, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp)
              }

              Text(
                text = activeBook.editionAr,
                color = Color(0xFF94A3B8),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = activeBook.titleAr,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )

            Text(
              text = activeBook.subtitleAr,
              color = Color(0xFF38BDF8),
              fontSize = 12.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chapter selector pills (Horizontal scroll or wrapped)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0F172A))
            .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          activeBook.chapters.forEachIndexed { chIdx, chapter ->
            val isChSelected = selectedChapterIndex == chIdx
            Box(
              modifier = Modifier
                .weight(1f)
                .height(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isChSelected) FalconCyan.copy(alpha = 0.25f) else Color.Transparent)
                .border(
                  1.dp,
                  if (isChSelected) FalconCyan else Color.Transparent,
                  RoundedCornerShape(8.dp)
                )
                .clickable { selectedChapterIndex = chIdx }
                .padding(horizontal = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "فصل ${chapter.chapterNumber}",
                color = if (isChSelected) FalconCyan else Color(0xFF64748B),
                fontWeight = if (isChSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chapter Content Reading Area (Vertical Scroll)
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF0F1922))
            .border(1.dp, Color(0xFF1E2D3D), RoundedCornerShape(14.dp))
            .padding(14.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
          ) {
            // Chapter Title
            Text(
              text = activeChapter.titleAr,
              color = FalconGold,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 16.sp
            )

            Text(
              text = activeChapter.subtitleAr,
              color = Color(0xFFA5F3FC),
              fontSize = 12.sp,
              modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            Divider(color = Color(0xFF1E2D3D))

            Spacer(modifier = Modifier.height(10.dp))

            // Body text
            Text(
              text = activeChapter.contentMarkdownAr,
              color = Color(0xFFE2E8F0),
              fontSize = 13.5.sp,
              lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Key Takeaways Box
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFF0A2E28)),
              border = BorderStroke(1.dp, FalconGreen.copy(alpha = 0.5f))
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = FalconGreen,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "خلاصة الفصل التكتيكية والتشريعية:",
                    color = FalconGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))

                activeChapter.keyTakeaways.forEach { takeaway ->
                  Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                  ) {
                    Text("• ", color = FalconGreen, fontWeight = FontWeight.Bold)
                    Text(
                      text = takeaway,
                      color = Color.White,
                      fontSize = 12.sp,
                      lineHeight = 18.sp
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legal & Global Sovereignty Certification Seal
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFF161F2E)),
              border = BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.4f))
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.VerifiedUser,
                  contentDescription = null,
                  tint = Color(0xFF60A5FA),
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "معتمد دولياً • Non-Custodial Sovereign Standard",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  )
                  Text(
                    text = "متوافق بالكامل مع تشريعات MiCA الأوروبية وقوانين الخصوصية المشفرة السويسرية.",
                    color = Color(0xFF94A3B8),
                    fontSize = 10.sp
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Action Buttons Bottom
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = {
              if (selectedChapterIndex < activeBook.chapters.size - 1) {
                selectedChapterIndex++
              } else if (selectedBookIndex < books.size - 1) {
                selectedBookIndex++
                selectedChapterIndex = 0
              }
            },
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .testTag("next_chapter_ebook_button"),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, FalconCyan),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = FalconCyan)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("الفصل التالي", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.width(4.dp))
              Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
          }

          Button(
            onClick = {
              onDismiss()
              onOpenSubscriptionHub()
            },
            modifier = Modifier
              .weight(1.3f)
              .height(44.dp)
              .testTag("upgrade_from_ebook_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = FalconGold,
              contentColor = Color.Black
            )
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Diamond, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("ترقية وتفعيل الحساب ($29 - $49)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
