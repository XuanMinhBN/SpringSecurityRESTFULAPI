CREATE TABLE IF NOT EXISTS users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id CHARACTER VARYING(255) NOT NULL,
    first_name CHARACTER VARYING(255),
    last_name CHARACTER VARYING(255),
    email CHARACTER VARYING(255),
    phone CHARACTER VARYING(255),
    bio CHARACTER VARYING(255),
    reference_id CHARACTER VARYING(255),
    qr_code_secret CHARACTER VARYING(255),
    qr_code_image_uri LONGTEXT,
    image_url CHARACTER VARYING(255),
    last_login TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    login_attempts INTEGER DEFAULT 0,
    mfa BOOLEAN NOT NULL DEFAULT FALSE,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    account_non_expired BOOLEAN NOT NULL DEFAULT FALSE,
    account_non_locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT uq_users_user_id UNIQUE (user_id),
    CONSTRAINT fk_users_created_by FOREIGN KEY (created_by) REFERENCES users(id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE ,
    CONSTRAINT fk_users_updated_by FOREIGN KEY (updated_by) REFERENCES users(id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS confirmation(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    theKey CHARACTER VARYING(255),
    user_id BIGINT NOT NULL,
    reference_id CHARACTER VARYING(255),
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT uq_confirmations_user_id UNIQUE (user_id),
    CONSTRAINT uq_confirmations_key UNIQUE (theKey),
    CONSTRAINT fk_confirmations_user_id FOREIGN KEY (user_id) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_confirmations_created_by FOREIGN KEY (created_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_confirmations_updated_by FOREIGN KEY (updated_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS credentials(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    password CHARACTER VARYING(255) NOT NULL,
    reference_id CHARACTER VARYING(255),
    user_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT uq_credentials_user_id UNIQUE (user_id),
    CONSTRAINT fk_credentials_user_id FOREIGN KEY (user_id) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_credentials_created_by FOREIGN KEY (created_by) REFERENCES users(id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE ,
    CONSTRAINT fk_credentials_updated_by FOREIGN KEY (updated_by) REFERENCES users(id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS documents(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id CHARACTER VARYING(255) NOT NULL,
    extension CHARACTER VARYING(10),
    formatted_size CHARACTER VARYING(255),
    icon CHARACTER VARYING(255),
    name CHARACTER VARYING(255),
    size BIGINT NOT NULL,
    uri CHARACTER VARYING(255),
    description CHARACTER VARYING(255),
    reference_id CHARACTER VARYING(255),
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT uq_documents_document_id UNIQUE (document_id),
    CONSTRAINT fk_documents_created_id FOREIGN KEY (created_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_documents_updated_by FOREIGN KEY (created_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS roles(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    authority CHARACTER VARYING(255),
    name CHARACTER VARYING(255),
    reference_id CHARACTER VARYING(255),
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_roles_created_by FOREIGN KEY (created_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_roles_updated_by FOREIGN KEY (updated_by) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS user_roles(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE INDEX index_users_email ON users (email);

CREATE INDEX index_users_user_id ON users (user_id);

CREATE INDEX index_confirmations_user_id ON confirmation (user_id);

CREATE INDEX index_credentials_user_id ON credentials (user_id);

CREATE INDEX index_user_roles_user_id ON user_roles (user_id);







































