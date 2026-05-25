using UnityEngine;

namespace Game.Entity
{
    /// <summary>
    /// Absorbs AnimationEvents emitted by the StarterAssets locomotion clips
    /// (OnFootstep, OnLand) so Unity doesn't spam "AnimationEvent has no receiver"
    /// warnings now that the original ThirdPersonController has been stripped.
    ///
    /// Attached at runtime by EntitySpawner on the player visual root.
    /// Hook audio/VFX here later if desired.
    /// </summary>
    public class AnimationEventReceiver : MonoBehaviour
    {
        // Signature must match the AnimationEvents in the FBX clips.
        // ReSharper disable once UnusedMember.Global
        private void OnFootstep(AnimationEvent animationEvent)
        {
            // No-op. Override later for footstep SFX.
        }

        // ReSharper disable once UnusedMember.Global
        private void OnLand(AnimationEvent animationEvent)
        {
            // No-op. Override later for landing SFX.
        }
    }
}
