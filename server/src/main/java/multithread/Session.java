package multithread;

import exceptions.ConnectionException;
import exceptions.InvalidSelectionKeyException;
import login.User;
import net.Request;
import net.Response;
import start.Launcher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Session implements Runnable{
    private User user;
    private SocketChannel channel;
    private ExecutorService responseProcessor = Executors.newCachedThreadPool();

    public Session(SocketChannel socketChannel) {
        this.channel = socketChannel;
    }

    @Override
    public void run(){
        Launcher.log.info("Processing new request on " + Thread.currentThread().getName());
        Request request = read(channel);
        Launcher.log.info("Request " + request.toString());
        try {
            responseProcessor.invokeAll(List.of(new RequestHandler(request, channel)));
        }
        catch (InterruptedException e){
            Launcher.log.error(e.getMessage());
        }


    }

    private Request read(SocketChannel socketChannel) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(4096);

            int amount = socketChannel.read(buffer);

            if (amount == -1){
                Launcher.log.info("Disconnected from " + socketChannel.getRemoteAddress().toString());
                socketChannel.close();
                return null;
            }

            byte[] data = new byte[amount];
            System.arraycopy(buffer.array(), 0, data, 0, amount);
            String json = new String(data, StandardCharsets.UTF_8);
            return Request.fromJson(json);
        } catch (IOException e) {
            Launcher.log.fatal("Error while reading request: " + e.getClass().getName() + " " + e.getMessage());
            throw new ConnectionException();
        }
    }

}
