using System;

namespace RobClient.Framework {
    public class StateProxy<T> {
        private T _source;

        public StateProxy(T source) {
            _source = source;
        }

        public delegate void SetValue();

        public T GetSource() => _source;

        public virtual void SetSourceValue(SetValue callback)
        {
            callback.Invoke();
        }
    }
}