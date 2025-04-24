package model.dao;
import java.sql.Connection;

//Super class of DAO classes that stores the database information 

public abstract class DBConfig {   
    protected String URL = "jdbc:sqlite:database/database.db";     
    protected String driver = "org.sqlite.jdbc"; 
    protected Connection conn; 
}