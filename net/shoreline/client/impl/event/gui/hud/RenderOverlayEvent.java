package net.shoreline.client.impl.event.gui.hud;

import net.minecraft.class_332;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

public class RenderOverlayEvent extends Event {
   private final class_332 context;

   public RenderOverlayEvent(class_332 context) {
      this.context = context;
   }

   public class_332 getContext() {
      return this.context;
   }

   @Cancelable
   public static class Frostbite extends RenderOverlayEvent {
      public Frostbite(class_332 context) {
         super(context);
      }
   }

   @Cancelable
   public static class BossBar extends RenderOverlayEvent {
      public BossBar(class_332 context) {
         super(context);
      }
   }

   @Cancelable
   public static class Pumpkin extends RenderOverlayEvent {
      public Pumpkin(class_332 context) {
         super(context);
      }
   }

   @Cancelable
   public static class Spyglass extends RenderOverlayEvent {
      public Spyglass(class_332 context) {
         super(context);
      }
   }

   @Cancelable
   public static class Block extends RenderOverlayEvent {
      public Block(class_332 context) {
         super(context);
      }
   }

   @Cancelable
   public static class Water extends RenderOverlayEvent {
      public Water(class_332 context) {
         super(context);
      }
   }

   @Cancelable
   public static class Fire extends RenderOverlayEvent {
      public Fire(class_332 context) {
         super(context);
      }
   }

   @Cancelable
   public static class ItemName extends RenderOverlayEvent {
      private int x;
      private int y;

      public ItemName(class_332 context) {
         super(context);
      }

      public boolean isUpdateXY() {
         return this.x != 0 && this.y != 0;
      }

      public int getX() {
         return this.x;
      }

      public void setX(int x) {
         this.x = x;
      }

      public int getY() {
         return this.y;
      }

      public void setY(int y) {
         this.y = y;
      }
   }

   @Cancelable
   public static class StatusEffect extends RenderOverlayEvent {
      public StatusEffect(class_332 context) {
         super(context);
      }
   }

   public static class Post extends RenderOverlayEvent {
      private final float tickDelta;

      public Post(class_332 context, float tickDelta) {
         super(context);
         this.tickDelta = tickDelta;
      }

      public float getTickDelta() {
         return this.tickDelta;
      }
   }
}
