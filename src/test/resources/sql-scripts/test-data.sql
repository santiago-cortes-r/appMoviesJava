CREATE TABLE IF NOT EXISTS movies(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    minutes INT NOT NULL,
    genre VARCHAR(50) NOT NULL,
    director VARCHAR(50) NOT NULL
);

INSERT INTO movies (name, minutes, genre, director) VALUES
    ('Dark Knight', 152, 'ACTION', 'carlos'),
    ('Memento', 113, 'THRILLER', 'carlos'),
    ('Matrix', 136, 'ACTION', 'santiago')