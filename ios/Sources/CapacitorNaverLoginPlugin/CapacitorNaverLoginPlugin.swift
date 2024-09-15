import Foundation
import Capacitor
import NaverThirdPartyLogin


@objc(CapacitorNaverLoginPlugin)
public class CapacitorNaverLoginPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "CapacitorNaverLoginPlugin"
    public let jsName = "CapacitorNaverLogin"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "initialize", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "login", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "logout", returnType: CAPPluginReturnPromise)
    ]
    private let loginInstance = NaverThirdPartyLoginConnection.getSharedInstance()
    private var loginCall: CAPPluginCall?

    @objc func initialize(_ call: CAPPluginCall) {
        loginInstance?.isInAppOauthEnable = true // Example configuration
        loginInstance?.isNaverAppOauthEnable = true
        loginInstance?.isOnlyPortraitSupportedInIphone()

        if let serviceUrlScheme = getConfig().getString("urlScheme") as? String {
            loginInstance?.serviceUrlScheme = serviceUrlScheme
        } else {
            call.reject("Failed to fetch urlScheme")
            return
        }

        if let consumerKey = getConfig().getString("consumerKey") as? String {
            loginInstance?.consumerKey = consumerKey
        } else {
            call.reject("Failed to fetch consumerKey")
            return
        }

        if let consumerSecret = getConfig().getString("consumerSecret") as? String {
            loginInstance?.consumerSecret = consumerSecret
        } else {
            call.reject("Failed to fetch consumerSecret")
            return
        }

        if let oAuthClientName = getConfig().getString("appName") as? String {
            loginInstance?.appName = oAuthClientName
        } else {
            call.reject("Failed to fetch appName")
            return
        }

        // Resolve the call if initialization was successful
        call.resolve([
            "message": "Naver login initialized successfully"
        ])
    }

    override public func load() {}

    @objc func login(_ call: CAPPluginCall) {
        loginCall = call
        loginInstance?.delegate = self
        loginInstance?.resetToken()
        DispatchQueue.main.async {
          self.loginInstance?.requestThirdPartyLogin()
        }
    }

    @objc func logout(_ call: CAPPluginCall) {
        loginInstance?.requestDeleteToken()
    }
}

// Make sure to conform to the delegate to handle login callbacks
extension CapacitorNaverLoginPlugin: NaverThirdPartyLoginConnectionDelegate {
    public func oauth20ConnectionDidFinishRequestACTokenWithAuthCode() {
        print("Finished request act token with auth code")
        let userData: [String: Any] = [
                    "accessToken": loginInstance?.accessToken,
                    "refreshToken": loginInstance?.refreshToken,
                    "tokenType": loginInstance?.tokenType
                ];
        loginCall!.resolve(userData);
        // Handle successful login, e.g., resolve a Capacitor promise
    }

    public func oauth20ConnectionDidFinishRequestACTokenWithRefreshToken() {
        print("Finished request act token with refresh token")
        // Token refresh logic here
    }

    public func oauth20ConnectionDidFinishDeleteToken() {
        print("Successfully logged out")
        // Handle logout
    }

    public func oauth20Connection(_ oauthConnection: NaverThirdPartyLoginConnection!, didFailWithError error: Error!) {
        print("Error: \(error.localizedDescription)")
        loginCall?.reject("Login failed", error.localizedDescription, error)
    }
}
