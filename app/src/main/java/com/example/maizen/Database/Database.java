package com.example.maizen.Database;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database extends Exception {

    public void insertUser(String firstName, String lastName, String dayOfBirth, String monthOfBirth,
                           String yearOfBirth, String gender, String weight, String heightFeet,
                           String heightInches, String userId) throws Exception {
        String errorMessage = "";
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                if (userId == null) {
                    errorMessage = "Error: could not retrieve user's ID.";
                } else {
                    Statement stmt = con.createStatement();
                    int result = stmt.executeUpdate(
                            "INSERT INTO USERS (user_id, first_name, last_name, " +
                                    "month_of_birth, day_of_birth, year_of_birth, gender, " +
                                    "weight_pounds, height_feet, height_inches, current_challenges)" +
                                    "VALUES ('" + userId + "', '" +
                                    firstName + "', '" + lastName + "', " +
                                    Integer.parseInt(monthOfBirth) + ", " +
                                    Integer.parseInt(dayOfBirth) + ", " +
                                    Integer.parseInt(yearOfBirth) + ", '" +
                                    gender + "', " +
                                    Integer.parseInt(weight) + ", " +
                                    Integer.parseInt(heightFeet) + ", " +
                                    Integer.parseInt(heightInches) + ",-1)"
                    );

                    // Failed to insert the new user
                    if (result <= 0) {
                        errorMessage = "Failed to insert new user.";
                    }
                }
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        if (!errorMessage.isEmpty()) {
            throw new Exception(errorMessage);
        }
    }

    public ResultSet getName(String user_id) throws Exception {
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                ResultSet name = stmt.executeQuery(
                        "SELECT first_name, last_name " +
                                "FROM USERS " +
                                "WHERE user_id = '" + user_id + "'"
                );

                if (!name.next()) {
                    errorMessage = "Failed to find user's name.";
                } else {
                    return name;
                }
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        if (!errorMessage.isEmpty()) {
            throw new Exception(errorMessage);
        }

        return null;
    }

    public ResultSet getTop10() throws Exception {
        String errorMessage;

        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                return stmt.executeQuery(
                        "SELECT TOP 10 t.user_id, t.first_name, t.last_name, SUM(t.steps) as total_steps " +
                                "FROM (" +
                                "SELECT user_id, first_name, last_name, steps " +
                                "FROM USER_ACTIVITIES " +
                                ") t " +
                                "GROUP BY user_id, first_name, last_name " +
                                "ORDER BY total_steps DESC"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        if (!errorMessage.isEmpty()) {
            throw new Exception(errorMessage);
        }

        return null;
    }

    public void acceptChallenge(String user_id, Integer challenge_id) {
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                stmt.executeQuery(
                        "UPDATE USERS " +
                                "SET current_challenges = " + challenge_id + " " +
                                "WHERE user_id = '" + user_id + "'"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

    }

    public ResultSet getCurrentChallengeInfo(String user_id) {
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(
                        "SELECT challenge_id, challenge_name, activity, time_of_day, duration " +
                                "FROM CHALLENGES " +
                                "WHERE challenge_id IN ( " +
                                "SELECT current_challenges " +
                                "FROM Users " +
                                "WHERE user_id = '" + user_id + "'" +
                                ")"
                );

                return rst;
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        return null;
    }

    public ResultSet getChallengeProgress(String user_id, String activity, String time_of_day) {
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(
                        "SELECT SUM(duration) as progress " +
                                "FROM USER_ACTIVITIES " +
                                "WHERE activity = '" + activity + "'" + " AND " +
                                "time_of_day = '" + time_of_day + "'" + " AND " +
                                "user_id = '" + user_id + "'"
                );

                return rst;
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }


        return null;
    }

    public void insertCompletedChallenge(String user_id, Integer challenge_id) {
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                stmt.executeQuery(
                        "INSERT INTO USER_COMPLETED_CHALLENGES (user_id, challenge_id) " +
                                "FROM USER_ACTIVITIES " +
                                "VALUES ('" +
                                user_id + "', " +
                                challenge_id + ")"
                );
                stmt.executeQuery(
                        "UPDATE USERS " +
                                "SET current_challenge = -1 " +
                                "WHERE user_id = " + user_id
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

    }

    public void insertUserActivity(String user_id, String activity, String duration, String timeOfDay) {
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                ResultSet name = stmt.executeQuery(
                        "SELECT first_name, last_name " +
                                "FROM USERS " +
                                "WHERE user_id = '" + user_id + "'"
                );
                String firstName = name.getString(1);
                String lastName = name.getString(2);

                stmt = con.createStatement();
                stmt.executeQuery(
                        "INSERT INTO USER_ACTIVITIES " +
                                "VALUES (" + user_id + ", '" + firstName +
                                "', '" + lastName + "', '" + activity + "', '" +
                                "0, 0, '" + timeOfDay + "'')"
                );
            }
        } catch (Exception ex ){
            errorMessage = ex.getMessage();
        }
    }

    public void insertUserChallenge(String user_id, String challenge_name, String activity, String timeOfDay, String duration) {
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                ResultSet maxChallengeID = stmt.executeQuery(
                        "SELECT MAX(challenge_id) FROM CHALLENGES"
                );
                Integer challenge_id = maxChallengeID.getInt(1);

                stmt = con.createStatement();
                stmt.executeQuery(
                        "INSERT INTO CHALLENGES " +
                                "VALUES (" + challenge_id + ", '" + challenge_name + "', '" +
                                user_id + "', '" + activity + "', '" +
                                timeOfDay + "', " + Integer.parseInt(duration) + ", 1)"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }
    }

    public ResultSet getUserCreatedChallenges() {
        String errorMessage;
        ResultSet rst = null;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                rst = stmt.executeQuery(
                        "SELECT * " +
                                "FROM CHALLENGES " +
                                "WHERE challenge_id > 11 " +
                                "ORDER BY likes DESC"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        return rst;
    }

    public ResultSet getUserInfo(String user_id) {
        String errorMessage;
        ResultSet rst = null;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                rst = stmt.executeQuery(
                        "SELECT * " +
                                "FROM USERS " +
                                "WHERE user_id='" + user_id + "'"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        return rst;
    }

    public ResultSet getUserStepStatistics(String user_id) {
        String errorMessage;
        ResultSet rst = null;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                rst = stmt.executeQuery(
                        "SELECT MIN(steps), AVG(steps), MAX(steps) " +
                                "FROM USER_ACTIVITIES " +
                                "WHERE user_id='" + user_id + "'"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        return rst;
    }

    public ResultSet getUserCompletedChallenges(String user_id) {
        ResultSet rst = null;
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                rst = stmt.executeQuery(
                        "SELECT " +
                                "FROM USER_COMPLETED_CHALLENGES " +
                                "WHERE user_id='" + user_id + "'"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        return rst;
    }

    public ResultSet getCompletedUserChallengeCount(String user_id) {
        String errorMessage;
        ResultSet rst = null;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                rst = stmt.executeQuery(
                        "SELECT SUM(CASE WHEN challenge_id > 11 THEN 1 ELSE 0 END) " +
                                "FROM USER_COMPLETED_CHALLENGES " +
                                "WHERE user_id='" + user_id + "'"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }


        return rst;
    }

    public ResultSet getCompletedMaizenChallengeCount(String user_id) {
        String errorMessage;
        ResultSet rst = null;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                rst = stmt.executeQuery(
                        "SELECT SUM(CASE WHEN challenge_id < 12 THEN 1 ELSE 0 END) " +
                                "FROM USER_COMPLETED_CHALLENGES " +
                                "WHERE user_id='" + user_id + "'"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }


        return rst;
    }

    public void changeUserHeight(String user_id, Integer newHeightInFeet, Integer newHeightInInches) {
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(
                        "UPDATE USERS " +
                                "SET height_feet=" + newHeightInFeet + "," +
                                "height_inches=" + newHeightInInches + " " +
                                "WHERE user_id='" + user_id + "'"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

    }

    public void changeUserWeight(String user_id, Integer newWeight) {
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(
                        "UPDATE USERS " +
                                "SET weight_pounds=" + newWeight + " " +
                                "WHERE user_id='" + user_id + "'"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

    }

    public void changeUserName(String user_id, String new_first_name, String new_last_name ) {
        String errorMessage;
        try {
            Connection con = connectionClass();
            if (con == null) {
                errorMessage = "Error: please check your internet access.";
            } else {
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(
                        "UPDATE USERS " +
                                "SET first_name='" + new_first_name + "'," +
                                "last_name='" + new_last_name + "' " +
                                "WHERE user_id='" + user_id + "'"
                );
            }
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

    }


    private Connection connectionClass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://maizenserver.database.windows.net:1433;DatabaseName=Maizen;user=hwo@maizenserver;password=maizenDB1;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException ex) {
            Log.e("Error here 1 : ", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Log.e("Error here 2 :", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Error here 3: ", ex.getMessage());
        }

        return connection;
    }
}
