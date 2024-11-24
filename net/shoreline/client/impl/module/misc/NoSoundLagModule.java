package net.shoreline.client.impl.module.misc;

import java.util.Set;
import net.minecraft.class_2596;
import net.minecraft.class_2765;
import net.minecraft.class_2767;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PacketEvent;

public class NoSoundLagModule extends ToggleModule {
   private static final Set<class_3414> LAG_SOUNDS;

   public NoSoundLagModule() {
      super("NoSoundLag", "Prevents sound effects from lagging the game", ModuleCategory.MISCELLANEOUS);
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      label20: {
         class_2596 var4 = event.getPacket();
         if (var4 instanceof class_2765) {
            class_2765 packet = (class_2765)var4;
            if (LAG_SOUNDS.contains(packet.method_11882().comp_349())) {
               break label20;
            }
         }

         var4 = event.getPacket();
         if (!(var4 instanceof class_2767)) {
            return;
         }

         class_2767 packet2 = (class_2767)var4;
         if (!LAG_SOUNDS.contains(packet2.method_11894().comp_349())) {
            return;
         }
      }

      event.cancel();
   }

   static {
      LAG_SOUNDS = Set.of(class_3417.field_14883, class_3417.field_14966, class_3417.field_21866, class_3417.field_15103, class_3417.field_14862, class_3417.field_14761, class_3417.field_15191, class_3417.field_14581);
   }
}
