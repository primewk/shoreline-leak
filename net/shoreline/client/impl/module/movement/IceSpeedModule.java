package net.shoreline.client.impl.module.movement;

import net.minecraft.class_2246;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.mixin.accessor.AccessorAbstractBlock;

public class IceSpeedModule extends ToggleModule {
   public IceSpeedModule() {
      super("IceSpeed", "Modifies the walking speed on ice", ModuleCategory.MOVEMENT);
   }

   public void onEnable() {
      if (mc.field_1687 != null) {
         ((AccessorAbstractBlock)class_2246.field_10295).setSlipperiness(0.4F);
         ((AccessorAbstractBlock)class_2246.field_10225).setSlipperiness(0.4F);
         ((AccessorAbstractBlock)class_2246.field_10384).setSlipperiness(0.4F);
         ((AccessorAbstractBlock)class_2246.field_10110).setSlipperiness(0.4F);
      }
   }

   public void onDisable() {
      if (mc.field_1687 != null) {
         ((AccessorAbstractBlock)class_2246.field_10295).setSlipperiness(0.98F);
         ((AccessorAbstractBlock)class_2246.field_10225).setSlipperiness(0.98F);
         ((AccessorAbstractBlock)class_2246.field_10384).setSlipperiness(0.98F);
         ((AccessorAbstractBlock)class_2246.field_10110).setSlipperiness(0.98F);
      }
   }
}
