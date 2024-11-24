package net.shoreline.client.impl.event.render.entity;

import java.util.List;
import net.minecraft.class_1309;
import net.minecraft.class_1921;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_583;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class RenderEntityEvent<T extends class_1309> extends Event {
   public final class_1309 entity;
   public final float f;
   public final float g;
   public final class_4587 matrixStack;
   public final class_4597 vertexConsumerProvider;
   public final int i;
   public final class_1921 layer;
   public final class_583 model;
   public final List<class_3887<T, class_583<T>>> features;

   public RenderEntityEvent(class_1309 entity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, class_583 model, class_1921 layer, List<class_3887<T, class_583<T>>> features) {
      this.entity = entity;
      this.f = f;
      this.g = g;
      this.matrixStack = matrixStack;
      this.vertexConsumerProvider = vertexConsumerProvider;
      this.i = i;
      this.model = model;
      this.layer = layer;
      this.features = features;
   }
}
