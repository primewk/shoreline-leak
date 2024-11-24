package net.shoreline.client.impl.module.misc;

import net.minecraft.class_1536;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2767;
import net.minecraft.class_3417;
import net.minecraft.class_408;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.imixin.IMinecraftClient;

public class AutoFishModule extends ToggleModule {
   Config<Boolean> openInventoryConfig = new BooleanConfig("OpenInventory", "Allows you to fish while in the inventory", true);
   Config<Integer> castDelayConfig = new NumberConfig("CastingDelay", "The delay between fishing rod casts", 10, 15, 25);
   Config<Float> maxSoundDistConfig = new NumberConfig("MaxSoundDist", "The maximum distance from the splash sound", 0.0F, 2.0F, 5.0F);
   private boolean autoReel;
   private int autoReelTicks;
   private int autoCastTicks;

   public AutoFishModule() {
      super("AutoFish", "Automatically casts and reels fishing rods", ModuleCategory.MISCELLANEOUS);
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1724 != null) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2767) {
            class_2767 packet = (class_2767)var3;
            if (packet.method_11894().comp_349() == class_3417.field_14660 && mc.field_1724.method_6047().method_7909() == class_1802.field_8378) {
               class_1536 fishHook = mc.field_1724.field_7513;
               if (fishHook == null || fishHook.method_6947() != mc.field_1724) {
                  return;
               }

               double dist = fishHook.method_5649(packet.method_11890(), packet.method_11889(), packet.method_11893());
               if (dist <= (double)(Float)this.maxSoundDistConfig.getValue()) {
                  this.autoReel = true;
                  this.autoReelTicks = 4;
               }
            }
         }

      }
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (mc.field_1755 == null || mc.field_1755 instanceof class_408 || (Boolean)this.openInventoryConfig.getValue()) {
            if (mc.field_1724.method_6047().method_7909() != class_1802.field_8378) {
               return;
            }

            class_1536 fishHook = mc.field_1724.field_7513;
            if ((fishHook == null || fishHook.method_26957() != null) && this.autoCastTicks <= 0) {
               ((IMinecraftClient)mc).rightClick();
               this.autoCastTicks = (Integer)this.castDelayConfig.getValue();
               return;
            }

            if (this.autoReel) {
               if (this.autoReelTicks <= 0) {
                  ((IMinecraftClient)mc).rightClick();
                  this.autoReel = false;
                  return;
               }

               --this.autoReelTicks;
            }
         }

         --this.autoCastTicks;
      }
   }
}
