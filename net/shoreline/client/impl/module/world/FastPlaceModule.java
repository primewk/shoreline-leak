package net.shoreline.client.impl.module.world;

import java.util.List;
import java.util.concurrent.TimeUnit;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.ItemListConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.mixin.accessor.AccessorMinecraftClient;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.world.SneakBlocks;

public class FastPlaceModule extends ToggleModule {
   Config<FastPlaceModule.Selection> selectionConfig;
   Config<Integer> delayConfig;
   Config<Float> startDelayConfig;
   Config<Boolean> ghostFixConfig;
   Config<List<class_1792>> whitelistConfig;
   Config<List<class_1792>> blacklistConfig;
   private final CacheTimer startTimer;

   public FastPlaceModule() {
      super("FastPlace", "Place items and blocks faster", ModuleCategory.WORLD);
      this.selectionConfig = new EnumConfig("Selection", "The selection of items to apply fast placements", FastPlaceModule.Selection.WHITELIST, FastPlaceModule.Selection.values());
      this.delayConfig = new NumberConfig("Delay", "Fast place click delay", 0, 1, 4);
      this.startDelayConfig = new NumberConfig("StartDelay", "Fast place start delay", 0.0F, 0.0F, 1.0F);
      this.ghostFixConfig = new BooleanConfig("GhostFix", "Fixes item ghosting issue on some servers", false);
      this.whitelistConfig = new ItemListConfig("Whitelist", "Valid item whitelist", new class_1792[]{class_1802.field_8287, class_1802.field_8543, class_1802.field_8803});
      this.blacklistConfig = new ItemListConfig("Blacklist", "Valid item blacklist", new class_1792[]{class_1802.field_8634, class_1802.field_8449});
      this.startTimer = new CacheTimer();
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (!mc.field_1690.field_1904.method_1434()) {
            this.startTimer.reset();
         } else if (this.startTimer.passed((Number)this.startDelayConfig.getValue(), TimeUnit.SECONDS) && ((AccessorMinecraftClient)mc).hookGetItemUseCooldown() > (Integer)this.delayConfig.getValue() && this.placeCheck(mc.field_1724.method_6047())) {
            if ((Boolean)this.ghostFixConfig.getValue()) {
               Managers.NETWORK.sendSequencedPacket((id) -> {
                  return new class_2886(mc.field_1724.method_6058(), id);
               });
            }

            ((AccessorMinecraftClient)mc).hookSetItemUseCooldown((Integer)this.delayConfig.getValue());
         }

      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2885) {
            class_2885 packet = (class_2885)var3;
            if ((Boolean)this.ghostFixConfig.getValue() && !event.isClientPacket() && this.placeCheck(mc.field_1724.method_5998(packet.method_12546()))) {
               class_2680 state = mc.field_1687.method_8320(packet.method_12543().method_17777());
               if (!SneakBlocks.isSneakBlock(state)) {
                  event.cancel();
               }
            }
         }

      }
   }

   private boolean placeCheck(class_1799 held) {
      boolean var10000;
      switch((FastPlaceModule.Selection)this.selectionConfig.getValue()) {
      case WHITELIST:
         var10000 = ((ItemListConfig)this.whitelistConfig).contains(held.method_7909());
         break;
      case BLACKLIST:
         var10000 = !((ItemListConfig)this.blacklistConfig).contains(held.method_7909());
         break;
      case ALL:
         var10000 = true;
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   public static enum Selection {
      WHITELIST,
      BLACKLIST,
      ALL;

      // $FF: synthetic method
      private static FastPlaceModule.Selection[] $values() {
         return new FastPlaceModule.Selection[]{WHITELIST, BLACKLIST, ALL};
      }
   }
}
