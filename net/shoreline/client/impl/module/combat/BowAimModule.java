package net.shoreline.client.impl.module.combat;

import java.util.Iterator;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1753;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.impl.event.entity.LookDirectionEvent;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.world.EntityUtil;

public class BowAimModule extends RotationModule {
   Config<Boolean> playersConfig = new BooleanConfig("Players", "Aims bow at players", true);
   Config<Boolean> monstersConfig = new BooleanConfig("Monsters", "Aims bow at monsters", false);
   Config<Boolean> neutralsConfig = new BooleanConfig("Neutrals", "Aims bow at neutrals", false);
   Config<Boolean> animalsConfig = new BooleanConfig("Animals", "Aims bow at animals", false);
   Config<Boolean> invisiblesConfig = new BooleanConfig("Invisibles", "Aims bow at invisible entities", false);
   private class_1297 aimTarget;

   public BowAimModule() {
      super("BowAim", "Automatically aims charged bow at nearby entities", ModuleCategory.COMBAT);
   }

   @EventListener
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (event.getStage() == EventStage.PRE) {
         this.aimTarget = null;
         if (mc.field_1724.method_6047().method_7909() instanceof class_1753 && mc.field_1724.method_6048() >= 3) {
            double minDist = Double.MAX_VALUE;
            Iterator var4 = mc.field_1687.method_18112().iterator();

            class_1297 entity;
            while(var4.hasNext()) {
               entity = (class_1297)var4.next();
               if (entity != null && entity != mc.field_1724 && entity.method_5805() && this.isValidAimTarget(entity) && !Managers.SOCIAL.isFriend(entity.method_5477())) {
                  double dist = (double)mc.field_1724.method_5739(entity);
                  if (dist < minDist) {
                     minDist = dist;
                     this.aimTarget = entity;
                  }
               }
            }

            entity = this.aimTarget;
            if (entity instanceof class_1309) {
               class_1309 target = (class_1309)entity;
               float[] rotations = this.getBowRotationsTo(target);
               this.setRotationClient(rotations[0], rotations[1]);
            }
         }

      }
   }

   @EventListener
   public void onLookDirection(LookDirectionEvent event) {
      if (this.aimTarget != null) {
         event.cancel();
      }

   }

   private float[] getBowRotationsTo(class_1297 entity) {
      float duration = (float)(mc.field_1724.method_6030().method_7935() - mc.field_1724.method_6048()) / 20.0F;
      duration = (duration * duration + duration * 2.0F) / 3.0F;
      if (duration >= 1.0F) {
         duration = 1.0F;
      }

      double duration1 = (double)(duration * 3.0F);
      double coeff = 0.05000000074505806D;
      float pitch = (float)(-Math.toDegrees((double)this.calculateArc(entity, duration1, coeff)));
      double ix = entity.method_23317() - entity.field_6014;
      double iz = entity.method_23321() - entity.field_5969;
      double d = (double)mc.field_1724.method_5739(entity);
      d -= d % 2.0D;
      ix = d / 2.0D * ix * (mc.field_1724.method_5624() ? 1.3D : 1.1D);
      iz = d / 2.0D * iz * (mc.field_1724.method_5624() ? 1.3D : 1.1D);
      float yaw = (float)Math.toDegrees(Math.atan2(entity.method_23321() + iz - mc.field_1724.method_23321(), entity.method_23317() + ix - mc.field_1724.method_23317())) - 90.0F;
      return new float[]{yaw, pitch};
   }

   private float calculateArc(class_1297 target, double duration, double coeff) {
      double yArc = target.method_23318() + (double)(target.method_5751() / 2.0F) - (mc.field_1724.method_23318() + (double)mc.field_1724.method_5751());
      double dX = target.method_23317() - mc.field_1724.method_23317();
      double dZ = target.method_23321() - mc.field_1724.method_23321();
      double dirRoot = Math.sqrt(dX * dX + dZ * dZ);
      return this.calculateArc(duration, coeff, dirRoot, yArc);
   }

   private float calculateArc(double duration, double coeff, double root, double yArc) {
      double dirCoeff = coeff * root * root;
      yArc = 2.0D * yArc * duration * duration;
      yArc = coeff * (dirCoeff + yArc);
      yArc = Math.sqrt(duration * duration * duration * duration - yArc);
      duration = duration * duration - yArc;
      yArc = Math.atan2(duration * duration + yArc, coeff * root);
      duration = Math.atan2(duration, coeff * root);
      return (float)Math.min(yArc, duration);
   }

   private boolean isValidAimTarget(class_1297 entity) {
      if (entity.method_5767() && !(Boolean)this.invisiblesConfig.getValue()) {
         return false;
      } else {
         return entity instanceof class_1657 && (Boolean)this.playersConfig.getValue() || EntityUtil.isMonster(entity) && (Boolean)this.monstersConfig.getValue() || EntityUtil.isNeutral(entity) && (Boolean)this.neutralsConfig.getValue() || EntityUtil.isPassive(entity) && (Boolean)this.animalsConfig.getValue();
      }
   }
}
