CREATE TABLE User (
    UserId INTEGER PRIMARY KEY AUTOINCREMENT,
    FirstName VARCHAR(30),
    LastName VARCHAR(30),
    Email VARCHAR(50) NOT NULL,
    Phone VARCHAR(17),
    Password VARCHAR(20)
);

CREATE TABLE Staff (
    UserId INTEGER PRIMARY KEY,
    StaffCardId INTEGER NOT NULL,
    FOREIGN KEY (UserId) REFERENCES User(UserId) ON DELETE CASCADE
);

CREATE TABLE Customer (
    UserId INTEGER PRIMARY KEY,
    CartId INTEGER,
    FOREIGN KEY (UserId) REFERENCES User(UserId) ON DELETE CASCADE,
    FOREIGN KEY (CartId) REFERENCES Cart(CartId)
);

CREATE TABLE Product (
    ProductId INTEGER PRIMARY KEY AUTOINCREMENT,
    Name VARCHAR(30) NOT NULL,
    Description VARCHAR(30) NULL,
    Cost DECIMAL(10, 2) NOT NULL,
    Stock INTEGER NOT NULL,
    ImageUrl VARCHAR(500)
);

CREATE TABLE ProductListEntry (
    ProductListId INTEGER,
    ProductId INTEGER,
    Quantity INTEGER NOT NULL,
    PRIMARY KEY (ProductListId, ProductId),
    FOREIGN KEY (ProductId) REFERENCES Product(ProductId) ON DELETE CASCADE
);

CREATE TABLE Cart (
    CartId INTEGER PRIMARY KEY,
    ProductListId INTEGER,
    LastUpdated DATETIME
);

CREATE TABLE `Order` (
    OrderId INTEGER PRIMARY KEY,
    UserId INTEGER,
    ProductListId INTEGER,
    PaymentId INTEGER,
    DeliveryId INTEGER,
    DatePlaced DATETIME NOT NULL,
    FOREIGN KEY (UserId) REFERENCES User(UserId),
    
    FOREIGN KEY (PaymentId) REFERENCES Payment(PaymentId),
    FOREIGN KEY (DeliveryId) REFERENCES Delivery(DeliveryId)
);

CREATE TABLE Delivery (
    DeliveryId INTEGER PRIMARY KEY,
    SourceAddressId INTEGER,
    DestinationAddressId INTEGER,
    Courier VARCHAR(30) NOT NULL,
    CourierDeliveryId INTEGER NOT NULL,
    FOREIGN KEY (SourceAddressId) REFERENCES Address(AddressId),
    FOREIGN KEY (DestinationAddressId) REFERENCES Address(AddressId)
);

CREATE TABLE Address (
    AddressId INTEGER PRIMARY KEY,
    StreetNumber VARCHAR(5),
    Street VARCHAR(30) NOT NULL,
    Suburb VARCHAR(30) NOT NULL,
    State INTEGER NOT NULL, 
    Postcode INTEGER NOT NULL
);

CREATE TABLE Payment (
    PaymentId INTEGER PRIMARY KEY,
    CardId INTEGER,
    Amount DECIMAL(10, 2) NOT NULL,
    PaymentStatus INTEGER NOT NULL,
    FOREIGN KEY (CardId) REFERENCES Card(CardId)
);

CREATE TABLE Card (
    CardId INTEGER PRIMARY KEY,
    Name VARCHAR(30),
    Number VARCHAR(19) NOT NULL,
    Expiry DATE NOT NULL,
    CVC VARCHAR(3) NOT NULL
);

CREATE TABLE ApplicationAccessLog (
    AppAccLogId INTEGER PRIMARY KEY AUTOINCREMENT,
    UserId INTEGER,
    ApplicationAction INTEGER NOT NULL,
    DateTime DATETIME NOT NULL,
    FOREIGN KEY (UserId) REFERENCES User(UserId)
);

CREATE TABLE Shipment (
    ShipmentId INTEGER PRIMARY KEY AUTOINCREMENT,
    OrderId INTEGER NOT NULL,
    AddressId INTEGER NOT NULL,
    Method VARCHAR(30) NOT NULL,
    ShipmentDate DATETIME NOT NULL,
    FOREIGN KEY (OrderId) REFERENCES `Order`(OrderId) ON DELETE CASCADE,
    FOREIGN KEY (AddressId) REFERENCES Address(AddressId)
);
