/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import BackgroundTask from './src/HeadlessJS/BackgroundTask';

AppRegistry.registerHeadlessTask('BackgroundTask', () => BackgroundTask);
AppRegistry.registerComponent(appName, () => App);
