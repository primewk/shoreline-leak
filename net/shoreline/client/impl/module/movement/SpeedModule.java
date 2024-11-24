package net.shoreline.client.impl.module.movement;

import java.util.Iterator;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_3532;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.config.ConfigUpdateEvent;
import net.shoreline.client.impl.event.entity.player.PlayerMoveEvent;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.math.MathUtil;
import net.shoreline.client.util.player.MovementUtil;
import net.shoreline.client.util.string.EnumFormatter;
import net.shoreline.client.util.world.FakePlayerEntity;

public class SpeedModule extends ToggleModule {
   Config<SpeedModule.Speed> speedModeConfig;
   Config<Boolean> vanillaStrafeConfig;
   Config<Float> speedConfig;
   Config<Boolean> timerConfig;
   Config<Boolean> strafeBoostConfig;
   Config<Integer> boostTicksConfig;
   Config<Boolean> speedWaterConfig;
   private int strafe;
   private boolean accel;
   private int strictTicks;
   private int boostTicks;
   private double speed;
   private double boostSpeed;
   private double distance;
   private boolean prevTimer;

   public SpeedModule() {
      super("Speed", "Move faster", ModuleCategory.MOVEMENT);
      this.speedModeConfig = new EnumConfig("Mode", "Speed mode", SpeedModule.Speed.STRAFE, SpeedModule.Speed.values());
      this.vanillaStrafeConfig = new BooleanConfig("Strafe-Vanilla", "Applies strafe speeds to vanilla speed", false, () -> {
         return this.speedModeConfig.getValue() == SpeedModule.Speed.VANILLA;
      });
      this.speedConfig = new NumberConfig("Speed", "The speed for alternative modes", 0.1F, 4.0F, 10.0F);
      this.timerConfig = new BooleanConfig("UseTimer", "Uses timer to increase acceleration", false);
      this.strafeBoostConfig = new BooleanConfig("StrafeBoost", "Uses explosion velocity to boost Strafe", false);
      this.boostTicksConfig = new NumberConfig("BoostTicks", "The number of ticks to boost strafe", 10, 20, 40, () -> {
         return (Boolean)this.strafeBoostConfig.getValue();
      });
      this.speedWaterConfig = new BooleanConfig("SpeedInWater", "Applies speed even in water and lava", false);
      this.strafe = 4;
   }

   public String getModuleData() {
      return this.speedModeConfig.getValue() == SpeedModule.Speed.GRIM_COLLIDE ? "Grim" : EnumFormatter.formatEnum((Enum)this.speedModeConfig.getValue());
   }

   public void onEnable() {
      this.prevTimer = Modules.TIMER.isEnabled();
      if ((Boolean)this.timerConfig.getValue() && !this.prevTimer && this.isStrafe()) {
         Modules.TIMER.enable();
      }

   }

