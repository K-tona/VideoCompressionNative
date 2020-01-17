import queueFactory from 'react-native-queue';
import {useState, useEffect} from 'react';

const useQueueFactory = () => {
  const [queue, setQueue] = useState();

  useEffect(() => {
    init();
  }, []);

  async function init() {
    const _queue = await queueFactory();

    _queue.addWorker('standard-example', async (id, payload) => {
      console.log('standard-example job ' + id + ' executed.');
    });

    let recursionCounter = 1;
    _queue.addWorker('recursive-example', async (id, payload) => {
      console.log('recursive-example job ' + id + ' started');
      console.log(recursionCounter, 'recursionCounter');

      recursionCounter++;

      await new Promise(resolve => {
        setTimeout(() => {
          console.log('recursive-example ' + id + ' has completed!');

          // Keep creating these jobs until counter reaches 3.
          if (recursionCounter <= 3) {
            _queue.createJob('recursive-example');
          }

          resolve();
        }, 1000);
      });
    });

    _queue.addWorker('start-job-chain', async (id, payload) => {
      console.log('start-job-chain job ' + id + ' started');
      console.log('step: ' + payload.step);

      await new Promise(resolve => {
        setTimeout(() => {
          console.log('start-job-chain ' + id + ' has completed!');

          // Create job for next step in chain
          _queue.createJob('job-chain-2nd-step', {
            callerJobName: 'start-job-chain',
            step: payload.step + 1,
          });

          resolve();
        }, 1000);
      });
    });

    _queue.addWorker('job-chain-2nd-step', async (id, payload) => {
      console.log('job-chain-2nd-step job ' + id + ' started');
      console.log('step: ' + payload.step);

      await new Promise(resolve => {
        setTimeout(() => {
          console.log('job-chain-2nd-step ' + id + ' has completed!');

          // Create job for last step in chain
          _queue.createJob('job-chain-final-step', {
            callerJobName: 'job-chain-2nd-step',
            step: payload.step + 1,
          });

          resolve();
        }, 1000);
      });
    });

    _queue.addWorker('job-chain-final-step', async (id, payload) => {
      console.log('job-chain-final-step job ' + id + ' started');
      console.log('step: ' + payload.step);

      await new Promise(resolve => {
        setTimeout(() => {
          console.log('job-chain-final-step ' + id + ' has completed!');
          console.log('Job chain is now completed!');

          resolve();
        }, 1000);
      });
    });
    _queue.start();

    setQueue(_queue);
  }
  return queue;
};

const useQueueBackgroundTask = () => {
  const [queue, setQueue] = useState();

  useEffect(() => {
    init();
  }, []);

  async function init() {
    const _queue = await queueFactory();

    // Add the worker.
    _queue.addWorker('background-example', async (id, payload) => {
      // Worker has to be defined before related jobs can be added to queue.
      // Since this example is only concerned with OS background task worker execution,
      // We will make this a dummy function in this context.
      console.log(`Working from background ${id}`);
    });

    // Attach initialized queue to state.
    setQueue(_queue);
  }
  return queue;
};

export {useQueueFactory, useQueueBackgroundTask};
