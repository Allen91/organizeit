CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER NOT NULL,
    username VARCHAR(64) NOT NULL,
    phone VARCHAR(16) NOT NULL,
    email VARCHAR(64) NOT NULL,
    PRIMARY KEY (user_id)
    );
--;;
CREATE TABLE IF NOT EXISTS groups (
    group_id INTEGER NOT NULL,
    group_name VARCHAR(64) NOT NULL,
    link VARCHAR(16),
    PRIMARY KEY (group_id)
    );
--;;
CREATE TABLE IF NOT EXISTS stores (
    store_id INTEGER NOT NULL,
    store_name VARCHAR(64) NOT NULL,
    PRIMARY KEY (store_id)
    );
--;;
CREATE TABLE IF NOT EXISTS items (
    item_id INTEGER NOT NULL,
    item_name VARCHAR(64) NOT NULL,
    PRIMARY KEY (item_id)
    );
--;;
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    group_id INTEGER NOT NULL,
    PRIMARY KEY (enrollment_id),
    CONSTRAINT fk_enroll_users
        FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION,
    CONSTRAINT fk_enroll_groups
        FOREIGN KEY (group_id)
        REFERENCES groups (group_id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
    );
)
--;;
CREATE TABLE IF NOT EXISTS transactions (
    store_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    group_id INTEGER NOT NULL,
    bought VARCHAR(8) NOT NULL,
    PRIMARY KEY (store_id, item_id, group_id),
    CONSTRAINT fk_trans_stores
        FOREIGN KEY (store_id)
        REFERENCES stores (store_id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION,
    CONSTRAINT fk_trans_items
        FOREIGN KEY (item_id)
        REFERENCES items (item_id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION,
    CONSTRAINT fk_trans_groups
        FOREIGN KEY (group_id)
        REFERENCES groups (group_id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
    );
)
--;;
CREATE TABLE IF NOT EXISTS utilities (
    utilities_id INTEGER NOT NULL,
    group_id INTEGER NOT NULL,
    rent TIMESTAMP,
    electricity TIMESTAMP,
    internet TIMESTAMP,
    mailbox TIMESTAMP,
    PRIMARY KEY (utilities_id),
    CONSTRAINT fk_utilities_groups
        FOREIGN KEY (group_id)
        REFERENCES groups (group_id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
    );
--;;