package minisu.ipdip.sse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.servlets.EventSource;

public class FakeSseEmitter implements EventSource.Emitter {

    public final Set<String> emittedStrings = new HashSet<>();

    @Override
    public void event(final String name, final String data) throws IOException { }

    @Override
    public void data(final String data) throws IOException {
        emittedStrings.add(data);
    }

    @Override
    public void comment(final String comment) throws IOException { }

    @Override
    public void close() { }
}
