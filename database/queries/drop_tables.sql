PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS ApplicationAccessLog;
DROP TABLE IF EXISTS `Order`;
DROP TABLE IF EXISTS Delivery;
DROP TABLE IF EXISTS Payment;
DROP TABLE IF EXISTS Card;
DROP TABLE IF EXISTS Cart;
DROP TABLE IF EXISTS ProductListEntry;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Shipment;
DROP TABLE IF EXISTS Staff;
DROP TABLE IF EXISTS Address;
DROP TABLE IF EXISTS User;

PRAGMA foreign_keys = ON;