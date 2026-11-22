package com.cogniapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Warm, Approachable Quiz App Palette (Warm bone canvas, crisp dark ink, amber & sky/teal accents)
// Light Canvas & Surface Tokens
val WarmCanvas = Color(0xFFEFEEE3)          // Warm bone / cream canvas (#EFEEE3)
val CardWhite = Color(0xFFFFFFFF)           // Crisp white card surface
val WarmBorder = Color(0xFFDEDDCF)          // Crisp framing border
val WarmBorderSubtle = Color(0xFFE8E7DC)

// Ink Tokens (High Contrast)
val CrispDarkInk = Color(0xFF070706)        // Crisp dark ink (#070706)
val SlateInkDark = CrispDarkInk
val SlateInkSecondary = Color(0xFF4C5C5E)   // Warm secondary slate (#4C5C5E)
val SlateInkMuted = Color(0xFFA3A39D)       // Muted secondary slate (#A3A39D)

// Thoughtful Approachable Accents
val WarmAmber = Color(0xFFFAAF14)           // Warm amber accent highlight (#FAAF14)
val SkyBlue = Color(0xFF2F98E8)             // Calm energetic sky accent (#2F98E8)
val DeepTeal = Color(0xFF41798C)            // Calm deep slate teal (#41798C)
val CoralOrange = Color(0xFFE84A26)         // Energetic coral-orange accent

val SkyTint = Color(0xFFE6F3FC)             // Soft sky selection tint
val SkyBorder = Color(0xFF2F98E8)
val SkyText = Color(0xFF156AA3)

// Gentle Reveal Surface (Soft Sage / Mint)
val SoftMintReveal = Color(0xFFEBF5F0)
val SoftMintBorder = Color(0xFF8DC4AA)

// Badge & Neutral Tokens (Light)
val BadgeBgLight = Color(0xFFE4E3D7)
val BadgeBorderLight = Color(0xFFDEDDCF)
val BadgeTextLight = Color(0xFF4C5C5E)

// Soft Light-Red Unmatched / Incorrect Tokens (Calm, non-neon light red)
val LightRedSoftBg = Color(0xFFFDECEC)
val LightRedSoftBorder = Color(0xFFC95B5B)
val LightRedSoftText = Color(0xFFA33838)

// Refined Dark Editorial Palette: Soft Charcoal-Slate & Elevated Slate Cards
val CharcoalSlateCanvas = Color(0xFF171A1E)
val SlateCardElevated = Color(0xFF222830)
val SlateCardBorder = Color(0xFF353C47)
val SlateBorderSubtle = Color(0xFF2B313C)

// Ink Tokens (Dark Mode: Crisp Off-White & Soft Silver)
val DarkInkPrimary = Color(0xFFF3F4F6)
val DarkInkSecondary = Color(0xFF9CA5B4)
val DarkInkMuted = Color(0xFF747D8C)

// Accents (Dark Mode)
val DarkSkyAction = Color(0xFF3DA5F5)
val DarkSkyTint = Color(0xFF183348)
val DarkSkyBorder = Color(0xFF3DA5F5)
val DarkSkyText = Color(0xFF80C6FA)

// Gentle Reveal Surface (Dark Mode)
val DarkSageReveal = Color(0xFF1C2E2A)
val DarkSageBorder = Color(0xFF457262)

// Badge & Neutral Tokens (Dark Mode)
val DarkBadgeBg = Color(0xFF2A303A)
val DarkBadgeBorder = Color(0xFF3B4350)
val DarkBadgeText = Color(0xFF9CA5B4)

// Soft Rose / Light-Red Unmatched Tokens (Dark Mode)
val DarkRoseSoftBg = Color(0xFF3A2224)
val DarkRoseSoftBorder = Color(0xFFE07A7A)
val DarkRoseSoftText = Color(0xFFF5A3A3)

// Backward-compatibility aliases for existing references
val WarmPaper = WarmCanvas
val BorderCrisp = WarmBorder
val BorderSubtle = WarmBorderSubtle
val InkPrimary = CrispDarkInk
val InkSecondary = SlateInkSecondary
val InkMuted = SlateInkMuted
val DeepPine = SkyBlue
val PineSoftTint = SkyTint
val PineBorder = SkyBorder
val SageStoneReveal = SoftMintReveal
val SageStoneBorder = SoftMintBorder
val BadgeBg = BadgeBgLight
val BadgeBorder = BadgeBorderLight
val BadgeText = BadgeTextLight
val MidnightNavy = WarmCanvas
val WarmCreamCard = CardWhite
val GoldenAmberReveal = SoftMintReveal
val CardTextDark = CrispDarkInk
val TextLightPrimary = CrispDarkInk
val TextLightSecondary = SlateInkSecondary
val TextLightTertiary = SlateInkMuted
val DarkPineAction = DarkSkyAction
val DarkPineTint = DarkSkyTint
val DarkPineBorder = DarkSkyBorder
val DarkPineText = DarkSkyText

