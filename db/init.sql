CREATE DATABASE IF NOT EXISTS e199;
USE e199;

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER NOT NULL AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(32) NOT NULL,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    birthdate DATE NOT NULL,
    gender VARCHAR(7) NOT NULL,
    afm VARCHAR(10) NOT NULL,
    country VARCHAR(30) NOT NULL,
    address VARCHAR(100) NOT NULL,
    municipality VARCHAR(50) NOT NULL,
    prefecture VARCHAR(15) NOT NULL,
    job VARCHAR(200) NOT NULL,
    telephone VARCHAR(14) NOT NULL UNIQUE,
    lat DOUBLE,
    lon DOUBLE,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS volunteers (
    volunteer_id INTEGER NOT NULL AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(32) NOT NULL,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    birthdate DATE NOT NULL,
    gender VARCHAR(7) NOT NULL,
    afm VARCHAR(10) NOT NULL,
    country VARCHAR(30) NOT NULL,
    address VARCHAR(100) NOT NULL,
    municipality VARCHAR(50) NOT NULL,
    prefecture VARCHAR(15) NOT NULL,
    job VARCHAR(200) NOT NULL,
    telephone VARCHAR(14) NOT NULL UNIQUE,
    lat DOUBLE,
    lon DOUBLE,
    volunteer_type VARCHAR(50) NOT NULL,
    height DOUBLE,
    weight DOUBLE,
    PRIMARY KEY (volunteer_id)
);

CREATE TABLE IF NOT EXISTS incidents (
    incident_id INTEGER NOT NULL AUTO_INCREMENT,
    incident_type VARCHAR(10) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    user_phone VARCHAR(14) NOT NULL,
    user_type VARCHAR(10) NOT NULL,
    address VARCHAR(100) NOT NULL,
    lat DOUBLE,
    lon DOUBLE,
    municipality VARCHAR(50),
    prefecture VARCHAR(15),
    start_datetime DATETIME NOT NULL,
    end_datetime DATETIME DEFAULT NULL,
    danger VARCHAR(15),
    status VARCHAR(15),
    finalResult VARCHAR(200),
    vehicles INTEGER,
    firemen INTEGER,
    PRIMARY KEY (incident_id)
);

CREATE TABLE IF NOT EXISTS participants (
    participant_id INTEGER NOT NULL AUTO_INCREMENT,
    incident_id INTEGER NOT NULL,
    volunteer_username VARCHAR(30),
    volunteer_type VARCHAR(50) NOT NULL,
    status VARCHAR(15) NOT NULL,
    success VARCHAR(10),
    comment VARCHAR(300),
    FOREIGN KEY (incident_id) REFERENCES incidents(incident_id),
    PRIMARY KEY (participant_id)
);

CREATE TABLE IF NOT EXISTS messages (
    message_id INTEGER NOT NULL AUTO_INCREMENT,
    incident_id INTEGER NOT NULL,
    message VARCHAR(400) NOT NULL,
    sender VARCHAR(50) NOT NULL,
    recipient VARCHAR(50) NOT NULL,
    date_time DATETIME NOT NULL,
    FOREIGN KEY (incident_id) REFERENCES incidents(incident_id),
    PRIMARY KEY (message_id)
);
