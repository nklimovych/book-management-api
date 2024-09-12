DROP TABLE IF EXISTS books;

CREATE TABLE books (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       publication_year INT NOT NULL,
                       genre VARCHAR(100),
                       isbn VARCHAR(17) NOT NULL UNIQUE
);

INSERT INTO books (id, title, author, publication_year, genre, isbn)
VALUES
    (1, 'Kobzar', 'Taras Shevchenko', '1840', 'Poetry', '978-1-1516-4732-0'),
    (2, 'Earth', 'Olha Kobylianska', '1902', 'Novel', '978-7-3664-5711-2'),
    (3, 'Tiger hunters', 'Ivan Bahriany', '1944', 'Novel', '978-8-9176-0894-6');