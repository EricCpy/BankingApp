import { environment } from './src/environments/environment.development';
import { defineConfig } from "cypress";

export default defineConfig({
  video: false,
  screenshotOnRunFailure: false,
  env: {
    serverUrl: environment.backendUrl
  },
  e2e: {
    'baseUrl': "http://localhost:4200"
  },
});
