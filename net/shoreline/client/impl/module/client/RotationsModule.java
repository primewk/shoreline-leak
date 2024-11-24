package net.shoreline.client.impl.module.client;

import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.module.ConcurrentModule;
import net.shoreline.client.api.module.ModuleCategory;

public class RotationsModule extends ConcurrentModule {
   Config<Float> preserveTicksConfig = new NumberConfig("PreserveTicks", "Time to preserve rotations after reaching the target rotations", 0.0F, 10.0F, 20.0F);
   Config<Boolean> movementFixConfig = new BooleanConfig("MovementFix", "Fixes movement on Grim when rotating", false);
   private float prevYaw;

   public RotationsModule() {
      super("Rotations", "Manages client rotations", ModuleCategory.CLIENT);
   }

   public boolean getMovementFix() {
      return (Boolean)this.movementFixConfig.getValue();
   }

   public float getPreserveTicks() {
      return (Float)this.preserveTicksConfig.getValue();
   }
}
