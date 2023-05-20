namespace RobClient.Game.Interaction.Action {
    public interface IAction {
        void Invoke(int deltaTime);
        bool ShouldBeRepeated();
    } 
}