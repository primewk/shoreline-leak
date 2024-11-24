package net.shoreline.client.impl.module.misc;

import java.util.Optional;
import net.minecraft.class_1291;
import net.minecraft.class_1704;
import net.minecraft.class_2596;
import net.minecraft.class_2866;
import net.minecraft.class_437;
import net.minecraft.class_466;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.ScreenOpenEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.gui.beacon.BeaconSelectorScreen;
import net.shoreline.client.mixin.accessor.AccessorUpdateBeaconC2SPacket;

public class BeaconSelectorModule extends ToggleModule {
   private class_1291 primaryEffect;
   private class_1291 secondaryEffect;
   private boolean customBeacon;

   public BeaconSelectorModule() {
      super("BeaconSelector", "Allows you to change beacon effects", ModuleCategory.MISCELLANEOUS);
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2866) {
         class_2866 packet = (class_2866)var3;
         ((AccessorUpdateBeaconC2SPacket)packet).setPrimaryEffect(Optional.ofNullable(this.primaryEffect));
         ((AccessorUpdateBeaconC2SPacket)packet).setSecondaryEffect(Optional.ofNullable(this.secondaryEffect));
      }

   }

   @EventListener
   public void onScreenOpen(ScreenOpenEvent event) {
      class_437 var3 = event.getScreen();
      if (var3 instanceof class_466) {
         class_466 screen = (class_466)var3;
         if (!this.customBeacon) {
            event.cancel();
            this.customBeacon = true;
            mc.method_1507(new BeaconSelectorScreen((class_1704)screen.method_17577(), mc.field_1724.method_31548(), screen.method_25440()));
            this.customBeacon = false;
         }
      }

   }
}
