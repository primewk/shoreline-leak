package net.shoreline.client.impl.module.combat;

import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Stream;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1667;
import net.minecraft.class_1683;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_1835;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2824;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_2848.class_2849;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.NumberDisplay;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.api.render.Interpolation;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.impl.event.world.RemoveEntityEvent;
import net.shoreline.client.impl.manager.world.tick.TickSync;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;
import net.shoreline.client.util.player.PlayerUtil;
import net.shoreline.client.util.player.RotationUtil;
import net.shoreline.client.util.string.EnumFormatter;
import net.shoreline.client.util.world.EntityUtil;

public class AuraModule extends RotationModule {
   Config<Boolean> swingConfig = new BooleanConfig("Swing", "Swings the hand after attacking", true);
   Config<AuraModule.TargetMode> modeConfig;
   Config<AuraModule.Priority> priorityConfig;
   Config<Float> searchRangeConfig;
   Config<Float> rangeConfig;
   Config<Float> wallRangeConfig;
   Config<Boolean> vanillaRangeConfig;
   Config<Float> fovConfig;
   Config<Boolean> attackDelayConfig;
   Config<Float> attackSpeedConfig;
   Config<Float> randomSpeedConfig;
   Config<Float> swapDelayConfig;
   Config<TickSync> tpsSyncConfig;
   Config<Boolean> awaitCritsConfig;
   Config<Boolean> autoSwapConfig;
   Config<Boolean> swordCheckConfig;
   Config<AuraModule.Vector> hitVectorConfig;
   Config<Boolean> rotateConfig;
   Config<Boolean> silentRotateConfig;
   Config<Boolean> strictRotateConfig;
   Config<Integer> rotateLimitConfig;
   Config<Integer> ticksExistedConfig;
   Config<Boolean> armorCheckConfig;
   Config<Boolean> stopSprintConfig;
   Config<Boolean> stopShieldConfig;
   Config<Boolean> playersConfig;
   Config<Boolean> monstersConfig;
   Config<Boolean> neutralsConfig;
   Config<Boolean> animalsConfig;
   Config<Boolean> invisiblesConfig;
   Config<Boolean> renderConfig;
   Config<Boolean> disableDeathConfig;
   private class_1297 entityTarget;
   private long randomDelay;
   private boolean shielding;
   private boolean sneaking;
   private boolean sprinting;
   private final Timer attackTimer;
   private final Timer critTimer;
   private final Timer autoSwapTimer;
   private final Timer switchTimer;
   private boolean rotated;
   private float[] silentRotations;

