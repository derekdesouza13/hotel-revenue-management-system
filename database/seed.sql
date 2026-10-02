INSERT INTO hotels (
    name,
    location,
    total_rooms,
    created_at
)
SELECT
    'Grand Pune Hotel',
    'Pune',
    100,
    NOW()
WHERE NOT EXISTS (
    SELECT 1
    FROM hotels
    WHERE name = 'Grand Pune Hotel'
);


INSERT INTO hotels (
    name,
    location,
    total_rooms,
    created_at
)
SELECT
    'Mumbai Business Hotel',
    'Mumbai',
    150,
    NOW()
WHERE NOT EXISTS (
    SELECT 1
    FROM hotels
    WHERE name = 'Mumbai Business Hotel'
);