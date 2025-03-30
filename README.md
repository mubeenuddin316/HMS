# Hospital Management System

A robust Hospital Management System built using Java, Spring Boot, Thymeleaf, and MySQL. This project supports multiple user roles including Super Admin, Hospital Admin, Doctor, and Patient. It provides functionalities such as appointments management, OPD queue handling, bed management, analytics, and more.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- **Multi-Role Authentication:**  
  Separate login and dashboard for Super Admin, Hospital Admin, Doctor, and Patient.
  
- **Appointment Management:**  
  Create, reschedule, and cancel appointments with status transitions based on changes.
  
- **OPD Queue Management:**  
  Manage queue entries with filtering by patient, doctor, or hospital; support for walk-in patients.
  
- **Bed Management:**  
  View available and occupied beds per hospital.
  
- **Analytics & Reports:**  
  Visualize appointment and queue trends using charts (bar, line, and donut charts) with interactive tooltips.
  
- **Responsive UI:**  
  A modern, pastel-themed interface using Thymeleaf and custom CSS.

## Technologies

- **Backend:** Java, Spring Boot, Spring Data JPA
- **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript (Chart.js)
- **Database:** MySQL
- **Build Tool:** Maven
- **Version Control:** Git

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/your-username/hospital-management-system.git
   cd hospital-management-system

   ```
   
2. **Configure the Database:**

   Install MySQL and create a database (e.g., medorb_hms).
    
   Update the application.properties file (located in src/main/resources) with your MySQL credentials:

  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/medorb_hms?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
  spring.datasource.username=your_mysql_username
  spring.datasource.password=your_mysql_password
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
  server.servlet.context-path=/HMS
  spring.thymeleaf.prefix=classpath:/templates/
  spring.thymeleaf.suffix=.html
  spring.thymeleaf.cache=false    
    
  ```

3. **Build and Run the Project:**

   Using Maven:
   
  ```bash
  mvn clean install
  mvn spring-boot:run
  
  ```
  
4. **Access the Application:**

   Open your browser and navigate to:
   
     http://localhost:8080/HMS/
     
## Usage

- **Login:**
  Use the login forms on the index page for each role (Super Admin, Hospital Admin, Doctor, Patient).
  
- **Dashboards:**
  Upon successful login, you are redirected to your respective dashboard.
  
- **Management Features:** Super Admin: Manage hospitals, hospital admins, doctors, beds, appointments, and OPD queues.
- **Hospital Admin:** Manage hospital-specific data like doctors, beds, OPD queues, and appointments.
- **Doctor:** View upcoming appointments and patient information.
- **Patient:** Register, update profile, book appointments, and view visit history.
- **Analytics & Reports:** Access the analytics page from the sidebar to view charts representing appointment trends and other metrics.


## Project Structure

``` graphql
hospital-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/medorb/HMS/
│   │   │   ├── controller/         # All Controllers (e.g., ViewController, SuperAdminViewController, PatientDashboardController, etc.)
│   │   │   ├── dto/                # Data Transfer Objects (e.g., HospitalAppointmentCountDTO, TimeSeriesDTO)
│   │   │   ├── model/              # JPA Entity classes (e.g., Appointment, Hospital, Doctor, Patient, OpdQueue, etc.)
│   │   │   ├── repository/         # Spring Data JPA repositories
│   │   │   ├── service/            # Service interfaces and implementations
│   │   │   └── HospitalManagementSystemApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/          # Thymeleaf templates (e.g., index.html, super-admin-dashboard.html, patient-dashboard.html, etc.)
│   │       └── static/             # Static assets (CSS, JS, images)
└── pom.xml

```


## Screenshots
Include screenshots of the main pages and dashboards here if available.

## Contributing
Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License
This project is fully developed by me @mubeenuddin316 and all rights reserved.

## Contact
For any questions or suggestions, please contact:
Mohammed Mubeenuddin
Email: mubeenuddin316@gmail.com
GitHub: https://github.com/mubeenuddin316/HMS
