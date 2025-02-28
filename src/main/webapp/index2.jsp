<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <link rel="stylesheet" href="css/index-style.css">
    <script>
        function showLoginForm(role) {
            console.log("Button clicked for: " + role);
            
            // Hide all login forms
            document.querySelectorAll('.form-container').forEach(form => {
                form.style.display = 'none';
            });
            
            // Show the selected form
            var formId = role + '-form';
            var form = document.getElementById(formId);
            if (form) {
                console.log("Showing form: " + formId);
                form.style.display = 'block';
            } else {
                console.error("Form with ID " + formId + " not found.");
            }
        }
        
        // Handle admin login form submission
        document.addEventListener('DOMContentLoaded', function() {
            const adminForm = document.getElementById('admin-login-form');
            
            if (adminForm) {
                adminForm.addEventListener('submit', function(e) {
                    e.preventDefault();
                    
                    const email = this.querySelector('input[name="email"]').value;
                    const password = this.querySelector('input[name="password"]').value;
                    
                    fetch('/api/superAdmins/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'email=' + encodeURIComponent(email) + '&password=' + encodeURIComponent(password)
                    })
                    .then(response => response.json())
                    .then(data => {
                        console.log('Login response:', data);
                        if (data.success) {
                            // Store admin ID in session storage if needed
                            sessionStorage.setItem('adminId', data.adminId);
                            // Redirect to dashboard
                            window.location.href = data.redirect;
                        } else {
                            // Show error message
                            alert(data.message);
                        }
                    })
                    .catch(error => {
                        console.error('Login error:', error);
                        alert('An error occurred during login. Please try again.');
                    });
                });
            }
        });
    </script>
</head>
<body>
    <div class="login-container">
        <div class="left-section-container">
            <img src="images/logo.png" alt="Hospital Image" style="max-width: 100%; height: 100%;">
        </div>
        
        <div class="login-section" onclick="showLoginForm('admin')">Admin Login</div>
        <div class="login-section" onclick="showLoginForm('hospital')">Hospital Admin Login</div>
        <div class="login-section" onclick="showLoginForm('doctor')">Doctor Login</div>
        <div class="login-section" onclick="showLoginForm('patient')">Patient Login</div>
    </div>

    <!-- Main Content (Divided into Left & Right) -->
    <div class="main-container">
        <!-- Left: Image -->
        <div class="left-section">
            <img src="images/indeximage.jpg" alt="Hospital Image" style="max-width: 100%; height: auto;">
        </div>

        <div class="right-section">
            <div id="admin-form" class="form-container">
                <h3>Admin Login</h3>
                <form id="admin-login-form">
                    <input type="email" name="email" placeholder="Enter Email" required><br>
                    <input type="password" name="password" placeholder="Enter Password" required><br>
                    <input type="submit" value="Login">
                </form>
            </div>

            <div id="hospital-form" class="form-container" style="display: none;">
                <h3>Hospital Admin Login</h3>
                <form action="hospitalLogin" method="post">
                    <input type="text" name="username" placeholder="Enter Username" required><br>
                    <input type="password" name="password" placeholder="Enter Password" required><br>
                    <input type="submit" value="Login">
                </form>
            </div>

            <div id="doctor-form" class="form-container" style="display: none;">
                <h3>Doctor Login</h3>
                <form action="doctorLogin" method="post">
                    <input type="text" name="username" placeholder="Enter Username" required><br>
                    <input type="password" name="password" placeholder="Enter Password" required><br>
                    <input type="submit" value="Login">
                </form>
            </div>

            <div id="patient-form" class="form-container" style="display: none;">
                <h3>Patient Login</h3>
                <form action="patientLogin" method="post">
                    <input type="text" name="username" placeholder="Enter Username" required><br>
                    <input type="password" name="password" placeholder="Enter Password" required><br>
                    <input type="submit" value="Login">
                </form>
            </div>
        </div>
    </div>
</body>
</html>