import { registerPlugin } from '@capacitor/core';

import type { CapacitorNaverLoginPlugin, NaverLoginResponse } from './definitions';

const CapacitorNaverLogin = registerPlugin<CapacitorNaverLoginPlugin>(
  'CapacitorNaverLogin',
  {
    web: () => import('./web').then(m => new m.CapacitorNaverLoginWeb()),
  },
);

export * from './definitions';
export { CapacitorNaverLogin, NaverLoginResponse };
