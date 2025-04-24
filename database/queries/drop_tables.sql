PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS ApplicationAccessLog;
DROP TABLE IF EXISTS "Order";
DROP TABLE IF EXISTS Delivery;
DROP TABLE IF EXISTS Payment;
DROP TABLE IF EXISTS Card;
DROP TABLE IF EXISTS Cart;
DROP TABLE IF EXISTS ProductListEntry;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Staff;
DROP TABLE IF EXISTS Address;
DROP TABLE IF EXISTS User;

PRAGMA foreign_keys = ON;

CREATE TABLE User (
    UserId INT PRIMARY KEY,
    FirstName VARCHAR(30),
    LastName VARCHAR(30),
    Email VARCHAR(50) NOT NULL,
    Phone VARCHAR(17),
    Password VARCHAR(20)
);

CREATE TABLE Staff (
    UserId INT PRIMARY KEY,
    StaffCardId INT NOT NULL,
    FOREIGN KEY (UserId) REFERENCES User(UserId)
);

CREATE TABLE Customer (
    UserId INT PRIMARY KEY,
    CartId INT,
    FOREIGN KEY (UserId) REFERENCES User(UserId),
    FOREIGN KEY (CartId) REFERENCES Cart(CartId)
);

CREATE TABLE Product (
    ProductId INT PRIMARY KEY,
    Name VARCHAR(30) NOT NULL,
    Description VARCHAR(30),
    Cost DECIMAL(10, 2) NOT NULL,
    Stock INT NOT NULL
);

CREATE TABLE ProductListEntry (
    ProductListId INT,
    ProductId INT,
    Quantity INT NOT NULL,
    PRIMARY KEY (ProductListId, ProductId),
    FOREIGN KEY (ProductId) REFERENCES Product(ProductId)
);

CREATE TABLE Cart (
    CartId INT PRIMARY KEY,
    ProductListId INT,
    LastUpdated DATETIME,
    FOREIGN KEY (ProductListId) REFERENCES ProductListEntry(ProductListId)
);

CREATE TABLE `Order` (
    OrderId INT PRIMARY KEY,
    UserId INT,
    ProductListId INT,
    PaymentId INT,
    DeliveryId INT,
    DatePlaced DATETIME NOT NULL,
    FOREIGN KEY (UserId) REFERENCES User(UserId),
    FOREIGN KEY (ProductListId) REFERENCES ProductListEntry(ProductListId),
    FOREIGN KEY (PaymentId) REFERENCES Payment(PaymentId),
    FOREIGN KEY (DeliveryId) REFERENCES Delivery(DeliveryId)
);

CREATE TABLE Delivery (
    DeliveryId INT PRIMARY KEY,
    SourceAddressId INT,
    DestinationAddressId INT,
    Courier VARCHAR(30) NOT NULL,
    CourierDeliveryId INT NOT NULL,
    FOREIGN KEY (SourceAddressId) REFERENCES Address(AddressId),
    FOREIGN KEY (DestinationAddressId) REFERENCES Address(AddressId)
);

CREATE TABLE Address (
    AddressId INT PRIMARY KEY,
    StreetNumber VARCHAR(5),
    Street VARCHAR(30) NOT NULL,
    Suburb VARCHAR(30) NOT NULL,
    State INT NOT NULL, 
    Postcode INT NOT NULL
);

CREATE TABLE Payment (
    PaymentId INT PRIMARY KEY,
    CardId INT,
    Amount DECIMAL(10, 2) NOT NULL,
    PaymentStatus INT NOT NULL,
    FOREIGN KEY (CardId) REFERENCES Card(CardId)
);

CREATE TABLE Card (
    CardId INT PRIMARY KEY,
    Name VARCHAR(30),
    Number VARCHAR(19) NOT NULL,
    Expiry DATE NOT NULL,
    CVC VARCHAR(3) NOT NULL
);

CREATE TABLE ApplicationAccessLog (
    AppAccLogId INT PRIMARY KEY,
    UserId INT,
    ApplicationAction INT NOT NULL,
    DateTime DATETIME NOT NULL,
    FOREIGN KEY (UserId) REFERENCES User(UserId)
);