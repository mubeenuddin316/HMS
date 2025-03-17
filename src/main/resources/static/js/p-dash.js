/**
 * 
 */

document.addEventListener("DOMContentLoaded", function () {
    generateCalendar();
});

function generateCalendar() {
    const calendarBody = document.getElementById("calendar-body");
    const monthYear = document.getElementById("monthYear");

    const today = new Date();
    const currentMonth = today.getMonth();
    const currentYear = today.getFullYear();

    const monthNames = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    monthYear.textContent = `${monthNames[currentMonth]} ${currentYear}`;

    const firstDay = new Date(currentYear, currentMonth, 1).getDay();
    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();

    calendarBody.innerHTML = "";

    let date = 1;
    for (let i = 0; i < 6; i++) {
        let row = document.createElement("tr");

        for (let j = 0; j < 7; j++) {
            let cell = document.createElement("td");

            if (i === 0 && j < firstDay) {
                cell.textContent = "";
            } else if (date > daysInMonth) {
                break;
            } else {
                cell.textContent = date;
                if (date === today.getDate()) {
                    cell.classList.add("today");
                }
                date++;
            }
            row.appendChild(cell);
        }
        calendarBody.appendChild(row);
    }
}