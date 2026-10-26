package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.TtsItemType
import com.example.audio.TtsPlaybackState
import com.example.audio.TtsPlaylistItem
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.CardElevatedBg
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.LabBorder
import com.example.ui.theme.LabTableBg
import com.example.ui.theme.SlateNavySurface
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TtsAudioPlayerBar(
    ttsState: TtsPlaybackState,
    onPlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onStop: () -> Unit,
    onOpenPlaylist: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isVisible = ttsState.isPlaying || ttsState.isPaused || ttsState.playlist.isNotEmpty() || ttsState.currentSpeakingItem != null

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        val activeItem = ttsState.currentSpeakingItem
            ?: ttsState.playlist.getOrNull(ttsState.currentItemIndex)
            ?: ttsState.playlist.firstOrNull()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .testTag("tts_audio_player_bar"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SlateNavySurface),
            border = CardBorderStroke
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Audio Wave / Speaker Icon
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (ttsState.isPlaying) TealAccent.copy(alpha = 0.2f) else LabTableBg)
                        .border(1.dp, if (ttsState.isPlaying) TealAccent else LabBorder, CircleShape)
                        .clickable { onOpenPlaylist() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (ttsState.isPlaying) Icons.Filled.GraphicEq else Icons.Filled.Headphones,
                        contentDescription = "Audio Player",
                        tint = if (ttsState.isPlaying) TealAccent else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Track Details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenPlaylist() }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (activeItem != null) {
                            val typeColor = if (activeItem.type == TtsItemType.SUMMARY) TealAccent else Color(0xFFD66371)
                            Text(
                                text = if (activeItem.type == TtsItemType.SUMMARY) "SUMMARY" else "FLASHCARD",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = typeColor,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(typeColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        if (ttsState.playlist.isNotEmpty()) {
                            Text(
                                text = "${ttsState.currentItemIndex + 1} of ${ttsState.playlist.size}",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        } else {
                            Text(
                                text = "Direct Listen",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = activeItem?.title ?: "FRCPath Audio Study Session",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Playback Controls
                if (ttsState.playlist.size > 1) {
                    IconButton(
                        onClick = onSkipPrevious,
                        modifier = Modifier.size(32.dp).testTag("tts_prev_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SkipPrevious,
                            contentDescription = "Previous Track",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Play / Pause Button
                IconButton(
                    onClick = onPlayPause,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(TealAccent.copy(alpha = 0.2f))
                        .testTag("tts_play_pause_button")
                ) {
                    Icon(
                        imageVector = if (ttsState.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (ttsState.isPlaying) "Pause" else "Play",
                        tint = TealAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }

                if (ttsState.playlist.size > 1) {
                    IconButton(
                        onClick = onSkipNext,
                        modifier = Modifier.size(32.dp).testTag("tts_next_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SkipNext,
                            contentDescription = "Next Track",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Playlist Queue Button
                IconButton(
                    onClick = onOpenPlaylist,
                    modifier = Modifier.size(32.dp).testTag("tts_open_playlist_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                        contentDescription = "Open Playlist Queue",
                        tint = if (ttsState.playlist.isNotEmpty()) CyanPrimary else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Stop Button
                IconButton(
                    onClick = onStop,
                    modifier = Modifier.size(28.dp).testTag("tts_stop_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Stop Audio",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TtsPlaylistDialog(
    ttsState: TtsPlaybackState,
    onDismiss: () -> Unit,
    onPlayItem: (Int) -> Unit,
    onPlayPause: () -> Unit,
    onRemoveItem: (Int) -> Unit,
    onClearPlaylist: () -> Unit,
    onSetSpeechRate: (Float) -> Unit,
    onAddAllSummaries: () -> Unit,
    onAddDueCards: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth().testTag("tts_playlist_dialog"),
        containerColor = SlateNavySurface,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.PlaylistPlay,
                        contentDescription = null,
                        tint = TealAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Audio Revision Playlist",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Speech Rate Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Speed,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Speech Rate:",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(0.85f to "0.85x", 1.0f to "1.0x", 1.25f to "1.25x", 1.5f to "1.5x").forEach { (rate, label) ->
                            val isSelected = (ttsState.speechRate - rate) in -0.05f..0.05f
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) TealAccent.copy(alpha = 0.25f) else LabTableBg)
                                    .border(1.dp, if (isSelected) TealAccent else LabBorder, RoundedCornerShape(6.dp))
                                    .clickable { onSetSpeechRate(rate) }
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) TealAccent else TextMuted
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Add Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = onAddAllSummaries,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TealAccent),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TealAccent.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.PlaylistAdd, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ All Summaries", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = onAddDueCards,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD66371)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD66371).copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.PlaylistAdd, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Due Flashcards", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Status row: Queue size & Clear button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Queue: ${ttsState.playlist.size} items",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    if (ttsState.playlist.isNotEmpty()) {
                        Text(
                            text = "Clear All",
                            fontSize = 12.sp,
                            color = Color(0xFFD66371),
                            modifier = Modifier
                                .clickable { onClearPlaylist() }
                                .padding(vertical = 2.dp, horizontal = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Playlist Items List
                if (ttsState.playlist.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(LabTableBg)
                            .border(1.dp, LabBorder, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Queue is currently empty",
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                            Text(
                                text = "Use '+ Add' above or click the TTS buttons in Summaries and Flashcards",
                                fontSize = 11.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 280.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        itemsIndexed(ttsState.playlist) { index, item ->
                            val isCurrentlyPlaying = ttsState.isPlaying && ttsState.currentItemIndex == index
                            val isSelected = ttsState.currentItemIndex == index
                            val itemColor = if (item.type == TtsItemType.SUMMARY) TealAccent else Color(0xFFD66371)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isCurrentlyPlaying) TealAccent.copy(alpha = 0.15f) else LabTableBg)
                                    .border(
                                        1.dp,
                                        if (isCurrentlyPlaying) TealAccent else if (isSelected) LabBorder else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onPlayItem(index) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Index badge
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrentlyPlaying) TealAccent else TextMuted,
                                    modifier = Modifier.width(22.dp)
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (item.type == TtsItemType.SUMMARY) "SUMMARY" else "CARD",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = itemColor,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(itemColor.copy(alpha = 0.15f))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "~${item.estimatedDurationSeconds}s",
                                            fontSize = 9.sp,
                                            color = TextMuted
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.title,
                                        fontSize = 12.sp,
                                        fontWeight = if (isCurrentlyPlaying) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isCurrentlyPlaying) TextPrimary else TextSecondary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                // Play / Playing indicator
                                if (isCurrentlyPlaying) {
                                    Icon(
                                        imageVector = Icons.Filled.GraphicEq,
                                        contentDescription = "Playing",
                                        tint = TealAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    IconButton(
                                        onClick = { onPlayItem(index) },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.PlayArrow,
                                            contentDescription = "Play this track",
                                            tint = TextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = { onRemoveItem(index) },
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Remove from queue",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (ttsState.playlist.isNotEmpty()) {
                Button(
                    onClick = {
                        if (ttsState.isPlaying) {
                            onPlayPause()
                        } else {
                            onPlayItem(ttsState.currentItemIndex)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = if (ttsState.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = DeepNavyBg,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (ttsState.isPlaying) "Pause Playback" else "Start Playlist (${ttsState.playlist.size} items)",
                        color = DeepNavyBg,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                border = androidx.compose.foundation.BorderStroke(1.dp, LabBorder),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Close")
            }
        }
    )
}
