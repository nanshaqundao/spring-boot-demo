import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend } from 'k6/metrics';

// Define custom metric
const reactiveTrend = new Trend('reactive_duration');

// Options
export let options = {
  stages: [
    { duration: '2m', target: 300 },   // ramp-up to 300 users
    { duration: '2m', target: 500 },   // hold at 500 users
    { duration: '1m', target: 0 },    // ramp-down
  ],
};

export default function () {
  let reactiveResponse = http.get('http://host.docker.internal:8082/api/r2dbc/business-info/name/Global%20Trade');
  check(reactiveResponse, {
    'reactive status is 200': (r) => r.status === 200,
  });
  reactiveTrend.add(reactiveResponse.timings.duration);

  // Pause to simulate real-world usage patterns
  sleep(1);
}
