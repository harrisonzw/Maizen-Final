CREATE TABLE USERS
(
  user_id         VARCHAR(40) NOT NULL,
  first_name      VARCHAR(20) NOT NULL,
  last_name       VARCHAR(20) NOT NULL,
  month_of_birth  INTEGER NOT NULL,
  day_of_birth    INTEGER NOT NULL,
  year_of_birth   INTEGER NOT NULL,
  gender          VARCHAR(20),
  weight_pounds   INTEGER NOT NULL,
  height_feet     INTEGER NOT NULL,
  height_inches   INTEGER NOT NULL,
  current_challenges INTEGER NOT NULL,
  PRIMARY KEY (user_id),
  CONSTRAINT CHK_month_of_birth   CHECK (month_of_birth > 0 AND month_of_birth <= 12),
  CONSTRAINT CHK_day_of_birth     CHECK (day_of_birth > 0 AND day_of_birth <= 31),
  CONSTRAINT CHK_year_of_birth    CHECK (year_of_birth > 1900 AND year_of_birth <= 2019),
  CONSTRAINT CHK_gender           CHECK (gender IN ('male', 'female', 'nonbinary')),
  CONSTRAINT CHK_weight_pounds    CHECK (weight_pounds > 0 AND weight_pounds <= 1000),
  CONSTRAINT CHK_height_feet      CHECK (height_feet > 0 AND height_feet <= 10),
  CONSTRAINT CHK_height_inches    CHECK (height_inches > 0 AND height_inches <= 12)
);

CREATE TABLE USER_ACTIVITIES
(
  user_id           VARCHAR(40) NOT NULL,
  first_name      VARCHAR(20) NOT NULL,
  last_name       VARCHAR(20) NOT NULL,
  activity        VARCHAR(20) NOT NULL,
  steps             INTEGER NOT NULL,
  duration_in_min   INTEGER NOT NULL,
  calories_burned   INTEGER NOT NULL,
  time_of_day       VARCHAR(20) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES Users(user_id),
  CONSTRAINT CHK_activity         CHECK (activity in ('walking', 'running', 'swimming', 'basketball', 'soccer', 
                                                        'tennis', 'cycling', 'ping pong', 'golf', 'gymnastics', 
                                                        'handball', 'lacrosse', 'racquetball', 'skateboading')),
  CONSTRAINT CHK_duration_in_min      CHECK (duration_in_min > 0 AND duration_in_min < 300),
  CONSTRAINT CHK_calories_burned      CHECK (calories_burned > 0 AND calories_burned < 5000),
  CONSTRAINT CHK_activity_time_of_day      CHECK (time_of_day in ('morning', 'noon', 'afternoon', 'night', 'all_day'))
);

CREATE TABLE CHALLENGES
(
  challenge_id     INTEGER NOT NULL,
  challenge_name   VARCHAR(40) NOT NULL,
  author           VARCHAR(40) NOT NULL,
  activity         VARCHAR(20) NOT NULL,
  time_of_day      VARCHAR(20) NOT NULL,
  duration         INTEGER NOT NULL,
  likes            INTEGER NOT NULL,
  PRIMARY KEY (challenge_id),
  CONSTRAINT CHK_challenge_activity         CHECK (activity in ('walking', 'running', 'swimming', 'basketball', 'soccer', 
                                                        'tennis', 'cycling', 'ping pong', 'golf', 'gymnastics', 
                                                        'handball', 'lacrosse', 'racquetball', 'skateboard')),
  CONSTRAINT CHK_duration         CHECK (duration > 0 AND duration < 300),
  CONSTRAINT CHK_challenge_time_of_day      CHECK (time_of_day in ('morning', 'noon', 'afternoon', 'night', 'all_day'))
);

CREATE TABLE USER_COMPLETED_CHALLENGES
(
  user_id       VARCHAR(40) NOT NULL,
  challenge_id  INTEGER NOT NULL,
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
)

CREATE TABLE USER_FOOD_CONSUMPTION
(
  user_id     VARCHAR(40) NOT NULL,
  meal_type   VARCHAR(10) NOT NULL,
  food        VARCHAR(20) NOT NULL,
  amount      INTEGER NOT NULL,
  calories    INTEGER NOT NULL,
  protein     INTEGER NOT NULL,
  carb        INTEGER NOT NULL,
  fat         INTEGER NOT NULL,
  sugar       INTEGER NOT NULL,
  date        DATETIME NOT NULL,
  FOREIGN KEY (user_id) REFERENCES Users(user_id),
  CONSTRAINT CHK_meal_type    CHECK (meal_type in ('breakfast', 'lunch', 'dinner', 'snack')),
  CONSTRAINT CHK_amount       CHECK (amount > 0 AND amount < 2000),
  CONSTRAINT CHK_calories     CHECK (calories > 0 AND calories < 5000)
);

