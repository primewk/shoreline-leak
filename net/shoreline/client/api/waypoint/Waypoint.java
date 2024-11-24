package net.shoreline.client.api.waypoint;

import net.minecraft.class_2374;
import net.minecraft.class_243;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.ConfigContainer;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;

public class Waypoint extends ConfigContainer implements class_2374 {
   private final String ip;
   private final Config<Double> xConfig = new NumberConfig("X", "X position of waypoint.", 0.0D, 0.0D, Double.MAX_VALUE);
   private final Config<Double> yConfig = new NumberConfig("Y", "Y position of waypoint.", 0.0D, 0.0D, Double.MAX_VALUE);
   private final Config<Double> zConfig = new NumberConfig("Z", "Z position of waypoint.", 0.0D, 0.0D, Double.MAX_VALUE);
   private final Timer timer;

   public Waypoint(String name, String ip, double x, double y, double z) {
      super(name);
      this.ip = ip;
      this.xConfig.setValue(x);
      this.yConfig.setValue(y);
      this.zConfig.setValue(z);
      this.timer = new CacheTimer();
   }

   private boolean passedTime(long time) {
      return this.timer.passed(time);
   }

   public String getIp() {
      return this.ip;
   }

   public double method_10216() {
      return (Double)this.xConfig.getValue();
   }

   public double method_10214() {
      return (Double)this.yConfig.getValue();
   }

   public double method_10215() {
      return (Double)this.zConfig.getValue();
   }

   public class_243 getPos() {
      return new class_243(this.method_10216(), this.method_10214(), this.method_10215());
   }
}
