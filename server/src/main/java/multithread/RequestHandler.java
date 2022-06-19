package multithread;

import dataClasses.HumanBeing;
import login.UserManager;
import login.User;
import net.Request;
import net.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import other.Predicates;
import repositories.PostgreRepository;
import specifications.HumanBeingSpecifications;
import specifications.base.Specification;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestHandler implements Callable<String> {


    private Logger log = LogManager.getLogger(RequestHandler.class);
    private PostgreRepository database;;
    private UserManager manager;
    private Request request;
    private ExecutorService senderPool = Executors.newCachedThreadPool();
    private SocketChannel channel;

    public RequestHandler(Request request, SocketChannel channel) {
        this.database = PostgreRepository.getRepository();
        this.manager = UserManager.getManager();
        this.request = request;
        this.channel = channel;
    }

    public String call(){
        Response response = new Response(Response.StatusCode.ERROR, request.method, null);
        try{
        switch (request.method){

            case GET -> {
                Specification<HumanBeing> specification = HumanBeingSpecifications.fromPredicate((Predicates.Predicate) request.body);
                List<HumanBeing> result = (ArrayList<HumanBeing>) ((specification == null) ? database.query(HumanBeingSpecifications.user(request.user)) : database.query(specification.And(HumanBeingSpecifications.user(request.user))));
                response = new Response(Response.StatusCode.OK, request.method, result);
            }
            case POST -> {
                List<HumanBeing> toPost = (ArrayList<HumanBeing>) request.body;
                List<HumanBeing> res = database.insertEntity(toPost);
                response = new Response(Response.StatusCode.OK, request.method, res);
            }
            case EXIT -> {
                manager.logout(request.user);
                response = new Response(Response.StatusCode.OK, request.method, null);
            }
            case UPDATE -> {
                List<HumanBeing> toUpdate = (ArrayList<HumanBeing>) request.body;
                List<HumanBeing> res = database.updateEntity(toUpdate);

                response = new Response(Response.StatusCode.OK, request.method,  res);
            }

            case INFO -> {
                response = new Response(Response.StatusCode.OK, request.method, database.getInfo());
            }

            case DELETE -> {
                ArrayList<HumanBeing> toRemove = (ArrayList<HumanBeing>) request.body;
                database.removeEntity(toRemove);
                response = new Response(Response.StatusCode.OK, request.method, toRemove);
            }

            case LOGIN -> {
                User user = request.user;
                Response.StatusCode code;
                String message = "Logged in";
                if (manager.login(user)){
                    code = Response.StatusCode.OK;
                }
                else{
                    code = Response.StatusCode.ERROR;
                    message = "No such user";
                }
                response = new Response(code, request.method, message);
            }

            case REGISTER -> {
                User user = request.user;
                manager.register(user);
                response = new Response(Response.StatusCode.OK, request.method, "Signed up");
            }
        }
            ResponseSender sender = new ResponseSender(channel, response);
            senderPool.invokeAll(List.of(sender));

        }
        catch (InterruptedException e){
            log.error(e.getMessage());
        }
        catch (RuntimeException e){
            response = new Response(Response.StatusCode.ERROR, request.method, e.toString());
            log.error(e);
            return e.getMessage();
        }
    return "success";
    }
}