   public AuraModule() {
      super("Aura", "Attacks nearby entities", ModuleCategory.COMBAT, 700);
      this.modeConfig = new EnumConfig("Mode", "The mode for targeting entities to attack", AuraModule.TargetMode.SWITCH, AuraModule.TargetMode.values());
      this.priorityConfig = new EnumConfig("Priority", "The value to prioritize when searching for targets", AuraModule.Priority.HEALTH, AuraModule.Priority.values());
      this.searchRangeConfig = new NumberConfig("EnemyRange", "Range to search for targets", 1.0F, 5.0F, 10.0F);
      this.rangeConfig = new NumberConfig("Range", "Range to attack entities", 1.0F, 4.5F, 6.0F);
      this.wallRangeConfig = new NumberConfig("WallRange", "Range to attack entities through walls", 1.0F, 4.5F, 6.0F);
      this.vanillaRangeConfig = new BooleanConfig("VanillaRange", "Only attack within vanilla range", false);
      this.fovConfig = new NumberConfig("FOV", "Field of view to attack entities", 1.0F, 180.0F, 180.0F);
      this.attackDelayConfig = new BooleanConfig("AttackDelay", "Delays attacks according to minecraft hit delays for maximum damage per attack", true);
      this.attackSpeedConfig = new NumberConfig("AttackSpeed", "Delay for attacks (Only functions if AttackDelay is off)", 1.0F, 20.0F, 20.0F, () -> {
         return !(Boolean)this.attackDelayConfig.getValue();
      });
      this.randomSpeedConfig = new NumberConfig("RandomSpeed", "Randomized delay for attacks (Only functions if AttackDelay is off)", 0.0F, 0.0F, 10.0F, () -> {
         return !(Boolean)this.attackDelayConfig.getValue();
      });
      this.swapDelayConfig = new NumberConfig("SwapPenalty", "Delay for attacking after swapping items which prevents NCP flags", 0.0F, 0.0F, 10.0F);
      this.tpsSyncConfig = new EnumConfig("TPS-Sync", "Syncs the attacks with the server TPS", TickSync.NONE, TickSync.values());
      this.awaitCritsConfig = new BooleanConfig("AwaitCriticals", "Aura will wait for a critical hit when falling", false);
      this.autoSwapConfig = new BooleanConfig("AutoSwap", "Automatically swaps to a weapon before attacking", true);
      this.swordCheckConfig = new BooleanConfig("Sword-Check", "Checks if a weapon is in the hand before attacking", true);
      this.hitVectorConfig = new EnumConfig("HitVector", "The vector to aim for when attacking entities", AuraModule.Vector.FEET, AuraModule.Vector.values());
      this.rotateConfig = new BooleanConfig("Rotate", "Rotate before attacking", false);
      this.silentRotateConfig = new BooleanConfig("RotateSilent", "Rotates silently to server", false, () -> {
         return (Boolean)this.rotateConfig.getValue();
      });
      this.strictRotateConfig = new BooleanConfig("YawStep", "Rotates yaw over multiple ticks to prevent certain rotation flags in NCP", false, () -> {
         return (Boolean)this.rotateConfig.getValue();
      });
      this.rotateLimitConfig = new NumberConfig("YawStep-Limit", "Maximum yaw rotation in degrees for one tick", 1, 180, 180, NumberDisplay.DEGREES, () -> {
         return (Boolean)this.rotateConfig.getValue() && (Boolean)this.strictRotateConfig.getValue();
      });
      this.ticksExistedConfig = new NumberConfig("TicksExisted", "The minimum age of the entity to be considered for attack", 0, 50, 200);
      this.armorCheckConfig = new BooleanConfig("ArmorCheck", "Checks if target has armor before attacking", false);
      this.stopSprintConfig = new BooleanConfig("StopSprint", "Stops sprinting before attacking to maintain vanilla behavior", false);
      this.stopShieldConfig = new BooleanConfig("StopShield", "Automatically handles shielding before attacking", false);
      this.playersConfig = new BooleanConfig("Players", "Target players", true);
      this.monstersConfig = new BooleanConfig("Monsters", "Target monsters", false);
      this.neutralsConfig = new BooleanConfig("Neutrals", "Target neutrals", false);
      this.animalsConfig = new BooleanConfig("Animals", "Target animals", false);
      this.invisiblesConfig = new BooleanConfig("Invisibles", "Target invisible entities", true);
      this.renderConfig = new BooleanConfig("Render", "Renders an indicator over the target", true);
      this.disableDeathConfig = new BooleanConfig("DisableOnDeath", "Disables during disconnect/death", false);
      this.randomDelay = -1L;
      this.attackTimer = new CacheTimer();
      this.critTimer = new CacheTimer();
      this.autoSwapTimer = new CacheTimer();
      this.switchTimer = new CacheTimer();
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
   }

   public void onDisable() {
      this.entityTarget = null;
      this.silentRotations = null;
   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      if ((Boolean)this.disableDeathConfig.getValue()) {
         this.disable();
      }

   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent event) {
      if ((Boolean)this.disableDeathConfig.getValue() && event.getEntity() == mc.field_1724) {
         this.disable();
      }

   }

