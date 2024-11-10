import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend } from 'k6/metrics';

// Define custom metric
const reactiveTrend = new Trend('reactive_duration');

// Options
export let options = {
  stages: [
    { duration: '30s', target: 10 },  // ramp-up to 10 users
    { duration: '1m', target: 20 },   // hold at 20 users
    { duration: '30s', target: 0 },   // ramp-down
  ],
};

export default function () {
  let reactiveResponse = http.get('http://host.docker.internal:8080/api/mvc/business-info/reactive-with-buffer/name/Edu%20World?bufferSize=5');
  check(reactiveResponse, {
    'reactive status is 200': (r) => r.status === 200,
  });
  reactiveTrend.add(reactiveResponse.timings.duration);

  // Pause to simulate real-world usage patterns
  sleep(1);
}
