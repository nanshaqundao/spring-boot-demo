import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend } from 'k6/metrics';

// Define custom metric
const nonReactiveTrend = new Trend('non_reactive_duration');

// Options
export let options = {
  stages: [
    { duration: '2m', target: 300 },   // ramp-up to 300 users
    { duration: '2m', target: 500 },   // hold at 500 users
    { duration: '1m', target: 0 },    // ramp-down
  ],
};

export default function () {
  let nonReactiveResponse = http.get('http://host.docker.internal:8080/api/mvc/business-info/name/Global%20Trade');
  check(nonReactiveResponse, {
    'non-reactive status is 200': (r) => r.status === 200,
  });
  nonReactiveTrend.add(nonReactiveResponse.timings.duration);

  // Pause to simulate real-world usage patterns
  sleep(1);
}
