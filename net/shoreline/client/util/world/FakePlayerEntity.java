package net.shoreline.client.util.world;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_745;
import net.minecraft.class_1297.class_5529;
import net.shoreline.client.util.Globals;

public class FakePlayerEntity extends class_745 implements Globals {
   public static final AtomicInteger CURRENT_ID = new AtomicInteger(1000000);
   private final class_1657 player;

   public FakePlayerEntity(class_1657 player, String name) {
      super(class_310.method_1551().field_1687, new GameProfile(UUID.fromString("8667ba71-b85a-4004-af54-457a9734eed7"), name));
      this.player = player;
      this.method_5719(player);
      this.field_5982 = this.method_36454();
      this.field_6004 = this.method_36455();
      this.field_6241 = player.field_6241;
      this.field_6259 = this.field_6241;
      this.field_6283 = player.field_6283;
      this.field_6220 = this.field_6283;
      Byte playerModel = (Byte)player.method_5841().method_12789(class_1657.field_7518);
      this.field_6011.method_12778(class_1657.field_7518, playerModel);
      this.method_6127().method_26846(player.method_6127());
      this.method_18380(player.method_18376());
      this.method_6033(player.method_6032());
      this.method_6073(player.method_6067());
      this.method_31548().method_7377(player.method_31548());
      this.method_5838(CURRENT_ID.incrementAndGet());
      this.field_6012 = 100;
   }

   public FakePlayerEntity(class_1657 player, GameProfile profile) {
      super(class_310.method_1551().field_1687, profile);
      this.player = player;
      this.method_5719(player);
      this.field_5982 = this.method_36454();
      this.field_6004 = this.method_36455();
      this.field_6241 = player.field_6241;
      this.field_6259 = this.field_6241;
      this.field_6283 = player.field_6283;
      this.field_6220 = this.field_6283;
      Byte playerModel = (Byte)player.method_5841().method_12789(class_1657.field_7518);
      this.field_6011.method_12778(class_1657.field_7518, playerModel);
      this.method_6127().method_26846(player.method_6127());
      this.method_18380(player.method_18376());
      this.method_6033(player.method_6032());
      this.method_6073(player.method_6067());
      this.method_31548().method_7377(player.method_31548());
      this.method_5838(CURRENT_ID.incrementAndGet());
      this.field_6012 = 100;
   }

   public FakePlayerEntity(class_1657 player) {
      this(player, player.method_5477().getString());
   }

   public void spawnPlayer() {
      if (mc.field_1687 != null) {
         this.method_31482();
         mc.field_1687.method_53875(this);
      }

   }

   public void despawnPlayer() {
      if (mc.field_1687 != null) {
         mc.field_1687.method_2945(this.method_5628(), class_5529.field_26999);
         this.method_31745(class_5529.field_26999);
      }

   }

   public boolean method_29504() {
      return false;
   }

   public class_1657 getPlayer() {
      return this.player;
   }
}
