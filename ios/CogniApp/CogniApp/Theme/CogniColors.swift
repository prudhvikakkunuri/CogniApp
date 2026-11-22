import SwiftUI

// MARK: - Color Hex Extension
extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3: // RGB (12-bit)
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: // RGB (24-bit)
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8: // ARGB (32-bit)
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (255, 0, 0, 0)
        }

        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue: Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}

// MARK: - Design System Color Tokens
public struct CogniColors {
    // Light Canvas & Surface Tokens
    public static let warmCanvas = Color(hex: "EFEEE3")
    public static let cardWhite = Color(hex: "FFFFFF")
    public static let warmBorder = Color(hex: "DEDDCF")
    public static let warmBorderSubtle = Color(hex: "E8E7DC")

    // Ink Tokens (High Contrast)
    public static let crispDarkInk = Color(hex: "070706")
    public static let slateInkSecondary = Color(hex: "4C5C5E")
    public static let slateInkMuted = Color(hex: "A3A39D")

    // Thoughtful Quiz Accents
    public static let warmAmber = Color(hex: "FAAF14")
    public static let skyBlue = Color(hex: "2F98E8")
    public static let skyTint = Color(hex: "E6F3FC")
    public static let skyBorder = Color(hex: "2F98E8")
    public static let skyText = Color(hex: "156AA3")
    public static let deepTeal = Color(hex: "41798C")
    public static let coralOrange = Color(hex: "E84A26")

    // Gentle Reveal Surface
    public static let softMintReveal = Color(hex: "EBF5F0")
    public static let softMintBorder = Color(hex: "8DC4AA")

    // Badges Light
    public static let badgeBgLight = Color(hex: "E4E3D7")
    public static let badgeBorderLight = Color(hex: "DEDDCF")
    public static let badgeTextLight = Color(hex: "4C5C5E")

    // Soft Light-Red Unmatched Feedback (Light Mode)
    public static let lightRedSoftBg = Color(hex: "FDECEC")
    public static let lightRedSoftBorder = Color(hex: "C95B5B")
    public static let lightRedSoftText = Color(hex: "A33838")

    // Dark Mode Palette
    public static let charcoalSlateCanvas = Color(hex: "171A1E")
    public static let slateCardElevated = Color(hex: "222830")
    public static let slateCardBorder = Color(hex: "353C47")

    public static let darkInkPrimary = Color(hex: "F3F4F6")
    public static let darkInkSecondary = Color(hex: "9CA5B4")
    public static let darkInkMuted = Color(hex: "747D8C")

    public static let darkSkyAction = Color(hex: "3DA5F5")
    public static let darkSkyTint = Color(hex: "183348")
    public static let darkSkyBorder = Color(hex: "3DA5F5")
    public static let darkSkyText = Color(hex: "80C6FA")

    public static let darkSageReveal = Color(hex: "1C2E2A")
    public static let darkSageBorder = Color(hex: "457262")

    public static let darkBadgeBg = Color(hex: "2A303A")
    public static let darkBadgeBorder = Color(hex: "3B4350")
    public static let darkBadgeText = Color(hex: "9CA5B4")

    public static let darkRoseSoftBg = Color(hex: "3A2224")
    public static let darkRoseSoftBorder = Color(hex: "E07A7A")
    public static let darkRoseSoftText = Color(hex: "F5A3A3")
}

// MARK: - Dynamic Theme Palette Resolver
public struct CogniThemePalette {
    public let isDark: Bool

    public var background: Color {
        isDark ? CogniColors.charcoalSlateCanvas : CogniColors.warmCanvas
    }

    public var surface: Color {
        isDark ? CogniColors.slateCardElevated : CogniColors.cardWhite
    }

    public var cardBorder: Color {
        isDark ? CogniColors.slateCardBorder : CogniColors.warmBorder
    }

    public var clueCardSurface: Color {
        isDark ? CogniColors.slateCardElevated : CogniColors.cardWhite
    }

    public var revealCardSurface: Color {
        isDark ? CogniColors.darkSageReveal : CogniColors.softMintReveal
    }

    public var revealCardBorder: Color {
        isDark ? CogniColors.darkSageBorder : CogniColors.softMintBorder
    }

    public var primaryInk: Color {
        isDark ? CogniColors.darkInkPrimary : CogniColors.crispDarkInk
    }

    public var secondaryInk: Color {
        isDark ? CogniColors.darkInkSecondary : CogniColors.slateInkSecondary
    }

    public var mutedInk: Color {
        isDark ? CogniColors.darkInkMuted : CogniColors.slateInkMuted
    }

    public var primaryAction: Color {
        isDark ? CogniColors.darkSkyAction : CogniColors.skyBlue
    }

    public var primaryActionText: Color {
        Color.white
    }

    public var selectedOptionBg: Color {
        isDark ? CogniColors.darkSkyTint : CogniColors.skyTint
    }

    public var selectedOptionBorder: Color {
        isDark ? CogniColors.darkSkyBorder : CogniColors.skyBorder
    }

    public var selectedOptionText: Color {
        isDark ? CogniColors.darkSkyText : CogniColors.skyText
    }

    public var badgeBg: Color {
        isDark ? CogniColors.darkBadgeBg : CogniColors.badgeBgLight
    }

    public var badgeBorder: Color {
        isDark ? CogniColors.darkBadgeBorder : CogniColors.badgeBorderLight
    }

    public var badgeText: Color {
        isDark ? CogniColors.darkBadgeText : CogniColors.badgeTextLight
    }

    public var unmatchedBg: Color {
        isDark ? CogniColors.darkRoseSoftBg : CogniColors.lightRedSoftBg
    }

    public var unmatchedBorder: Color {
        isDark ? CogniColors.darkRoseSoftBorder : CogniColors.lightRedSoftBorder
    }

    public var unmatchedText: Color {
        isDark ? CogniColors.darkRoseSoftText : CogniColors.lightRedSoftText
    }
}
