using Fr.Raven.Proto.Message.Gateway;

namespace RobClient;

class Listener {
    private Func<Packet, Boolean> _callback;

    public Listener(Func<Packet, Boolean> callback) {
        _callback = callback;
    }

    public Boolean Call(Packet packet) {
        return _callback(packet);
    }
}