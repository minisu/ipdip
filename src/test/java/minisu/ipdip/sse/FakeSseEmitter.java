package minisu.ipdip.sse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.servlets.EventSource;

public class FakeSseEmitter implements EventSource.Emitter {

    public final Set<Event> emittedStrings = new HashSet<>();

    @Override
    public void event(String name, String data) throws IOException { emittedStrings.add(Event.of(name, data)); }

    @Override
    public void data(String data) throws IOException {

    }

    @Override
    public void comment(String comment) throws IOException { }

    @Override
    public void close() { }
}
