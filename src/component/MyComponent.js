import React from 'react';
import {useQueueFactory} from '../utils/custom-hooks';
import {Button, View, NativeModules} from 'react-native';
import ImagePicker from 'react-native-image-crop-picker';

const MyComponent = props => {
  const queue = useQueueFactory();
  const {BackgroundTask, VideoCompressor} = NativeModules;

  return (
    <View>
      <Button
        title="Initiate Queue task"
        onPress={() => {
          queue.createJob('start-job-chain', {step: 1}, {atempt: 3}, true);
          console.log('button pressed');
        }}
      />
      {/* <Button
        title="Testing Bg Compress"
        onPress={() => {
          BackgroundTask.upload();
          console.log('on Pressed');
        }}
      /> */}
      <Button
        title="Testing Compress"
        onPress={() =>
          VideoCompressor.compress(
            `/sdcard/Download/SampleVideo_1280x720_30mb.mp4`,
            `storage/emulated/0/Download/VID-${new Date().getTime()}.mp4`,
            1,
          )
        }
      />
      <Button
        title="Compress Video"
        onPress={() =>
          ImagePicker.openPicker({
            includeExif: false,
            compressVideoPreset: 0.5,
            mediaType: 'video',
            multiple: true,
          }).then(videos => {
            videos.map(i => {
              switch (Platform.OS) {
                case 'ios':
                  ProcessingManager.compress(i.path).then(res =>
                    console.log(res),
                  );
                  break;
                case 'android':
                  temp = i.path.substr(7);

                  temp &&
                    BackgroundTask.compress(
                      temp,
                      `storage/emulated/0/Download/VID-${new Date().getTime()}.mp4`,
                    );
                  break;
                default:
                  break;
              }
            });
          })
        }
      />
    </View>
  );
};

export default MyComponent;
