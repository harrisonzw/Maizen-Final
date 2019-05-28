CREATE VIEW VIEW_DAILY_STEPS_LEADERBOARD (user_id, steps)
SELECT TOP 10 today.user_id, SUM(today.steps) as total_steps
FROM (
	SELECT user_id, steps
	FROM User_Exercise
	WHERE date BETWEEN GETDATE() - 1 AND GETDATE()
) today
GROUP BY user_id
ORDER BY total_steps DESC

CREATE VIEW VIEW_WEEKLY_STEPS_LEADERBOARD (user_id, steps)
SELECT TOP 10 today.user_id, SUM(today.steps) as total_steps
FROM (
	SELECT user_id, steps
	FROM User_Exercise
	WHERE date BETWEEN GETDATE() - 7 AND GETDATE()
) today
GROUP BY user_id
ORDER BY total_steps DESC

CREATE VIEW VIEW_MONTHLY_STEPS_LEADERBOARD (user_id, steps)
SELECT TOP 10 today.user_id, SUM(today.steps) as total_steps
FROM (
	SELECT user_id, steps
	FROM User_Exercise
	WHERE date BETWEEN GETDATE() - 30 AND GETDATE()
) today
GROUP BY user_id
ORDER BY total_steps DESC

CREATE VIEW VIEW_DAILY_CALORIES_LEADERBOARD (user_id, calories)
SELECT TOP 10 today.user_id, SUM(today.calories) as total_calories
FROM (
	SELECT user_id, calories_burned
	FROM User_Exercise
	WHERE date BETWEEN GETDATE() - 1 AND GETDATE()
) today
GROUP BY user_id
ORDER BY total_calories DESC

CREATE VIEW VIEW_WEEKLY_CALORIES_LEADERBOARD (user_id, calories)
SELECT TOP 10 today.user_id, SUM(today.calories) as total_calories
FROM (
	SELECT user_id, calories_burned
	FROM User_Exercise
	WHERE date BETWEEN GETDATE() - 7 AND GETDATE()
) today
GROUP BY user_id
ORDER BY total_calories DESC

CREATE VIEW VIEW_MONTHLY_CALORIES_LEADERBOARD (user_id, calories)
SELECT TOP 10 today.user_id, SUM(today.calories) as total_calories
FROM (
	SELECT user_id, calories_burned
	FROM User_Exercise
	WHERE date BETWEEN GETDATE() - 30 AND GETDATE()
) today
GROUP BY user_id
ORDER BY total_calories DESC