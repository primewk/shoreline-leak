package net.shoreline.client.impl.event.render.item;

import net.minecraft.class_1007;
import net.minecraft.class_1306;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class RenderArmEvent extends Event {
   public final class_4587 matrices;
   public final class_4597 vertexConsumers;
   public final int light;
   public final class_1306 arm;
   public final float equipProgress;
   public final float swingProgress;
   public final class_1007 playerEntityRenderer;

   public RenderArmEvent(class_4587 matrices, class_4597 vertexConsumers, int light, float equipProgress, float swingProgress, class_1306 arm, class_1007 playerEntityRenderer) {
      this.matrices = matrices;
      this.vertexConsumers = vertexConsumers;
      this.light = light;
      this.equipProgress = equipProgress;
      this.swingProgress = swingProgress;
      this.arm = arm;
      this.playerEntityRenderer = playerEntityRenderer;
   }
}
