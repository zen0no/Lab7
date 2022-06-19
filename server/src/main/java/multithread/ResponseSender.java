package multithread;

import net.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class ResponseSender implements Callable<String> {
    private Logger log = LogManager.getLogger(ResponseSender.class);
    private SocketChannel channel;
    private Response response;

    public ResponseSender(SocketChannel channel, Response response) {
        this.channel = channel;
        this.response = response;
    }

    @Override
    public String call() {
        try {
            byte[] data = Response.toJson(response).getBytes(StandardCharsets.UTF_8);
            int size = data.length;
            ByteBuffer buffer = ByteBuffer.allocate(4100);
            int packagesNumber = size / 4092 + (size % 4092 == 0 ? 0 : 1);

            log.info("Sending response...");
            log.info("Packages number: " + packagesNumber);

            for (int i = 1; i < packagesNumber; i++) {
                buffer.putInt(i);
                buffer.putInt(packagesNumber);
                buffer.put(Arrays.copyOfRange(data, (i - 1) * 4092, i * 4092));
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }
            buffer.putInt(packagesNumber);
            buffer.putInt(packagesNumber);
            buffer.put(Arrays.copyOfRange(data, (packagesNumber - 1) * 4092, size));
            buffer.flip();
            channel.write(buffer);
            log.info("Disconnecting from " + channel.getRemoteAddress());
            channel.close();
            return "success";
        } catch (IOException e) {
            log.fatal("Error while sending response");
            return "error";
        }
    }
}
