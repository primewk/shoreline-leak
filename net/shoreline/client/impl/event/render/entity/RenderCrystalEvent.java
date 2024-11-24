package net.shoreline.client.impl.event.render.entity;

import net.minecraft.class_1511;
import net.minecraft.class_4587;
import net.minecraft.class_630;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class RenderCrystalEvent extends Event {
   public final class_1511 endCrystalEntity;
   public final float f;
   public final float g;
   public final class_4587 matrixStack;
   public final int i;
   public final class_630 core;
   public final class_630 frame;

   public RenderCrystalEvent(class_1511 endCrystalEntity, float f, float g, class_4587 matrixStack, int i, class_630 core, class_630 frame) {
      this.endCrystalEntity = endCrystalEntity;
      this.f = f;
      this.g = g;
      this.matrixStack = matrixStack;
      this.i = i;
      this.core = core;
      this.frame = frame;
   }
}
