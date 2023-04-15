DELETE FROM delivery_record_entity;
ALTER TABLE delivery_record_entity SET REFERENTIAL_INTEGRITY FALSE;
INSERT INTO delivery_record_entity (delivery_record_id, last_modified_at, value, delivery_id, courier_id) VALUES ( 'ce317eaa-eb0f-4765-922e-04e7d49149c9', '2023-04-15 22:07:54', 16.21, 'b1ab2bb5-c23c-4860-b206-0154045a1139', 'courier' ), ( 'ce317eaa-eb0f-4765-922e-04e7d49249c9', '2023-04-15 23:09:54', 14.11, 'b1ab2bb5-c23c-4860-b206-1144045a1139', 'courier' );
