INSERT INTO Product(ProductId, Name, Cost, Stock, ImageUrl)
VALUES (0, 'Raspberry Pi', 99.99, 3, 'https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcSksSIv20lOBy1zAyO0r-tDFlUFCiE-8pTyqFT0WbtlUfqwt2yT31aY2_xRoCbjdcSu_FPJgL2Y');

INSERT INTO Delivery(DeliveryId, OrderId, SourceAddressId, DestinationAddressId, Courier, CourierDeliveryId)
VALUES (1, 1, 2, 3, 'Best Couriers', 4);

INSERT INTO Address(AddressId, StreetNumber, Street, Suburb, State, Postcode)
VALUES (2, '5', 'Source Avenue', 'Source Valley', 2, 321);

INSERT INTO Address(AddressId, StreetNumber, Street, Suburb, State, Postcode)
VALUES (3, '10', 'Destination Avenue', 'Destination Valley', 3, 456);

INSERT INTO Payment(PaymentId, CardId, Amount, PaymentStatus)
VALUES(1, 1, 23.45, 1);

INSERT INTO Card(CardId, Name, Number, Expiry, CVC)
VALUES(1, 'John Smith', '123456789', '2026-08-31 00:00:00.000', '123');

INSERT INTO `Order`(OrderId, UserId, CartId, PaymentId, DatePlaced, OrderStatus)
VALUES (1, 0, 1, 1, '2025-04-25 00:00:00', 'PROCESSING');

INSERT INTO User (UserId, FirstName, LastName, Email, Phone, Password)
VALUES (0, 'John', 'Smith', 'john.smith@gmail.com', '+61412345678', 'johnsPassword'),
(1, 'Gregory', 'Stafferson', 'gregory.stafferson@iotbay.com', '+61487654321', '!@#$%^&*()');

INSERT INTO Customer (UserId, CartId)
VALUES (0, 999);

INSERT INTO Staff (UserId, StaffCardId)
VALUES (1, 1001);

INSERT INTO ApplicationAccessLog (AppAccLogId, UserId, ApplicationAction, DateTime)
VALUES (0, 0, 0, 1745632800000),
(1, 0, 1, 1745633100000);

INSERT INTO ProductListEntry(CartId, ProductId, Quantity)
VALUES (1, 0, 1);

INSERT INTO Cart (LastUpdated)
VALUES ('2026-08-31 00:00:00.000');
-- Additional addresses for shipments
INSERT INTO Address(AddressId, StreetNumber, Street, Suburb, State, Postcode)
VALUES 
    (10, '42', 'Shipping Lane', 'Delivery Bay', 0, 2000),
    (11, '123', 'Parcel Street', 'Package City', 1, 3000),
    (12, '7', 'Post Road', 'Mail Town', 2, 4000);

-- Sample shipments
INSERT INTO Shipment(ShipmentId, OrderId, AddressId, Method, ShipmentDate)
VALUES 
    (1, 1, 10, 'Standard', '2025-05-01 10:00:00'),
    (2, 1, 11, 'Express', '2025-05-02 09:30:00'),
    (3, 1, 12, 'Priority', '2025-05-03 08:15:00');