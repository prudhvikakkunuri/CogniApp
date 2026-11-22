import SwiftUI

/// Calm, respectful splash screen introducing the CogniApp brand.
/// Transitions smoothly with a gentle fade to the Home Screen.
public struct SplashView: View {
    @Binding public var isSplashActive: Bool
    @State private var opacity: Double = 0.0
    @State private var scale: CGFloat = 0.92

    public var body: some View {
        ZStack {
            CogniColors.warmCanvas
                .ignoresSafeArea()

            VStack(spacing: 16) {
                ZStack {
                    RoundedRectangle(cornerRadius: 24, style: .continuous)
                        .fill(CogniColors.cardWhite)
                        .frame(width: 96, height: 96)
                        .overlay(
                            RoundedRectangle(cornerRadius: 24, style: .continuous)
                                .stroke(CogniColors.warmBorder, lineWidth: 1.5)
                        )
                        .shadow(color: Color.black.opacity(0.06), radius: 10, x: 0, y: 4)

                    Text("📖")
                        .font(.system(size: 46))
                }

                Text("CogniApp")
                    .font(CogniTypography.displayLarge(size: 32))
                    .foregroundColor(CogniColors.crispDarkInk)

                Text("Calm practice for everyday memory and focus")
                    .font(CogniTypography.body(size: 16))
                    .foregroundColor(CogniColors.slateInkSecondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
            }
            .opacity(opacity)
            .scaleEffect(scale)
        }
        .onAppear {
            withAnimation(.easeOut(duration: 0.8)) {
                opacity = 1.0
                scale = 1.0
            }

            DispatchQueue.main.asyncAfter(deadline: .now() + 1.6) {
                withAnimation(.easeInOut(duration: 0.5)) {
                    isSplashActive = false
                }
            }
        }
    }
}
