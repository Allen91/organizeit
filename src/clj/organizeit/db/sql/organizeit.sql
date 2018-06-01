-- :name get-stores :? :*
SELECT store_id, store_name
FROM stores
WHERE group_id = :group_id

-- :name update-mailbox :!
UPDATE utilities
SET mailbox = :mailbox
WHERE group_id = :group_id

-- :name get-mailbox :? :1
SELECT mailbox
FROM utilities
WHERE group_id = :group_id

-- :name update-rent :!
UPDATE utilities
SET rent = :rent
WHERE group_id = :group_id

-- :name get-rent :? :1
SELECT rent
FROM utilities
WHERE group_id = :group_id

-- :name update-electricity :!
UPDATE utilities
SET electricity = :electricity
WHERE group_id = :group_id

-- :name get-electricity :? :1
SELECT electricity
FROM utilities
WHERE group_id = :group_id

-- :name update-internet :!
UPDATE utilities
SET internet = :internet
WHERE group_id = :group_id

-- :name get-internet :? :1
SELECT internet
FROM utilities
WHERE group_id = :group_id

-- :name delete-store :!
DELETE
FROM stores
WHERE store_id = :store_id

-- :name insert-store :!
INSERT INTO stores (store_name, group_id)
VALUES (:store_name, :group_id)

-- :name insert-item :i!
INSERT INTO items (item_name)
VALUES (:item_name)

-- :name insert-transaction :!
INSERT INTO transactions (store_id, item_id, bought)
VALUES (:store_id, :item_id, 'false')

-- :name update-transaction :!
UPDATE transactions
SET bought = :bought
WHERE store_id = :store_id
AND item_id = :item_id

-- :name update-transactions-store :!
UPDATE transactions
SET bought = 'true'
WHERE store_id = :store_id

-- :name delete-transactions-store :!
DELETE
FROM transactions
WHERE store_id = :store_id
AND bought = 'true'

-- :name get-groceries :? :*
SELECT stores.store_id AS store_id, transactions.item_id AS item_id, item_name, bought
FROM stores
LEFT OUTER JOIN transactions
ON transactions.store_id = stores.store_id
LEFT OUTER JOIN items
ON items.item_id = transactions.item_id
WHERE stores.group_id = :group_id;

-- :name get-store-groceries :? :*
SELECT stores.store_id AS store_id, transactions.item_id AS item_id, item_name, bought
FROM stores
LEFT OUTER JOIN transactions
ON transactions.store_id = stores.store_id
LEFT OUTER JOIN items
ON items.item_id = transactions.item_id
WHERE stores.group_id = :group_id
AND transactions.store_id = :store_id;