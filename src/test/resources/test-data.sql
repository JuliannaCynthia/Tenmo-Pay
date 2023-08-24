BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, user_friends, transfer;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance numeric(13, 2) NOT NULL DEFAULT 1000,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

CREATE SEQUENCE seq_transfer_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;

CREATE TABLE transfer(
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	user_transfer_to varchar(50) NOT NULL,
	user_transfer_from varchar(50) NOT NULL,
	transfer_amount numeric(13,2) NOT NULL,
	created_at timestamp NOT NULL DEFAULT NOW(),
	is_pending boolean NOT NULL,
	is_approved boolean NOT NULL,
	CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT FK_user_to FOREIGN KEY(user_transfer_to) references tenmo_user(username),
	CONSTRAINT FK_user_from FOREIGN KEY(user_transfer_from) references tenmo_user(username),
	CONSTRAINT CHK_transfer_amount CHECK(transfer_amount > 0)
);

CREATE TABLE user_friends(
	user_id_request int NOT NULL,
	user_id_receive int NOT NULL,
	approved boolean NOT NULL,
	CONSTRAINT PK_userA_userB PRIMARY KEY (user_id_request,user_id_receive),
	CONSTRAINT FK_user_request FOREIGN KEY(user_id_request) references tenmo_user(user_id),
	CONSTRAINT FK_user_receive FOREIGN KEY(user_id_receive) references tenmo_user(user_id)
);



INSERT INTO tenmo_user (username, password_hash)
VALUES ('bob', '$2a$10$ZO4waJUrDU8vgYJ/6.95jeluZ.OZRre9DKNovzZq2J.wVDIPjDfOW'),
       ('user', '$2a$10$vChdCD40NaW324TbMdx88.NCWBPd9Sj5Wwe3SY8mX49RLmZb.7BZC');

INSERT INTO account (user_id) VALUES (1001);
INSERT INTO account (user_id) VALUES (1002);

INSERT INTO transfer (user_transfer_to, user_transfer_from, transfer_amount, is_pending, is_approved)
VALUES ('bob', 'user', 100, false, true),
       ('user', 'bob', 100, true, false);
COMMIT;