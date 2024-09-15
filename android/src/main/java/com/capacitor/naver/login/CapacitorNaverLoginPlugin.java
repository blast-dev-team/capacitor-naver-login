package com.capacitor.naver.login;

import android.util.Log;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginConfig;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthErrorCode;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.OAuthLoginCallback;

@CapacitorPlugin(name = "CapacitorNaverLogin")
public class CapacitorNaverLoginPlugin extends Plugin {

    @PluginMethod
    public void initialize(PluginCall call) {
        PluginConfig config = getConfig();
        String consumerKey = (String) config.getString("consumerKey");
        String consumerSecret = (String) config.getString("consumerSecret");
        String appName = (String) config.getString("appName");

        if (consumerKey == null || consumerSecret == null || appName == null) {
            call.reject("Missing configuration parameters: consumerKey, consumerSecret, and appName are required.");
            return;
        }

        getActivity().runOnUiThread(() -> {
            NaverIdLoginSDK.INSTANCE.initialize(
                    getContext(),
                    consumerKey,
                    consumerSecret,
                    appName
            );
            call.resolve();
        });
    }

    @PluginMethod
    public void login(final PluginCall call) {
        getActivity().runOnUiThread(() -> {
            try {
                NaverIdLoginSDK.INSTANCE.authenticate(getActivity(), new OAuthLoginCallback() {
                    @Override
                    public void onSuccess() {
                        JSObject result = createLoginSuccessResponse();
                        call.resolve(result);
                    }

                    @Override
                    public void onFailure(int httpStatus, String message) {
                        call.reject("Login failed: " + message);
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        call.reject("Login error: " + message);
                    }
                });
            } catch (Exception e) {
                call.reject("Login exception: " + e.getLocalizedMessage());
            }
        });
    }

    @PluginMethod
    public void logout(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            try {
                NaverIdLoginSDK.INSTANCE.logout();
                call.resolve();
            } catch (Exception e) {
                call.reject("Logout failed: " + e.getLocalizedMessage());
            }
        });
    }

    @PluginMethod
    public void deleteToken(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            NidOAuthLogin nidOAuthLogin = new NidOAuthLogin();
            nidOAuthLogin.callDeleteTokenApi(new OAuthLoginCallback() {
                @Override
                public void onSuccess() {
                    call.resolve();
                }

                @Override
                public void onFailure(int httpStatus, String message) {
                    call.reject("Failed to delete token: " + message);
                }

                @Override
                public void onError(int errorCode, String message) {
                    call.reject("Error deleting token: " + message);
                }
            });
        });
    }

    private JSObject createLoginSuccessResponse() {
        JSObject result = new JSObject();
        result.put("isSuccess", true);

        JSObject successResponse = new JSObject();
        successResponse.put("accessToken", NaverIdLoginSDK.INSTANCE.getAccessToken());
        successResponse.put("refreshToken", NaverIdLoginSDK.INSTANCE.getRefreshToken());
        successResponse.put("expiresAtUnixSecondString", NaverIdLoginSDK.INSTANCE.getExpiresAt());
        successResponse.put("tokenType", NaverIdLoginSDK.INSTANCE.getTokenType());

        result.put("successResponse", successResponse);
        return result;
    }

    private JSObject createLoginFailureResponse(String message) {
        JSObject result = new JSObject();
        result.put("isSuccess", false);

        JSObject failureResponse = new JSObject();
        String errorCode, errorDescription;

        try {
            errorCode = NaverIdLoginSDK.INSTANCE.getLastErrorCode().getCode();
            errorDescription = NaverIdLoginSDK.INSTANCE.getLastErrorDescription();
        } catch (Exception e) {
            errorCode = "unknown_error";
            errorDescription = "Failed to retrieve error details from Naver SDK";
        }

        boolean isCancel = errorCode.equals(NidOAuthErrorCode.CLIENT_USER_CANCEL.getCode());

        failureResponse.put("message", message != null ? message : "Unknown error occurred");
        failureResponse.put("lastErrorCodeFromNaverSDK", errorCode);
        failureResponse.put("lastErrorDescriptionFromNaverSDK", errorDescription);
        failureResponse.put("isCancel", isCancel);

        result.put("failureResponse", failureResponse);
        return result;
    }
}
