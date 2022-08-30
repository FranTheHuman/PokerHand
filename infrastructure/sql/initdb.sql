DROP TABLE IF EXISTS "games";

CREATE TABLE IF NOT EXISTS "games" (
  id BIGSERIAL PRIMARY KEY,
  poker_type VARCHAR(255) NOT NULL,
  result VARCHAR(255) NOT NULL,
  played_at DATE DEFAULT NULL
);