INSERT INTO Product(ProductId, Name, Cost, Stock)
VALUES (0, 'Raspberry Pi', 99.99, 3);

INSERT INTO Delivery(DeliveryId, SourceAddressId, DestinationAddressId, Courier, CourierDeliveryId)
VALUES (1, 2, 3, 'Best Couriers', 4);

INSERT INTO Address(AddressId, StreetNumber, Street, Suburb, State, Postcode)
VALUES (2, '5', 'Source Avenue', 'Source Valley', 2, 321);

INSERT INTO Address(AddressId, StreetNumber, Street, Suburb, State, Postcode)
VALUES (3, '10', 'Destination Avenue', 'Destination Valley', 3, 456);

INSERT INTO Payment(PaymentId, CardId, Amount, PaymentStatus)
VALUES(1, 1, 23.45, 1);

INSERT INTO Card(CardId, Name, Number, Expiry, CVC)
VALUES(1, 'John Smith', '123456789', '2026-08-31 00:00:00.000', '123');

INSERT INTO `Order`(OrderId, UserId, ProductListId, PaymentId, DeliveryId, DatePlaced)
VALUES (1, 1, 1, 1, 1, '2025-04-25 00:00:00');

INSERT INTO User (UserId, FirstName, LastName, Email, Phone, Password)
VALUES (1, 'John', 'Smith', 'john.smith@gmail.com', '+61 412 345 678', 'johnsPassword'),
(100, 'Gregory', 'Stafferson', 'gregory.stafferson@iotbay.com', '+61 487 654 321', '!@#$%^&*()');

INSERT INTO Customer (UserId, CartId)
VALUES (1, 999);

INSERT INTO Staff (UserId, StaffCardId)
Values (100, 1001);
