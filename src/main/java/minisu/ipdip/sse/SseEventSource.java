package minisu.ipdip.sse;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jetty.servlets.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SseEventSource implements EventSource, Consumer<Event> {

    private static final Logger log = LoggerFactory.getLogger(SseEventSource.class);

    private volatile Optional<Emitter> emitter = Optional.empty();
    private volatile Optional<Runnable> onCloseHandler = Optional.empty();

    @Override
    public void onOpen(Emitter emitter) throws IOException {
        log.info("onOpen");
        this.emitter = Optional.of(emitter);
    }

    @Override
    public void onClose() {
        log.info("onClose");
        onCloseHandler.ifPresent(Runnable::run);
    }

    public void setOnClose(Runnable onClose) {
        this.onCloseHandler = Optional.of(onClose);
    }

    @Override
    public void accept(Event event) {
        log.info("pushEvent");
        emitter.ifPresent(emitter -> {
            try {
                emitter.event(event.type, event.data);
            } catch(IOException e) {
                log.warn("Failed to push to client ", e);
            }
        });
    }
}
