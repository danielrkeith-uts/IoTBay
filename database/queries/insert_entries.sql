-- Example to generate 10 users
INSERT INTO User (UserId, FirstName, LastName, Email, Phone, Password)
SELECT
    10000 + seq AS UserId,
    CONCAT('First', seq) AS FirstName,
    CONCAT('Last', seq) AS LastName,
    CONCAT('user', seq, '@example.com') AS Email,
    CONCAT('0412', LPAD(seq, 6, '0')) AS Phone,
    'hashedpassword' AS Password
FROM (
    SELECT 1 AS seq UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
    UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
) AS numbers;
