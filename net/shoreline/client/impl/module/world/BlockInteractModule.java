package net.shoreline.client.impl.module.world;

import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.NumberDisplay;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PlayerTickEvent;

public final class BlockInteractModule extends ToggleModule {
   Config<Float> range;
   Config<Boolean> fluids;

   public BlockInteractModule() {
      super("BlockInteract", "Allows you to place blocks in the air", ModuleCategory.WORLD);
      this.range = new NumberConfig("Range", "", 1.0F, 4.0F, 10.0F, NumberDisplay.DEFAULT);
      this.fluids = new BooleanConfig("Fluids", "", false);
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      class_1799 stack = mc.field_1724.method_6047();
      if (!stack.method_7960() && stack.method_7909() instanceof class_1747 && mc.field_1690.field_1904.method_1434()) {
         class_239 result = mc.field_1724.method_5745((double)(Float)this.range.getValue(), 1.0F, (Boolean)this.fluids.getValue());
         if (result instanceof class_3965) {
            class_3965 blockHitResult = (class_3965)result;
            class_1269 actionResult = mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, blockHitResult);
            if (actionResult.method_23665() && actionResult.method_23666()) {
               mc.field_1724.method_6104(class_1268.field_5808);
            }
         }

      }
   }
}
