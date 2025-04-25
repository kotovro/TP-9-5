CREATE TABLE speaker (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         name TEXT NOT NULL,
                         image blob
);

CREATE TABLE tag (
                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                     name TEXT NOT NULL
);

CREATE TABLE meeting (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         name VARCHAR(100) UNIQUE,
                         date TEXT NOT NULL
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
                          conclusion TEXT,
                          meeting_id INTEGER UNIQUE,
                          FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE
);

CREATE TABLE task (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      description TEXT NOT NULL,
                      conclusion TEXT,
                      protocol_id INTEGER,
                      FOREIGN KEY (protocol_id) REFERENCES protocol(id) ON DELETE CASCADE
);

CREATE TABLE speaker_tag (
                                 speaker_id INTEGER,
                                 tag_id INTEGER,
                                 PRIMARY KEY (speaker_id, tag_id),
                                 FOREIGN KEY (speaker_id) REFERENCES speaker(id) ON DELETE CASCADE,
                                 FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);
