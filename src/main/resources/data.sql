TRUNCATE TABLE items, bookings, comments, requests, users RESTART IDENTITY CASCADE;

ALTER SEQUENCE items_id_seq RESTART WITH 1;
ALTER SEQUENCE bookings_id_seq RESTART WITH 1;
ALTER SEQUENCE comments_id_seq RESTART WITH 1;
ALTER SEQUENCE requests_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;