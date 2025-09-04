-- 币种初始化数据
INSERT INTO currency (code, name, symbol, exchange_rate, is_active, created_at, updated_at) VALUES
('USD', '美元', '$', 1.0000, true, NOW(), NOW()),
('EUR', '欧元', '€', 0.8500, true, NOW(), NOW()),
('CNY', '人民币', '¥', 7.2000, true, NOW(), NOW()),
('GBP', '英镑', '£', 0.7300, true, NOW(), NOW()),
('JPY', '日元', '¥', 110.0000, true, NOW(), NOW()),
('AUD', '澳元', 'A$', 1.3500, true, NOW(), NOW()),
('CAD', '加元', 'C$', 1.2500, true, NOW(), NOW()),
('CHF', '瑞士法郎', 'CHF', 0.9200, true, NOW(), NOW());