   @EventListener
   public void onPlayerUpdate(PlayerTickEvent event) {
      if (!Modules.AUTO_CRYSTAL.isAttacking() && !Modules.AUTO_CRYSTAL.isPlacing()) {
         class_243 eyepos = Managers.POSITION.getEyePos();
         switch((AuraModule.TargetMode)this.modeConfig.getValue()) {
         case SWITCH:
            this.entityTarget = this.getAttackTarget(eyepos);
            break;
         case SINGLE:
            if (this.entityTarget == null || !this.entityTarget.method_5805() || !this.isInAttackRange(eyepos, this.entityTarget)) {
               this.entityTarget = this.getAttackTarget(eyepos);
            }
         }

         if (this.entityTarget != null && this.switchTimer.passed((Float)this.swapDelayConfig.getValue() * 25.0F)) {
            if (mc.field_1724.method_6115() && mc.field_1724.method_6058() == class_1268.field_5808 || mc.field_1690.field_1886.method_1434() || PlayerUtil.isHotbarKeysPressed()) {
               this.autoSwapTimer.reset();
            }

            boolean sword = mc.field_1724.method_6047().method_7909() instanceof class_1829;
            if ((Boolean)this.autoSwapConfig.getValue() && this.autoSwapTimer.passed(500) && !sword) {
               int slot = this.getSwordSlot();
               if (slot != -1) {
                  Managers.INVENTORY.setClientSlot(slot);
               }
            }

            if (this.isHoldingSword()) {
               float serverYaw;
               if ((Boolean)this.rotateConfig.getValue()) {
                  float[] rotation = RotationUtil.getRotationsTo(mc.field_1724.method_33571(), this.getAttackRotateVec(this.entityTarget));
                  if (!(Boolean)this.silentRotateConfig.getValue() && (Boolean)this.strictRotateConfig.getValue()) {
                     serverYaw = Managers.ROTATION.getWrappedYaw();
                     float diff = serverYaw - rotation[0];
                     float diff1 = Math.abs(diff);
                     if (diff1 > 180.0F) {
                        diff += diff > 0.0F ? -360.0F : 360.0F;
                     }

                     int dir = diff > 0.0F ? -1 : 1;
                     float deltaYaw = (float)(dir * (Integer)this.rotateLimitConfig.getValue());
                     float yaw;
                     if (diff1 > (float)(Integer)this.rotateLimitConfig.getValue()) {
                        yaw = serverYaw + deltaYaw;
                        this.rotated = false;
                     } else {
                        yaw = rotation[0];
                        this.rotated = true;
                     }

                     rotation[0] = yaw;
                  } else {
                     this.rotated = true;
                  }

                  if ((Boolean)this.silentRotateConfig.getValue()) {
                     this.silentRotations = rotation;
                  } else {
                     this.setRotation(rotation[0], rotation[1]);
                  }
               }

               if (!this.isRotationBlocked() && this.shouldWaitCrit() && (this.rotated || !(Boolean)this.rotateConfig.getValue()) && this.isInAttackRange(eyepos, this.entityTarget)) {
                  float delay;
                  if ((Boolean)this.attackDelayConfig.getValue()) {
                     delay = 20.0F - Managers.TICK.getTickSync((TickSync)this.tpsSyncConfig.getValue());
                     serverYaw = mc.field_1724.method_7261(delay);
                     if (serverYaw >= 1.0F && this.attackTarget(this.entityTarget)) {
                        mc.field_1724.method_7350();
                     }
                  } else {
                     if (this.randomDelay < 0L) {
                        this.randomDelay = (long)RANDOM.nextFloat((Float)this.randomSpeedConfig.getValue() * 10.0F + 1.0F);
                     }

                     delay = (Float)this.attackSpeedConfig.getValue() * 50.0F + (float)this.randomDelay;
                     if (this.attackTimer.passed(1000.0F - delay) && this.attackTarget(this.entityTarget)) {
                        this.randomDelay = -1L;
                        this.attackTimer.reset();
                     }
                  }

               }
            }
         } else {
            this.silentRotations = null;
         }
      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (mc.field_1724 != null) {
         if (event.getPacket() instanceof class_2868) {
            this.switchTimer.reset();
         }

      }
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (this.entityTarget != null && (Boolean)this.renderConfig.getValue() && this.isHoldingSword()) {
         float delay = (Float)this.attackSpeedConfig.getValue() * 50.0F + (float)this.randomDelay;
         int attackDelay;
         float animFactor;
         if ((Boolean)this.attackDelayConfig.getValue()) {
            animFactor = 1.0F - mc.field_1724.method_7261(0.0F);
            attackDelay = (int)(100.0D * (double)animFactor);
         } else {
            animFactor = 1.0F - class_3532.method_15363((float)this.attackTimer.getElapsedTime() / (1000.0F - delay), 0.0F, 1.0F);
            attackDelay = (int)(100.0D * (double)animFactor);
         }

         RenderManager.renderBox(event.getMatrices(), Interpolation.getInterpolatedEntityBox(this.entityTarget), Modules.COLORS.getRGB(60 + attackDelay));
         RenderManager.renderBoundingBox(event.getMatrices(), Interpolation.getInterpolatedEntityBox(this.entityTarget), 1.5F, Modules.COLORS.getRGB(145));
      }

   }

   private boolean attackTarget(class_1297 entity) {
      this.preAttackTarget();
      if ((Boolean)this.silentRotateConfig.getValue() && this.silentRotations != null) {
         this.setRotationSilent(this.silentRotations[0], this.silentRotations[1]);
      }

      class_2824 packet = class_2824.method_34206(entity, Managers.POSITION.isSneaking());
      Managers.NETWORK.sendPacket(packet);
      this.postAttackTarget(entity);
      if ((Boolean)this.swingConfig.getValue()) {
         mc.field_1724.method_6104(class_1268.field_5808);
      } else {
         Managers.NETWORK.sendPacket(new class_2879(class_1268.field_5808));
      }

      if ((Boolean)this.silentRotateConfig.getValue()) {
         Managers.ROTATION.setRotationSilentSync(true);
      }

      return true;
   }

   private int getSwordSlot() {
      float sharp = 0.0F;
      int slot = -1;

      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         class_1792 var7 = stack.method_7909();
         float dmg;
         float sharpness;
         if (var7 instanceof class_1829) {
            class_1829 swordItem = (class_1829)var7;
            sharpness = (float)class_1890.method_8225(class_1893.field_9118, stack) * 0.5F + 0.5F;
            dmg = swordItem.method_8020() + sharpness;
            if (dmg > sharp) {
               sharp = dmg;
               slot = i;
            }
         } else {
            var7 = stack.method_7909();
            if (var7 instanceof class_1743) {
               class_1743 axeItem = (class_1743)var7;
               sharpness = (float)class_1890.method_8225(class_1893.field_9118, stack) * 0.5F + 0.5F;
               dmg = axeItem.method_26366() + sharpness;
               if (dmg > sharp) {
                  sharp = dmg;
                  slot = i;
               }
            } else if (stack.method_7909() instanceof class_1835) {
               sharpness = (float)class_1890.method_8225(class_1893.field_9118, stack) * 0.5F + 0.5F;
               dmg = 8.0F + sharpness;
               if (dmg > sharp) {
                  sharp = dmg;
                  slot = i;
               }
            }
         }
      }

      return slot;
   }

