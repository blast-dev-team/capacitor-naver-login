# capacitor-naver-login

Developed by Blast Team

Naver Login feature for Ionic / Capacitor apps, allowing you to easily integrate Naver login into your Ionic
applications.

## Installation

To install the plugin, run the following command in your project:

```bash
npm install capacitor-naver-login
npx cap sync
```

## Setting
Before you use this plugin, you need to set your credentials

capacitor.config.ts
```typescript
{
    plugins: {
        CapacitorNaverLogin: {
            'urlScheme': 'YOUR_SCHEME',
            'consumerKey': 'YOUR_CONSUMER_KEY',
            'consumerSecret': 'YOUR_CONSUMER_SECRET',
            'appName': 'YOUR_APP_NAME'   
        }
    }
}
```

## Usage

Ensure that you have set up your Naver Developer application and obtained the consumerKey, consumerSecret, and appName
values required for the plugin.

### Example

```typescript
import { CapacitorNaverLogin } from 'capacitor-naver-login';

// Initialize the Naver Login SDK
await CapacitorNaverLogin.initialize();

// Perform the login process
const result = await CapacitorNaverLogin.login();
if (result.isSuccess) {
  console.log('Access Token:', result.successResponse.accessToken);
  console.log('Refresh Token:', result.successResponse.refreshToken);
  console.log('Expires At:', result.successResponse.expiresAtUnixSecondString);
  console.log('Token Type:', result.successResponse.tokenType);
} else {
  console.error('Login failed:', result.failureResponse);
}
```
