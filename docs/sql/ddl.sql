-- 사용자 테이블
CREATE TABLE `USER`
(
    USER_ID          BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_NAME        VARCHAR(50)  NOT NULL UNIQUE,
    EMAIL            VARCHAR(100) NOT NULL UNIQUE,
    PASSWORD         VARCHAR(255) NULL,
    AUTH_PROVIDER    VARCHAR(50)  NULL,
    AUTH_PROVIDER_ID VARCHAR(100) NULL,
    CREATED_AT       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 깃허브 레포지토리 테이블
CREATE TABLE REPOSITORY
(
    REPO_ID     BIGINT PRIMARY KEY AUTO_INCREMENT,
    REPO_URL    VARCHAR(255) NOT NULL UNIQUE,
    NAME        VARCHAR(100) NOT NULL,
    DESCRIPTION TEXT,
    STAR_COUNT  INT       DEFAULT 0,
    FORK_COUNT  INT       DEFAULT 0,
    CREATED_AT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 게시글 테이블
CREATE TABLE POST
(
    POST_ID    BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_ID    BIGINT       NOT NULL,
    REPO_ID    BIGINT       NULL,
    TITLE      VARCHAR(255) NOT NULL,
    CONTENT    TEXT         NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (REPO_ID) REFERENCES REPOSITORY (REPO_ID) ON DELETE SET NULL
);

-- 태그 테이블
CREATE TABLE TAG
(
    TAG_ID     BIGINT PRIMARY KEY AUTO_INCREMENT,
    TAG_NAME   VARCHAR(50) NOT NULL UNIQUE,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 게시글-태그 매핑 테이블
CREATE TABLE POST_TAG
(
    POST_TAG_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    POST_ID     BIGINT NOT NULL,
    TAG_ID      BIGINT NOT NULL,
    CREATED_AT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (POST_ID) REFERENCES POST (POST_ID) ON DELETE CASCADE,
    FOREIGN KEY (TAG_ID) REFERENCES TAG (TAG_ID) ON DELETE CASCADE,
    UNIQUE (POST_ID, TAG_ID)
);

-- 사용자 관심 태그 테이블
CREATE TABLE USER_INTEREST
(
    INTEREST_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_ID     BIGINT NOT NULL,
    TAG_ID      BIGINT NOT NULL,
    CREATED_AT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (TAG_ID) REFERENCES TAG (TAG_ID) ON DELETE CASCADE,
    UNIQUE (USER_ID, TAG_ID)
);

-- 댓글 테이블
CREATE TABLE COMMENT
(
    COMMENT_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    POST_ID    BIGINT NOT NULL,
    USER_ID    BIGINT NOT NULL,
    CONTENT    TEXT   NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (POST_ID) REFERENCES POST (POST_ID) ON DELETE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE
);

-- 북마크 테이블
CREATE TABLE BOOKMARK
(
    BOOKMARK_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_ID     BIGINT NOT NULL,
    POST_ID     BIGINT NOT NULL,
    CREATED_AT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (POST_ID) REFERENCES POST (POST_ID) ON DELETE CASCADE,
    UNIQUE (USER_ID, POST_ID)
);

-- 게시글 좋아요 테이블
CREATE TABLE USER_LIKE_POST
(
    LIKE_ID    BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_ID    BIGINT NOT NULL,
    POST_ID    BIGINT NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (POST_ID) REFERENCES POST (POST_ID) ON DELETE CASCADE,
    UNIQUE (USER_ID, POST_ID)
);

-- 댓글 좋아요 테이블
CREATE TABLE USER_LIKE_COMMENT
(
    LIKE_ID    BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_ID    BIGINT NOT NULL,
    COMMENT_ID BIGINT NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (USER_ID) REFERENCES USER (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (COMMENT_ID) REFERENCES COMMENT (COMMENT_ID) ON DELETE CASCADE,
    UNIQUE (USER_ID, COMMENT_ID)
);

-- 인기 개발자 테이블
CREATE TABLE DEVELOPER
(
    DEVELOPER_ID     BIGINT PRIMARY KEY AUTO_INCREMENT,
    GITHUB_USER_NAME VARCHAR(50) NOT NULL UNIQUE,
    TOTAL_STARS      INT       DEFAULT 0,
    TOTAL_FORKS      INT       DEFAULT 0,
    LAST_UPDATED_AT  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
