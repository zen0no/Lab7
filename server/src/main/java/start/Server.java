package start;

import com.google.gson.GsonBuilder;
import exceptions.ConnectionException;
import multithread.Session;
import net.Method;
import net.Request;
import net.Response;
import other.typeAdapters.DateAdapter;
import other.typeAdapters.ZonedDateTimeAdapter;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private static Server server;


    private ServerSocketChannel channel;
    private Selector selector;
    private final InetSocketAddress address = new InetSocketAddress(System.getenv("server_host"), Integer.parseInt(System.getenv("server_port")));
    private final Set<SocketChannel> session;
    private GsonBuilder builder;
    private Set<SelectionKey> keys = new HashSet<>();


    private Server(){
        session = new HashSet<>();
        this.builder = new GsonBuilder();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter());
        builder.registerTypeAdapter(Date.class, new DateAdapter());
   }

    public static Server getServer(){
       if (server == null){
           server = new Server();
       }
       return server;
    }


    public void start(){
       if(!bindChannel()) return;

       while (channel.isOpen()){
            try{
               selector.select();
                for (SelectionKey key : selector.selectedKeys()) {
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        accept(key);
                        continue;
                    }

                    else if (key.isReadable()) {
                        Session session = new Session((SocketChannel) key.channel());
                        Thread thread = new Thread(session);
                        Launcher.log.info("Starting new thread for " + key.toString());
                        thread.start();
                        key.cancel();


                    } else key.cancel();
                }
           }
            catch (IOException e) {
                Launcher.log.error("Server is stopping");
            }
       }
    }

    private void stop(){
        try{
            Launcher.log.info("Closing channel...");
            channel.close();
            Launcher.log.info("Server is stopped");
        }

        catch (IOException e) {
            Launcher.log.fatal("Error while stopping server");
        }
    }


    private void accept(SelectionKey key) {
       try{
           SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();

           if (channel == null) {
               return;
           }

           channel.configureBlocking(false);
           channel.register(selector, SelectionKey.OP_READ);
           session.add(channel);
           Launcher.log.info("Connected to " + channel.getRemoteAddress().toString());
       } catch (IOException e) {
           Launcher.log.fatal("Selector error");
       }
    }


    private boolean bindChannel(){
       try{
           Launcher.log.info("Starting server on " + address.toString());
           selector = Selector.open();
           channel = ServerSocketChannel.open();
           channel.bind(address);
           channel.configureBlocking(false);
           channel.register(selector, SelectionKey.OP_ACCEPT);
           return true;
        } catch (ClosedChannelException e) {
           Launcher.log.fatal("Channel was interrupted");
           return false;
       } catch (BindException e ){
           Launcher.log.fatal("Address is already bound");
           return false;
       } catch (IOException e) {
           Launcher.log.fatal("Server error");
           return false;
       }
    }
}
