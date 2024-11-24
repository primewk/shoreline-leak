package net.shoreline.client.impl.manager.world;

import io.netty.util.internal.ConcurrentSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.shoreline.client.api.waypoint.Waypoint;

public class WaypointManager {
   private final Set<Waypoint> waypoints = new ConcurrentSet();

   public void register(Waypoint waypoint) {
      this.waypoints.add(waypoint);
   }

   public void register(Waypoint... waypoints) {
      Waypoint[] var2 = waypoints;
      int var3 = waypoints.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Waypoint waypoint = var2[var4];
         this.register(waypoint);
      }

   }

   public boolean remove(Waypoint waypoint) {
      return this.waypoints.remove(waypoint);
   }

   public boolean remove(String waypoint) {
      Iterator var2 = this.getWaypoints().iterator();

      Waypoint w;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         w = (Waypoint)var2.next();
      } while(!w.getName().equalsIgnoreCase(waypoint));

      return this.waypoints.remove(w);
   }

   public Collection<Waypoint> getWaypoints() {
      return this.waypoints;
   }

   public Collection<String> getIps() {
      Set<String> ips = new HashSet();
      Iterator var2 = this.getWaypoints().iterator();

      while(var2.hasNext()) {
         Waypoint waypoint = (Waypoint)var2.next();
         ips.add(waypoint.getIp());
      }

      return ips;
   }
}
