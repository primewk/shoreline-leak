package net.shoreline.client.util.world;

import com.google.common.collect.Multimap;
import java.util.Iterator;
import java.util.function.BiFunction;
import net.minecraft.class_1280;
import net.minecraft.class_1282;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1320;
import net.minecraft.class_1322;
import net.minecraft.class_1324;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1922;
import net.minecraft.class_1927;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import net.minecraft.class_5132;
import net.minecraft.class_5134;
import net.minecraft.class_5135;
import net.shoreline.client.util.Globals;

public class ExplosionUtil implements Globals {
   public static double getDamageTo(class_1297 entity, class_243 explosion) {
      return getDamageTo(entity, explosion, false);
   }

   public static double getDamageTo(class_1297 entity, class_243 explosion, boolean ignoreTerrain) {
      return getDamageTo(entity, explosion, ignoreTerrain, 12.0F);
   }

   public static double getDamageTo(class_1297 entity, class_243 explosion, boolean ignoreTerrain, float power) {
      double d = Math.sqrt(entity.method_5707(explosion));
      double ab = (double)getExposure(explosion, entity, ignoreTerrain);
      double w = d / (double)power;
      double ac = (1.0D - w) * ab;
      double dmg = (double)((float)((int)((ac * ac + ac) / 2.0D * 7.0D * 12.0D + 1.0D)));
      dmg = getReduction(entity, mc.field_1687.method_48963().method_48807((class_1927)null), dmg);
      return Math.max(0.0D, dmg);
   }

   public static double getDamageToPos(class_243 pos, class_1297 entity, class_243 explosion, boolean ignoreTerrain) {
      class_238 bb = entity.method_5829();
      double dx = pos.method_10216() - bb.field_1323;
      double dy = pos.method_10214() - bb.field_1322;
      double dz = pos.method_10215() - bb.field_1321;
      class_238 box = bb.method_989(dx, dy, dz);
      double ab = (double)getExposure(explosion, box, ignoreTerrain);
      double w = Math.sqrt(pos.method_1025(explosion)) / 12.0D;
      double ac = (1.0D - w) * ab;
      double dmg = (double)((float)((int)((ac * ac + ac) / 2.0D * 7.0D * 12.0D + 1.0D)));
      dmg = getReduction(entity, mc.field_1687.method_48963().method_48807((class_1927)null), dmg);
      return Math.max(0.0D, dmg);
   }

   private static double getReduction(class_1297 entity, class_1282 damageSource, double damage) {
      if (damageSource.method_5514()) {
         switch(mc.field_1687.method_8407()) {
         case field_5805:
            damage = Math.min(damage / 2.0D + 1.0D, damage);
            break;
         case field_5807:
            damage *= 1.5D;
         }
      }

      if (entity instanceof class_1309) {
         class_1309 livingEntity = (class_1309)entity;
         damage = (double)class_1280.method_5496((float)damage, getArmor(livingEntity), (float)getAttributeValue(livingEntity, class_5134.field_23725));
         damage = (double)getProtectionReduction(entity, damage, damageSource);
      }

      return Math.max(damage, 0.0D);
   }

   private static float getArmor(class_1309 entity) {
      return (float)Math.floor(getAttributeValue(entity, class_5134.field_23724));
   }

   private static float getProtectionReduction(class_1297 player, double damage, class_1282 source) {
      int protLevel = class_1890.method_8219(player.method_5661(), source);
      return class_1280.method_5497((float)damage, (float)protLevel);
   }

   public static double getAttributeValue(class_1309 entity, class_1320 attribute) {
      return getAttributeInstance(entity, attribute).method_6194();
   }

