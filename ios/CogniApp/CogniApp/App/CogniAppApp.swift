import SwiftUI

@main
struct CogniAppApp: App {
    @StateObject private var viewModel = PracticeViewModel()
    @State private var isSplashActive: Bool = true

    var body: some Scene {
        WindowGroup {
            ZStack {
                if isSplashActive {
                    SplashView(isSplashActive: $isSplashActive)
                } else if !viewModel.isSessionStarted {
                    HomeScreen(viewModel: viewModel)
                        .transition(.opacity)
                } else {
                    GameScreen(viewModel: viewModel)
                        .transition(.opacity)
                }
            }
            .animation(.easeInOut(duration: 0.35), value: viewModel.isSessionStarted)
            .animation(.easeInOut(duration: 0.35), value: isSplashActive)
            .preferredColorScheme(viewModel.isDarkMode ? .dark : .light)
        }
    }
}
