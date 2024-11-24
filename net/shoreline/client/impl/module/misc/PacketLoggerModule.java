package net.shoreline.client.impl.module.misc;

import net.minecraft.class_2596;
import net.minecraft.class_2793;
import net.minecraft.class_2799;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2833;
import net.minecraft.class_2838;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.minecraft.class_3965;
import net.minecraft.class_6374;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2830;
import net.minecraft.class_2828.class_2831;
import net.minecraft.class_2828.class_5911;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.imixin.IPlayerInteractEntityC2SPacket;

public class PacketLoggerModule extends ToggleModule {
   Config<Boolean> chatConfig = new BooleanConfig("LogChat", "Logs packets in the chats", false);
   Config<Boolean> moveFullConfig = new BooleanConfig("PlayerMoveFull", "Logs PlayerMoveC2SPacket", false);
   Config<Boolean> moveLookConfig = new BooleanConfig("PlayerMoveLook", "Logs PlayerMoveC2SPacket", false);
   Config<Boolean> movePosConfig = new BooleanConfig("PlayerMovePosition", "Logs PlayerMoveC2SPacket", false);
   Config<Boolean> moveGroundConfig = new BooleanConfig("PlayerMoveGround", "Logs PlayerMoveC2SPacket", false);
   Config<Boolean> vehicleMoveConfig = new BooleanConfig("VehicleMove", "Logs VehicleMoveC2SPacket", false);
   Config<Boolean> playerActionConfig = new BooleanConfig("PlayerAction", "Logs PlayerActionC2SPacket", false);
   Config<Boolean> updateSlotConfig = new BooleanConfig("UpdateSelectedSlot", "Logs UpdateSelectedSlotC2SPacket", false);
   Config<Boolean> clickSlotConfig = new BooleanConfig("ClickSlot", "Logs ClickSlotC2SPacket", false);
   Config<Boolean> pickInventoryConfig = new BooleanConfig("PickInventory", "Logs PickFromInventoryC2SPacket", false);
   Config<Boolean> handSwingConfig = new BooleanConfig("HandSwing", "Logs HandSwingC2SPacket", false);
   Config<Boolean> interactEntityConfig = new BooleanConfig("InteractEntity", "Logs PlayerInteractEntityC2SPacket", false);
   Config<Boolean> interactBlockConfig = new BooleanConfig("InteractBlock", "Logs PlayerInteractBlockC2SPacket", false);
   Config<Boolean> interactItemConfig = new BooleanConfig("InteractItem", "Logs PlayerInteractItemC2SPacket", false);
   Config<Boolean> commandConfig = new BooleanConfig("ClientCommand", "Logs ClientCommandC2SPacket", false);
   Config<Boolean> statusConfig = new BooleanConfig("ClientStatus", "Logs ClientStatusC2SPacket", false);
   Config<Boolean> closeScreenConfig = new BooleanConfig("CloseScreen", "Logs CloseHandledScreenC2SPacket", false);
   Config<Boolean> teleportConfirmConfig = new BooleanConfig("TeleportConfirm", "Logs TeleportConfirmC2SPacket", false);
   Config<Boolean> pongConfig = new BooleanConfig("Pong", "Logs CommonPongC2SPacket", false);

   public PacketLoggerModule() {
      super("PacketLogger", "Logs client packets", ModuleCategory.MISCELLANEOUS);
   }

