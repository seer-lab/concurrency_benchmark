package apps.AdaptiveQuadrature;

import ca.replicatedworkers.Collection;
import java.util.Vector;

class FIFOCollection extends Vector implements Collection
{
  public final Object take()
  {
    Object tmp; 
    tmp = firstElement();
    removeElementAt(0);
    return tmp;
  }
}


