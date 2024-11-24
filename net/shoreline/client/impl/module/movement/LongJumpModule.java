package net.shoreline.client.impl.module.movement;

import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_241;
import net.minecraft.class_2708;
import net.minecraft.class_434;
import net.minecraft.class_2828.class_2829;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.entity.player.PlayerMoveEvent;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerUpdateEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.player.MovementUtil;
import net.shoreline.client.util.string.EnumFormatter;

public class LongJumpModule extends ToggleModule {
   Config<LongJumpModule.JumpMode> modeConfig;
   Config<Float> boostConfig;
   Config<Boolean> autoDisableConfig;
   private int stage;
   private double distance;
   private double speed;
   private int airTicks;
   private int groundTicks;

   public LongJumpModule() {
      super("LongJump", "Allows the player to jump farther", ModuleCategory.MOVEMENT);
      this.modeConfig = new EnumConfig("Mode", "The mode for long jump", LongJumpModule.JumpMode.NORMAL, LongJumpModule.JumpMode.values());
      this.boostConfig = new NumberConfig("Boost", "The jump boost speed", 0.1F, 4.5F, 10.0F, () -> {
         return this.modeConfig.getValue() == LongJumpModule.JumpMode.NORMAL;
      });
      this.autoDisableConfig = new BooleanConfig("AutoDisable", "Automatically disables when rubberband is detected", true);
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum((Enum)this.modeConfig.getValue());
   }

   public void onEnable() {
      this.groundTicks = 0;
   }