   private void preAttackTarget() {
      class_1799 offhand = mc.field_1724.method_6079();
      this.shielding = false;
      if ((Boolean)this.stopShieldConfig.getValue()) {
         this.shielding = offhand.method_7909() == class_1802.field_8255 && mc.field_1724.method_6039();
         if (this.shielding) {
            Managers.NETWORK.sendPacket(new class_2846(class_2847.field_12974, Managers.POSITION.getBlockPos(), class_2350.method_10142(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321())));
         }
      }

      this.sneaking = false;
      this.sprinting = false;
      if ((Boolean)this.stopSprintConfig.getValue()) {
         this.sneaking = Managers.POSITION.isSneaking();
         if (this.sneaking) {
            Managers.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12984));
         }

         this.sprinting = Managers.POSITION.isSprinting();
         if (this.sprinting) {
            Managers.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12985));
         }
      }

   }

   private void postAttackTarget(class_1297 entity) {
      if (this.shielding) {
         Managers.NETWORK.sendSequencedPacket((s) -> {
            return new class_2886(class_1268.field_5810, s);
         });
      }

      if (this.sneaking) {
         Managers.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12979));
      }

      if (this.sprinting) {
         Managers.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12981));
      }

      if (Modules.CRITICALS.isEnabled() && this.critTimer.passed(500)) {
         if (!mc.field_1724.method_24828() || mc.field_1724.method_3144() || mc.field_1724.method_5869() || mc.field_1724.method_5771() || mc.field_1724.method_21754() || mc.field_1724.method_6059(class_1294.field_5919) || mc.field_1724.field_3913.field_3904) {
            return;
         }

         Modules.CRITICALS.preAttackPacket();
         this.critTimer.reset();
         mc.field_1724.method_7277(entity);
      }

   }

   private class_1297 getAttackTarget(class_243 pos) {
      double min = Double.MAX_VALUE;
      class_1297 attackTarget = null;
      Iterator var5 = mc.field_1687.method_18112().iterator();

      while(true) {
         class_1297 entity;
         class_1657 player;
         do {
            do {
               do {
                  do {
                     do {
                        do {
                           do {
                              do {
                                 do {
                                    do {
                                       do {
                                          if (!var5.hasNext()) {
                                             return attackTarget;
                                          }

                                          entity = (class_1297)var5.next();
                                       } while(entity == null);
                                    } while(entity == mc.field_1724);
                                 } while(!entity.method_5805());
                              } while(!this.isEnemy(entity));
                           } while(Managers.SOCIAL.isFriend(entity.method_5477()));
                        } while(entity instanceof class_1511);
                     } while(entity instanceof class_1542);
                  } while(entity instanceof class_1667);
               } while(entity instanceof class_1683);

               if (!(entity instanceof class_1657)) {
                  break;
               }

               player = (class_1657)entity;
            } while(player.method_7337());
         } while((Boolean)this.armorCheckConfig.getValue() && entity instanceof class_1309 && !entity.method_5661().iterator().hasNext());

         double dist = pos.method_1022(entity.method_19538());
         if (dist <= (double)(Float)this.searchRangeConfig.getValue() && entity.field_6012 >= (Integer)this.ticksExistedConfig.getValue()) {
            class_1309 e;
            float armor;
            switch((AuraModule.Priority)this.priorityConfig.getValue()) {
            case DISTANCE:
               if (dist < min) {
                  min = dist;
                  attackTarget = entity;
               }
               break;
            case HEALTH:
               if (entity instanceof class_1309) {
                  e = (class_1309)entity;
                  armor = e.method_6032() + e.method_6067();
                  if ((double)armor < min) {
                     min = (double)armor;
                     attackTarget = entity;
                  }
               }
               break;
            case ARMOR:
               if (entity instanceof class_1309) {
                  e = (class_1309)entity;
                  armor = this.getArmorDurability(e);
                  if ((double)armor < min) {
                     min = (double)armor;
                     attackTarget = entity;
                  }
               }
            }
         }
      }
   }

   private float getArmorDurability(class_1309 e) {
      float edmg = 0.0F;
      float emax = 0.0F;
      Iterator var4 = e.method_5661().iterator();

      while(var4.hasNext()) {
         class_1799 armor = (class_1799)var4.next();
         if (armor != null && !armor.method_7960()) {
            edmg += (float)armor.method_7919();
            emax += (float)armor.method_7936();
         }
      }

      return 100.0F - edmg / emax;
   }

   public boolean isInAttackRange(class_243 pos, class_1297 entity) {
      class_243 entityPos = this.getAttackRotateVec(entity);
      double dist = pos.method_1022(entityPos);
      return this.isInAttackRange(dist, pos, entityPos);
   }

   public boolean isInAttackRange(double dist, class_243 pos, class_243 entityPos) {
      if ((Boolean)this.vanillaRangeConfig.getValue() && dist > 3.0D) {
         return false;
      } else if (dist > (double)(Float)this.rangeConfig.getValue()) {
         return false;
      } else {
         class_3965 result = mc.field_1687.method_17742(new class_3959(pos, entityPos, class_3960.field_17558, class_242.field_1348, mc.field_1724));
         if (result != null && dist > (double)(Float)this.wallRangeConfig.getValue()) {
            return false;
         } else if ((Float)this.fovConfig.getValue() != 180.0F) {
            float[] rots = RotationUtil.getRotationsTo(pos, entityPos);
            float diff = class_3532.method_15393(mc.field_1724.method_36454()) - rots[0];
            float magnitude = Math.abs(diff);
            return magnitude <= (Float)this.fovConfig.getValue();
         } else {
            return true;
         }
      }
   }

   public boolean isHoldingSword() {
      return !(Boolean)this.swordCheckConfig.getValue() || mc.field_1724.method_6047().method_7909() instanceof class_1829 || mc.field_1724.method_6047().method_7909() instanceof class_1743 || mc.field_1724.method_6047().method_7909() instanceof class_1835;
   }

   public boolean shouldWaitCrit() {
      return !mc.field_1724.method_24828() && mc.field_1724.field_6017 > 0.0F && mc.field_1724.field_6017 < 1.0F && !mc.field_1724.method_6059(class_1294.field_5919) && !mc.field_1724.method_6101() && !mc.field_1724.method_5799() || !(Boolean)this.awaitCritsConfig.getValue() || !mc.field_1690.field_1903.method_1434();
   }

   private class_243 getAttackRotateVec(class_1297 entity) {
      class_243 feetPos = entity.method_19538();
      class_243 var10000;
      switch((AuraModule.Vector)this.hitVectorConfig.getValue()) {
      case FEET:
         var10000 = feetPos;
         break;
      case TORSO:
         var10000 = feetPos.method_1031(0.0D, (double)(entity.method_17682() / 2.0F), 0.0D);
         break;
      case EYES:
         var10000 = entity.method_33571();
         break;
      case AUTO:
         class_243 torsoPos = feetPos.method_1031(0.0D, (double)(entity.method_17682() / 2.0F), 0.0D);
         class_243 eyesPos = entity.method_33571();
         var10000 = (class_243)Stream.of(feetPos, torsoPos, eyesPos).min(Comparator.comparing((b) -> {
            return mc.field_1724.method_33571().method_1025(b);
         })).orElse(eyesPos);
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   private boolean isEnemy(class_1297 e) {
      return (!e.method_5767() || (Boolean)this.invisiblesConfig.getValue()) && e instanceof class_1657 && (Boolean)this.playersConfig.getValue() || EntityUtil.isMonster(e) && (Boolean)this.monstersConfig.getValue() || EntityUtil.isNeutral(e) && (Boolean)this.neutralsConfig.getValue() || EntityUtil.isPassive(e) && (Boolean)this.animalsConfig.getValue();
   }

   public class_1297 getEntityTarget() {
      return this.entityTarget;
   }

   public static enum TargetMode {
      SWITCH,
      SINGLE;

      // $FF: synthetic method
      private static AuraModule.TargetMode[] $values() {
         return new AuraModule.TargetMode[]{SWITCH, SINGLE};
      }
   }

   public static enum Priority {
      HEALTH,
      DISTANCE,
      ARMOR;

      // $FF: synthetic method
      private static AuraModule.Priority[] $values() {
         return new AuraModule.Priority[]{HEALTH, DISTANCE, ARMOR};
      }
   }

   public static enum Vector {
      EYES,
      TORSO,
      FEET,
      AUTO;

      // $FF: synthetic method
      private static AuraModule.Vector[] $values() {
         return new AuraModule.Vector[]{EYES, TORSO, FEET, AUTO};
      }
   }
}
