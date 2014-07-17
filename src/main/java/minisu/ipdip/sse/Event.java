package minisu.ipdip.sse;

import minisu.ipdip.auth.User;

import com.google.common.base.Objects;

public class Event {

    public final String type;
    public final String data;

    private Event(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public static Event decisionMade(String decidedAlternative) {
        return new Event("decisionMade", decidedAlternative);
    }

    public static Event newVisitor(String name) {
        return new Event("newVisitor", name);
    }

    public static Event newVisitor(User user) {
        return new Event("newVisitor", user.getId() + " " + user.getProfileImageURL() );
    }

    public static Event of(String type, String data) {
        return new Event(type, data);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("type", type)
                .add("data", data)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, data);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Event) {
            Event that = (Event) other;
            return this.type.equals(that.type)
                    && this.data.equals(that.data);
        }
        return false;
    }
}
