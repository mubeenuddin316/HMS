<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>MedOrb HMS - Welcome</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* Custom CSS for layout adjustments if needed */
        body { padding-top: 70px; /* Adjust for fixed header height */ }
        #left-section { padding: 20px; }
        #center-section { padding: 20px; }
        #right-section { padding: 20px; border: 1px solid #ccc; border-radius: 5px; } /* Example styling for right section */
    </style>
</head>
<body>

    <header class="fixed-top bg-light">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">
                    <img src="holder.js/30x30?theme=gray&bg=e9ecef&fg=6c757d&text=Logo" width="30" height="30" class="d-inline-block align-top" alt="Logo">
                    MedOrb HMS
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <button class="btn btn-outline-primary me-2" onclick="showLoginForm('patient')">Patient Login</button>
                        </li>
                        <li class="nav-item">
                            <button class="btn btn-outline-secondary me-2" onclick="showLoginForm('doctor')">Doctor Login</button>
                        </li>
                        <li class="nav-item">
                            <button class="btn btn-outline-success me-2" onclick="showLoginForm('hospitalAdmin')">Hospital Login</button>
                        </li>
                        <li class="nav-item">
                            <button class="btn btn-outline-info" onclick="showLoginForm('superAdmin')">Admin Login</button>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </header>

    <div class="container-fluid">
        <div class="row">
            <div class="col-md-4" id="left-section">
                <img src="holder.js/400x300?theme=sky&bg=f0f8ff&fg=87cefa&text=Hospital%20Image" class="img-fluid" alt="Hospital Welcome Image">
            </div>

            <div class="col-md-4" id="center-section">
                <h2>Welcome to MedOrb Hospitals</h2>
                <p>Your life but we too Care.</p>
                </div>

            <div class="col-md-4" id="right-section">
                <h3>Login</h3>
                <p>Please select a login type from the top right buttons.</p>
                <div id="login-form-container">
                    </div>
            </div>
        </div>
    </div>

    <footer class="footer bg-light text-center py-3 fixed-bottom">
        <div class="container">
            <span class="text-muted">&copy; <%= new java.util.Date().toString() %> MedOrb HMS. All Rights Reserved.</span>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/holder/2.9.9/holder.min.js"></script> <script>
        function showLoginForm(userType) {
            const loginFormContainer = document.getElementById('login-form-container');
            loginFormContainer.innerHTML = ''; // Clear previous form

            let formHTML = '';
            switch (userType) {
                case 'patient':
                    formHTML = '<h4>Patient Login Form</h4><p>Patient login form will be here.</p>'; // Replace with actual form later
                    break;
                case 'doctor':
                    formHTML = '<h4>Doctor Login Form</h4><p>Doctor login form will be here.</p>';     // Replace with actual form later
                    break;
                case 'hospitalAdmin':
                    formHTML = '<h4>Hospital Admin Login Form</h4><p>Hospital Admin login form will be here.</p>'; // Replace with actual form later
                    break;
                case 'superAdmin':
                    formHTML = '<h4>Admin (SuperAdmin) Login Form</h4><p>Super Admin login form will be here.</p>'; // Replace with actual form later
                    break;
                default:
                    formHTML = '<p>Please select a login type.</p>';
            }
            loginFormContainer.innerHTML = formHTML; // Set the form HTML
        }
    </script>

</body>
</html>