package start;

import jdbc.DBEngine;
import login.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repositories.PostgreRepository;


public class Launcher {

    public static Logger log = LogManager.getLogger(Launcher.class.getName());

    public static void main(String args[]){
        DBEngine.bindEngine();
        UserManager.load();
        PostgreRepository.load();
        Server server = Server.getServer();
        server.start();

    }
}
