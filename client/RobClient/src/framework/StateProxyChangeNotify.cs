namespace RobClient.Framework {
    public class StateProxyChangeNotify<T> : StateProxy<T>
    {
        private SetValue _setterCallback;

        public StateProxyChangeNotify(T source, SetValue setterCallback) : base(source)
        {
            _setterCallback = setterCallback;
        }

        public override void SetSourceValue(SetValue callback)
        {
            base.SetSourceValue(callback);

            _setterCallback.Invoke();
        }
    }
}