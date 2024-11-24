package net.shoreline.client.impl.module.movement;

import net.minecraft.class_1297;
import net.minecraft.class_1501;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.entity.passive.EntitySteerEvent;
import net.shoreline.client.impl.event.network.MountJumpStrengthEvent;

public class EntityControlModule extends ToggleModule {
   Config<Float> jumpStrengthConfig = new NumberConfig("JumpStrength", "The fixed jump strength of the mounted entity", 0.1F, 0.7F, 2.0F);
   Config<Boolean> noPigMoveConfig = new BooleanConfig("NoPigAI", "Prevents the pig movement when controlling pigs", false);

   public EntityControlModule() {
      super("EntityControl", "Allows you to steer entities without a saddle", ModuleCategory.MOVEMENT);
   }

   @EventListener
   public void onTick(TickEvent event) {
      class_1297 vehicle = mc.field_1724.method_5854();
      if (vehicle != null) {
         vehicle.method_36456(mc.field_1724.method_36454());
         if (vehicle instanceof class_1501) {
            class_1501 llama = (class_1501)vehicle;
            llama.field_6241 = mc.field_1724.method_36454();
         }

      }
   }

   @EventListener
   public void onEntitySteer(EntitySteerEvent event) {
      event.cancel();
   }

   @EventListener
   public void onMountJumpStrength(MountJumpStrengthEvent event) {
      event.cancel();
      event.setJumpStrength((Float)this.jumpStrengthConfig.getValue());
   }
}
