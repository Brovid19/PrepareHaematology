package com.example.ui.theme

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
  secondaryContainer = CardElevatedBg,
  onSecondaryContainer = TealAccent,
  tertiary = EmeraldSuccess,
  onTertiary = DeepNavyBg,
  tertiaryContainer = CardElevatedBg,
  onTertiaryContainer = EmeraldSuccess,
  background = DeepNavyBg,
  onBackground = TextPrimary,
  surface = SlateNavySurface,
  onSurface = TextPrimary,
  surfaceVariant = CardElevatedBg,
  onSurfaceVariant = TextSecondary,
  outline = CardBorderStroke,
  outlineVariant = LabBorder,
  error = CrimsonError,
  onError = DeepNavyBg,
  errorContainer = CardElevatedBg,
  onErrorContainer = CrimsonError
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Optimised for night mode by default
  content: @Composable () -> Unit,
) {
  // Prepare Haematology sleek, eye-comfort clinical night mode with softened reds, blacks, and greys
  MaterialTheme(
    colorScheme = PrepareHaematologyColorScheme,
    typography = Typography,
    content = content
  )
}
