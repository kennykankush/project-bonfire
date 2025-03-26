CREATE DATABASE IF NOT EXISTS bonfire;

USE bonfire;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(60) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    privacy_setting ENUM('PRIVATE', 'PUBLIC'),
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS social_circle (
    id BIGINT AUTO_INCREMENT,
    user_id BIGINT,
    following_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20), 
    CONSTAINT pk_follows PRIMARY KEY(id),
    CONSTRAINT fk_follows FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_follows_following FOREIGN KEY (following_id) REFERENCES users(user_id) ON DELETE CASCADE
);
