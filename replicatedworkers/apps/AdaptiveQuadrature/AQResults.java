package apps.AdaptiveQuadrature;

import java.util.Vector;
import java.util.Enumeration;
import ca.replicatedworkers.ResultItemComputation;

public class AQResults implements ResultItemComputation
{
   public static float totalsum;
   public float value;

   // Empty work object used to bind result computation to abstraction
   public AQResults() { value = (float)0.0; }

   public AQResults(float f) { value = f; }

   public boolean doResults()
   {
     totalsum += value;
     return false;
   }
}
