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