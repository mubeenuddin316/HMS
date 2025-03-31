/**
 * 
 */
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


document.addEventListener("DOMContentLoaded", function() {
    const ctx = document.getElementById('genderDistributionChart').getContext('2d');

    fetch('/HMS/api/superAdmin/genderDistribution')
        .then(response => response.json())
        .then(data => {
            // Extract labels and counts from the returned DTO list
            const labels = data.map(item => item.gender);
            const totalCounts = data.map(item => item.totalCount);
            // hospitalDetails is assumed to be an array of arrays
            // where each inner array contains objects with hospitalName and count
            const hospitalDetails = data.map(item => item.hospitalCounts);

            new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: labels,
                    datasets: [{
                        data: totalCounts,
                        backgroundColor: [
                            'rgba(54, 162, 235, 0.7)',  // Color for Male
                            'rgba(255, 99, 132, 0.7)',  // Color for Female
                            'rgba(255, 206, 86, 0.7)'   // Color for Other
                        ]
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        tooltip: {
                            callbacks: {
                                // Return an array of strings so each line appears separately
                                label: function(context) {
                                    const gender = context.label;
                                    const total = context.parsed;
                                    const hospitalCounts = hospitalDetails[context.dataIndex];
                                    let lines = [];
                                    lines.push(`${gender}: ${total}`);
                                    if (hospitalCounts && hospitalCounts.length > 0) {
                                        hospitalCounts.forEach(hc => {
                                            lines.push(`${hc.hospitalName}: ${hc.count}`);
                                        });
                                    }
                                    return lines;
                                }
                            }
                        },
                        title: {
                            display: true,
                            text: 'Gender Distribution Across Hospitals'
                        }
                    }
                }
            });
        })
        .catch(error => console.error('Error fetching gender distribution data:', error));
});

document.addEventListener("DOMContentLoaded", function() {
    const ctx = document.getElementById('hospitalBedsChart').getContext('2d');

    fetch('/HMS/api/superAdmin/hospitalBeds')
        .then(response => response.json())
        .then(data => {
            // Extract labels and counts for each hospital
            const labels = data.map(item => item.hospitalName);
            const occupiedCounts = data.map(item => item.occupiedBeds);
            const availableCounts = data.map(item => item.availableBeds);

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Occupied Beds',
                            data: occupiedCounts,
                            backgroundColor: 'rgba(255, 99, 132, 0.7)' // red-ish
                        },
                        {
                            label: 'Available Beds',
                            data: availableCounts,
                            backgroundColor: 'rgba(75, 192, 192, 0.7)' // teal/greenish
                        }
                    ]
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            stacked: true
                        },
                        y: {
                            beginAtZero: true,
                            stacked: true
                        }
                    }
                }
            });
        })
        .catch(error => console.error('Error fetching hospital beds data:', error));
});

// Add chart instance reference
let doctorPerformanceChart = null;

document.addEventListener("DOMContentLoaded", function() {
    const hospitalFilter = document.getElementById('hospitalFilter');
    hospitalFilter.addEventListener('change', fetchDoctorPerformance);

    // Add resize observer setup here
    const resizeObserver = new ResizeObserver(entries => {
        if (doctorPerformanceChart) {
            doctorPerformanceChart.resize();
        }
    });
    
    // Observe the chart container
    const chartContainer = document.querySelector('.chart-container');
    if (chartContainer) {
        resizeObserver.observe(chartContainer);
    }

    // Initial load
    fetchDoctorPerformance();
});

function fetchDoctorPerformance() {
    const hospitalId = document.getElementById('hospitalFilter').value;
    
    // Build URL with proper parameter handling
    const url = new URL('/HMS/api/superAdmin/doctorPerformance', window.location.origin);
    if (hospitalId) url.searchParams.set('hospitalId', hospitalId);

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.doctorName);
            const counts = data.map(item => item.performanceCount);
            renderDoctorPerformanceChart(labels, counts);
        })
        .catch(error => console.error('Error:', error));
}

function renderDoctorPerformanceChart(labels, data) {
    const ctx = document.getElementById('doctorPerformanceChart').getContext('2d');
    
    // Destroy previous chart if exists
    if (doctorPerformanceChart) {
        doctorPerformanceChart.destroy();
    }
    
    doctorPerformanceChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'No of Patients Attended',
                data: data,
                backgroundColor: 'rgba(54, 162, 235, 0.7)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false, // Crucial for fixed height
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1,
                        precision: 0
                    }
                }
            },
            plugins: {
                legend: {
                    position: 'top',
                }
            }
        }
    });
}