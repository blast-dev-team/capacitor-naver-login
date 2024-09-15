export interface NaverLoginResponse {
    accessToken: string;
    refreshToken: string;
    expiresAtUnixSecondString: string;
    tokenType: string;
}

export interface CapacitorNaverLoginPlugin {
    initialize(): Promise<void>;
    login(): Promise<NaverLoginResponse>;
    logout(): Promise<void>;
}
