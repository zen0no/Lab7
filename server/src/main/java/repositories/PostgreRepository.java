package repositories;

import dataClasses.HumanBeing;
import exceptions.PostgreSQLRepositoryException;
import jdbc.DBEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.HumanBeingBuilder;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostgreRepository extends HumanBeingRepository{

    private static Logger log = LogManager.getLogger(PostgreRepository.class);
    private static PostgreRepository postgreRepository;
    private static boolean isLoaded = false;

    private PostgreRepository(){
        try {
            Connection con = DBEngine.getConnection();

            String query_all = """
                    SELECT * FROM public.humanbeing
                    """;

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query_all);
            log.info("Reading data from database");

            HumanBeingBuilder builder = new HumanBeingBuilder();

            while (resultSet.next()){
                builder.create(resultSet.getString("primarykey"), resultSet.getInt("id_"), resultSet.getTimestamp("creationDate"), resultSet.getString("user_"));
                builder.build(resultSet);
                HumanBeing h = builder.get();
                values.put(h.getPrimaryKey(), h);
            }
            this.values = Collections.synchronizedMap(values);

        }

        catch (SQLException throwables)
        {
            log.fatal(throwables);
        }
    }

    public static PostgreRepository getRepository() throws PostgreSQLRepositoryException{
        if (!isLoaded) {
            throw new PostgreSQLRepositoryException("PostgreSQL repository have not loaded");
        };
        return postgreRepository;
    }

    public static void load() throws PostgreSQLRepositoryException{
        log.info("Loading PostgreSQL repository...");
        if (isLoaded){
            throw new PostgreSQLRepositoryException("Repository is already loaded");
        }
        isLoaded = true;
        postgreRepository = new PostgreRepository();
        log.info("Repository loaded");
    }


    public List<HumanBeing> insertEntity(HumanBeing entity){
        Connection con = DBEngine.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement("""
                    INSERT INTO public.humanbeing
                    VALUES(
                    ?,
                    DEFAULT,
                    ?,
                    ?,
                    ?,
                    to_timestamp(?, 'MM/DD/YYYY HH24:MI:SS'),
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?
                    )
                    """);
            statement.setString(1, entity.getPrimaryKey());
            statement.setString(2, entity.getName());
            statement.setDouble(3, entity.getCoordinates().getX());
            statement.setDouble(4, entity.getCoordinates().getY());
            statement.setString(5, (new SimpleDateFormat("MM/dd/YYYY HH:mm:ss")).format(entity.getCreationDate()));
            statement.setInt(6, entity.getImpactSpeed());
            statement.setBoolean(7, entity.getCar().isCool());
            statement.setBoolean(8, entity.isRealHero());
            statement.setBoolean(9, entity.isHasToothpick());
            statement.setString(10, entity.getWeaponType().toString());
            statement.setString(11, entity.getMood().toString());
            statement.setString(12, entity.getUserLogin());

            statement.executeUpdate();


            String select = """
                    SELECT * FROM public.humanbeing WHERE primarykey = ? AND user_ = ?
                    """;

            PreparedStatement queryStatement = con.prepareStatement(select);
            queryStatement.setString(1, entity.getPrimaryKey());
            queryStatement.setString(2, entity.getUserLogin());

            ResultSet res = queryStatement.executeQuery();
            while(res.next())
            {
                entity.setId(res.getInt("id_"));
                values.put(entity.getPrimaryKey(), entity);
            }
            return List.of(entity);
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
            return null;
        }
    }

    public List<HumanBeing> removeEntity(HumanBeing entity){
        Connection con = DBEngine.getConnection();
        try{
            String insert = """
                    DELETE FROM public.humanbeing WHERE primarykey = ? AND user_ = ?
                    """;
            PreparedStatement statement = con.prepareStatement(insert);
            statement.setString(1, entity.getPrimaryKey());
            statement.setString(2, entity.getUserLogin());
            statement.executeUpdate();
            values.remove(entity.getPrimaryKey());
            return List.of(entity);
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
            return null;
        }
    }

    public List<HumanBeing> removeEntity(List<HumanBeing> entities){
        ArrayList<HumanBeing> result = new ArrayList<>();
        for (HumanBeing h: entities){
            result.addAll(removeEntity(h));
        }
        return result;
    }

    public List<HumanBeing> updateEntity(HumanBeing entity){
        Connection con = DBEngine.getConnection();
        try{
            String insert = """
                    UPDATE public.humanbeing
                    SET
                    name_ = ?,
                    coordinates_x = ?,
                    coordinates_y = ?,
                    impactspeed = ?,
                    car_cool = ?,
                    hastoothpeak = ?,
                    realhero = ?,
                    weapontype = ?,
                    mood = ?
                    WHERE primarykey = ?
                    """;
            PreparedStatement statement = con.prepareStatement(insert);
            statement.setString(1, entity.getName());
            statement.setDouble(2, entity.getCoordinates().getX());
            statement.setDouble(3, entity.getCoordinates().getY());
            statement.setInt(4, entity.getImpactSpeed());
            statement.setBoolean(5, entity.getCar().isCool());
            statement.setBoolean(6, entity.isHasToothpick());
            statement.setBoolean(7, entity.isRealHero());
            statement.setString(8, entity.getWeaponType().toString());
            statement.setString(9, entity.getMood().toString());
            statement.setString(10, entity.getPrimaryKey());

            statement.executeUpdate();

            String select = """
                    SELECT * FROM public.humanbeing WHERE primarykey = ? AND user_ = ?
                    """;

            PreparedStatement queryStatement = con.prepareStatement(select);
            queryStatement.setString(1, entity.getPrimaryKey());
            queryStatement.setString(2, entity.getUserLogin());

            ResultSet res = queryStatement.executeQuery();
            while(res.next())
            {
                entity.setId(res.getInt("id_"));
                values.replace(entity.getPrimaryKey(), entity);
                return List.of(entity);
            }
            return List.of(entity);
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
            return null;
        }
    }

}