   public static class_1324 getAttributeInstance(class_1309 entity, class_1320 attribute) {
      double baseValue = getDefaultForEntity(entity).method_26864(attribute);
      class_1324 attributeInstance = new class_1324(attribute, (o1) -> {
      });
      attributeInstance.method_6192(baseValue);
      class_1304[] var5 = class_1304.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         class_1304 equipmentSlot = var5[var7];
         class_1799 stack = entity.method_6118(equipmentSlot);
         Multimap<class_1320, class_1322> modifiers = stack.method_7926(equipmentSlot);
         Iterator var11 = modifiers.get(attribute).iterator();

         while(var11.hasNext()) {
            class_1322 modifier = (class_1322)var11.next();
            attributeInstance.method_26835(modifier);
         }
      }

      return attributeInstance;
   }

   private static <T extends class_1309> class_5132 getDefaultForEntity(T entity) {
      return class_5135.method_26873(entity.method_5864());
   }

   private static float getExposure(class_243 source, class_1297 entity, boolean ignoreTerrain) {
      class_238 box = entity.method_5829();
      return getExposure(source, box, ignoreTerrain);
   }

   private static float getExposure(class_243 source, class_238 box, boolean ignoreTerrain) {
      ExplosionUtil.RaycastFactory raycastFactory = getRaycastFactory(ignoreTerrain);
      double xDiff = box.field_1320 - box.field_1323;
      double yDiff = box.field_1325 - box.field_1322;
      double zDiff = box.field_1324 - box.field_1321;
      double xStep = 1.0D / (xDiff * 2.0D + 1.0D);
      double yStep = 1.0D / (yDiff * 2.0D + 1.0D);
      double zStep = 1.0D / (zDiff * 2.0D + 1.0D);
      if (xStep > 0.0D && yStep > 0.0D && zStep > 0.0D) {
         int misses = 0;
         int hits = 0;
         double xOffset = (1.0D - Math.floor(1.0D / xStep) * xStep) * 0.5D;
         double zOffset = (1.0D - Math.floor(1.0D / zStep) * zStep) * 0.5D;
         xStep *= xDiff;
         yStep *= yDiff;
         zStep *= zDiff;
         double startX = box.field_1323 + xOffset;
         double startY = box.field_1322;
         double startZ = box.field_1321 + zOffset;
         double endX = box.field_1320 + xOffset;
         double endY = box.field_1325;
         double endZ = box.field_1324 + zOffset;

         for(double x = startX; x <= endX; x += xStep) {
            for(double y = startY; y <= endY; y += yStep) {
               for(double z = startZ; z <= endZ; z += zStep) {
                  class_243 position = new class_243(x, y, z);
                  if (raycast(new ExplosionUtil.ExposureRaycastContext(position, source), raycastFactory) == null) {
                     ++misses;
                  }

                  ++hits;
               }
            }
         }

         return (float)misses / (float)hits;
      } else {
         return 0.0F;
      }
   }

   private static ExplosionUtil.RaycastFactory getRaycastFactory(boolean ignoreTerrain) {
      return ignoreTerrain ? (context, blockPos) -> {
         class_2680 blockState = mc.field_1687.method_8320(blockPos);
         return blockState.method_26204().method_9520() < 600.0F ? null : blockState.method_26220(mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
      } : (context, blockPos) -> {
         class_2680 blockState = mc.field_1687.method_8320(blockPos);
         return blockState.method_26220(mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
      };
   }

   private static class_3965 raycast(ExplosionUtil.ExposureRaycastContext context, ExplosionUtil.RaycastFactory raycastFactory) {
      return (class_3965)class_1922.method_17744(context.start, context.end, context, raycastFactory, (ctx) -> {
         return null;
      });
   }

   @FunctionalInterface
   public interface RaycastFactory extends BiFunction<ExplosionUtil.ExposureRaycastContext, class_2338, class_3965> {
   }

   public static record ExposureRaycastContext(class_243 start, class_243 end) {
      public ExposureRaycastContext(class_243 start, class_243 end) {
         this.start = start;
         this.end = end;
      }

      public class_243 start() {
         return this.start;
      }

      public class_243 end() {
         return this.end;
      }
   }
}
