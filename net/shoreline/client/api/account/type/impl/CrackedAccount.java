package net.shoreline.client.api.account.type.impl;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.class_320;
import net.minecraft.class_320.class_321;
import net.shoreline.client.api.account.type.MinecraftAccount;

public record CrackedAccount(String username) implements MinecraftAccount {
   public CrackedAccount(String username) {
      this.username = username;
   }

   public class_320 login() {
      return new class_320(this.username(), UUID.randomUUID(), "", Optional.empty(), Optional.empty(), class_321.field_1990);
   }

   public String username() {
      return this.username;
   }
}
