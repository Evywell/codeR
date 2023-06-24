namespace RobClient.Game {
    public interface IAction {
        void Invoke(int deltaTime);
        bool ShouldBeRepeated();
    } 
}