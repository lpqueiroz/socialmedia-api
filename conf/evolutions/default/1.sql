-- !Ups

create table t_users(
    id SERIAL PRIMARY KEY,
    name VARCHAR(300) NOT NULL,
    email VARCHAR(300) NOT NULL
);

create table t_posts
(
     id SERIAL PRIMARY KEY,
     text TEXT NOT NULL,
     image VARCHAR(300) NOT NULL,
     created_at TIMESTAMP NOT NULL,
     created_by BIGINT,
     CONSTRAINT t_posts_created_by_fk FOREIGN KEY (created_by) REFERENCES t_users(id)
 );

create table t_comments(
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by BIGINT,
    post_id BIGINT,
    CONSTRAINT t_comments_created_by_fk FOREIGN KEY (created_by) REFERENCES t_users(id),
    CONSTRAINT t_posts_id_fk FOREIGN KEY (post_id) REFERENCES t_posts(id)
);


-- !Downs

--ALTER TABLE t_posts DROP CONSTRAINT t_posts_created_by_fk
--ALTER TABLE t_comments DROP CONSTRAINT t_comments_created_by_fk
DROP TABLE t_users;
DROP TABLE t_posts;
DROP TABLE t_comments;