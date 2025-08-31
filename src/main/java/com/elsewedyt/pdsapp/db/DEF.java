package com.elsewedyt.pdsapp.db;

public class DEF {
    // DB NAME
    public static final String DB_NAME = "pds.dbo";

    // Define  All ComboBox
    public static final String USER_ROLE_GENERAL_USER_STRING = "General_User"; // store 0 in DB
    public static final String USER_ROLE_USER_STRING = "User";//store 1 in DB
    public static final String USER_ROLE_SUPERVISOR_STRING = "Supervisor"; // store 2 in DB
    public static final String USER_ROLE_ADMIN_STRING = "Admin"; // store 3 in DB
    public static final String USER_ACTIVE_STRING = "Active";  // store 1 in DB
    public static final String USER_NOT_ACTIVE_STRING = "Not_Active";  // store 0 in DB






    // Define constants for table and column names
    /*Users-Table*/
    public static final String USERS_TABLE = "users";
    public static final String USERS_ID = "user_id";
    public static final String USERS_EMP_ID = "emp_code";
    public static final String USERS_USERNAME = "user_name";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_FULLNAME = "full_name";
    public static final String USERS_PHONE = "phone";
    public static final String USERS_ROLE = "role";
    public static final String USERS_ACTIVE = "active";
    public static final String USERS_CREATION_DATE = "creation_date";



}
