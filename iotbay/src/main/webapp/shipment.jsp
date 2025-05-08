<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>IoTBay - Shipment Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
    <style>
        /* Add additional custom styles here if needed */
        .section {
            display: none;
        }
        .section.active {
            display: block;
        }
        .badge-warning {
            background-color: #ffc107;
        }
        .badge-info {
            background-color: #17a2b8;
        }
        .badge-primary {
            background-color: #007bff;
        }
        .badge-success {
            background-color: #28a745;
        }
        .badge-danger {
            background-color: #dc3545;
        }
        .badge {
            color: white;
            padding: 0.375rem 0.5rem;
            border-radius: 0.25rem;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />
    
    <div class="container">
        <h1 id="pageTitle">Shipment Management</h1>
        
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                ${successMessage}
            </div>
        </c:if>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ${errorMessage}
            </div>
        </c:if>
        
        <!-- LIST SECTION -->
        <div id="listSection" class="section active">
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Search Shipments</h4>
                </div>
                <div class="card-body">
                    <form action="shipment" method="get">
                        <input type="hidden" name="action" value="searchShipments">
                        
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <label for="searchType" class="form-label">Search By:</label>
                                    <select class="form-select" id="searchType" name="searchType">
                                        <option value="id" ${searchType == 'id' ? 'selected' : ''}>Shipment ID</option>
                                        <option value="date" ${searchType == 'date' ? 'selected' : ''}>Estimated Delivery Date</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="searchValue" class="form-label">Search Value:</label>
                                    <input type="text" class="form-control" id="searchValue" name="searchValue" value="${searchValue}" 
                                           placeholder="Enter ID or date (YYYY-MM-DD)">
                                </div>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">&nbsp;</label>
                                <button type="submit" class="btn btn-primary w-100">Search</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h4 class="mb-0">Shipment List</h4>
                    <button class="btn btn-success" onclick="showCreateSection()">Create New Shipment</button>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty deliveries}">
                            <p>No shipments found.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>From</th>
                                            <th>To</th>
                                            <th>Courier</th>
                                            <th>Status</th>
                                            <th>Est. Delivery Date</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="delivery" items="${deliveries}">
                                            <tr>
                                                <td>${delivery.deliveryId}</td>
                                                <td>${delivery.source.suburb}, ${delivery.source.state}</td>
                                                <td>${delivery.destination.suburb}, ${delivery.destination.state}</td>
                                                <td>${delivery.courierName}</td>
                                                <td>
                                                    <span class="badge ${delivery.status eq 'PENDING' ? 'badge-warning' : 
                                                                        delivery.status eq 'FINALIZED' ? 'badge-info' : 
                                                                        delivery.status eq 'IN_TRANSIT' ? 'badge-primary' : 
                                                                        delivery.status eq 'DELIVERED' ? 'badge-success' : 
                                                                        'badge-danger'}">
                                                        ${delivery.statusDisplayName}
                                                    </span>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${delivery.estDeliveryDate}" pattern="dd/MM/yyyy" />
                                                </td>
                                                <td>
                                                    <div class="btn-group" role="group">
                                                        <button onclick="viewDelivery(${delivery.deliveryId})" class="btn btn-sm btn-info">View</button>
                                                        
                                                        <c:if test="${delivery.canBeModified()}">
                                                            <button onclick="editDelivery(${delivery.deliveryId})" class="btn btn-sm btn-primary">Edit</button>
                                                            <button onclick="confirmDelete(${delivery.deliveryId})" class="btn btn-sm btn-danger">Delete</button>
                                                        </c:if>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        
        <!-- VIEW SECTION -->
        <div id="viewSection" class="section">
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h4 class="mb-0">Shipment #<span id="viewDeliveryId">${delivery.deliveryId}</span></h4>
                    <div>
                        <button class="btn btn-secondary" onclick="showListSection()">Back to List</button>
                        <c:if test="${delivery.canBeModified()}">
                            <button class="btn btn-primary" onclick="editCurrentDelivery()">Edit</button>
                            <button class="btn btn-danger" onclick="confirmDelete(${delivery.deliveryId})">Delete</button>
                        </c:if>
                    </div>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <h5>Shipment Information</h5>
                            <table class="table table-bordered">
                                <tr>
                                    <th>Shipment ID:</th>
                                    <td id="viewShipmentId">${delivery.deliveryId}</td>
                                </tr>
                                <tr>
                                    <th>Status:</th>
                                    <td id="viewStatus">
                                        <span class="badge ${delivery.status eq 'PENDING' ? 'badge-warning' : 
                                                          delivery.status eq 'FINALIZED' ? 'badge-info' : 
                                                          delivery.status eq 'IN_TRANSIT' ? 'badge-primary' : 
                                                          delivery.status eq 'DELIVERED' ? 'badge-success' : 
                                                          'badge-danger'}">
                                            ${delivery.statusDisplayName}
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Courier:</th>
                                    <td id="viewCourier">${delivery.courierName}</td>
                                </tr>
                                <tr>
                                    <th>Tracking Number:</th>
                                    <td id="viewTrackingNumber">${delivery.trackingNumber}</td>
                                </tr>
                                <tr>
                                    <th>Est. Delivery Date:</th>
                                    <td id="viewDeliveryDate">
                                        <fmt:formatDate value="${delivery.estDeliveryDate}" pattern="dd/MM/yyyy" />
                                    </td>
                                </tr>
                            </table>
                        </div>
                        
                        <div class="col-md-6">
                            <h5>Associated Orders</h5>
                            <div id="viewOrders">
                                <c:choose>
                                    <c:when test="${empty delivery.orders}">
                                        <p>No orders associated with this shipment.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <table class="table table-bordered">
                                            <thead>
                                                <tr>
                                                    <th>Order ID</th>
                                                    <th>Date</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="order" items="${delivery.orders}">
                                                    <tr>
                                                        <td>${order.orderId}</td>
                                                        <td>
                                                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy" />
                                                        </td>
                                                        <td>
                                                            <a href="order?action=viewOrder&orderId=${order.orderId}" class="btn btn-sm btn-info">View Order</a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- CREATE SECTION -->
        <div id="createSection" class="section">
            <div class="card">
                <div class="card-header">
                    <h4>Create New Shipment</h4>
                </div>
                <div class="card-body">
                    <form id="createForm" action="shipment" method="post">
                        <input type="hidden" name="action" value="createShipment">
                        <input type="hidden" name="orderId" value="${param.orderId}">
                        
                        <!-- Source Address Section -->
                        <h3>Source Address</h3>
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <label for="srcStreetNumber" class="form-label">Street Number:</label>
                                    <input type="number" class="form-control" id="srcStreetNumber" name="srcStreetNumber" required>
                                </div>
                            </div>
                            <div class="col-md-8">
                                <div class="form-group mb-3">
                                    <label for="srcStreet" class="form-label">Street:</label>
                                    <input type="text" class="form-control" id="srcStreet" name="srcStreet" required>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="srcSuburb" class="form-label">Suburb:</label>
                                    <input type="text" class="form-control" id="srcSuburb" name="srcSuburb" required>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group mb-3">
                                    <label for="srcState" class="form-label">State:</label>
                                    <select class="form-select" id="srcState" name="srcState" required>
                                        <option value="NSW">NSW</option>
                                        <option value="VIC">VIC</option>
                                        <option value="QLD">QLD</option>
                                        <option value="WA">WA</option>
                                        <option value="SA">SA</option>
                                        <option value="TAS">TAS</option>
                                        <option value="ACT">ACT</option>
                                        <option value="NT">NT</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group mb-3">
                                    <label for="srcPostcode" class="form-label">Postcode:</label>
                                    <input type="text" class="form-control" id="srcPostcode" name="srcPostcode" pattern="[0-9]{4}" required>
                                </div>
                            </div>
                        </div>
                        
                        <hr>
                        
                        <!-- Destination Address Section -->
                        <h3>Destination Address</h3>
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <label for="destStreetNumber" class="form-label">Street Number:</label>
                                    <input type="number" class="form-control" id="destStreetNumber" name="destStreetNumber" required>
                                </div>
                            </div>
                            <div class="col-md-8">
                                <div class="form-group mb-3">
                                    <label for="destStreet" class="form-label">Street:</label>
                                    <input type="text" class="form-control" id="destStreet" name="destStreet" required>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="destSuburb" class="form-label">Suburb:</label>
                                    <input type="text" class="form-control" id="destSuburb" name="destSuburb" required>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group mb-3">
                                    <label for="destState" class="form-label">State:</label>
                                    <select class="form-select" id="destState" name="destState" required>
                                        <option value="NSW">NSW</option>
                                        <option value="VIC">VIC</option>
                                        <option value="QLD">QLD</option>
                                        <option value="WA">WA</option>
                                        <option value="SA">SA</option>
                                        <option value="TAS">TAS</option>
                                        <option value="ACT">ACT</option>
                                        <option value="NT">NT</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group mb-3">
                                    <label for="destPostcode" class="form-label">Postcode:</label>
                                    <input type="text" class="form-control" id="destPostcode" name="destPostcode" pattern="[0-9]{4}" required>
                                </div>
                            </div>
                        </div>
                        
                        <hr>
                        
                        <!-- Shipping Details Section -->
                        <h3>Shipping Details</h3>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="courier" class="form-label">Courier Service:</label>
                                    <select class="form-select" id="courier" name="courier" required>
                                        <option value="Australia Post">Australia Post</option>
                                        <option value="Toll">Toll</option>
                                        <option value="StarTrack">StarTrack</option>
                                        <option value="DHL">DHL</option>
                                        <option value="FedEx">FedEx</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="estimatedDeliveryDate" class="form-label">Estimated Delivery Date:</label>
                                    <input type="date" class="form-control" id="estimatedDeliveryDate" name="estimatedDeliveryDate" required>
                                </div>
                            </div>
                        </div>
                        
                        <div class="form-group text-center mt-4">
                            <button type="submit" class="btn btn-primary">Create Shipment</button>
                            <button type="button" class="btn btn-secondary" onclick="showListSection()">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <!-- EDIT SECTION -->
        <div id="editSection" class="section">
            <div class="card">
                <div class="card-header">
                    <h4>Update Shipment</h4>
                </div>
                <div class="card-body">
                    <form id="editForm" action="shipment" method="post">
                        <input type="hidden" name="action" value="updateShipment">
                        <input type="hidden" id="editDeliveryId" name="deliveryId" value="${delivery.deliveryId}">
                        
                        <!-- Source Address Section -->
                        <h3>Source Address</h3>
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <label for="editSrcStreetNumber" class="form-label">Street Number:</label>
                                    <input type="number" class="form-control" id="editSrcStreetNumber" name="srcStreetNumber" value="${delivery.source.streetNumber}" required>
                                </div>
                            </div>
                            <div class="col-md-8">
                                <div class="form-group mb-3">
                                    <label for="editSrcStreet" class="form-label">Street:</label>
                                    <input type="text" class="form-control" id="editSrcStreet" name="srcStreet" value="${delivery.source.street}" required>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="editSrcSuburb" class="form-label">Suburb:</label>
                                    <input type="text" class="form-control" id="editSrcSuburb" name="srcSuburb" value="${delivery.source.suburb}" required>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group mb-3">
                                    <label for="editSrcState" class="form-label">State:</label>
                                    <select class="form-select" id="editSrcState" name="srcState" required>
                                        <option value="NSW" ${delivery.source.state == 'NSW' ? 'selected' : ''}>NSW</option>
                                        <option value="VIC" ${delivery.source.state == 'VIC' ? 'selected' : ''}>VIC</option>
                                        <option value="QLD" ${delivery.source.state == 'QLD' ? 'selected' : ''}>QLD</option>
                                        <option value="WA" ${delivery.source.state == 'WA' ? 'selected' : ''}>WA</option>
                                        <option value="SA" ${delivery.source.state == 'SA' ? 'selected' : ''}>SA</option>
                                        <option value="TAS" ${delivery.source.state == 'TAS' ? 'selected' : ''}>TAS</option>
                                        <option value="ACT" ${delivery.source.state == 'ACT' ? 'selected' : ''}>ACT</option>
                                        <option value="NT" ${delivery.source.state == 'NT' ? 'selected' : ''}>NT</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group mb-3">
                                    <label for="editSrcPostcode" class="form-label">Postcode:</label>
                                    <input type="text" class="form-control" id="editSrcPostcode" name="srcPostcode" pattern="[0-9]{4}" value="${delivery.source.postcode}" required>
                                </div>
                            </div>
                        </div>
                        
                        <hr>
                        
                        <!-- Destination Address Section -->
                        <h3>Destination Address</h3>
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group mb-3">
                                    <label for="editDestStreetNumber" class="form-label">Street Number:</label>
                                    <input type="number" class="form-control" id="editDestStreetNumber" name="destStreetNumber" value="${delivery.destination.streetNumber}" required>
                                </div>
                            </div>
                            <div class="col-md-8">
                                <div class="form-group mb-3">
                                    <label for="editDestStreet" class="form-label">Street:</label>
                                    <input type="text" class="form-control" id="editDestStreet" name="destStreet" value="${delivery.destination.street}" required>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="editDestSuburb" class="form-label">Suburb:</label>
                                    <input type="text" class="form-control" id="editDestSuburb" name="destSuburb" value="${delivery.destination.suburb}" required>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group mb-3">
                                    <label for="editDestState" class="form-label">State:</label>
                                    <select class="form-select" id="editDestState" name="destState" required>
                                        <option value="NSW" ${delivery.destination.state == 'NSW' ? 'selected' : ''}>NSW</option>
                                        <option value="VIC" ${delivery.destination.state == 'VIC' ? 'selected' : ''}>VIC</option>
                                        <option value="QLD" ${delivery.destination.state == 'QLD' ? 'selected' : ''}>QLD</option>
                                        <option value="WA" ${delivery.destination.state == 'WA' ? 'selected' : ''}>WA</option>
                                        <option value="SA" ${delivery.destination.state == 'SA' ? 'selected' : ''}>SA</option>
                                        <option value="TAS" ${delivery.destination.state == 'TAS' ? 'selected' : ''}>TAS</option>
                                        <option value="ACT" ${delivery.destination.state == 'ACT' ? 'selected' : ''}>ACT</option>
                                        <option value="NT" ${delivery.destination.state == 'NT' ? 'selected' : ''}>NT</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group mb-3">
                                    <label for="editDestPostcode" class="form-label">Postcode:</label>
                                    <input type="text" class="form-control" id="editDestPostcode" name="destPostcode" pattern="[0-9]{4}" value="${delivery.destination.postcode}" required>
                                </div>
                            </div>
                        </div>
                        
                        <hr>
                        
                        <!-- Shipping Details Section -->
                        <h3>Shipping Details</h3>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="editCourier" class="form-label">Courier Service:</label>
                                    <select class="form-select" id="editCourier" name="courier" required>
                                        <option value="Australia Post" ${delivery.courierName == 'Australia Post' ? 'selected' : ''}>Australia Post</option>
                                        <option value="Toll" ${delivery.courierName == 'Toll' ? 'selected' : ''}>Toll</option>
                                        <option value="StarTrack" ${delivery.courierName == 'StarTrack' ? 'selected' : ''}>StarTrack</option>
                                        <option value="DHL" ${delivery.courierName == 'DHL' ? 'selected' : ''}>DHL</option>
                                        <option value="FedEx" ${delivery.courierName == 'FedEx' ? 'selected' : ''}>FedEx</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group mb-3">
                                    <label for="editEstimatedDeliveryDate" class="form-label">Estimated Delivery Date:</label>
                                    <input type="date" class="form-control" id="editEstimatedDeliveryDate" name="estimatedDeliveryDate" required>
                                </div>
                            </div>
                        </div>
                        
                        <div class="form-group text-center mt-4">
                            <button type="submit" class="btn btn-primary">Update Shipment</button>
                            <button type="button" class="btn btn-secondary" onclick="showListSection()">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteModalLabel">Confirm Delete</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Are you sure you want to delete this shipment?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form id="deleteForm" action="shipment" method="post">
                        <input type="hidden" name="action" value="deleteShipment">
                        <input type="hidden" id="deleteDeliveryId" name="deliveryId" value="">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="/WEB-INF/includes/footer.jsp" />
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Variables to store current delivery ID
        let currentDeliveryId = null;
        
        // Initialize components
        document.addEventListener('DOMContentLoaded', function() {
            // Set minimum date for estimated delivery date to tomorrow
            let today = new Date();
            today.setDate(today.getDate() + 1); // Set to tomorrow
            let tomorrow = today.toISOString().split('T')[0];
            document.getElementById('estimatedDeliveryDate').setAttribute('min', tomorrow);
            
            // Format the date in the edit form if available
            const editDateInput = document.getElementById('editEstimatedDeliveryDate');
            if (editDateInput) {
                const estDate = '${delivery.estDeliveryDate}';
                if (estDate) {
                    const date = new Date(estDate);
                    if (!isNaN(date.getTime())) {
                        editDateInput.value = date.toISOString().split('T')[0];
                    }
                }
            }
            
            // Check if there's an action parameter in the URL
            const urlParams = new URLSearchParams(window.location.search);
            const action = urlParams.get('action');
            
            if (action === 'viewShipment') {
                currentDeliveryId = urlParams.get('deliveryId');
                showViewSection();
            } else if (action === 'showUpdateForm') {
                currentDeliveryId = urlParams.get('deliveryId');
                showEditSection();
            } else if (action === 'showCreateForm') {
                showCreateSection();
            } else {
                showListSection();
            }
        });
        
        // Delete confirmation
        function confirmDelete(deliveryId) {
            document.getElementById('deleteDeliveryId').value = deliveryId;
            const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
            deleteModal.show();
        }
        
        // Section display functions
        function showListSection() {
            document.getElementById('pageTitle').textContent = 'Shipment Management';
            hideAllSections();
            document.getElementById('listSection').classList.add('active');
            history.pushState(null, null, 'shipment?action=listShipments');
        }
        
        function showViewSection() {
            document.getElementById('pageTitle').textContent = 'View Shipment';
            hideAllSections();
            document.getElementById('viewSection').classList.add('active');
            history.pushState(null, null, 'shipment?action=viewShipment&deliveryId=' + currentDeliveryId);
        }
        
        function showCreateSection() {
            document.getElementById('pageTitle').textContent = 'Create New Shipment';
            hideAllSections();
            document.getElementById('createSection').classList.add('active');
            history.pushState(null, null, 'shipment?action=showCreateForm');
        }
        
        function showEditSection() {
            document.getElementById('pageTitle').textContent = 'Edit Shipment';
            hideAllSections();
            document.getElementById('editSection').classList.add('active');
            history.pushState(null, null, 'shipment?action=showUpdateForm&deliveryId=' + currentDeliveryId);
        }
        
        function hideAllSections() {
            const sections = document.querySelectorAll('.section');
            sections.forEach(section => {
                section.classList.remove('active');
            });
        }
        
        // Navigation functions
        function viewDelivery(deliveryId) {
            currentDeliveryId = deliveryId;
            // Here we would normally fetch the delivery details via AJAX
            // For now, we'll just navigate to the view page
            window.location.href = 'shipment?action=viewShipment&deliveryId=' + deliveryId;
        }
        
        function editDelivery(deliveryId) {
            currentDeliveryId = deliveryId;
            // Here we would normally fetch the delivery details via AJAX
            // For now, we'll just navigate to the edit page
            window.location.href = 'shipment?action=showUpdateForm&deliveryId=' + deliveryId;
        }
        
        function editCurrentDelivery() {
            if (currentDeliveryId) {
                editDelivery(currentDeliveryId);
            }
        }
    </script>
</body>
</html>