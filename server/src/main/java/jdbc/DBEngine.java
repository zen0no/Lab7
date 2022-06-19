package jdbc;

import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DBEngine {

    private static Logger log = LogManager.getLogger(DBEngine.class.getName());


    private static String connectionURI;


    public static void bindEngine(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.error("Can't find driver for postgresql");
            throw new DatabaseException("Can't find driver for postgresql");

        }
        log.info("PostgreSQL driver is fine.");
        String db_host = System.getenv("db_host");
        String db_port = System.getenv("db_port");
        String db_name = System.getenv("db_name");
        String db_user = System.getenv("db_user");
        String db_password = System.getenv("db_password");
        connectionURI = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s&ssl=true;characterEncoding=utf8", db_host, db_port, db_name, db_user, db_password);


        Connection con = getConnection();
        String humanbeing_existence = """
            SELECT * FROM information_schema.tables
            WHERE table_schema = 'public'
            AND table_name='humanbeing'
            """;

        String user_exisence = """
            SELECT * FROM information_schema.tables
            WHERE table_schema = 'public'
            AND table_name='user_'
            """;

        String humanbeing_create_table = """
            CREATE TABLE humanBeing (
                           	primaryKey TEXT PRIMARY KEY,
                           	id_ SERIAL,
                           	name_ TEXT NOT NULL,
                           	coordinates_x DOUBLE PRECISION NOT NULL CHECK (coordinates_x < 673),
                           	coordinates_y DOUBLE PRECISION NOT NULL,
                           	creationDate TIMESTAMP NOT NULL,
                           	impactSpeed INT NOT NULL,
                           	car_cool BOOLEAN NOT NULL,
                           	realHero BOOLEAN NOT NULL,
                           	hasToothPeak BOOLEAN,
                           	weaponType VARCHAR(15),
                           	mood VARCHAR(15),
                           	user_ text,
                           	FOREIGN KEY (user_) REFERENCES user_(username)
                           	PRIMARY KEY us
                           )
            """;

        String user_create_table = """
                CREATE TABLE user_ (
                            username text PRIMARY KEY,
                            password_ text
                            )
                """;
        try {
            Statement userQueryStatement = con.createStatement();
            ResultSet resultSet =  userQueryStatement.executeQuery(user_exisence);
            if (!resultSet.next()) {
                Statement userStatement = con.createStatement();
                userStatement.executeUpdate(user_create_table);
                log.info("Creating public.user_ table");
            }

            Statement humanQueryStatement = con.createStatement();
            resultSet  = humanQueryStatement.executeQuery(humanbeing_existence);
            if (!resultSet.next()) {
                Statement humanStatement = con.createStatement();
                humanStatement.executeUpdate(humanbeing_create_table);
                log.info("Creating public.humanbeing table");
            }
        }
        catch (SQLException e){
            log.fatal(e);
            throw new DatabaseException(e.getMessage());
        }
    }

    public static Connection getConnection() throws DatabaseException{
        if (connectionURI == null)
        {
            throw new DatabaseException("DBEngine is not bound");
        }


        Connection con;
        try{
            con = DriverManager.getConnection(connectionURI);
            log.info("PostgreSQL connection created created: " + con.toString());
        } catch (java.sql.SQLException throwables) {
            log.error(throwables);
            throw new DatabaseException("Can't access database: " + throwables.getMessage());
        }
        return con;
    }
}