   public void onDisable() {
      this.stage = 0;
      this.distance = 0.0D;
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         double dx = mc.field_1724.method_23317() - mc.field_1724.field_6014;
         double dz = mc.field_1724.method_23321() - mc.field_1724.field_5969;
         this.distance = Math.sqrt(dx * dx + dz * dz);
      }
   }

   @EventListener
   public void onPlayerMove(PlayerMoveEvent event) {
      if (this.modeConfig.getValue() == LongJumpModule.JumpMode.NORMAL) {
         if (mc.field_1724 == null || mc.field_1687 == null || Modules.FLIGHT.isEnabled() || Modules.PACKET_FLY.isEnabled() || !MovementUtil.isInputtingMovement()) {
            return;
         }

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
         if (this.stage == 0) {
            this.stage = 1;
            this.speed = (double)(Float)this.boostConfig.getValue() * base - 0.01D;
         } else if (this.stage == 1) {
            this.stage = 2;
            Managers.MOVEMENT.setMotionY(0.42D);
            event.setY(0.42D);
            this.speed *= 2.149D;
         } else if (this.stage == 2) {
            this.stage = 3;
            double moveSpeed = 0.66D * (this.distance - base);
            this.speed = this.distance - moveSpeed;
         } else {
            if (!mc.field_1687.method_8587(mc.field_1724, mc.field_1724.method_5829().method_989(0.0D, mc.field_1724.method_18798().method_10214(), 0.0D)) || mc.field_1724.field_5992) {
               this.stage = 0;
            }

            this.speed = this.distance - this.distance / 159.0D;
         }

         this.speed = Math.max(this.speed, base);
         event.cancel();
         class_241 motion = Modules.SPEED.handleStrafeMotion((float)this.speed);
         event.setX((double)motion.field_1343);
         event.setZ((double)motion.field_1342);
      }

   }

   @EventListener
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (event.getStage() == EventStage.PRE && this.modeConfig.getValue() == LongJumpModule.JumpMode.GLIDE) {
         if (Modules.FLIGHT.isEnabled() || mc.field_1724.method_6128() || mc.field_1724.method_21754() || mc.field_1724.method_5799()) {
            return;
         }

         if (mc.field_1724.method_24828()) {
            this.distance = 0.0D;
         }

         float direction = mc.field_1724.method_36454() + (float)(mc.field_1724.field_6250 < 0.0F ? 180 : 0) + (mc.field_1724.field_6212 > 0.0F ? -90.0F * (mc.field_1724.field_6250 < 0.0F ? -0.5F : (mc.field_1724.field_6250 > 0.0F ? 0.5F : 1.0F)) : 0.0F) - (mc.field_1724.field_6212 < 0.0F ? -90.0F * (mc.field_1724.field_6250 < 0.0F ? -0.5F : (mc.field_1724.field_6250 > 0.0F ? 0.5F : 1.0F)) : 0.0F);
         float dx = (float)Math.cos((double)(direction + 90.0F) * 3.141592653589793D / 180.0D);
         float dz = (float)Math.sin((double)(direction + 90.0F) * 3.141592653589793D / 180.0D);
         if (!mc.field_1724.field_5992) {
            ++this.airTicks;
            if (mc.field_1724.field_3913.field_3903) {
               mc.field_1724.field_3944.method_52787(new class_2829(0.0D, 2.147483647E9D, 0.0D, false));
            }

            this.groundTicks = 0;
            if (!mc.field_1724.field_5992) {
               if (mc.field_1724.method_18798().field_1351 == -0.07190068807140403D) {
                  Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.3499999940395355D);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.10306193759436909D) {
                  Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.550000011920929D);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.13395038817442878D) {
                  Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.6700000166893005D);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.16635183030382D) {
                  Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.6899999976158142D);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.19088711097794803D) {
                  Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.7099999785423279D);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.21121925191528862D) {
                  Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.20000000298023224D);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.11979897632390576D) {
                  Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.9300000071525574D);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.18758479151225355D) {
                  Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.7200000286102295D);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.21075983825251726D) {
                  Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.7599999904632568D);
               }

               if (this.getJumpCollisions(mc.field_1724, 70.0D) < 0.5D) {
                  if (mc.field_1724.method_18798().field_1351 == -0.23537393014173347D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.029999999329447746D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08531999505205401D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * -0.5D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.03659320313669756D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * -0.10000000149011612D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.07481386749524899D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * -0.07000000029802322D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.0732677700939672D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * -0.05000000074505806D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.07480988066790395D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * -0.03999999910593033D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.0784000015258789D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.10000000149011612D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08608320193943977D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.10000000149011612D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08683615560584318D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.05000000074505806D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08265497329678266D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.05000000074505806D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08245009535659828D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.05000000074505806D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08244005633718426D) {
                     Managers.MOVEMENT.setMotionY(-0.08243956442521608D);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08243956442521608D) {
                     Managers.MOVEMENT.setMotionY(-0.08244005590677261D);
                  }

                  if (mc.field_1724.method_18798().field_1351 > -0.1D && mc.field_1724.method_18798().field_1351 < -0.08D && !mc.field_1724.method_24828() && mc.field_1724.field_3913.field_3910) {
                     Managers.MOVEMENT.setMotionY(-9.999999747378752E-5D);
                  }
               } else {
                  if (mc.field_1724.method_18798().field_1351 < -0.2D && mc.field_1724.method_18798().field_1351 > -0.24D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.7D);
                  }

                  if (mc.field_1724.method_18798().field_1351 < -0.25D && mc.field_1724.method_18798().field_1351 > -0.32D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.8D);
                  }

                  if (mc.field_1724.method_18798().field_1351 < -0.35D && mc.field_1724.method_18798().field_1351 > -0.8D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.98D);
                  }

                  if (mc.field_1724.method_18798().field_1351 < -0.8D && mc.field_1724.method_18798().field_1351 > -1.6D) {
                     Managers.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.99D);
                  }
               }
            }

            Managers.TICK.setClientTick(0.85F);
            double[] jumpFactor = new double[]{0.420606D, 0.417924D, 0.415258D, 0.412609D, 0.409977D, 0.407361D, 0.404761D, 0.402178D, 0.399611D, 0.39706D, 0.394525D, 0.392D, 0.3894D, 0.38644D, 0.383655D, 0.381105D, 0.37867D, 0.37625D, 0.37384D, 0.37145D, 0.369D, 0.3666D, 0.3642D, 0.3618D, 0.35945D, 0.357D, 0.354D, 0.351D, 0.348D, 0.345D, 0.342D, 0.339D, 0.336D, 0.333D, 0.33D, 0.327D, 0.324D, 0.321D, 0.318D, 0.315D, 0.312D, 0.309D, 0.307D, 0.305D, 0.303D, 0.3D, 0.297D, 0.295D, 0.293D, 0.291D, 0.289D, 0.287D, 0.285D, 0.283D, 0.281D, 0.279D, 0.277D, 0.275D, 0.273D, 0.271D, 0.269D, 0.267D, 0.265D, 0.263D, 0.261D, 0.259D, 0.257D, 0.255D, 0.253D, 0.251D, 0.249D, 0.247D, 0.245D, 0.243D, 0.241D, 0.239D, 0.237D};
            if (mc.field_1724.field_3913.field_3910) {
               try {
                  Managers.MOVEMENT.setMotionXZ((double)dx * jumpFactor[this.airTicks - 1] * 3.0D, (double)dz * jumpFactor[this.airTicks - 1] * 3.0D);
               } catch (ArrayIndexOutOfBoundsException var7) {
               }

               return;
            }

            Managers.MOVEMENT.setMotionXZ(0.0D, 0.0D);
            return;
         }

         Managers.TICK.setClientTick(1.0F);
         this.airTicks = 0;
         ++this.groundTicks;
         Managers.MOVEMENT.setMotionXZ(mc.field_1724.method_18798().field_1352 / 13.0D, mc.field_1724.method_18798().field_1350 / 13.0D);
         if (this.groundTicks == 1) {
            mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_24828()));
            mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317() + 0.0624D, mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_24828()));
            mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.419D, mc.field_1724.method_23321(), mc.field_1724.method_24828()));
            mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317() + 0.0624D, mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_24828()));
            mc.field_1724.field_3944.method_52787(new class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.419D, mc.field_1724.method_23321(), mc.field_1724.method_24828()));
         }

         if (this.groundTicks > 2) {
            this.groundTicks = 0;
            Managers.MOVEMENT.setMotionXZ((double)dx * 0.3D, (double)dz * 0.3D);
            Managers.MOVEMENT.setMotionY(0.42399999499320984D);
         }
      }

   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      if (mc.field_1724 != null && mc.field_1687 != null && !(mc.field_1755 instanceof class_434)) {
         if (event.getPacket() instanceof class_2708 && (Boolean)this.autoDisableConfig.getValue()) {
            this.disable();
         }

      }
   }

   private double getJumpCollisions(class_1657 player, double d) {
      return 1.0D;
   }

   public static enum JumpMode {
      NORMAL,
      GLIDE;

      // $FF: synthetic method
      private static LongJumpModule.JumpMode[] $values() {
         return new LongJumpModule.JumpMode[]{NORMAL, GLIDE};
      }
   }
}
