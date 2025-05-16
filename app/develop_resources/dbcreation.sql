CREATE TABLE speaker (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         name TEXT NOT NULL,
                         image blob
);

CREATE TABLE tag (
                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                     name TEXT NOT NULL
);

CREATE TABLE transcript (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name VARCHAR(100) UNIQUE,
                            date TEXT NOT NULL
);

CREATE TABLE replica (
                         transcript_id INTEGER,
                         order_number INTEGER,
                         speaker_id INTEGER NOT NULL,
                         content TEXT NOT NULL,
                         PRIMARY KEY (transcript_id, order_number),
                         FOREIGN KEY (transcript_id) REFERENCES transcript(id) ON DELETE CASCADE,
                         FOREIGN KEY (speaker_id) REFERENCES speaker(id) ON DELETE CASCADE
);



CREATE TABLE protocol (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          transcript_id INTEGER UNIQUE,
                          conclusion TEXT,
                          FOREIGN KEY (transcript_id) REFERENCES transcript(id)
);

CREATE TABLE task (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      description TEXT NOT NULL,
                      transcript_id INTEGER,
                      FOREIGN KEY (transcript_id) REFERENCES transcript(id) ON DELETE CASCADE
);

CREATE TABLE transcript_tag (
                                 transcript_id INTEGER,
                                 tag_id INTEGER,
                                 PRIMARY KEY (transcript_id, tag_id),
                                 FOREIGN KEY (transcript_id) REFERENCES transcript(id) ON DELETE CASCADE,
                                 FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);
