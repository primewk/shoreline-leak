package net.shoreline.client.impl.module.combat;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1774;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1829;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2767;
import net.minecraft.class_2824;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_1297.class_5529;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2846.class_2847;
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
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.RunTickEvent;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.impl.event.world.AddEntityEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.EvictingQueue;
import net.shoreline.client.util.math.timer.CacheTimer;
import net.shoreline.client.util.math.timer.Timer;
import net.shoreline.client.util.player.PlayerUtil;
import net.shoreline.client.util.player.RotationUtil;
import net.shoreline.client.util.render.animation.Animation;
import net.shoreline.client.util.world.EntityUtil;
import net.shoreline.client.util.world.ExplosionUtil;

public class AutoCrystalModule extends RotationModule {
   Config<Boolean> multitaskConfig = new BooleanConfig("Multitask", "Allows attacking while using items", false);
   Config<Boolean> whileMiningConfig = new BooleanConfig("WhileMining", "Allows attacking while mining blocks", false);
   Config<Float> targetRangeConfig = new NumberConfig("EnemyRange", "Range to search for potential enemies", 1.0F, 10.0F, 13.0F);
   Config<Boolean> instantConfig = new BooleanConfig("Instant", "Instantly attacks crystals when they spawn", false);
   Config<Boolean> instantCalcConfig = new BooleanConfig("Instant-Calc", "Calculates a crystal when it spawns and attacks if it meets MINIMUM requirements, this will result in non-ideal crystal attacks", false, () -> {
      return (Boolean)this.instantConfig.getValue();
   });
   Config<Float> instantDamageConfig = new NumberConfig("InstantDamage", "Minimum damage to attack crystals instantly", 1.0F, 6.0F, 10.0F, () -> {
      return (Boolean)this.instantConfig.getValue() && (Boolean)this.instantCalcConfig.getValue();
   });
   Config<Boolean> instantMaxConfig = new BooleanConfig("InstantMax", "Attacks crystals instantly if they exceed the previous max attack damage (Note: This is still not a perfect check because the next tick could have better damages)", true, () -> {
      return (Boolean)this.instantConfig.getValue();
   });
   Config<Boolean> raytraceConfig = new BooleanConfig("Raytrace", "Raytrace to crystal position", true);
   Config<Boolean> swingConfig = new BooleanConfig("Swing", "Swing hand when placing and attacking crystals", true);
   Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotate before placing and breaking", false);
   Config<AutoCrystalModule.Rotate> strictRotateConfig;
   Config<Integer> rotateLimitConfig;
   Config<Boolean> playersConfig;
   Config<Boolean> monstersConfig;
   Config<Boolean> neutralsConfig;
   Config<Boolean> animalsConfig;
   Config<Float> breakSpeedConfig;
   Config<Float> attackDelayConfig;
   Config<Integer> attackFactorConfig;
   Config<Float> randomSpeedConfig;
   Config<Boolean> breakDelayConfig;
   Config<Float> breakTimeoutConfig;
   Config<Float> minTimeoutConfig;
   Config<Integer> ticksExistedConfig;
   Config<Float> breakRangeConfig;
   Config<Float> maxYOffsetConfig;
   Config<Float> breakWallRangeConfig;
   Config<AutoCrystalModule.Swap> antiWeaknessConfig;
   Config<Float> swapDelayConfig;
   Config<Boolean> inhibitConfig;
   Config<Boolean> placeConfig;
   Config<Float> placeSpeedConfig;
   Config<Float> placeRangeConfig;
   Config<Float> placeWallRangeConfig;
   Config<Boolean> placeRangeEyeConfig;
   Config<Boolean> placeRangeCenterConfig;
   Config<Boolean> antiTotemConfig;
   Config<AutoCrystalModule.Swap> autoSwapConfig;
   Config<Float> alternateSpeedConfig;
   Config<Boolean> antiSurroundConfig;
   Config<Boolean> breakValidConfig;
   Config<Boolean> strictDirectionConfig;
   Config<Boolean> exposedDirectionConfig;
   Config<AutoCrystalModule.Placements> placementsConfig;
   Config<Float> minDamageConfig;
   Config<Boolean> armorBreakerConfig;
   Config<Float> armorScaleConfig;
   Config<Float> lethalMultiplier;
   Config<Boolean> lethalDamageConfig;
   Config<Boolean> safetyConfig;
   Config<Boolean> safetyOverride;
   Config<Float> maxLocalDamageConfig;
   Config<Boolean> blockDestructionConfig;
   Config<Boolean> extrapolateRangeConfig;
   Config<Integer> extrapolateTicksConfig;
   Config<Boolean> renderConfig;
   Config<Integer> fadeTimeConfig;
   Config<Boolean> breakDebugConfig;
   Config<Boolean> disableDeathConfig;
   private AutoCrystalModule.DamageData<class_1511> attackCrystal;
   private AutoCrystalModule.DamageData<class_2338> placeCrystal;
   private class_2338 renderPos;
   private class_2338 renderSpawnPos;
   private class_243 crystalRotation;
   private boolean attackRotate;
   private boolean rotated;
   private float[] silentRotations;
   private static final class_238 FULL_CRYSTAL_BB = new class_238(0.0D, 0.0D, 0.0D, 1.0D, 2.0D, 1.0D);
   private static final class_238 HALF_CRYSTAL_BB = new class_238(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
   private final Timer lastAttackTimer;
   private final Timer lastPlaceTimer;
   private final Timer lastSwapTimer;
   private final Timer autoSwapTimer;
   private final Deque<Long> attackLatency;
   private final Map<Integer, Long> attackPackets;
   private final Map<class_2338, Long> placePackets;
   private final Map<class_2338, Animation> fadeList;
   private final ExecutorService executor;

   public AutoCrystalModule() {
      super("AutoCrystal", "Attacks entities with end crystals", ModuleCategory.COMBAT, 750);
      this.strictRotateConfig = new EnumConfig("YawStep", "Rotates yaw over multiple ticks to prevent certain rotation flags in NCP", AutoCrystalModule.Rotate.OFF, AutoCrystalModule.Rotate.values(), () -> {
         return (Boolean)this.rotateConfig.getValue();
      });
      this.rotateLimitConfig = new NumberConfig("YawStep-Limit", "Maximum yaw rotation in degrees for one tick", 1, 180, 180, NumberDisplay.DEGREES, () -> {
         return (Boolean)this.rotateConfig.getValue() && this.strictRotateConfig.getValue() != AutoCrystalModule.Rotate.OFF;
      });
      this.playersConfig = new BooleanConfig("Players", "Target players", true);
      this.monstersConfig = new BooleanConfig("Monsters", "Target monsters", false);
      this.neutralsConfig = new BooleanConfig("Neutrals", "Target neutrals", false);
      this.animalsConfig = new BooleanConfig("Animals", "Target animals", false);
      this.breakSpeedConfig = new NumberConfig("BreakSpeed", "Speed to break crystals", 0.1F, 18.0F, 20.0F);
      this.attackDelayConfig = new NumberConfig("AttackDelay", "Added delays", 0.0F, 0.0F, 5.0F);
      this.attackFactorConfig = new NumberConfig("AttackFactor", "Factor of attack delay", 0, 0, 3, () -> {
         return (double)(Float)this.attackDelayConfig.getValue() > 0.0D;
      });
      this.randomSpeedConfig = new NumberConfig("RandomSpeed", "Randomized delay for breaking crystals", 0.0F, 0.0F, 10.0F);
      this.breakDelayConfig = new BooleanConfig("BreakDelay", "Uses attack latency to calculate break delays", false);
      this.breakTimeoutConfig = new NumberConfig("BreakTimeout", "Time after waiting for the average break time before considering a crystal attack failed", 0.0F, 3.0F, 10.0F, () -> {
         return (Boolean)this.breakDelayConfig.getValue();
      });
      this.minTimeoutConfig = new NumberConfig("MinTimeout", "Minimum time before considering a crystal break/place failed", 0.0F, 5.0F, 20.0F, () -> {
         return (Boolean)this.breakDelayConfig.getValue();
      });
      this.ticksExistedConfig = new NumberConfig("TicksExisted", "Minimum ticks alive to consider crystals for attack", 0, 0, 10);
      this.breakRangeConfig = new NumberConfig("BreakRange", "Range to break crystals", 0.1F, 4.0F, 6.0F);
      this.maxYOffsetConfig = new NumberConfig("MaxYOffset", "Maximum crystal y-offset difference", 1.0F, 5.0F, 10.0F);
      this.breakWallRangeConfig = new NumberConfig("BreakWallRange", "Range to break crystals through walls", 0.1F, 4.0F, 6.0F);
      this.antiWeaknessConfig = new EnumConfig("AntiWeakness", "Swap to tools before attacking crystals", AutoCrystalModule.Swap.OFF, AutoCrystalModule.Swap.values());
      this.swapDelayConfig = new NumberConfig("SwapPenalty", "Delay for attacking after swapping items which prevents NCP flags", 0.0F, 0.0F, 10.0F);
      this.inhibitConfig = new BooleanConfig("Inhibit", "Prevents excessive attacks", true);
      this.placeConfig = new BooleanConfig("Place", "Places crystals to damage enemies. Place settings will only function if this setting is enabled.", true);
      this.placeSpeedConfig = new NumberConfig("PlaceSpeed", "Speed to place crystals", 0.1F, 18.0F, 20.0F, () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.placeRangeConfig = new NumberConfig("PlaceRange", "Range to place crystals", 0.1F, 4.0F, 6.0F, () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.placeWallRangeConfig = new NumberConfig("PlaceWallRange", "Range to place crystals through walls", 0.1F, 4.0F, 6.0F, () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.placeRangeEyeConfig = new BooleanConfig("PlaceRangeEye", "Calculates place ranges starting from the eye position of the player", false, () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.placeRangeCenterConfig = new BooleanConfig("PlaceRangeCenter", "Calculates place ranges to the center of the block", true, () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.antiTotemConfig = new BooleanConfig("AntiTotem", "Predicts totems and places crystals to instantly double pop and kill the target", false, () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.autoSwapConfig = new EnumConfig("Swap", "Swaps to an end crystal before placing if the player is not holding one", AutoCrystalModule.Swap.OFF, AutoCrystalModule.Swap.values(), () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.alternateSpeedConfig = new NumberConfig("AlternateSpeed", "Speed for alternative swapping crystals", 1.0F, 18.0F, 20.0F, () -> {
         return (Boolean)this.placeConfig.getValue() && this.autoSwapConfig.getValue() == AutoCrystalModule.Swap.SILENT_ALT;
      });
      this.antiSurroundConfig = new BooleanConfig("AntiSurround", "Places on mining blocks that when broken, can be placed on to damage enemies. Instantly destroys items spawned from breaking block and allows faster placing", false, () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.breakValidConfig = new BooleanConfig("Strict", "Only places crystals that can be attacked", false, () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.strictDirectionConfig = new BooleanConfig("StrictDirection", "Interacts with only visible directions when placing crystals", false, () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.exposedDirectionConfig = new BooleanConfig("StrictDirection-Exposed", "Interacts with only exposed directions when placing crystals", false, () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.placementsConfig = new EnumConfig("Placements", "Version standard for placing end crystals", AutoCrystalModule.Placements.NATIVE, AutoCrystalModule.Placements.values(), () -> {
         return (Boolean)this.placeConfig.getValue();
      });
      this.minDamageConfig = new NumberConfig("MinDamage", "Minimum damage required to consider attacking or placing an end crystal", 1.0F, 4.0F, 10.0F);
      this.armorBreakerConfig = new BooleanConfig("ArmorBreaker", "Attempts to break enemy armor with crystals", true);
      this.armorScaleConfig = new NumberConfig("ArmorScale", "Armor damage scale before attempting to break enemy armor with crystals", 1.0F, 5.0F, 20.0F, NumberDisplay.PERCENT, () -> {
         return (Boolean)this.armorBreakerConfig.getValue();
      });
      this.lethalMultiplier = new NumberConfig("LethalMultiplier", "If we can kill an enemy with this many crystals, disregard damage values", 0.0F, 1.5F, 4.0F);
      this.lethalDamageConfig = new BooleanConfig("Lethal-DamageTick", "Places lethal crystals on ticks where they damage entities", false);
      this.safetyConfig = new BooleanConfig("Safety", "Accounts for total player safety when attacking and placing crystals", true);
      this.safetyOverride = new BooleanConfig("SafetyOverride", "Overrides the safety checks if the crystal will kill an enemy", false);
      this.maxLocalDamageConfig = new NumberConfig("MaxLocalDamage", "The maximum player damage", 4.0F, 12.0F, 20.0F);
      this.blockDestructionConfig = new BooleanConfig("BlockDestruction", "Accounts for explosion block destruction when calculating damages", false);
      this.extrapolateRangeConfig = new BooleanConfig("ExtrapolateRange", "Accounts for motion when calculating ranges", false);
      this.extrapolateTicksConfig = new NumberConfig("ExtrapolationTicks", "Accounts for motion when calculating enemy positions, not fully accurate.", 0, 0, 10);
      this.renderConfig = new BooleanConfig("Render", "Renders the current placement", true);
      this.fadeTimeConfig = new NumberConfig("Fade-Time", "Timer for the fade", 0, 250, 1000, () -> {
         return false;
      });
      this.breakDebugConfig = new BooleanConfig("Break-Debug", "Debugs break ms in data", false, () -> {
         return (Boolean)this.renderConfig.getValue();
      });
      this.disableDeathConfig = new BooleanConfig("DisableOnDeath", "Disables during disconnect/death", false);
      this.lastAttackTimer = new CacheTimer();
      this.lastPlaceTimer = new CacheTimer();
      this.lastSwapTimer = new CacheTimer();
      this.autoSwapTimer = new CacheTimer();
      this.attackLatency = new EvictingQueue(20);
      this.attackPackets = Collections.synchronizedMap(new ConcurrentHashMap());
      this.placePackets = Collections.synchronizedMap(new ConcurrentHashMap());
      this.fadeList = new HashMap();
      this.executor = Executors.newFixedThreadPool(2);
   }

   public String getModuleData() {
      return (Boolean)this.breakDebugConfig.getValue() ? String.format("%dms", this.getBreakMs()) : super.getModuleData();
   }

   public void onDisable() {
      this.renderPos = null;
      this.attackCrystal = null;
      this.placeCrystal = null;
      this.crystalRotation = null;
      this.silentRotations = null;
      this.attackPackets.clear();
      this.placePackets.clear();
      this.fadeList.clear();
      this.setStage("NONE");
   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      if ((Boolean)this.disableDeathConfig.getValue()) {
         this.disable();
      }

   }

   @EventListener
   public void onPlayerUpdate(PlayerTickEvent event) {
      if (mc.field_1724.method_6115() && mc.field_1724.method_6058() == class_1268.field_5808 || mc.field_1690.field_1886.method_1434() || PlayerUtil.isHotbarKeysPressed()) {
         this.autoSwapTimer.reset();
      }

      this.renderPos = null;
      ArrayList<class_1297> entities = Lists.newArrayList(mc.field_1687.method_18112());
      List<class_2338> blocks = this.getSphere(mc.field_1724.method_19538());
      this.attackCrystal = this.calculateAttackCrystal(entities);
      if ((Boolean)this.placeConfig.getValue()) {
         this.placeCrystal = this.calculatePlaceCrystal(blocks, entities);
      }

      float breakDelay = 1000.0F - (Float)this.breakSpeedConfig.getValue() * 50.0F;
      if ((Boolean)this.breakDelayConfig.getValue()) {
         breakDelay = Math.max((Float)this.minTimeoutConfig.getValue() * 50.0F, (float)this.getBreakMs() + (Float)this.breakTimeoutConfig.getValue() * 50.0F);
      }

      this.attackRotate = this.attackCrystal != null && (double)(Float)this.attackDelayConfig.getValue() <= 0.0D && this.lastAttackTimer.passed(breakDelay);
      if (this.attackCrystal != null) {
         this.crystalRotation = ((class_1511)this.attackCrystal.damageData).method_19538();
      } else if (this.placeCrystal != null) {
         this.crystalRotation = ((class_2338)this.placeCrystal.damageData).method_46558().method_1031(0.0D, 0.5D, 0.0D);
      }

      if ((Boolean)this.rotateConfig.getValue() && this.crystalRotation != null && (this.placeCrystal == null || this.canHoldCrystal())) {
         float[] rotations = RotationUtil.getRotationsTo(mc.field_1724.method_33571(), this.crystalRotation);
         if (this.strictRotateConfig.getValue() != AutoCrystalModule.Rotate.FULL && (this.strictRotateConfig.getValue() != AutoCrystalModule.Rotate.SEMI || !this.attackRotate)) {
            this.rotated = true;
            this.crystalRotation = null;
         } else {
            float serverYaw = Managers.ROTATION.getWrappedYaw();
            float diff = serverYaw - rotations[0];
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
               yaw = rotations[0];
               this.rotated = true;
               this.crystalRotation = null;
            }

            rotations[0] = yaw;
         }

         this.setRotation(rotations[0], rotations[1]);
      } else {
         this.silentRotations = null;
      }

      if (!this.isRotationBlocked() && (this.rotated || !(Boolean)this.rotateConfig.getValue())) {
         class_1268 hand = this.getCrystalHand();
         if (this.attackCrystal != null && this.attackRotate) {
            this.attackCrystal((class_1511)this.attackCrystal.getDamageData(), hand);
            this.setStage("ATTACKING");
            this.lastAttackTimer.reset();
         }

         if (this.placeCrystal != null) {
            this.renderPos = (class_2338)this.placeCrystal.getDamageData();
            if (this.lastPlaceTimer.passed(1000.0F - (Float)this.placeSpeedConfig.getValue() * 50.0F)) {
               this.placeCrystal((class_2338)this.placeCrystal.getDamageData(), hand);
               this.setStage("PLACING");
               this.lastPlaceTimer.reset();
            }
         }

      }
   }

   @EventListener
   public void onRunTick(RunTickEvent event) {
      if (mc.field_1724 != null && !((double)(Float)this.attackDelayConfig.getValue() <= 0.0D)) {
         float attackFactor = 50.0F / Math.max(1.0F, (float)(Integer)this.attackFactorConfig.getValue());
         if (this.attackCrystal != null && this.lastAttackTimer.passed((Float)this.attackDelayConfig.getValue() * attackFactor)) {
            this.attackCrystal((class_1511)this.attackCrystal.getDamageData(), this.getCrystalHand());
            this.lastAttackTimer.reset();
         }

      }
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if ((Boolean)this.renderConfig.getValue()) {
         Iterator var2 = this.fadeList.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<class_2338, Animation> set = (Entry)var2.next();
            if (set.getKey() != this.renderPos) {
               ((Animation)set.getValue()).setState(false);
               int boxAlpha = (int)(80.0D * ((Animation)set.getValue()).getFactor());
               int lineAlpha = (int)(145.0D * ((Animation)set.getValue()).getFactor());
               Color boxColor = Modules.COLORS.getColor(boxAlpha);
               Color lineColor = Modules.COLORS.getColor(lineAlpha);
               RenderManager.renderBox(event.getMatrices(), (class_2338)set.getKey(), boxColor.getRGB());
               RenderManager.renderBoundingBox(event.getMatrices(), (class_2338)set.getKey(), 1.5F, lineColor.getRGB());
            }
         }

         if (this.renderPos != null && this.isHoldingCrystal()) {
            Animation animation = new Animation(true, (float)(Integer)this.fadeTimeConfig.getValue());
            this.fadeList.put(this.renderPos, animation);
         }

         this.fadeList.entrySet().removeIf((e) -> {
            return ((Animation)e.getValue()).getFactor() == 0.0D;
         });
      }

   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2767) {
            class_2767 packet = (class_2767)var3;
            if (packet.method_11888() == class_3419.field_15245 && packet.method_11894().comp_349() == class_3417.field_15152) {
               Iterator var5 = Lists.newArrayList(mc.field_1687.method_18112()).iterator();

               while(var5.hasNext()) {
                  class_1297 entity = (class_1297)var5.next();
                  if (entity instanceof class_1511 && entity.method_5649(packet.method_11890(), packet.method_11889(), packet.method_11893()) < 144.0D) {
                     mc.method_40000(() -> {
                        mc.field_1687.method_2945(entity.method_5628(), class_5529.field_26998);
                     });
                  }
               }
            }
         }

      }
   }

   @EventListener
   public void onAddEntity(AddEntityEvent event) {
      class_1297 var3 = event.getEntity();
      if (var3 instanceof class_1511) {
         class_1511 crystalEntity = (class_1511)var3;
         class_243 crystalPos = crystalEntity.method_19538();
         class_2338 blockPos = class_2338.method_49638(crystalPos.method_1031(0.0D, -1.0D, 0.0D));
         if ((Boolean)this.instantConfig.getValue()) {
            this.renderSpawnPos = blockPos;
            Long time = (Long)this.placePackets.remove(blockPos);
            this.attackRotate = time != null;
            if (this.attackRotate) {
               this.attackInternal(crystalEntity, this.getCrystalHand());
               this.setStage("ATTACKING");
               this.lastAttackTimer.reset();
            } else if ((Boolean)this.instantCalcConfig.getValue()) {
               if (this.attackRangeCheck(crystalPos)) {
                  return;
               }

               double selfDamage = ExplosionUtil.getDamageTo(mc.field_1724, crystalPos, (Boolean)this.blockDestructionConfig.getValue());
               if (this.playerDamageCheck(selfDamage)) {
                  return;
               }

               Iterator var8 = mc.field_1687.method_18112().iterator();

               while(true) {
                  class_1297 entity;
                  double dist;
                  do {
                     double crystalDist;
                     do {
                        do {
                           do {
                              do {
                                 do {
                                    do {
                                       if (!var8.hasNext()) {
                                          return;
                                       }

                                       entity = (class_1297)var8.next();
                                    } while(entity == null);
                                 } while(!entity.method_5805());
                              } while(entity == mc.field_1724);
                           } while(!this.isValidTarget(entity));
                        } while(Managers.SOCIAL.isFriend(entity.method_5477()));

                        crystalDist = crystalPos.method_1025(entity.method_19538());
                     } while(crystalDist > 144.0D);

                     dist = mc.field_1724.method_5858(entity);
                  } while(dist > (double)((Float)this.targetRangeConfig.getValue() * (Float)this.targetRangeConfig.getValue()));

                  boolean var10001;
                  label113: {
                     double damage = ExplosionUtil.getDamageTo(entity, crystalPos, (Boolean)this.blockDestructionConfig.getValue());
                     AutoCrystalModule.DamageData<class_1511> data = new AutoCrystalModule.DamageData(crystalEntity, entity, damage, selfDamage, crystalEntity.method_24515().method_10074());
                     if (!(damage > (double)(Float)this.instantDamageConfig.getValue()) && (this.attackCrystal == null || !(damage >= this.attackCrystal.getDamage()) || !(Boolean)this.instantMaxConfig.getValue())) {
                        label114: {
                           if (entity instanceof class_1309) {
                              class_1309 entity1 = (class_1309)entity;
                              if (this.isCrystalLethalTo(data, entity1)) {
                                 break label114;
                              }
                           }

                           var10001 = false;
                           break label113;
                        }
                     }

                     var10001 = true;
                  }

                  this.attackRotate = var10001;
                  if (this.attackRotate) {
                     this.attackInternal(crystalEntity, this.getCrystalHand());
                     this.setStage("ATTACKING");
                     this.lastAttackTimer.reset();
                     break;
                  }
               }
            }

         }
      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      if (mc.field_1724 != null) {
         if (event.getPacket() instanceof class_2868) {
            this.lastSwapTimer.reset();
         } else {
            class_2596 var3 = event.getPacket();
            if (var3 instanceof class_2846) {
               class_2846 packet = (class_2846)var3;
               if (packet.method_12363() == class_2847.field_12968 && (Boolean)this.antiSurroundConfig.getValue() && this.canUseCrystalOnBlock(packet.method_12362())) {
               }
            }
         }

      }
   }

   public boolean isAttacking() {
      return this.attackCrystal != null;
   }

   public boolean isPlacing() {
      return this.placeCrystal != null && this.isHoldingCrystal();
   }

   public void attackCrystal(class_1511 entity, class_1268 hand) {
      if (!this.attackCheckPre(hand)) {
         class_1293 weakness = mc.field_1724.method_6112(class_1294.field_5911);
         class_1293 strength = mc.field_1724.method_6112(class_1294.field_5910);
         if (weakness != null && (strength == null || weakness.method_5578() > strength.method_5578())) {
            int slot = -1;

            for(int i = 0; i < 9; ++i) {
               class_1799 stack = mc.field_1724.method_31548().method_5438(i);
               if (!stack.method_7960() && (stack.method_7909() instanceof class_1829 || stack.method_7909() instanceof class_1743 || stack.method_7909() instanceof class_1810)) {
                  slot = i;
                  break;
               }
            }

            if (slot != -1) {
               boolean canSwap = this.antiWeaknessConfig.getValue() != AutoCrystalModule.Swap.NORMAL || this.autoSwapTimer.passed(500);
               if (this.antiWeaknessConfig.getValue() != AutoCrystalModule.Swap.OFF && canSwap) {
                  if (this.antiWeaknessConfig.getValue() == AutoCrystalModule.Swap.SILENT_ALT) {
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot + 36, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
                  } else if (this.antiWeaknessConfig.getValue() == AutoCrystalModule.Swap.SILENT) {
                     Managers.INVENTORY.setSlot(slot);
                  } else {
                     Managers.INVENTORY.setClientSlot(slot);
                  }
               }

               this.attackInternal(entity, class_1268.field_5808);
               if (canSwap) {
                  if (this.antiWeaknessConfig.getValue() == AutoCrystalModule.Swap.SILENT_ALT) {
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot + 36, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
                  } else if (this.antiWeaknessConfig.getValue() == AutoCrystalModule.Swap.SILENT) {
                     Managers.INVENTORY.syncToClient();
                  }
               }
            }
         } else {
            this.attackInternal(entity, hand);
         }

      }
   }

   private void attackInternal(class_1511 crystalEntity, class_1268 hand) {
      hand = hand != null ? hand : class_1268.field_5808;
      Managers.NETWORK.sendPacket(class_2824.method_34206(crystalEntity, mc.field_1724.method_5715()));
      this.attackPackets.put(crystalEntity.method_5628(), System.currentTimeMillis());
      if ((Boolean)this.swingConfig.getValue()) {
         mc.field_1724.method_6104(hand);
      } else {
         Managers.NETWORK.sendPacket(new class_2879(hand));
      }

   }

   private void placeCrystal(class_2338 blockPos, class_1268 hand) {
      if (!this.checkMultitask()) {
         class_2350 sidePlace = this.getPlaceDirection(blockPos);
         class_3965 result = new class_3965(blockPos.method_46558(), sidePlace, blockPos, false);
         if (this.autoSwapConfig.getValue() != AutoCrystalModule.Swap.OFF && hand != class_1268.field_5810 && this.getCrystalHand() == null) {
            if (this.isSilentSwap((AutoCrystalModule.Swap)this.autoSwapConfig.getValue()) && Managers.INVENTORY.count(class_1802.field_8301) == 0) {
               return;
            }

            int crystalSlot = this.getCrystalSlot();
            if (crystalSlot != -1) {
               boolean canSwap = this.autoSwapConfig.getValue() != AutoCrystalModule.Swap.NORMAL || this.autoSwapTimer.passed(500);
               if (canSwap) {
                  if (this.autoSwapConfig.getValue() == AutoCrystalModule.Swap.SILENT_ALT) {
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, crystalSlot + 36, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
                  } else if (this.autoSwapConfig.getValue() == AutoCrystalModule.Swap.SILENT) {
                     Managers.INVENTORY.setSlot(crystalSlot);
                  } else {
                     Managers.INVENTORY.setClientSlot(crystalSlot);
                  }
               }

               this.placeInternal(result, class_1268.field_5808);
               this.placePackets.put(blockPos, System.currentTimeMillis());
               if (canSwap) {
                  if (this.autoSwapConfig.getValue() == AutoCrystalModule.Swap.SILENT_ALT) {
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, crystalSlot + 36, mc.field_1724.method_31548().field_7545, class_1713.field_7791, mc.field_1724);
                  } else if (this.autoSwapConfig.getValue() == AutoCrystalModule.Swap.SILENT) {
                     Managers.INVENTORY.syncToClient();
                  }
               }
            }
         } else if (this.isHoldingCrystal()) {
            this.placeInternal(result, hand);
            this.placePackets.put(blockPos, System.currentTimeMillis());
         }

      }
   }

   private void placeInternal(class_3965 result, class_1268 hand) {
      if (hand != null) {
         Managers.NETWORK.sendSequencedPacket((id) -> {
            return new class_2885(hand, result, id);
         });
         if ((Boolean)this.swingConfig.getValue()) {
            mc.field_1724.method_6104(hand);
         } else {
            Managers.NETWORK.sendPacket(new class_2879(hand));
         }

      }
   }

   private boolean isSilentSwap(AutoCrystalModule.Swap swap) {
      return swap == AutoCrystalModule.Swap.SILENT || swap == AutoCrystalModule.Swap.SILENT_ALT;
   }

   private int getCrystalSlot() {
      int slot = -1;

      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (stack.method_7909() instanceof class_1774) {
            slot = i;
            break;
         }
      }

      return slot;
   }

   private class_2350 getPlaceDirection(class_2338 blockPos) {
      int x = blockPos.method_10263();
      int y = blockPos.method_10264();
      int z = blockPos.method_10260();
      class_3965 result;
      if ((Boolean)this.strictDirectionConfig.getValue()) {
         if (mc.field_1724.method_23318() >= (double)blockPos.method_10264()) {
            return class_2350.field_11036;
         }

         result = mc.field_1687.method_17742(new class_3959(mc.field_1724.method_33571(), new class_243((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D), class_3960.field_17559, class_242.field_1348, mc.field_1724));
         if (result != null && result.method_17783() == class_240.field_1332) {
            class_2350 direction = result.method_17780();
            if (!(Boolean)this.exposedDirectionConfig.getValue() || mc.field_1687.method_22347(blockPos.method_10093(direction))) {
               return direction;
            }
         }
      } else {
         if (mc.field_1687.method_24794(blockPos)) {
            return class_2350.field_11033;
         }

         result = mc.field_1687.method_17742(new class_3959(mc.field_1724.method_33571(), new class_243((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D), class_3960.field_17559, class_242.field_1348, mc.field_1724));
         if (result != null && result.method_17783() == class_240.field_1332) {
            return result.method_17780();
         }
      }

      return class_2350.field_11036;
   }

   private AutoCrystalModule.DamageData<class_1511> calculateAttackCrystal(List<class_1297> entities) {
      if (entities.isEmpty()) {
         return null;
      } else {
         AutoCrystalModule.DamageData<class_1511> data = null;
         Iterator var3 = entities.iterator();

         label127:
         while(true) {
            class_1297 crystal;
            class_1511 crystal1;
            double selfDamage;
            boolean unsafeToPlayer;
            do {
               boolean attacked;
               do {
                  do {
                     do {
                        do {
                           if (!var3.hasNext()) {
                              if (data != null && !this.targetDamageCheck(data)) {
                                 return data;
                              }

                              return null;
                           }

                           crystal = (class_1297)var3.next();
                        } while(!(crystal instanceof class_1511));

                        crystal1 = (class_1511)crystal;
                     } while(!crystal.method_5805());

                     Long time = (Long)this.attackPackets.get(crystal.method_5628());
                     attacked = time != null && time < (long)this.getBreakMs();
                  } while((crystal.field_6012 < (Integer)this.ticksExistedConfig.getValue() || attacked) && (Boolean)this.inhibitConfig.getValue());
               } while(this.attackRangeCheck(crystal1));

               selfDamage = ExplosionUtil.getDamageTo(mc.field_1724, crystal.method_19538(), (Boolean)this.blockDestructionConfig.getValue());
               unsafeToPlayer = this.playerDamageCheck(selfDamage);
            } while(unsafeToPlayer && !(Boolean)this.safetyOverride.getValue());

            Iterator var11 = entities.iterator();

            while(true) {
               class_1297 entity;
               double damage;
               do {
                  do {
                     double dist;
                     do {
                        double crystalDist;
                        do {
                           do {
                              do {
                                 do {
                                    do {
                                       do {
                                          if (!var11.hasNext()) {
                                             continue label127;
                                          }

                                          entity = (class_1297)var11.next();
                                       } while(entity == null);
                                    } while(!entity.method_5805());
                                 } while(entity == mc.field_1724);
                              } while(!this.isValidTarget(entity));
                           } while(Managers.SOCIAL.isFriend(entity.method_5477()));

                           crystalDist = crystal.method_5858(entity);
                        } while(crystalDist > 144.0D);

                        dist = mc.field_1724.method_5858(entity);
                     } while(dist > (double)((Float)this.targetRangeConfig.getValue() * (Float)this.targetRangeConfig.getValue()));

                     damage = ExplosionUtil.getDamageTo(entity, crystal.method_19538(), (Boolean)this.blockDestructionConfig.getValue());
                  } while(this.checkOverrideSafety(unsafeToPlayer, damage, entity));
               } while(data != null && !(damage > data.getDamage()));

               data = new AutoCrystalModule.DamageData(crystal1, entity, damage, selfDamage, crystal1.method_24515().method_10074());
            }
         }
      }
   }

   private boolean attackRangeCheck(class_1511 entity) {
      return this.attackRangeCheck(entity.method_19538());
   }

   private boolean attackRangeCheck(class_243 entityPos) {
      class_243 playerPos = mc.field_1724.method_33571();
      double dist = playerPos.method_1025(entityPos);
      if (dist > ((NumberConfig)this.breakRangeConfig).getValueSq()) {
         return true;
      } else {
         double yOff = Math.abs(entityPos.method_10214() - mc.field_1724.method_23318());
         if (yOff > (double)(Float)this.maxYOffsetConfig.getValue()) {
            return true;
         } else {
            class_3965 result = mc.field_1687.method_17742(new class_3959(playerPos, entityPos, class_3960.field_17558, class_242.field_1348, mc.field_1724));
            return result.method_17783() != class_240.field_1333 && dist > (double)((Float)this.breakWallRangeConfig.getValue() * (Float)this.breakWallRangeConfig.getValue());
         }
      }
   }

   private AutoCrystalModule.DamageData<class_2338> calculatePlaceCrystal(List<class_2338> placeBlocks, List<class_1297> entities) {
      if (!placeBlocks.isEmpty() && !entities.isEmpty()) {
         AutoCrystalModule.DamageData<class_2338> data = null;
         Iterator var4 = placeBlocks.iterator();

         label100:
         while(true) {
            class_2338 pos;
            double selfDamage;
            boolean unsafeToPlayer;
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        if (data != null && !this.targetDamageCheck(data)) {
                           return data;
                        }

                        return null;
                     }

                     pos = (class_2338)var4.next();
                  } while(!this.canUseCrystalOnBlock(pos));
               } while(this.placeRangeCheck(pos));

               selfDamage = ExplosionUtil.getDamageTo(mc.field_1724, this.crystalDamageVec(pos), (Boolean)this.blockDestructionConfig.getValue());
               unsafeToPlayer = this.playerDamageCheck(selfDamage);
            } while(unsafeToPlayer && !(Boolean)this.safetyOverride.getValue());

            Iterator var9 = entities.iterator();

            while(true) {
               class_1297 entity;
               double damage;
               do {
                  do {
                     double dist;
                     do {
                        double blockDist;
                        do {
                           do {
                              do {
                                 do {
                                    do {
                                       do {
                                          if (!var9.hasNext()) {
                                             continue label100;
                                          }

                                          entity = (class_1297)var9.next();
                                       } while(entity == null);
                                    } while(!entity.method_5805());
                                 } while(entity == mc.field_1724);
                              } while(!this.isValidTarget(entity));
                           } while(Managers.SOCIAL.isFriend(entity.method_5477()));

                           blockDist = pos.method_19770(entity.method_19538());
                        } while(blockDist > 144.0D);

                        dist = mc.field_1724.method_5858(entity);
                     } while(dist > (double)((Float)this.targetRangeConfig.getValue() * (Float)this.targetRangeConfig.getValue()));

                     damage = ExplosionUtil.getDamageTo(entity, this.crystalDamageVec(pos), (Boolean)this.blockDestructionConfig.getValue());
                  } while(this.checkOverrideSafety(unsafeToPlayer, damage, entity));
               } while(data != null && !(damage > data.getDamage()));

               data = new AutoCrystalModule.DamageData(pos, entity, damage, selfDamage);
            }
         }
      } else {
         return null;
      }
   }

   private boolean placeRangeCheck(class_2338 pos) {
      class_243 player = (Boolean)this.placeRangeEyeConfig.getValue() ? mc.field_1724.method_33571() : mc.field_1724.method_19538();
      double dist = (Boolean)this.placeRangeCenterConfig.getValue() ? player.method_1025(pos.method_46558()) : pos.method_40081(player.field_1352, player.field_1351, player.field_1350);
      if (dist > ((NumberConfig)this.placeRangeConfig).getValueSq()) {
         return true;
      } else {
         class_243 raytrace = class_243.method_24954(pos).method_1031(0.0D, (Boolean)this.raytraceConfig.getValue() ? 2.700000047683716D : 1.0D, 0.0D);
         class_3965 result = mc.field_1687.method_17742(new class_3959(mc.field_1724.method_33571(), raytrace, class_3960.field_17558, class_242.field_1348, mc.field_1724));
         float maxDist = (Float)this.breakRangeConfig.getValue() * (Float)this.breakRangeConfig.getValue();
         if (result != null && result.method_17783() == class_240.field_1332 && result.method_17777() != pos) {
            maxDist = (Float)this.breakWallRangeConfig.getValue() * (Float)this.breakWallRangeConfig.getValue();
            if (dist > (double)((Float)this.placeWallRangeConfig.getValue() * (Float)this.placeWallRangeConfig.getValue())) {
               return true;
            }
         }

         return (Boolean)this.breakValidConfig.getValue() && dist > (double)maxDist;
      }
   }

   private boolean checkOverrideSafety(boolean unsafeToPlayer, double damage, class_1297 entity) {
      return (Boolean)this.safetyOverride.getValue() && unsafeToPlayer && damage < (double)EntityUtil.getHealth(entity) + 0.5D;
   }

   private boolean targetDamageCheck(AutoCrystalModule.DamageData<?> crystal) {
      double minDmg = (double)(Float)this.minDamageConfig.getValue();
      class_1297 var5 = crystal.getAttackTarget();
      if (var5 instanceof class_1309) {
         class_1309 entity = (class_1309)var5;
         if (this.isCrystalLethalTo(crystal, entity)) {
            minDmg = 2.0D;
         }
      }

      return crystal.getDamage() < minDmg;
   }

   private boolean playerDamageCheck(double playerDamage) {
      if (!mc.field_1724.method_7337()) {
         float health = mc.field_1724.method_6032() + mc.field_1724.method_6067();
         if ((Boolean)this.safetyConfig.getValue() && playerDamage >= (double)(health + 0.5F)) {
            return true;
         } else {
            return playerDamage > (double)(Float)this.maxLocalDamageConfig.getValue();
         }
      } else {
         return false;
      }
   }

   private boolean isFeetSurrounded(class_1309 entity) {
      class_2338 pos1 = entity.method_24515();
      if (!mc.field_1687.method_8320(pos1).method_45474()) {
         return true;
      } else {
         class_2350[] var3 = class_2350.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            class_2350 direction = var3[var5];
            if (direction.method_10166().method_10179()) {
               class_2338 pos2 = pos1.method_10093(direction);
               if (mc.field_1687.method_8320(pos2).method_45474()) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean isCrystalLethalTo(AutoCrystalModule.DamageData<?> crystal, class_1309 entity) {
      if ((Boolean)this.lethalDamageConfig.getValue()) {
         return this.lastAttackTimer.passed(500);
      } else {
         float health = entity.method_6032() + entity.method_6067();
         if (crystal.getDamage() * (double)(1.0F + (Float)this.lethalMultiplier.getValue()) >= (double)(health + 0.5F)) {
            return true;
         } else {
            if ((Boolean)this.armorBreakerConfig.getValue()) {
               Iterator var4 = entity.method_5661().iterator();

               while(var4.hasNext()) {
                  class_1799 armorStack = (class_1799)var4.next();
                  int n = armorStack.method_7919();
                  int n1 = armorStack.method_7936();
                  float durability = (float)(n1 - n) / (float)n1 * 100.0F;
                  if (durability < (Float)this.armorScaleConfig.getValue()) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   private boolean attackCheckPre(class_1268 hand) {
      if (!this.lastSwapTimer.passed((Float)this.swapDelayConfig.getValue() * 25.0F)) {
         return true;
      } else {
         return hand == class_1268.field_5808 ? this.checkMultitask() : false;
      }
   }

   private boolean checkMultitask() {
      return !(Boolean)this.multitaskConfig.getValue() && mc.field_1724.method_6115() || !(Boolean)this.whileMiningConfig.getValue() && mc.field_1761.method_2923();
   }

   private boolean isHoldingCrystal() {
      if (this.checkMultitask() || this.autoSwapConfig.getValue() != AutoCrystalModule.Swap.SILENT && this.autoSwapConfig.getValue() != AutoCrystalModule.Swap.SILENT_ALT) {
         return this.getCrystalHand() != null;
      } else {
         return true;
      }
   }

   private class_243 crystalDamageVec(class_2338 pos) {
      return class_243.method_24954(pos).method_1031(0.5D, 1.0D, 0.5D);
   }

   private boolean isValidTarget(class_1297 e) {
      return e instanceof class_1657 && (Boolean)this.playersConfig.getValue() || EntityUtil.isMonster(e) && (Boolean)this.monstersConfig.getValue() || EntityUtil.isNeutral(e) && (Boolean)this.neutralsConfig.getValue() || EntityUtil.isPassive(e) && (Boolean)this.animalsConfig.getValue();
   }

   public boolean canUseCrystalOnBlock(class_2338 p) {
      class_2680 state = mc.field_1687.method_8320(p);
      if (!state.method_27852(class_2246.field_10540) && !state.method_27852(class_2246.field_9987)) {
         return false;
      } else {
         class_2338 p2 = p.method_10084();
         class_2680 state2 = mc.field_1687.method_8320(p2);
         if (this.placementsConfig.getValue() == AutoCrystalModule.Placements.PROTOCOL && !mc.field_1687.method_22347(p2.method_10084())) {
            return false;
         } else if (!mc.field_1687.method_22347(p2) && !state2.method_27852(class_2246.field_10036)) {
            return false;
         } else {
            class_238 bb = Managers.NETWORK.isCrystalPvpCC() ? HALF_CRYSTAL_BB : FULL_CRYSTAL_BB;
            double d = (double)p2.method_10263();
            double e = (double)p2.method_10264();
            double f = (double)p2.method_10260();
            List<class_1297> list = this.getEntitiesBlockingCrystal(new class_238(d, e, f, d + bb.field_1320, e + bb.field_1325, f + bb.field_1324));
            return list.isEmpty();
         }
      }
   }

   private List<class_1297> getEntitiesBlockingCrystal(class_238 box) {
      List<class_1297> entities = new CopyOnWriteArrayList(mc.field_1687.method_8335((class_1297)null, box));
      Iterator var3 = entities.iterator();

      while(true) {
         class_1297 entity;
         label33:
         do {
            while(var3.hasNext()) {
               entity = (class_1297)var3.next();
               if (entity != null && entity.method_5805() && !(entity instanceof class_1303)) {
                  if (entity instanceof class_1511) {
                     class_1511 entity1 = (class_1511)entity;
                     if (entity1.method_5829().method_994(box)) {
                        break label33;
                     }
                  }
                  continue label33;
               }

               entities.remove(entity);
            }

            return entities;
         } while(!this.attackPackets.containsKey(entity.method_5628()) || entity.field_6012 >= (Integer)this.ticksExistedConfig.getValue());

         entities.remove(entity);
      }
   }

   private boolean intersectingCrystalCheck(class_1511 entity) {
      return this.attackRangeCheck(entity);
   }

   private List<class_2338> getSphere(class_243 origin) {
      List<class_2338> sphere = new ArrayList();
      double rad = Math.ceil((double)(Float)this.placeRangeConfig.getValue());

      for(double x = -rad; x <= rad; ++x) {
         for(double y = -rad; y <= rad; ++y) {
            for(double z = -rad; z <= rad; ++z) {
               class_2382 pos = new class_2382((int)(origin.method_10216() + x), (int)(origin.method_10214() + y), (int)(origin.method_10215() + z));
               class_2338 p = new class_2338(pos);
               sphere.add(p);
            }
         }
      }

      return sphere;
   }

   private boolean canHoldCrystal() {
      return this.isHoldingCrystal() || this.autoSwapConfig.getValue() != AutoCrystalModule.Swap.OFF && this.getCrystalSlot() != -1;
   }

   private class_1268 getCrystalHand() {
      class_1799 offhand = mc.field_1724.method_6079();
      class_1799 mainhand = mc.field_1724.method_6047();
      if (offhand.method_7909() instanceof class_1774) {
         return class_1268.field_5810;
      } else {
         return mainhand.method_7909() instanceof class_1774 ? class_1268.field_5808 : null;
      }
   }

   public void setStage(String crystalStage) {
   }

   public int getBreakMs() {
      float avg = 0.0F;
      ArrayList<Long> latencyCopy = Lists.newArrayList(this.attackLatency);
      if (!latencyCopy.isEmpty()) {
         float t;
         for(Iterator var3 = latencyCopy.iterator(); var3.hasNext(); avg += t) {
            t = (float)(Long)var3.next();
         }

         avg /= (float)latencyCopy.size();
      }

      return (int)avg;
   }

   public static enum Rotate {
      FULL,
      SEMI,
      OFF;

      // $FF: synthetic method
      private static AutoCrystalModule.Rotate[] $values() {
         return new AutoCrystalModule.Rotate[]{FULL, SEMI, OFF};
      }
   }

   public static enum Swap {
      NORMAL,
      SILENT,
      SILENT_ALT,
      OFF;

      // $FF: synthetic method
      private static AutoCrystalModule.Swap[] $values() {
         return new AutoCrystalModule.Swap[]{NORMAL, SILENT, SILENT_ALT, OFF};
      }
   }

   public static enum Placements {
      NATIVE,
      PROTOCOL;

      // $FF: synthetic method
      private static AutoCrystalModule.Placements[] $values() {
         return new AutoCrystalModule.Placements[]{NATIVE, PROTOCOL};
      }
   }

   private static class DamageData<T> {
      private final List<String> tags = new ArrayList();
      private T damageData;
      private class_1297 attackTarget;
      private class_2338 blockPos;
      private double damage;
      private double selfDamage;

      public DamageData() {
      }

      public DamageData(class_2338 damageData, class_1297 attackTarget, double damage, double selfDamage) {
         this.damageData = damageData;
         this.attackTarget = attackTarget;
         this.damage = damage;
         this.selfDamage = selfDamage;
         this.blockPos = damageData;
      }

      public DamageData(T damageData, class_1297 attackTarget, double damage, double selfDamage, class_2338 blockPos) {
         this.damageData = damageData;
         this.attackTarget = attackTarget;
         this.damage = damage;
         this.selfDamage = selfDamage;
         this.blockPos = blockPos;
      }

      public void addTag(String tag) {
         this.tags.add(tag);
      }

      public void setDamageData(T damageData, class_1297 attackTarget, double damage, double selfDamage) {
         this.damageData = damageData;
         this.attackTarget = attackTarget;
         this.damage = damage;
         this.selfDamage = selfDamage;
      }

      public T getDamageData() {
         return this.damageData;
      }

      public class_1297 getAttackTarget() {
         return this.attackTarget;
      }

      public double getDamage() {
         return this.damage;
      }

      public double getSelfDamage() {
         return this.selfDamage;
      }

      public class_2338 getBlockPos() {
         return this.blockPos;
      }
   }

   private class PlaceCrystalTask implements Callable<AutoCrystalModule.DamageData<class_2338>> {
      private final List<class_2338> threadSafeBlocks;
      private final List<class_1297> threadSafeEntities;

      public PlaceCrystalTask(List<class_2338> threadSafeBlocks, List<class_1297> threadSafeEntities) {
         this.threadSafeBlocks = threadSafeBlocks;
         this.threadSafeEntities = threadSafeEntities;
      }

      public AutoCrystalModule.DamageData<class_2338> call() throws Exception {
         return AutoCrystalModule.this.calculatePlaceCrystal(this.threadSafeBlocks, this.threadSafeEntities);
      }
   }

   private class AttackCrystalTask implements Callable<AutoCrystalModule.DamageData<class_1511>> {
      private final List<class_1297> threadSafeEntities;

      public AttackCrystalTask(List<class_1297> threadSafeEntities) {
         this.threadSafeEntities = threadSafeEntities;
      }

      public AutoCrystalModule.DamageData<class_1511> call() throws Exception {
         return AutoCrystalModule.this.calculateAttackCrystal(this.threadSafeEntities);
      }
   }

   public static enum Sequential {
      NORMAL,
      STRICT,
      NONE;

      // $FF: synthetic method
      private static AutoCrystalModule.Sequential[] $values() {
         return new AutoCrystalModule.Sequential[]{NORMAL, STRICT, NONE};
      }
   }
}
