import {NativeModules} from 'react-native';

async function BackgroundTask({inPath, outPath}) {
  const {VideoCompressor} = NativeModules;

  console.log('Starting');
  VideoCompressor.compress(inPath, outPath, 1).then(({outPath}) =>
    console.log(outPath),
  );
  // VideoCompressor.compress(
  //   `/sdcard/Download/SampleVideo_1280x720_30mb.mp4`,
  //   `storage/emulated/0/Download/VID-${new Date().getTime()}.mp4`,
  //   1,
  // ).then(({outPath}) => console.log(outPath));
}

export default BackgroundTask;
