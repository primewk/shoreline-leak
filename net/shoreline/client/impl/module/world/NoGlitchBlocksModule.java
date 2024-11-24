package net.shoreline.client.impl.module.world;

import net.minecraft.class_2680;
import net.minecraft.class_2885;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.BreakBlockEvent;
import net.shoreline.client.impl.event.network.InteractBlockEvent;
import net.shoreline.client.init.Managers;

public class NoGlitchBlocksModule extends ToggleModule {
   Config<Boolean> placeConfig = new BooleanConfig("Place", "Places blocks only after the server confirms", true);
   Config<Boolean> destroyConfig = new BooleanConfig("Destroy", "Destroys blocks only after the server confirms", true);

   public NoGlitchBlocksModule() {
      super("NoGlitchBlocks", "Prevents blocks from being glitched in the world", ModuleCategory.WORLD);
   }

   @EventListener
   public void onInteractBlock(InteractBlockEvent event) {
      if ((Boolean)this.placeConfig.getValue() && !mc.method_1542()) {
         event.cancel();
         Managers.NETWORK.sendSequencedPacket((id) -> {
            return new class_2885(event.getHand(), event.getHitResult(), id);
         });
      }

   }

   @EventListener
   public void onBreakBlock(BreakBlockEvent event) {
      if ((Boolean)this.destroyConfig.getValue() && !mc.method_1542()) {
         event.cancel();
         class_2680 state = mc.field_1687.method_8320(event.getPos());
         state.method_26204().method_9576(mc.field_1687, event.getPos(), state, mc.field_1724);
      }

   }
}
