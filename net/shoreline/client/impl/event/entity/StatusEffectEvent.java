package net.shoreline.client.impl.event.entity;

import net.minecraft.class_1293;
import net.shoreline.client.api.event.Event;

public class StatusEffectEvent extends Event {
   private final class_1293 statusEffectInstance;

   public StatusEffectEvent(class_1293 statusEffectInstance) {
      this.statusEffectInstance = statusEffectInstance;
   }

   public class_1293 getStatusEffect() {
      return this.statusEffectInstance;
   }

   public static class Remove extends StatusEffectEvent {
      public Remove(class_1293 statusEffectInstance) {
         super(statusEffectInstance);
      }
   }

   public static class Add extends StatusEffectEvent {
      public Add(class_1293 statusEffectInstance) {
         super(statusEffectInstance);
      }
   }
}
