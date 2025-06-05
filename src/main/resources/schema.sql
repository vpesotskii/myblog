CREATE TABLE posts (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       image_path VARCHAR(500),
                       text CLOB,
                       likes_count BIGINT DEFAULT 0,
                       tags VARCHAR(1000)     -- напр. "java,spring,blog"
);

CREATE TABLE comments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          post_id BIGINT NOT NULL,
                          text CLOB NOT NULL,
                          FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

INSERT INTO posts (title, image_path, text, likes_count, tags)
VALUES
    (
        'First Post',
        '/images/first.jpg',
        'First post text',
        5,
        'java,spring,blog'
    ),
    (
        'Second Post',
        '/images/second.jpg',
        'Second Post text',
        10,
        'web,frontend'
    ),
    (
        'Third Post',
        '/images/third.jpg',
        'content...',
        2,
        'news,tech'
    );

INSERT INTO comments (post_id, text) VALUES (1, 'Great post! Really enjoyed it.');
INSERT INTO comments (post_id, text) VALUES (1, 'Thanks for sharing this information.');

INSERT INTO comments (post_id, text) VALUES (2, 'Interesting perspective, I had not considered that.');
INSERT INTO comments (post_id, text) VALUES (2, 'Could you explain more about the second point?');

INSERT INTO comments (post_id, text) VALUES (3, 'This is very helpful, thank you!');
INSERT INTO comments (post_id, text) VALUES (3, 'Looking forward to your next post.');