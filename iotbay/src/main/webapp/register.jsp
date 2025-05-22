<%@ page import="model.User" %>
<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <%
        String error = (String) session.getAttribute("registerError");
        session.removeAttribute("registerError");
    %>
    <head>
        <link rel="stylesheet" href="css/main.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <script>
            function handleStaffCheckbox() {
                const staffSection = document.getElementById('staff-section');
                const staffCheckbox = document.getElementById('staff-checkbox');
                staffSection.style.display = staffCheckbox.checked ? 'block' : 'none';
            }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="banner">
            <h1>Internet of Things Store</h1>
            <jsp:include page="navbar.jsp" />
        </div>
        <div class="content">
            <h2>Register</h2>
            <form action="RegisterServlet" method="post">
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="text" name="email" id="email" class="form-control" required />
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" name="password" id="password" class="form-control" required />
                    <p class="fst-italic">Must be at least 8 characters long, and include a lowercase and uppercase letter, a number, and a special character</p>
                </div>
                <div class="mb-3">
                    <label for="firstName" class="form-label">First Name (optional)</label>
                    <input type="text" name="firstName" id="firstName" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="lastName" class="form-label">Last Name (optional)</label>
                    <input type="text" name="lastName" id="lastName" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label">Phone Number (optional)</label>
                    <input type="text" name="phone" id="phone" class="form-control" />
                </div>
                <div class="mb-3" id="customer-type-section">
                    <label for="type" class="form-label">User Type</label>
                    <select name="type" id="type" class="form-select">
                        <option value="INDIVIDUAL">Individual</option>
                        <option value="COMPANY">Company</option>
                    </select>
                </div>
                <div class="mb-3 form-check">
                    <input id="staff-checkbox" type="checkbox" name="isStaff" class="form-check-input" onclick="handleStaffCheckbox()" />
                    <label for="staff-checkbox" class="form-check-label">Register as staff</label>
                </div>
                <div class="mb-3 form-check" id="system-admin-section">
                    <input id="system-admin-checkbox" type="checkbox" name="isSystemAdmin" class="form-check-input" />
                    <label for="system-admin-checkbox" class="form-check-label">Register as system admin</label>
                </div>
                <div id="staff-section" style="display: none;">
                    <div class="mb-3">
                        <label for="staffCardId" class="form-label">Staff Card ID</label>
                        <input type="text" name="staffCardId" id="staffCardId" class="form-control" />
                    </div>
                    <div class="mb-3">
                        <label for="staffPassword" class="form-label">Staff Password</label>
                        <input type="password" name="staffPassword" id="staffPassword" class="form-control" />
                    </div>
                    <div class="mb-3">
                        <label for="systemAdminPassword" class="form-label">System Admin Password</label>
                        <input type="password" name="systemAdminPassword" id="systemAdminPassword" class="form-control" />
                    </div>
                </div>
                <p class="error text-danger"><%= (error == null ? "" : error) %></p>
                <input type="submit" class="btn btn-success" value="Register" />
            </form>
        </div>
    </body>
</html>
