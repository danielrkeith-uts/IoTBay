CREATE TABLE User (
    UserId      INTEGER PRIMARY KEY AUTOINCREMENT,
    FirstName   VARCHAR(30),
    LastName    VARCHAR(30),
    Email       VARCHAR(50)  NOT NULL,
    Phone       VARCHAR(17),
    Password    VARCHAR(20),
    Deactivated INTEGER      DEFAULT 0
);

CREATE TABLE Staff (
    UserId INTEGER PRIMARY KEY,
    StaffCardId INTEGER NOT NULL,
    Admin BOOLEAN DEFAULT FALSE,
    Position TEXT DEFAULT 'STAFF',
    FOREIGN KEY (UserId) REFERENCES User(UserId) ON DELETE CASCADE
);

CREATE TABLE Customer (
    UserId INTEGER PRIMARY KEY,
    CartId INTEGER,
    Type TEXT DEFAULT 'INDIVIDUAL',
    FOREIGN KEY (UserId) REFERENCES User(UserId) ON DELETE CASCADE,
    FOREIGN KEY (CartId) REFERENCES Cart(CartId) ON DELETE SET NULL
);

CREATE TABLE Product (
    ProductId INTEGER PRIMARY KEY AUTOINCREMENT,
    Name VARCHAR(30) NOT NULL,
    Description VARCHAR(30) NULL,
    Type TEXT NOT NULL DEFAULT 'OTHER',
    Cost DECIMAL(10, 2) NOT NULL,
    Stock INTEGER NOT NULL,
    ImageUrl VARCHAR(500)
);

CREATE TABLE ProductListEntry (
    CartId INTEGER,
    ProductId INTEGER,
    Quantity INTEGER NOT NULL,
    PRIMARY KEY (CartId, ProductId),
    FOREIGN KEY (ProductId) REFERENCES Product(ProductId) ON DELETE CASCADE
);

CREATE TABLE Cart (
    CartId INTEGER PRIMARY KEY AUTOINCREMENT,
    LastUpdated DATETIME 
);

CREATE TABLE `Order` (
    OrderId INTEGER PRIMARY KEY,
    UserId INTEGER,
    CartId INTEGER,
    PaymentId INTEGER,
    DatePlaced DATETIME NOT NULL,  -- Changed from TEXT to DATETIME
    OrderStatus VARCHAR(50),
    FOREIGN KEY (UserId) REFERENCES User(UserId) ON DELETE SET NULL,
    FOREIGN KEY (PaymentId) REFERENCES Payment(PaymentId),
    FOREIGN KEY (CartId) REFERENCES Cart(CartId),
    CHECK (OrderStatus IN ('PLACED', 'CANCELLED', 'PROCESSING', 'COMPLETE', 'SAVED')) 
);

CREATE TABLE Delivery (
    DeliveryId INTEGER PRIMARY KEY,
    OrderId INTEGER NOT NULL,
    SourceAddressId INTEGER,
    DestinationAddressId INTEGER,
    Courier VARCHAR(30) NOT NULL,
    CourierDeliveryId INTEGER NOT NULL,
    FOREIGN KEY (OrderId) REFERENCES `Order`(OrderId) ON DELETE CASCADE,
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
    PaymentId INTEGER PRIMARY KEY AUTOINCREMENT,
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
    AccessLogId INTEGER PRIMARY KEY AUTOINCREMENT,
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