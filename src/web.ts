import { WebPlugin } from '@capacitor/core';

import type {
  CapacitorNaverLoginPlugin,
  NaverLoginResponse,
} from './definitions';

export class CapacitorNaverLoginWeb
  extends WebPlugin
  implements CapacitorNaverLoginPlugin
{
  async initialize(): Promise<void> {
    throw new Error('Not Implemented Yet');
  }

  async logout() {
    throw new Error('Not Implemented Yet');
  }

  async login(): Promise<NaverLoginResponse> {
    // TODO: Need to implement
    return {
      accessToken: '',
      refreshToken: '',
      expiresAtUnixSecondString: '',
      tokenType: '',
    };
  }
}
