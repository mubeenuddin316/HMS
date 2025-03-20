document.addEventListener("DOMContentLoaded", function () {
    generateCalendar();
	
	
	// set up event listeners for the new buttons
	const pastBtn = document.getElementById("pastVisitsBtn");
	const upcomingBtn = document.getElementById("upcomingVisitsBtn");

	pastBtn.addEventListener("click", () => {
	    // Show the Past Visits section, hide the Upcoming
	    document.getElementById("pastVisitsSection").style.display = "block";
	    document.getElementById("upcomingSection").style.display = "none";
	});

	upcomingBtn.addEventListener("click", () => {
	    // Hide the Past Visits section, show the Upcoming
	    document.getElementById("pastVisitsSection").style.display = "none";
	    document.getElementById("upcomingSection").style.display = "block";

	    // Now fetch the upcoming visits from the server
	    fetchUpcomingVisits();
	});
	
	
});

function generateCalendar() {
    const calendarBody = document.getElementById("calendar-body");
    const monthYear = document.getElementById("monthYear");

    // 1) We get today's date
    const today = new Date();
    const currentMonth = today.getMonth();   // 0-based (0=Jan,1=Feb,...)
    const currentYear = today.getFullYear();

    // Month names array
    const monthNames = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    // Display "March 2025" or whichever
    monthYear.textContent = `${monthNames[currentMonth]} ${currentYear}`;

    // 2) Calculate first day, days in month
    const firstDay = new Date(currentYear, currentMonth, 1).getDay();
    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();

    // Clear old calendar if any
    calendarBody.innerHTML = "";

    let date = 1;
    for (let i = 0; i < 6; i++) { // up to 6 weeks
        let row = document.createElement("tr");

        for (let j = 0; j < 7; j++) {
            let cell = document.createElement("td");

            if (i === 0 && j < firstDay) {
                cell.textContent = "";
            } else if (date > daysInMonth) {
                break;
            } else {
                cell.textContent = date;

                // highlight "today"
                if (
                    date === today.getDate() &&
                    currentMonth === today.getMonth() &&
                    currentYear === today.getFullYear()
                ) {
                    cell.classList.add("today");
                }

                date++;
            }
            row.appendChild(cell);
        }
        calendarBody.appendChild(row);
    }

    // 3) Now fetch the upcoming appointment dates for this month/year
    //    We pass month+1 because JS months are 0-based, while our server expects 1-based
    fetch(`/HMS/api/patient/upcomingDates?year=${currentYear}&month=${currentMonth+1}`)
        .then(response => response.json())
        .then(dateArray => {
            // dateArray is something like ["2025-03-01","2025-03-12","2025-03-27"]
            highlightUpcomingDates(dateArray, currentYear, currentMonth);
        })
        .catch(error => console.error("Error fetching upcomingDates:", error));
}

function highlightUpcomingDates(dateArray, year, monthIndex) {
    // dateArray e.g. ["2025-03-01","2025-03-12"]
    // We have "year" and "monthIndex" for the displayed calendar
    // We'll loop over each date string, parse it, and if matches the displayed month/year, highlight

    // select all cells
    const cells = document.querySelectorAll("#calendar-body td");

    dateArray.forEach(dateStr => {
        // parse "yyyy-MM-dd"
        const [yyyy, mm, dd] = dateStr.split("-");
        const apptYear = parseInt(yyyy, 10);
        const apptMonth = parseInt(mm, 10);  // 1-based
        const apptDay = parseInt(dd, 10);

        // check if this date belongs to the current displayed month/year
        if (apptYear === year && apptMonth === (monthIndex + 1)) {
            // find the cell that has textContent = dd
            cells.forEach(cell => {
                if (cell.textContent === String(apptDay)) {
                    // add a highlight class
                    cell.classList.add("upcoming-day");
                }
            });
        }
    });
}


function fetchUpcomingVisits() {
    fetch("/HMS/api/patient/upcomingVisits") // adjust if no /HMS context path
        .then(response => response.json())
        .then(data => {
            // data is a list of Appointment objects
            populateUpcomingTable(data);
        })
        .catch(error => console.error("Error fetching upcoming visits:", error));
}

function populateUpcomingTable(appointments) {
    const tbody = document.getElementById("upcomingTableBody");
    tbody.innerHTML = ""; // clear any existing rows

    appointments.forEach(app => {
        const tr = document.createElement("tr");

        // date
        const tdDate = document.createElement("td");
        const dateObj = new Date(app.appointmentDatetime); 
        // e.g. "2025-03-22T09:00:00"
        const y  = dateObj.getFullYear();
        const m  = String(dateObj.getMonth() + 1).padStart(2, '0');
        const d  = String(dateObj.getDate()).padStart(2, '0');
        tdDate.textContent = `${y}-${m}-${d}`;
        tr.appendChild(tdDate);

        // doctor
        const tdDoctor = document.createElement("td");
        tdDoctor.textContent = app.doctor ? app.doctor.name : "N/A";
        tr.appendChild(tdDoctor);

        // notes
        const tdNotes = document.createElement("td");
        tdNotes.textContent = app.notes || "";
        tr.appendChild(tdNotes);

        // status
        const tdStatus = document.createElement("td");
        tdStatus.textContent = app.status;
        tr.appendChild(tdStatus);

        tbody.appendChild(tr);
    });
}
