package net.shoreline.client.impl.module.combat;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_1835;
import net.minecraft.class_2596;
import net.minecraft.class_2749;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.player.InventoryUtil;
import net.shoreline.client.util.player.PlayerUtil;
import net.shoreline.client.util.world.ExplosionUtil;

public final class AutoTotemModule extends ToggleModule {
   EnumConfig<AutoTotemModule.OffhandItem> itemConfig;
   NumberConfig<Float> healthConfig;
   Config<Boolean> gappleConfig;
   Config<Boolean> crappleConfig;
   Config<Boolean> lethalConfig;
   Config<Boolean> fastConfig;
   Config<Boolean> debugConfig;
   private int lastHotbarSlot;
   private int lastTotemCount;
   private class_1792 lastHotbarItem;

   public AutoTotemModule() {
      super("AutoTotem", "Automatically replenishes the totem in your offhand", ModuleCategory.COMBAT);
      this.itemConfig = new EnumConfig("Item", "The item to wield in your offhand", AutoTotemModule.OffhandItem.TOTEM, AutoTotemModule.OffhandItem.values());
      this.healthConfig = new NumberConfig("Health", "The health required to fall below before swapping to a totem", 0.0F, 14.0F, 20.0F);
      this.gappleConfig = new BooleanConfig("OffhandGapple", "If to equip a golden apple if holding down the item use button", true);
      this.crappleConfig = new BooleanConfig("Crapple", "If to use a normal golden apple if Absorption is present", true);
      this.lethalConfig = new BooleanConfig("Lethal", "Calculate lethal damage sources", false);
      this.fastConfig = new BooleanConfig("FastSwap", "Swaps items to offhand", true);
      this.debugConfig = new BooleanConfig("Debug", "If to debug on death", false);
   }

   public String getModuleData() {
      return String.valueOf(Managers.INVENTORY.count(class_1802.field_8288));
   }

   public void onDisable() {
      super.onDisable();
      this.lastHotbarSlot = -1;
      this.lastHotbarItem = null;
      this.lastTotemCount = 0;
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      class_1792 itemToWield = this.getItemToWield();
      if (!mc.field_1724.method_6079().method_7909().equals(itemToWield)) {
         int itemSlot = this.getSlotFor(itemToWield);
         if (itemSlot != -1) {
            if (itemSlot < 9) {
               this.lastHotbarItem = itemToWield;
               this.lastHotbarSlot = itemSlot;
            }

            if ((Boolean)this.fastConfig.getValue()) {
               mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, itemSlot < 9 ? itemSlot + 36 : itemSlot, 40, class_1713.field_7791, mc.field_1724);
            } else {
               mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, itemSlot < 9 ? itemSlot + 36 : itemSlot, 0, class_1713.field_7790, mc.field_1724);
               mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, 45, 0, class_1713.field_7790, mc.field_1724);
               if (!mc.field_1724.field_7498.method_34255().method_7960()) {
                  mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, itemSlot < 9 ? itemSlot + 36 : itemSlot, 0, class_1713.field_7790, mc.field_1724);
               }
            }

            this.lastTotemCount = Managers.INVENTORY.count(class_1802.field_8288) - 1;
         }

      }
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2749) {
         class_2749 packet = (class_2749)var3;
         if (packet.method_11833() <= 0.0F && (Boolean)this.debugConfig.getValue()) {
            Set<String> reasons = new LinkedHashSet();
            if (this.lastTotemCount <= 0) {
               reasons.add("no_totems");
            }

            if (mc.field_1724.field_7512.field_7763 != 0) {
               reasons.add("gui_fail(" + mc.field_1724.field_7512.field_7763 + ")");
            }

            if (!mc.field_1724.field_7512.method_34255().method_7960()) {
               reasons.add("cursor_stack=" + mc.field_1724.field_7512.method_34255().method_7909());
            }

            if (!reasons.isEmpty()) {
               this.sendModuleMessage("Possible failure reasons: " + String.join(", ", reasons));
            } else {
               int totemCount = Managers.INVENTORY.count(class_1802.field_8288);
               this.sendModuleMessage("Could not figure out possible reasons. meta:{totemCount=" + totemCount + ", matchesCache=" + (totemCount == this.lastTotemCount) + ", cached=" + this.lastTotemCount + "}");
            }
         }
      }

   }

   private int getSlotFor(class_1792 item) {
      if (this.lastHotbarSlot != -1 && this.lastHotbarItem != null) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(this.lastHotbarSlot);
         if (stack.method_7909().equals(item) && this.lastHotbarItem.equals(mc.field_1724.method_6079().method_7909())) {
            int tmp = this.lastHotbarSlot;
            this.lastHotbarSlot = -1;
            this.lastHotbarItem = null;
            return tmp;
         }
      }

      for(int slot = 36; slot >= 0; --slot) {
         class_1799 itemStack = mc.field_1724.method_31548().method_5438(slot);
         if (!itemStack.method_7960() && itemStack.method_7909().equals(item)) {
            return slot;
         }
      }

      return -1;
   }

   private class_1792 getItemToWield() {
      float health = PlayerUtil.getLocalPlayerHealth();
      if (health <= (Float)this.healthConfig.getValue()) {
         return class_1802.field_8288;
      } else if ((float)PlayerUtil.computeFallDamage(mc.field_1724.field_6017, 1.0F) + 0.5F > mc.field_1724.method_6032()) {
         return class_1802.field_8288;
      } else {
         if ((Boolean)this.lethalConfig.getValue()) {
            List<class_1297> entities = Lists.newArrayList(mc.field_1687.method_18112());
            Iterator var3 = entities.iterator();

            while(var3.hasNext()) {
               class_1297 e = (class_1297)var3.next();
               if (e != null && e.method_5805() && e instanceof class_1511) {
                  class_1511 crystal = (class_1511)e;
                  if (!(mc.field_1724.method_5858(e) > 144.0D)) {
                     double potential = ExplosionUtil.getDamageTo(mc.field_1724, crystal.method_19538());
                     if (!((double)health + 0.5D > potential)) {
                        return class_1802.field_8288;
                     }
                  }
               }
            }
         }

         return !(Boolean)this.gappleConfig.getValue() || !mc.field_1690.field_1904.method_1434() || !(mc.field_1724.method_6047().method_7909() instanceof class_1829) && !(mc.field_1724.method_6047().method_7909() instanceof class_1835) && !(mc.field_1724.method_6047().method_7909() instanceof class_1743) ? ((AutoTotemModule.OffhandItem)this.itemConfig.getValue()).getItem() : this.getGoldenAppleType();
      }
   }

   private class_1792 getGoldenAppleType() {
      return (Boolean)this.crappleConfig.getValue() && mc.field_1724.method_6059(class_1294.field_5898) && InventoryUtil.hasItemInInventory(class_1802.field_8463, true) ? class_1802.field_8463 : class_1802.field_8367;
   }

   private static enum OffhandItem {
      TOTEM(class_1802.field_8288),
      GAPPLE(class_1802.field_8367),
      CRYSTAL(class_1802.field_8301);

      private final class_1792 item;

      private OffhandItem(class_1792 item) {
         this.item = item;
      }

      public class_1792 getItem() {
         return this.item;
      }

      // $FF: synthetic method
      private static AutoTotemModule.OffhandItem[] $values() {
         return new AutoTotemModule.OffhandItem[]{TOTEM, GAPPLE, CRYSTAL};
      }
   }
}
