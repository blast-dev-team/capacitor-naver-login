// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorNaverLogin",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "CapacitorNaverLogin",
            targets: ["CapacitorNaverLoginPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main"),
        .package(url: "https://github.com/naver/naveridlogin-sdk-ios", from: "4.1.3")
    ],
    targets: [
        .target(
            name: "CapacitorNaverLoginPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/CapacitorNaverLoginPlugin"),
        .testTarget(
            name: "CapacitorNaverLoginPluginTests",
            dependencies: ["CapacitorNaverLoginPlugin"],
            path: "ios/Tests/CapacitorNaverLoginPluginTests")
    ]
)
