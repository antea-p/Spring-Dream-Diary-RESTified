DROP SCHEMA IF EXISTS dream_diary CASCADE;
CREATE SCHEMA dream_diary;

CREATE TABLE dream_diary.users
(
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(150) NOT NULL,
    is_admin BOOLEAN NOT NULL
);

INSERT INTO dream_diary.users (user_id, username, password, is_admin)
VALUES (1, 'omori', '$2a$12$hJqGL7dZIgEecr8lamjVkOM6fKSvUFWAup8KZhHD0X5ppIzNcsqXK', false),
       (2, 'sunny', '$2a$12$hJqGL7dZIgEecr8lamjVkOM6fKSvUFWAup8KZhHD0X5ppIzNcsqXK', true);

CREATE TABLE dream_diary.diary_entries (
    entry_id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    created_date DATE NOT NULL,
    user_id INTEGER REFERENCES dream_diary.users(user_id) ON DELETE CASCADE
);

INSERT INTO dream_diary.diary_entries (title, content, user_id, created_date)
VALUES
('Welcome to White Space', 'I''ve been here as long as I can remember. It''s peaceful, yet somehow unsettling. The blank canvas of this space invites introspection, but also a lingering sense of isolation. Mewo is here. There''s a laptop, a tissue box, and my sketchbook. Everything is as it should be.', 1, '2024-05-01'),
('Picnic with Friends in Headspace', 'Had a picnic with Aubrey, Kel, and Hero today in the vast fields of Headspace. The sun was shining, and we shared watermelon slices. For a moment, everything felt perfect. But why does perfection feel so fragile?', 1, '2024-05-02'),
('Shadows in Faraway Town', 'Walked through Faraway Town today. The streets feel different, shadows longer. Passed by Mari''s piano. The melody it once played echoes in my mind, but I can''t quite grasp it. Why does everything feel so distant?', 1, '2024-05-03'),
('Basil''s Photo Album',
 'Looked through Basil''s photo album today. So many memories, frozen in time. But there''s one photo that''s missing. Why can''t I remember what it was? Basil seemed nervous when I asked about it.', 1, '2024-05-04'),
('Class Trial Revelation', 'As the truth unveils, the weight of every decision bears down on me.', 2, '2024-05-05'),
('The Space Between Dreams', 'Floating in an endless void where thoughts and reality blur.', 1, '2024-05-06'),
('Puzzle of Infinite Doors', 'Each door I open leads to another puzzle, and the clock is ticking...', 2, '2024-05-07'),
('Silent School Hallways', 'The emptiness of the school at night makes every sound echo infinitely...', 2, '2024-05-08'),
('Finding a Hidden Room', 'Behind the bookshelf, there was a hidden door to a room filled with secrets...', 2, '2024-05-09'),
('Nightmare with No End', 'Running endlessly through dark corridors, the exit always just out of reach...', 2, '2024-05-10'),
('Revelations in a Dream', 'In the dream, I discovered a truth that shattered my perception of reality...', 2, '2024-05-11');

CREATE TABLE dream_diary.tags (
    tag_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    entry_id INTEGER REFERENCES dream_diary.diary_entries(entry_id) ON DELETE CASCADE
);

INSERT INTO dream_diary.tags (name, entry_id)
VALUES
('White Space', 1),
('Neighbor''s Room', 2),
('Picnic', 3),
('Mystery', 4),
('Shadows', 4),
('Trial', 5),
('Revelation', 5),
('Space', 6),
('Dreams', 6);

CREATE TABLE dream_diary.emotions (
    emotion_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO dream_diary.emotions (name)
VALUES
('Nostalgia'),
('Wonder'),
('Joy'),
('Fear'),
('Curiosity'),
('Shock'),
('Determination'),
('Loneliness'),
('Hope');

CREATE TABLE dream_diary.diary_entry_emotions (
    entry_emotion_id SERIAL PRIMARY KEY,
    entry_id INTEGER REFERENCES dream_diary.diary_entries(entry_id) ON DELETE CASCADE,
    emotion_id INTEGER REFERENCES dream_diary.emotions(emotion_id) ON DELETE CASCADE,
    UNIQUE (entry_id, emotion_id)
);

INSERT INTO dream_diary.diary_entry_emotions (entry_id, emotion_id)
VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(3, 3),
(4, 4),
(4, 5),
(5, 6),
(5, 7),
(6, 8),
(6, 9);


SELECT SETVAL('dream_diary.users_user_id_seq', (SELECT MAX(user_id) FROM dream_diary.users) + 1);
-- TODO: more sequencing resets as needed