CREATE TABLE participant (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE tag (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE meeting (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    date TEXT NOT NULL
);

CREATE TABLE replica (
    meeting_id INTEGER,
    order_number INTEGER,
    participant_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    PRIMARY KEY (meeting_id, order_number),
    FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE,
    FOREIGN KEY (participant_id) REFERENCES participant(id) ON DELETE CASCADE
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

CREATE TABLE participant_tag (
    participant_id INTEGER,
    tag_id INTEGER,
    PRIMARY KEY (participant_id, tag_id),
    FOREIGN KEY (participant_id) REFERENCES participant(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);