   private void logPacket(String msg, Object... args) {
      String s = String.format(msg, args);
      if ((Boolean)this.chatConfig.getValue()) {
         this.sendModuleMessage(s);
      } else {
         System.out.println(s);
      }

   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      class_2596 var3 = event.getPacket();
      StringBuilder builder;
      if (var3 instanceof class_2830) {
         class_2830 packet = (class_2830)var3;
         if ((Boolean)this.moveFullConfig.getValue()) {
            builder = new StringBuilder();
            builder.append("PlayerMove Full - ");
            if (packet.method_36171()) {
               builder.append("x: ").append(packet.method_12269(0.0D)).append(", y: ").append(packet.method_12268(0.0D)).append(", z: ").append(packet.method_12274(0.0D)).append(" ");
            }

            if (packet.method_36172()) {
               builder.append("yaw: ").append(packet.method_12271(0.0F)).append(", pitch: ").append(packet.method_12270(0.0F)).append(" ");
            }

            builder.append(" onground: ").append(packet.method_12273());
            this.logPacket(builder.toString());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2829) {
         class_2829 packet = (class_2829)var3;
         if ((Boolean)this.movePosConfig.getValue()) {
            builder = new StringBuilder();
            builder.append("PlayerMove PosGround - ");
            if (packet.method_36171()) {
               builder.append("x: ").append(packet.method_12269(0.0D)).append(", y: ").append(packet.method_12268(0.0D)).append(", z: ").append(packet.method_12274(0.0D)).append(" ");
            }

            builder.append(" onground: ").append(packet.method_12273());
            this.logPacket(builder.toString());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2831) {
         class_2831 packet = (class_2831)var3;
         if ((Boolean)this.moveLookConfig.getValue()) {
            builder = new StringBuilder();
            builder.append("PlayerMove LookGround - ");
            if (packet.method_36172()) {
               builder.append("yaw: ").append(packet.method_12271(0.0F)).append(", pitch: ").append(packet.method_12270(0.0F)).append(" ");
            }

            builder.append(" onground: ").append(packet.method_12273());
            this.logPacket(builder.toString());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_5911) {
         class_5911 packet = (class_5911)var3;
         if ((Boolean)this.moveGroundConfig.getValue()) {
            String s = "PlayerMove Ground - onground: " + packet.method_12273();
            this.logPacket(s);
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2833) {
         class_2833 packet = (class_2833)var3;
         if ((Boolean)this.vehicleMoveConfig.getValue()) {
            this.logPacket("VehicleMove - x: %s, y: %s, z: %s, yaw: %s, pitch: %s", packet.method_12279(), packet.method_12280(), packet.method_12276(), packet.method_12281(), packet.method_12277());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2846) {
         class_2846 packet = (class_2846)var3;
         if ((Boolean)this.playerActionConfig.getValue()) {
            this.logPacket("PlayerAction - action: %s, direction: %s, pos: %s", packet.method_12363().name(), packet.method_12360().name(), packet.method_12362().method_23854());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2868) {
         class_2868 packet = (class_2868)var3;
         if ((Boolean)this.updateSlotConfig.getValue()) {
            this.logPacket("UpdateSlot - slot: %d", packet.method_12442());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2879) {
         class_2879 packet = (class_2879)var3;
         if ((Boolean)this.handSwingConfig.getValue()) {
            this.logPacket("HandSwing - hand: %s", packet.method_12512().name());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_6374) {
         class_6374 packet = (class_6374)var3;
         if ((Boolean)this.pongConfig.getValue()) {
            this.logPacket("Pong - %d", packet.method_36960());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof IPlayerInteractEntityC2SPacket) {
         IPlayerInteractEntityC2SPacket packet = (IPlayerInteractEntityC2SPacket)var3;
         if ((Boolean)this.interactEntityConfig.getValue()) {
            this.logPacket("InteractEntity - %s", packet.getEntity().method_5477().getString());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2885) {
         class_2885 packet = (class_2885)var3;
         if ((Boolean)this.interactBlockConfig.getValue()) {
            class_3965 blockHitResult = packet.method_12543();
            this.logPacket("InteractBlock - pos: %s, dir: %s, hand: %s", blockHitResult.method_17777().method_23854(), blockHitResult.method_17780().name(), packet.method_12546().name());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2886) {
         class_2886 packet = (class_2886)var3;
         if ((Boolean)this.interactItemConfig.getValue()) {
            this.logPacket("InteractItem - hand: %s", packet.method_12551().name());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2815) {
         class_2815 packet = (class_2815)var3;
         if ((Boolean)this.closeScreenConfig.getValue()) {
            this.logPacket("CloseScreen - id: %s", packet.method_36168());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2848) {
         class_2848 packet = (class_2848)var3;
         if ((Boolean)this.commandConfig.getValue()) {
            this.logPacket("ClientCommand - mode: %s", packet.method_12365().name());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2799) {
         class_2799 packet = (class_2799)var3;
         if ((Boolean)this.statusConfig.getValue()) {
            this.logPacket("ClientStatus - mode: %s", packet.method_12119().name());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2813) {
         class_2813 packet = (class_2813)var3;
         if ((Boolean)this.clickSlotConfig.getValue()) {
            this.logPacket("ClickSlot - type: %s, slot: %s, button: %s, id: %s", packet.method_12195().name(), packet.method_12192(), packet.method_12193(), packet.method_12194());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2838) {
         class_2838 packet = (class_2838)var3;
         if ((Boolean)this.pickInventoryConfig.getValue()) {
            this.logPacket("PickInventory - slot: %s", packet.method_12293());
         }
      }

      var3 = event.getPacket();
      if (var3 instanceof class_2793) {
         class_2793 packet = (class_2793)var3;
         if ((Boolean)this.teleportConfirmConfig.getValue()) {
            this.logPacket("TeleportConfirm - id: %s", packet.method_12086());
         }
      }

   }
}