/**
 * Custom color palette for CogniApp's accessible, warm quiz-style workbook design.
 * Supports both calm high-contrast Light mode and soothing Dark mode.
 */
@Immutable
data class CogniColors(
    val isDark: Boolean = false,
    val background: Color = WarmCanvas,
    val surface: Color = CardWhite,
    val cardBorder: Color = WarmBorder,
    val clueCardSurface: Color = CardWhite,
    val revealCardSurface: Color = SoftMintReveal,
    val revealCardBorder: Color = SoftMintBorder,
    val primaryInk: Color = CrispDarkInk,
    val secondaryInk: Color = SlateInkSecondary,
    val mutedInk: Color = SlateInkMuted,
    val primaryAction: Color = SkyBlue,
    val primaryActionText: Color = Color.White,
    val optionButtonDefaultBg: Color = CardWhite,
    val optionButtonDefaultBorder: Color = WarmBorder,
    val optionButtonDefaultText: Color = CrispDarkInk,
    val selectedOptionBg: Color = SkyTint,
    val selectedOptionBorder: Color = SkyBorder,
    val selectedOptionText: Color = SkyText,
    val badgeBg: Color = BadgeBgLight,
    val badgeBorder: Color = BadgeBorderLight,
    val badgeText: Color = BadgeTextLight,
    val unmatchedBg: Color = LightRedSoftBg,
    val unmatchedBorder: Color = LightRedSoftBorder,
    val unmatchedText: Color = LightRedSoftText,
    val accentSky: Color = SkyBlue,
    val accentTeal: Color = DeepTeal,
    val accentAmber: Color = WarmAmber,
    val accentCoral: Color = CoralOrange
)

fun lightCogniColors() = CogniColors(
    isDark = false,
    background = WarmCanvas,
    surface = CardWhite,
    cardBorder = WarmBorder,
    clueCardSurface = CardWhite,
    revealCardSurface = SoftMintReveal,
    revealCardBorder = SoftMintBorder,
    primaryInk = CrispDarkInk,
    secondaryInk = SlateInkSecondary,
    mutedInk = SlateInkMuted,
    primaryAction = SkyBlue,
    primaryActionText = Color.White,
    optionButtonDefaultBg = CardWhite,
    optionButtonDefaultBorder = WarmBorder,
    optionButtonDefaultText = CrispDarkInk,
    selectedOptionBg = SkyTint,
    selectedOptionBorder = SkyBorder,
    selectedOptionText = SkyText,
    badgeBg = BadgeBgLight,
    badgeBorder = BadgeBorderLight,
    badgeText = BadgeTextLight,
    unmatchedBg = LightRedSoftBg,
    unmatchedBorder = LightRedSoftBorder,
    unmatchedText = LightRedSoftText,
    accentSky = SkyBlue,
    accentTeal = DeepTeal,
    accentAmber = WarmAmber,
    accentCoral = CoralOrange
)

fun darkCogniColors() = CogniColors(
    isDark = true,
    background = CharcoalSlateCanvas,
    surface = SlateCardElevated,
    cardBorder = SlateCardBorder,
    clueCardSurface = SlateCardElevated,
    revealCardSurface = DarkSageReveal,
    revealCardBorder = DarkSageBorder,
    primaryInk = DarkInkPrimary,
    secondaryInk = DarkInkSecondary,
    mutedInk = DarkInkMuted,
    primaryAction = DarkSkyAction,
    primaryActionText = Color.White,
    optionButtonDefaultBg = SlateCardElevated,
    optionButtonDefaultBorder = SlateCardBorder,
    optionButtonDefaultText = DarkInkPrimary,
    selectedOptionBg = DarkSkyTint,
    selectedOptionBorder = DarkSkyBorder,
    selectedOptionText = DarkSkyText,
    badgeBg = DarkBadgeBg,
    badgeBorder = DarkBadgeBorder,
    badgeText = DarkBadgeText,
    unmatchedBg = DarkRoseSoftBg,
    unmatchedBorder = DarkRoseSoftBorder,
    unmatchedText = DarkRoseSoftText,
    accentSky = DarkSkyAction,
    accentTeal = DeepTeal,
    accentAmber = WarmAmber,
    accentCoral = CoralOrange
)

val LocalCogniColors = staticCompositionLocalOf { lightCogniColors() }
