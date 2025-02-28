<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Patient Dashboard</title>
</head>
<body>
    <%-- Access the patientName from the loggedInPatient object in the Model --%>
    <h1>Welcome, ${loggedInPatient.patientName}!</h1>  <%-- Display Patient's Name --%>
    <p>This is the dashboard page for patients.</p>
    <p><a href="index.jsp">Back to Home Page</a></p>
</body>
</html>