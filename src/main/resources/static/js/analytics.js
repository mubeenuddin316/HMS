/**
 * 
 */

document.addEventListener("DOMContentLoaded", function() {
  // Example: Initialize Chart 1 (Bar Chart)
  const ctx1 = document.getElementById('chart1').getContext('2d');
  new Chart(ctx1, {
      type: 'bar',
      data: {
          labels: ['Hospital A', 'Hospital B', 'Hospital C'],
          datasets: [{
              label: 'Appointments',
              data: [12, 19, 7],
              backgroundColor: 'rgba(54, 162, 235, 0.7)'
          },
          {
              label: 'OPD Queues',
              data: [5, 10, 3],
              backgroundColor: 'rgba(255, 159, 64, 0.7)'
          }]
      },
      options: {
          responsive: true,
          scales: {
              y: {
                  beginAtZero: true
              }
          }
      }
  });
  
  // Example: Initialize Chart 2 (Line Chart)
  const ctx2 = document.getElementById('chart2').getContext('2d');
  new Chart(ctx2, {
      type: 'line',
      data: {
          labels: ['Jan', 'Feb', 'Mar', 'Apr'],
          datasets: [{
              label: 'Monthly Trend',
              data: [5, 15, 10, 20],
              borderColor: 'rgba(255, 159, 64, 1)',
              backgroundColor: 'rgba(255, 159, 64, 0.5)',
              fill: false,
              tension: 0.1
          }]
      },
      options: {
          responsive: true,
          scales: {
              y: {
                  beginAtZero: true
              }
          }
      }
  });
});

document.addEventListener("DOMContentLoaded", function() {
  const ctx = document.getElementById('apptVsQueueLineChart').getContext('2d');

  // 1) Fetch the monthly (or daily) data
  fetch('/HMS/api/superAdmin/timeSeries/appointmentsVsQueue')
    .then(response => response.json())
    .then(data => {
      // data is an array of { periodLabel, appointmentCount, opdQueueCount }
      // Build arrays for the chart
      const labels = data.map(item => item.periodLabel); 
      const apptData = data.map(item => item.appointmentCount);
      const queueData = data.map(item => item.opdQueueCount);

      // 2) Create the multi-line chart
      new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [
            {
              label: 'Appointments',
              data: apptData,
              borderColor: 'rgba(54, 162, 235, 1)', // e.g. blue line
              backgroundColor: 'rgba(54, 162, 235, 0.2)', 
              fill: true,
              tension: 0.1
            },
            {
              label: 'OPD Queues',
              data: queueData,
              borderColor: 'rgba(255, 159, 64, 1)', // e.g. orange line
              backgroundColor: 'rgba(255, 159, 64, 0.2)',
              fill: true,
              tension: 0.1
            }
          ]
        },
        options: {
          responsive: true,
          scales: {
            y: { beginAtZero: true }
          }
        }
      });
    })
    .catch(error => console.error('Error fetching time-series data:', error));
});

