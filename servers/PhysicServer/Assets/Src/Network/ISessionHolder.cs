using System.Collections.Generic;

namespace App.Network {
    public interface ISessionHolder {
        void AddSession(Session session);
        void RemoveSession(Session session);
        void RemoveSession(int identifier);
        Session GetSession(int identifier);
        IEnumerable<Session> GetSessionEnumerable();
    }
}