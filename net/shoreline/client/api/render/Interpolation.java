package net.shoreline.client.api.render;

import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.shoreline.client.util.Globals;

public class Interpolation implements Globals {
   public static class_243 getRenderPosition(class_1297 entity, float tickDelta) {
      return new class_243(entity.method_23317() - class_3532.method_16436((double)tickDelta, entity.field_6038, entity.method_23317()), entity.method_23318() - class_3532.method_16436((double)tickDelta, entity.field_5971, entity.method_23318()), entity.method_23321() - class_3532.method_16436((double)tickDelta, entity.field_5989, entity.method_23321()));
   }

   public static class_243 getInterpolatedPosition(class_1297 entity, float tickDelta) {
      return new class_243(entity.field_6014 + (entity.method_23317() - entity.field_6014) * (double)tickDelta, entity.field_6036 + (entity.method_23318() - entity.field_6036) * (double)tickDelta, entity.field_5969 + (entity.method_23321() - entity.field_5969) * (double)tickDelta);
   }

   public static float interpolateFloat(float prev, float value, float factor) {
      return prev + (value - prev) * factor;
   }

   public static double interpolateDouble(double prev, double value, double factor) {
      return prev + (value - prev) * factor;
   }

   public static class_238 getInterpolatedBox(class_238 prevBox, class_238 box) {
      double delta = mc.method_1493() ? 1.0D : (double)mc.method_1488();
      return new class_238(interpolateDouble(prevBox.field_1323, box.field_1323, delta), interpolateDouble(prevBox.field_1322, box.field_1322, delta), interpolateDouble(prevBox.field_1321, box.field_1321, delta), interpolateDouble(prevBox.field_1320, box.field_1320, delta), interpolateDouble(prevBox.field_1325, box.field_1325, delta), interpolateDouble(prevBox.field_1324, box.field_1324, delta));
   }

   public static class_238 getInterpolatedEntityBox(class_1297 entity) {
      class_238 box = entity.method_5829();
      class_238 prevBox = entity.method_5829().method_989(entity.field_6014 - entity.method_23317(), entity.field_6036 - entity.method_23318(), entity.field_5969 - entity.method_23321());
      return getInterpolatedBox(prevBox, box);
   }
}
