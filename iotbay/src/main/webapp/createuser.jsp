<%@ page import="model.User, model.Staff, model.Customer" %>
<%
    String error = (String) session.getAttribute("createUserError");
    session.removeAttribute("createUserError");

    // Check admin permissions
    User loggedInUser = (User) session.getAttribute("user");
    boolean isAdmin = false;
    if (loggedInUser instanceof Staff) {
        isAdmin = ((Staff) loggedInUser).isAdmin();
    }

    if (!isAdmin) {
        response.sendRedirect("userlist.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create User - IoTBay</title>
    
    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="css/edituser.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
</head>
<body>
    <div class="banner">
        <h1>Internet of Things Store</h1>
        <jsp:include page="navbar.jsp" />
    </div>
    
    <div class="container mt-4">
        <h1>Create New User</h1>
        
        <% if (error != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= error %>
            </div>
        <% } %>

        <form action="CreateUserServlet" method="POST" class="needs-validation" novalidate>
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="firstName" class="form-label">First Name</label>
                    <input type="text" class="form-control" id="firstName" name="firstName" required>
                </div>
                <div class="col-md-6">
                    <label for="lastName" class="form-label">Last Name</label>
                    <input type="text" class="form-control" id="lastName" name="lastName" required>
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <div class="col-md-6">
                    <label for="phone" class="form-label">Phone</label>
                    <input type="tel" class="form-control" id="phone" name="phone" required>
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <div class="col-md-6">
                    <label for="confirmPassword" class="form-label">Confirm Password</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-md-6">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="isStaff" name="isStaff" value="true">
                        <label class="form-check-label" for="isStaff">
                            Staff Member
                        </label>
                    </div>
                </div>
                <div class="col-md-6 staff-fields" style="display: none;">
                    <div class="row">
                        <div class="col">
                            <label for="staffCardId" class="form-label">Staff Card ID</label>
                            <input type="number" class="form-control" id="staffCardId" name="staffCardId">
                        </div>
                        <div class="col">
                            <div class="form-check mt-4">
                                <input class="form-check-input" type="checkbox" id="isAdmin" name="isAdmin" value="true">
                                <label class="form-check-label" for="isAdmin">
                                    Admin Access
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row mt-3">
                        <div class="col">
                            <div class="staff-password-section" style="display: none;">
                                <label for="staffPassword" class="form-label">Staff Password</label>
                                <input type="password" class="form-control" id="staffPassword" name="staffPassword">
                                <small class="form-text text-muted">Required for staff role</small>
                            </div>
                            <div class="admin-password-section" style="display: none;">
                                <label for="systemAdminPassword" class="form-label">System Admin Password</label>
                                <input type="password" class="form-control" id="systemAdminPassword" name="systemAdminPassword">
                                <small class="form-text text-muted">Required for admin role</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-md-6">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="isCustomer" name="isCustomer" value="true">
                        <label class="form-check-label" for="isCustomer">
                            Customer
                        </label>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="customer-type-section" style="display: none;">
                        <label for="customerType" class="form-label">Customer Type</label>
                        <select class="form-select" id="customerType" name="customerType">
                            <option value="INDIVIDUAL">Individual</option>
                            <option value="COMPANY">Company</option>
                        </select>
                    </div>
                </div>
            </div>

            <div class="row mt-4">
                <div class="col">
                    <a href="UserListServlet" class="btn btn-secondary">Cancel</a>
                    <button type="submit" class="btn btn-primary">Create User</button>
                </div>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    
    <script>
        // Toggle staff fields visibility
        document.getElementById('isStaff').addEventListener('change', function() {
            const staffFields = document.querySelector('.staff-fields');
            staffFields.style.display = this.checked ? 'block' : 'none';
            
            // If checking staff, uncheck customer
            if (this.checked) {
                document.getElementById('isCustomer').checked = false;
                document.querySelector('.customer-type-section').style.display = 'none';
            }
            
            updatePasswordFields();
        });

        // Toggle customer fields visibility
        document.getElementById('isCustomer').addEventListener('change', function() {
            const customerTypeSection = document.querySelector('.customer-type-section');
            customerTypeSection.style.display = this.checked ? 'block' : 'none';
            
            // If checking customer, uncheck staff and admin
            if (this.checked) {
                document.getElementById('isStaff').checked = false;
                document.getElementById('isAdmin').checked = false;
                document.querySelector('.staff-fields').style.display = 'none';
            }
        });

        // Toggle admin password field visibility
        document.getElementById('isAdmin').addEventListener('change', function() {
            updatePasswordFields();
        });

        function updatePasswordFields() {
            const isStaffChecked = document.getElementById('isStaff').checked;
            const isAdminChecked = document.getElementById('isAdmin').checked;
            const staffPasswordSection = document.querySelector('.staff-password-section');
            const adminPasswordSection = document.querySelector('.admin-password-section');
            
            staffPasswordSection.style.display = 'none';
            adminPasswordSection.style.display = 'none';
            
            if (isStaffChecked) {
                if (isAdminChecked) {
                    adminPasswordSection.style.display = 'block';
                } else {
                    staffPasswordSection.style.display = 'block';
                }
            }
        }

        // Form validation
        (function () {
            'use strict'
            var forms = document.querySelectorAll('.needs-validation')
            Array.prototype.slice.call(forms).forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
        })()
    </script>
</body>
</html> 