package net.shoreline.client.impl.module.world;

import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_259;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.world.BlockCollisionEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.world.BlockUtil;

public class AvoidModule extends ToggleModule {
   Config<Boolean> voidConfig = new BooleanConfig("Void", "Prevents player from falling into the void", true);
   Config<Boolean> fireConfig = new BooleanConfig("Fire", "Prevents player from walking into fire", false);
   Config<Boolean> berryBushConfig = new BooleanConfig("BerryBush", "Prevents player from walking into sweet berry bushes", false);
   Config<Boolean> cactiConfig = new BooleanConfig("Cactus", "Prevents player from walking into cacti", false);
   Config<Boolean> unloadedConfig = new BooleanConfig("Unloaded", "Prevents player from entering chunks that haven't been loaded", false);

   public AvoidModule() {
      super("Avoid", "Prevents player from entering harmful areas", ModuleCategory.WORLD);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE && (Boolean)this.voidConfig.getValue() && !mc.field_1724.method_7325() && mc.field_1724.method_23318() < (double)mc.field_1687.method_31607()) {
         Managers.MOVEMENT.setMotionY(0.0D);
      }

   }

   @EventListener
   public void onBlockCollision(BlockCollisionEvent event) {
      class_2338 pos = event.getPos();
      if ((Boolean)this.fireConfig.getValue() && event.getBlock() == class_2246.field_10036 && mc.field_1724.method_23318() < (double)pos.method_10264() + 1.0D || (Boolean)this.cactiConfig.getValue() && event.getBlock() == class_2246.field_10029 || (Boolean)this.berryBushConfig.getValue() && event.getBlock() == class_2246.field_16999 || (Boolean)this.unloadedConfig.getValue() && !BlockUtil.isBlockLoaded((double)pos.method_10263(), (double)pos.method_10260())) {
         event.cancel();
         event.setVoxelShape(class_259.method_1077());
      }

   }
}