   public void onDisable() {
      this.resetStrafe();
      if (Modules.TIMER.isEnabled()) {
         Modules.TIMER.resetTimer();
         if (!this.prevTimer) {
            Modules.TIMER.disable();
         }
      }

   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      this.disable();
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         ++this.boostTicks;
         if (this.boostTicks > (Integer)this.boostTicksConfig.getValue()) {
            this.boostSpeed = 0.0D;
         }

         double dx = mc.field_1724.method_23317() - mc.field_1724.field_6014;
         double dz = mc.field_1724.method_23321() - mc.field_1724.field_5969;
         this.distance = Math.sqrt(dx * dx + dz * dz);
         if (this.speedModeConfig.getValue() == SpeedModule.Speed.GRIM_COLLIDE && MovementUtil.isInputtingMovement()) {
            int collisions = 0;
            Iterator var7 = mc.field_1687.method_18112().iterator();

            while(var7.hasNext()) {
               class_1297 entity = (class_1297)var7.next();
               if (this.checkIsCollidingEntity(entity) && (double)class_3532.method_15355((float)mc.field_1724.method_5858(entity)) <= 1.5D) {
                  ++collisions;
               }
            }

            if (collisions > 0) {
               class_243 velocity = mc.field_1724.method_18798();
               double factor = 0.08D * (double)collisions;
               class_241 strafe = this.handleStrafeMotion((float)factor);
               mc.field_1724.method_18800(velocity.field_1352 + (double)strafe.field_1343, velocity.field_1351, velocity.field_1350 + (double)strafe.field_1342);
            }
         }
      }

   }

   @EventListener
   public void onPlayerMove(PlayerMoveEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (!MovementUtil.isInputtingMovement() || Modules.FLIGHT.isEnabled() || Modules.LONG_JUMP.isEnabled() || mc.field_1724.method_3144() || mc.field_1724.method_6128() || mc.field_1724.method_21754() || mc.field_1724.field_6017 > 2.0F || (mc.field_1724.method_5771() || mc.field_1724.method_5799()) && !(Boolean)this.speedWaterConfig.getValue()) {
            this.resetStrafe();
            Modules.TIMER.setTimer(1.0F);
            return;
         }

         event.cancel();
         double speedEffect = 1.0D;
         double slowEffect = 1.0D;
         double base;
         if (mc.field_1724.method_6059(class_1294.field_5904)) {
            base = (double)mc.field_1724.method_6112(class_1294.field_5904).method_5578();
            speedEffect = 1.0D + 0.2D * (base + 1.0D);
         }

         if (mc.field_1724.method_6059(class_1294.field_5909)) {
            base = (double)mc.field_1724.method_6112(class_1294.field_5909).method_5578();
            slowEffect = 1.0D + 0.2D * (base + 1.0D);
         }

         base = 0.2872999906539917D * speedEffect / slowEffect;
         float jumpEffect = 0.0F;
         if (mc.field_1724.method_6059(class_1294.field_5913)) {
            jumpEffect += (float)(mc.field_1724.method_6112(class_1294.field_5913).method_5578() + 1) * 0.1F;
         }

         double moveSpeed;
         float jump;
         class_241 motion;
         if (this.speedModeConfig.getValue() != SpeedModule.Speed.STRAFE && this.speedModeConfig.getValue() != SpeedModule.Speed.STRAFE_B_HOP) {
            if (this.speedModeConfig.getValue() == SpeedModule.Speed.STRAFE_STRICT) {
               if (!Managers.ANTICHEAT.hasPassed(100L)) {
                  return;
               }

               if (this.strafe == 1) {
                  this.speed = 1.350000023841858D * base - 0.009999999776482582D;
               } else if (this.strafe == 2) {
                  if (mc.field_1724.field_3913.field_3904 || !mc.field_1724.method_24828()) {
                     return;
                  }

                  jump = 0.39999995F + jumpEffect;
                  event.setY((double)jump);
                  Managers.MOVEMENT.setMotionY((double)jump);
                  this.speed *= 2.149D;
               } else if (this.strafe == 3) {
                  moveSpeed = 0.66D * (this.distance - base);
                  this.speed = this.distance - moveSpeed;
               } else {
                  if ((!mc.field_1687.method_8587(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, mc.field_1724.method_18798().method_10214(), 0.0D)) || mc.field_1724.field_5992) && this.strafe > 0) {
                     this.strafe = MovementUtil.isInputtingMovement() ? 1 : 0;
                  }

                  this.speed = this.distance - this.distance / 159.0D;
               }

               ++this.strictTicks;
               this.speed = Math.max(this.speed, base);
               if ((Boolean)this.timerConfig.getValue()) {
                  Modules.TIMER.setTimer(1.0888F);
               }

               moveSpeed = 0.465D * speedEffect / slowEffect;
               double baseMin = 0.44D * speedEffect / slowEffect;
               this.speed = Math.min(this.speed, this.strictTicks > 25 ? moveSpeed : baseMin);
               if ((Boolean)this.strafeBoostConfig.getValue()) {
                  this.speed += this.boostSpeed;
               }

               if (this.strictTicks > 50) {
                  this.strictTicks = 0;
               }

               class_241 motion = this.handleStrafeMotion((float)this.speed);
               event.setX((double)motion.field_1343);
               event.setZ((double)motion.field_1342);
               ++this.strafe;
            } else if (this.speedModeConfig.getValue() == SpeedModule.Speed.LOW_HOP) {
               if (!Managers.ANTICHEAT.hasPassed(100L)) {
                  return;
               }

               if ((Boolean)this.timerConfig.getValue()) {
                  Modules.TIMER.setTimer(1.0888F);
               }

               if (MathUtil.round(mc.field_1724.method_23318() - (double)((int)mc.field_1724.method_23318()), 3) == MathUtil.round(0.4D, 3)) {
                  Managers.MOVEMENT.setMotionY(0.31D + (double)jumpEffect);
                  event.setY(0.31D + (double)jumpEffect);
               } else if (MathUtil.round(mc.field_1724.method_23318() - (double)((int)mc.field_1724.method_23318()), 3) == MathUtil.round(0.71D, 3)) {
                  Managers.MOVEMENT.setMotionY(0.04D + (double)jumpEffect);
                  event.setY(0.04D + (double)jumpEffect);
               } else if (MathUtil.round(mc.field_1724.method_23318() - (double)((int)mc.field_1724.method_23318()), 3) == MathUtil.round(0.75D, 3)) {
                  Managers.MOVEMENT.setMotionY(-0.2D - (double)jumpEffect);
                  event.setY(-0.2D - (double)jumpEffect);
               } else if (MathUtil.round(mc.field_1724.method_23318() - (double)((int)mc.field_1724.method_23318()), 3) == MathUtil.round(0.55D, 3)) {
                  Managers.MOVEMENT.setMotionY(-0.14D + (double)jumpEffect);
                  event.setY(-0.14D + (double)jumpEffect);
               } else if (MathUtil.round(mc.field_1724.method_23318() - (double)((int)mc.field_1724.method_23318()), 3) == MathUtil.round(0.41D, 3)) {
                  Managers.MOVEMENT.setMotionY(-0.2D + (double)jumpEffect);
                  event.setY(-0.2D + (double)jumpEffect);
               }

               if (this.strafe == 1) {
                  this.speed = 1.350000023841858D * base - 0.009999999776482582D;
               } else if (this.strafe == 2) {
                  moveSpeed = (this.isBoxColliding() ? 0.2D : 0.3999D) + (double)jumpEffect;
                  Managers.MOVEMENT.setMotionY(moveSpeed);
                  event.setY(moveSpeed);
                  this.speed *= this.accel ? 1.5685D : 1.3445D;
               } else if (this.strafe == 3) {
                  moveSpeed = 0.66D * (this.distance - base);
                  this.speed = this.distance - moveSpeed;
                  this.accel = !this.accel;
               } else {
                  if (mc.field_1724.method_24828() && this.strafe > 0) {
                     this.strafe = MovementUtil.isInputtingMovement() ? 1 : 0;
                  }

                  this.speed = this.distance - this.distance / 159.0D;
               }

               this.speed = Math.max(this.speed, base);
               motion = this.handleVanillaMotion((float)this.speed);
               event.setX((double)motion.field_1343);
               event.setZ((double)motion.field_1342);
               ++this.strafe;
            } else if (this.speedModeConfig.getValue() == SpeedModule.Speed.GAY_HOP) {
               if (!Managers.ANTICHEAT.hasPassed(100L)) {
                  this.strafe = 1;
                  return;
               }

               if (this.strafe == 1 && mc.field_1724.field_5992 && MovementUtil.isInputtingMovement()) {
                  this.speed = 1.25D * base - 0.009999999776482582D;
               } else if (this.strafe == 2 && mc.field_1724.field_5992 && MovementUtil.isInputtingMovement()) {
                  jump = (this.isBoxColliding() ? 0.2F : 0.4F) + jumpEffect;
                  event.setY((double)jump);
                  Managers.MOVEMENT.setMotionY((double)jump);
                  this.speed *= 2.149D;
               } else if (this.strafe == 3) {
                  moveSpeed = 0.66D * (this.distance - base);
                  this.speed = this.distance - moveSpeed;
               } else {
                  if (mc.field_1724.method_24828() && this.strafe > 0) {
                     if (1.35D * base - 0.01D > this.speed) {
                        this.strafe = 0;
                     } else {
                        this.strafe = MovementUtil.isInputtingMovement() ? 1 : 0;
                     }
                  }

                  this.speed = this.distance - this.distance / 159.0D;
               }

               this.speed = Math.max(this.speed, base);
               if (this.strafe > 0) {
                  motion = this.handleStrafeMotion((float)this.speed);
                  event.setX((double)motion.field_1343);
                  event.setZ((double)motion.field_1342);
               }

               ++this.strafe;
            } else if (this.speedModeConfig.getValue() == SpeedModule.Speed.V_HOP) {
               if (!Managers.ANTICHEAT.hasPassed(100L)) {
                  this.strafe = 1;
                  return;
               }

               if (MathUtil.round(mc.field_1724.method_23318() - (double)((int)mc.field_1724.method_23318()), 3) == MathUtil.round(0.4D, 3)) {
                  Managers.MOVEMENT.setMotionY(0.31D + (double)jumpEffect);
                  event.setY(0.31D + (double)jumpEffect);
               } else if (MathUtil.round(mc.field_1724.method_23318() - (double)((int)mc.field_1724.method_23318()), 3) == MathUtil.round(0.71D, 3)) {
                  Managers.MOVEMENT.setMotionY(0.04D + (double)jumpEffect);
                  event.setY(0.04D + (double)jumpEffect);
               } else if (MathUtil.round(mc.field_1724.method_23318() - (double)((int)mc.field_1724.method_23318()), 3) == MathUtil.round(0.75D, 3)) {
                  Managers.MOVEMENT.setMotionY(-0.2D - (double)jumpEffect);
                  event.setY(-0.2D - (double)jumpEffect);
               }

               if (!mc.field_1687.method_8587((class_1297)null, mc.field_1724.method_5829().method_989(0.0D, -0.56D, 0.0D)) && MathUtil.round(mc.field_1724.method_23318() - (double)((int)mc.field_1724.method_23318()), 3) == MathUtil.round(0.55D, 3)) {
                  Managers.MOVEMENT.setMotionY(-0.14D + (double)jumpEffect);
                  event.setY(-0.14D + (double)jumpEffect);
               }

               if (this.strafe == 1 && mc.field_1724.field_5992 && (mc.field_1724.field_6250 != 0.0F || mc.field_1724.field_6212 != 0.0F)) {
                  this.speed = 2.0D * base - 0.01D;
               } else if (this.strafe != 2 || !mc.field_1724.field_5992 || mc.field_1724.field_6250 == 0.0F && mc.field_1724.field_6212 == 0.0F) {
                  if (this.strafe == 3) {
                     moveSpeed = 0.66D * (this.distance - base);
                     this.speed = this.distance - moveSpeed;
                  } else {
                     if (mc.field_1724.method_24828() && this.strafe > 0) {
                        if (1.35D * base - 0.01D > this.speed) {
                           this.strafe = 0;
                        } else {
                           this.strafe = MovementUtil.isInputtingMovement() ? 1 : 0;
                        }
                     }

                     this.speed = this.distance - this.distance / 159.0D;
                  }
               } else {
                  moveSpeed = (this.isBoxColliding() ? 0.2D : 0.4D) + (double)jumpEffect;
                  Managers.MOVEMENT.setMotionY(moveSpeed);
                  event.setY(moveSpeed);
                  this.speed *= 2.149D;
               }

               if (this.strafe > 8) {
                  this.speed = base;
               }

               this.speed = Math.max(this.speed, base);
               motion = this.handleStrafeMotion((float)this.speed);
               event.setX((double)motion.field_1343);
               event.setZ((double)motion.field_1342);
               ++this.strafe;
            } else if (this.speedModeConfig.getValue() == SpeedModule.Speed.B_HOP) {
               if (!Managers.ANTICHEAT.hasPassed(100L)) {
                  this.strafe = 4;
                  return;
               }

               if (MathUtil.round(mc.field_1724.method_23318() - (double)((int)mc.field_1724.method_23318()), 3) == MathUtil.round(0.138D, 3)) {
                  Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 - (0.08D + (double)jumpEffect));
                  event.setY(event.getY() - (0.0931D + (double)jumpEffect));
                  Managers.POSITION.setPositionY(mc.field_1724.method_23318() - (0.0931D + (double)jumpEffect));
               }

               if (this.strafe == 2 && (mc.field_1724.field_6250 != 0.0F || mc.field_1724.field_6212 != 0.0F)) {
                  moveSpeed = (this.isBoxColliding() ? 0.2D : 0.4D) + (double)jumpEffect;
                  Managers.MOVEMENT.setMotionY(moveSpeed);
                  event.setY(moveSpeed);
                  this.speed *= 2.149D;
               } else if (this.strafe == 3) {
                  moveSpeed = 0.66D * (this.distance - base);
                  this.speed = this.distance - moveSpeed;
               } else {
                  if (mc.field_1724.method_24828()) {
                     this.strafe = 1;
                  }

                  this.speed = this.distance - this.distance / 159.0D;
               }

               this.speed = Math.max(this.speed, base);
               motion = this.handleStrafeMotion((float)this.speed);
               event.setX((double)motion.field_1343);
               event.setZ((double)motion.field_1342);
               ++this.strafe;
            } else if (this.speedModeConfig.getValue() == SpeedModule.Speed.VANILLA) {
               motion = this.handleVanillaMotion((Boolean)this.vanillaStrafeConfig.getValue() ? (float)base : (Float)this.speedConfig.getValue() / 10.0F);
               event.setX((double)motion.field_1343);
               event.setZ((double)motion.field_1342);
            }
         } else {
            if (!Managers.ANTICHEAT.hasPassed(100L)) {
               return;
            }

            if ((Boolean)this.timerConfig.getValue()) {
               Modules.TIMER.setTimer(1.0888F);
            }

            if (this.strafe == 1) {
               this.speed = 1.350000023841858D * base - 0.009999999776482582D;
            } else if (this.strafe == 2) {
               if (mc.field_1724.field_3913.field_3904 || !mc.field_1724.method_24828()) {
                  return;
               }

               jump = (this.speedModeConfig.getValue() == SpeedModule.Speed.STRAFE_B_HOP ? 0.4F : 0.39999995F) + jumpEffect;
               event.setY((double)jump);
               Managers.MOVEMENT.setMotionY((double)jump);
               this.speed *= this.speedModeConfig.getValue() == SpeedModule.Speed.STRAFE_B_HOP ? 1.535D : (this.accel ? 1.6835D : 1.395D);
            } else if (this.strafe == 3) {
               moveSpeed = 0.66D * (this.distance - base);
               this.speed = this.distance - moveSpeed;
               this.accel = !this.accel;
            } else {
               if ((!mc.field_1687.method_8587(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, mc.field_1724.method_18798().method_10214(), 0.0D)) || mc.field_1724.field_5992) && this.strafe > 0) {
                  this.strafe = MovementUtil.isInputtingMovement() ? 1 : 0;
               }

               this.speed = this.distance - this.distance / 159.0D;
            }

            this.speed = Math.max(this.speed, base);
            if ((Boolean)this.strafeBoostConfig.getValue()) {
               this.speed += this.boostSpeed;
            }

            motion = this.handleStrafeMotion((float)this.speed);
            event.setX((double)motion.field_1343);
            event.setZ((double)motion.field_1342);
            ++this.strafe;
         }
      }

   }

   public class_241 handleStrafeMotion(float speed) {
      float forward = mc.field_1724.field_3913.field_3905;
      float strafe = mc.field_1724.field_3913.field_3907;
      float yaw = mc.field_1724.field_5982 + (mc.field_1724.method_36454() - mc.field_1724.field_5982) * mc.method_1488();
      if (forward == 0.0F && strafe == 0.0F) {
         return class_241.field_1340;
      } else {
         if (forward != 0.0F) {
            if (strafe >= 1.0F) {
               yaw += forward > 0.0F ? -45.0F : 45.0F;
               strafe = 0.0F;
            } else if (strafe <= -1.0F) {
               yaw += forward > 0.0F ? 45.0F : -45.0F;
               strafe = 0.0F;
            }

            if (forward > 0.0F) {
               forward = 1.0F;
            } else if (forward < 0.0F) {
               forward = -1.0F;
            }
         }

         float rx = (float)Math.cos(Math.toRadians((double)yaw));
         float rz = (float)(-Math.sin(Math.toRadians((double)yaw)));
         return new class_241(forward * speed * rz + strafe * speed * rx, forward * speed * rx - strafe * speed * rz);
      }
   }

   public class_241 handleVanillaMotion(float speed) {
      float forward = mc.field_1724.field_3913.field_3905;
      float strafe = mc.field_1724.field_3913.field_3907;
      if (forward == 0.0F && strafe == 0.0F) {
         return class_241.field_1340;
      } else {
         if (forward != 0.0F && strafe != 0.0F) {
            forward *= (float)Math.sin(0.7853981633974483D);
            strafe *= (float)Math.cos(0.7853981633974483D);
         }

         return new class_241((float)((double)(forward * speed) * -Math.sin(Math.toRadians((double)mc.field_1724.method_36454())) + (double)(strafe * speed) * Math.cos(Math.toRadians((double)mc.field_1724.method_36454()))), (float)((double)(forward * speed) * Math.cos(Math.toRadians((double)mc.field_1724.method_36454())) - (double)(strafe * speed) * -Math.sin(Math.toRadians((double)mc.field_1724.method_36454()))));
      }
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_2596 var4 = event.getPacket();
         double var6;
         double x;
         if (var4 instanceof class_2664) {
            class_2664 packet = (class_2664)var4;
            x = (double)packet.method_11472();
            var6 = (double)packet.method_11474();
         } else {
            var4 = event.getPacket();
            if (var4 instanceof class_2743) {
               class_2743 packet = (class_2743)var4;
               if (packet.method_11818() == mc.field_1724.method_5628()) {
                  x = (double)packet.method_11815();
                  var6 = (double)packet.method_11819();
                  return;
               }
            }

            if (event.getPacket() instanceof class_2708) {
               this.resetStrafe();
            }
         }

      }
   }

   @EventListener
   public void onConfigUpdate(ConfigUpdateEvent event) {
      if (event.getConfig() == this.timerConfig && event.getStage() == EventStage.POST && this.isStrafe()) {
         if ((Boolean)this.timerConfig.getValue()) {
            this.prevTimer = Modules.TIMER.isEnabled();
            if (!this.prevTimer) {
               Modules.TIMER.enable();
            }
         } else if (Modules.TIMER.isEnabled()) {
            Modules.TIMER.resetTimer();
            if (!this.prevTimer) {
               Modules.TIMER.disable();
            }
         }
      }

   }

   public boolean isBoxColliding() {
      return !mc.field_1687.method_8587(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, 0.21D, 0.0D));
   }

   public boolean checkIsCollidingEntity(class_1297 entity) {
      return entity != null && entity != mc.field_1724 && entity instanceof class_1309 && !(entity instanceof FakePlayerEntity) && !(entity instanceof class_1531);
   }

   public void setPrevTimer() {
      this.prevTimer = !this.prevTimer;
   }

   public boolean isUsingTimer() {
      return this.isEnabled() && (Boolean)this.timerConfig.getValue();
   }

   public void resetStrafe() {
      this.strafe = 4;
      this.strictTicks = 0;
      this.speed = 0.0D;
      this.distance = 0.0D;
      this.accel = false;
   }

   public boolean isStrafe() {
      return this.speedModeConfig.getValue() != SpeedModule.Speed.FIREWORK && this.speedModeConfig.getValue() != SpeedModule.Speed.GRIM_COLLIDE && this.speedModeConfig.getValue() != SpeedModule.Speed.VANILLA;
   }

   private static enum Speed {
      STRAFE,
      STRAFE_STRICT,
      STRAFE_B_HOP,
      LOW_HOP,
      GAY_HOP,
      V_HOP,
      B_HOP,
      VANILLA,
      GRIM_COLLIDE,
      FIREWORK;

      // $FF: synthetic method
      private static SpeedModule.Speed[] $values() {
         return new SpeedModule.Speed[]{STRAFE, STRAFE_STRICT, STRAFE_B_HOP, LOW_HOP, GAY_HOP, V_HOP, B_HOP, VANILLA, GRIM_COLLIDE, FIREWORK};
      }
   }
}
