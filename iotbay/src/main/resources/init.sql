-- Clear existing entries for CartId 1
DELETE FROM ProductListEntry WHERE CartId = 1;

-- Insert the two expected products
INSERT INTO ProductListEntry (CartId, ProductId, Quantity) VALUES
(1, (SELECT ProductId FROM Product WHERE Name = 'Google Home Voice Controller'), 1),
(1, (SELECT ProductId FROM Product WHERE Name = 'Philips Hue Smart Bulbs'), 1); 