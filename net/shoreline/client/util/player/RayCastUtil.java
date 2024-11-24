package net.shoreline.client.util.player;

import net.minecraft.class_1675;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3959;
import net.minecraft.class_4184;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import net.shoreline.client.util.Globals;

public final class RayCastUtil implements Globals {
   public static class_239 raycastEntity(double reach, class_243 position, float[] angles) {
      class_4184 view = mc.field_1773.method_19418();
      class_243 vec3d2 = RotationUtil.getRotationVector(angles[1], angles[0]);
      class_243 vec3d3 = position.method_1031(vec3d2.field_1352 * reach, vec3d2.field_1351 * reach, vec3d2.field_1350 * reach);
      class_238 box = (new class_238(position, position)).method_18804(vec3d2.method_1021(reach)).method_1009(1.0D, 1.0D, 1.0D);
      return class_1675.method_18075(view.method_19331(), position, vec3d3, box, (entity) -> {
         return !entity.method_7325() && entity.method_5863();
      }, reach * reach);
   }

   public static class_239 raycastEntity(double reach) {
      class_4184 view = mc.field_1773.method_19418();
      class_243 vec3d = view.method_19326();
      class_243 vec3d2 = RotationUtil.getRotationVector(view.method_19329(), view.method_19330());
      class_243 vec3d3 = vec3d.method_1031(vec3d2.field_1352 * reach, vec3d2.field_1351 * reach, vec3d2.field_1350 * reach);
      class_238 box = view.method_19331().method_5829().method_18804(vec3d2.method_1021(reach)).method_1009(1.0D, 1.0D, 1.0D);
      return class_1675.method_18075(view.method_19331(), vec3d, vec3d3, box, (entity) -> {
         return !entity.method_7325() && entity.method_5863();
      }, reach * reach);
   }

   public static class_239 rayCast(double reach, float[] angles) {
      double eyeHeight = (double)mc.field_1724.method_5751();
      class_243 eyes = new class_243(mc.field_1724.method_23317(), mc.field_1724.method_23318() + eyeHeight, mc.field_1724.method_23321());
      return rayCast(reach, eyes, angles);
   }

   public static class_239 rayCast(double reach, class_243 position, float[] angles) {
      if (!Float.isNaN(angles[0]) && !Float.isNaN(angles[1])) {
         class_243 rotationVector = RotationUtil.getRotationVector(angles[1], angles[0]);
         return mc.field_1687.method_17742(new class_3959(position, position.method_1031(rotationVector.field_1352 * reach, rotationVector.field_1351 * reach, rotationVector.field_1350 * reach), class_3960.field_17558, class_242.field_1348, mc.field_1724));
      } else {
         return null;
      }
   }
}
