// Инициализация базы данных ERP Inventory
db = db.getSiblingDB('erp_inventory');

// Создание коллекций
db.createCollection('companies');
db.createCollection('users');
db.createCollection('warehouses');
db.createCollection('products');
db.createCollection('stock_change_logs');

// Создание индексов
db.companies.createIndex({ "name": 1 });
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "companyId": 1 });
db.warehouses.createIndex({ "companyId": 1 });
db.products.createIndex({ "companyId": 1 });
db.products.createIndex({ "warehouseId": 1 });
db.stock_change_logs.createIndex({ "productId": 1 });
db.stock_change_logs.createIndex({ "warehouseId": 1 });
db.stock_change_logs.createIndex({ "timestamp": -1 });

print('MongoDB initialization completed'); 