package net.shoreline.client.impl.module.world;

import net.minecraft.class_1799;
import net.minecraft.class_1812;
import net.minecraft.class_2828.class_2829;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.network.SetCurrentHandEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.player.MovementUtil;
import net.shoreline.client.util.string.EnumFormatter;

public final class FastEatModule extends ToggleModule {
   Config<FastEatModule.Mode> modeConfig;
   Config<Integer> ticksConfig;
   private int packets;

   public FastEatModule() {
      super("FastEat", "Allows you to consume items faster", ModuleCategory.WORLD);
      this.modeConfig = new EnumConfig("Mode", "The bypass mode", FastEatModule.Mode.VANILLA, FastEatModule.Mode.values());
      this.ticksConfig = new NumberConfig("Ticks", "The amount of ticks to have 'consumed' an item before fast eating", 0, 10, 30);
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      if (!MovementUtil.isMoving() && mc.field_1724.method_24828()) {
         ++this.packets;
         if (this.packets > 100) {
            this.packets = 100;
         }
      } else {
         --this.packets;
         if (this.packets <= 0) {
            this.packets = 0;
         }
      }

      if (mc.field_1724.method_6115()) {
         class_1799 stack = mc.field_1724.method_5998(mc.field_1724.method_6058());
         if (!stack.method_7960() && stack.method_7909().method_19263()) {
            int timeUsed = mc.field_1724.method_6048();
            if (timeUsed >= (Integer)this.ticksConfig.getValue()) {
               int usePackets = 32 - timeUsed;

               for(int i = 0; i < usePackets; ++i) {
                  int var10000 = null.$SwitchMap$net$shoreline$client$impl$module$world$FastEatModule$Mode[((FastEatModule.Mode)this.modeConfig.getValue()).ordinal()];
               }
            }

         }
      }
   }

   @EventListener
   public void onSetCurrentHand(SetCurrentHandEvent event) {
      if (this.modeConfig.getValue() == FastEatModule.Mode.SHIFT) {
         class_1799 stack = event.getStackInHand();
         if (!stack.method_7909().method_19263() && !(stack.method_7909() instanceof class_1812)) {
            return;
         }

         int maxUseTime = stack.method_7935();
         if (this.packets < maxUseTime) {
            return;
         }

         for(int i = 0; i < maxUseTime; ++i) {
            Managers.NETWORK.sendPacket(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_24828()));
            this.packets -= maxUseTime;
         }

         event.cancel();
         stack.method_7909().method_7861(stack, mc.field_1687, mc.field_1724);
      }

   }

   private static enum Mode {
      VANILLA,
      SHIFT,
      GRIM;

      // $FF: synthetic method
      private static FastEatModule.Mode[] $values() {
         return new FastEatModule.Mode[]{VANILLA, SHIFT, GRIM};
      }
   }
}
