import SwiftUI

public struct CogniTypography {
    public static func displayLarge(size: CGFloat = 28) -> Font {
        .system(size: size, weight: .bold, design: .rounded)
    }

    public static func headline(size: CGFloat = 22) -> Font {
        .system(size: size, weight: .bold, design: .rounded)
    }

    public static func title(size: CGFloat = 18) -> Font {
        .system(size: size, weight: .semibold, design: .rounded)
    }

    public static func body(size: CGFloat = 16) -> Font {
        .system(size: size, weight: .regular, design: .default)
    }

    public static func bodyBold(size: CGFloat = 16) -> Font {
        .system(size: size, weight: .semibold, design: .default)
    }

    public static func caption(size: CGFloat = 13) -> Font {
        .system(size: size, weight: .medium, design: .default)
    }

    public static func button(size: CGFloat = 18) -> Font {
        .system(size: size, weight: .bold, design: .rounded)
    }
}
