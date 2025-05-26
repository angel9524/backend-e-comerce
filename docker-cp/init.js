db = db.getSiblingDB('db_cart');
db.createCollection('carts');

db = db.getSiblingDB('db_orders');
db.createCollection('orders');

db = db.getSiblingDB('db_users');
db.createCollection('users');
