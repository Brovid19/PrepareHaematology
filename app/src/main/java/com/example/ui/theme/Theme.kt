package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PrepareHaematologyColorScheme = darkColorScheme(
  primary = CyanPrimary,
  onPrimary = DeepNavyBg,
  primaryContainer = CardElevatedBg,
  onPrimaryContainer = CyanPrimary,
  secondary = TealAccent,
  onSecondary = DeepNavyBg,
  tertiary = EmeraldSuccess,
  onTertiary = DeepNavyBg,
  background = DeepNavyBg,
  onBackground = TextPrimary,
  surface = SlateNavySurface,
  onSurface = TextPrimary,
  surfaceVariant = CardElevatedBg,
  onSurfaceVariant = TextSecondary,
  outline = CardBorderStroke,
  error = CrimsonError,
  onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  // Always use Prepare Haematology sleek clinical dark mode with reds, blacks and greys
  MaterialTheme(
    colorScheme = PrepareHaematologyColorScheme,
    typography = Typography,
    content = content
  )
}
