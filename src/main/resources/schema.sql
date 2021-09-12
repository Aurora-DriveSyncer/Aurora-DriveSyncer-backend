CREATE TABLE IF NOT EXISTS file_info (
  id INT IDENTITY PRIMARY KEY,
  filename VARCHAR(500) NOT NULL,
  path VARCHAR(500) NOT NULL,
  creation_time INT,
  last_access_time INT,
  last_modified_time INT,
  is_directory INT,
  size INT,
  hash VARCHAR(64),
  status VARCHAR(20),
  version INT,
);

CREATE INDEX file_info_path_filename
ON file_info(path, filename);