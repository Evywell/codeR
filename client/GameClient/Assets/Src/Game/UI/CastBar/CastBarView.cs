using UnityEngine;
using UnityEngine.UIElements;

namespace Game.UI.CastBar
{
    [RequireComponent(typeof(UIDocument))]
    public class CastBarView : MonoBehaviour
    {
        private VisualElement _root;
        private ProgressBar _progressBar;

        private void Awake()
        {
            var document = GetComponent<UIDocument>();
            _root = document.rootVisualElement.Q("cast-bar-root");
            _progressBar = document.rootVisualElement.Q<ProgressBar>("cast-progress");
        }

        public void Show()  => _root.style.display = DisplayStyle.Flex;
        public void Hide()  => _root.style.display = DisplayStyle.None;

        public void SetProgress(float value)
        {
            _progressBar.value = value * 100f;
        }
    }
}