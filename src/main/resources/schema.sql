CREATE TABLE IF NOT EXISTS file_info (
  id INT IDENTITY PRIMARY KEY,
  filename VARCHAR(500) NOT NULL,
  path VARCHAR(500) NOT NULL,
  creation_time VARCHAR(100),
  last_access_time VARCHAR(100),
  last_modified_time VARCHAR(100),
  is_directory INT,
  size INT,
  hash VARCHAR(64),
  status VARCHAR(20)
);

-- CREATE CONSTRAINT unique_file_info_path_filename
-- ON file_info(path, filename);
CREATE UNIQUE INDEX unique_index_file_info_path_filename
ON file_info(path, filename);