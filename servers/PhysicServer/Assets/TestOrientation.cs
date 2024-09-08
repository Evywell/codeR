using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TestOrientation : MonoBehaviour
{
    // Update is called once per frame
    void Update()
    {
        if (Input.GetMouseButtonDown(0)) {
            transform.Rotate(0, 5, 0);
        }

        var rayStartPosition = transform.position + new Vector3(0, 2, 0);
        var directionVector = new Vector3(transform.forward.x, 0, transform.forward.z).normalized;

        Debug.DrawRay(
            rayStartPosition, 
            directionVector, 
            Color.red, 
            0, 
            false
        );

        float orientationRad = transform.rotation.eulerAngles.y * Mathf.Deg2Rad;

        Debug.Log($"Orientation = {orientationRad}");
    }
}
