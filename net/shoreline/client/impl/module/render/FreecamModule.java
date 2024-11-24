package net.shoreline.client.impl.module.render;

import net.minecraft.class_239;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_315;
import net.minecraft.class_3532;
import net.minecraft.class_743;
import net.minecraft.class_239.class_240;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.MacroConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.macro.Macro;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.MouseUpdateEvent;
import net.shoreline.client.impl.event.PerspectiveEvent;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.impl.event.camera.CameraPositionEvent;
import net.shoreline.client.impl.event.camera.CameraRotationEvent;
import net.shoreline.client.impl.event.camera.EntityCameraPositionEvent;
import net.shoreline.client.impl.event.entity.EntityRotationVectorEvent;
import net.shoreline.client.impl.event.keyboard.KeyboardInputEvent;
import net.shoreline.client.impl.event.network.DisconnectEvent;
import net.shoreline.client.impl.event.render.BobViewEvent;
import net.shoreline.client.impl.manager.player.rotation.Rotation;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.player.RayCastUtil;
import net.shoreline.client.util.player.RotationUtil;

public class FreecamModule extends ToggleModule {
   Config<Float> speedConfig = new NumberConfig("Speed", "The move speed of the camera", 0.1F, 4.0F, 10.0F);
   Config<Macro> controlConfig = new MacroConfig("ControlKey", "", new Macro(this.getId() + "-control", 342, () -> {
   }));
   Config<Boolean> toggleControlConfig = new BooleanConfig("ToggleControl", "Allows toggling control key instead of holding", false);
   Config<FreecamModule.Interact> interactConfig;
   Config<Boolean> rotateConfig;
   public class_243 position;
   public class_243 lastPosition;
   public float yaw;
   public float pitch;
   private boolean control;

   public FreecamModule() {
      super("Freecam", "Allows you to control the camera separately from the player", ModuleCategory.RENDER);
      this.interactConfig = new EnumConfig("Interact", "The interaction type of the camera", FreecamModule.Interact.CAMERA, FreecamModule.Interact.values());
      this.rotateConfig = new BooleanConfig("Rotate", "Rotate to the point of interaction", false);
      this.control = false;
   }

   protected void onEnable() {
      if (mc.field_1724 != null) {
         this.control = false;
         this.position = mc.field_1773.method_19418().method_19326();
         this.lastPosition = this.position;
         this.yaw = mc.field_1724.method_36454();
         this.pitch = mc.field_1724.method_36455();
         mc.field_1724.field_3913 = new FreecamModule.FreecamKeyboardInput(mc.field_1690);
      }
   }

   protected void onDisable() {
      if (mc.field_1724 != null) {
         mc.field_1724.field_3913 = new class_743(mc.field_1690);
      }
   }

   @EventListener
   public void onKey(KeyboardInputEvent event) {
      if (event.getAction() != 2 && event.getKeycode() == ((Macro)this.controlConfig.getValue()).getKeycode()) {
         if (!(Boolean)this.toggleControlConfig.getValue()) {
            this.control = event.getAction() == 1;
         } else if (event.getAction() == 1) {
            this.control = !this.control;
         }
      }

   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      this.disable();
   }

   @EventListener
   public void onCameraPosition(CameraPositionEvent event) {
      event.setPosition(this.control ? this.position : this.lastPosition.method_35590(this.position, (double)event.getTickDelta()));
   }

   @EventListener
   public void onCameraRotation(CameraRotationEvent event) {
      event.setRotation(new class_241(this.yaw, this.pitch));
   }

   @EventListener
   public void onMouseUpdate(MouseUpdateEvent event) {
      if (!this.control) {
         event.cancel();
         this.changeLookDirection(event.getCursorDeltaX(), event.getCursorDeltaY());
      }

   }

   @EventListener
   public void onEntityCameraPosition(EntityCameraPositionEvent event) {
      if (event.getEntity() == mc.field_1724) {
         if (!this.control && this.interactConfig.getValue() == FreecamModule.Interact.CAMERA) {
            event.setPosition(this.position);
         }

      }
   }

   @EventListener
   public void onEntityRotation(EntityRotationVectorEvent event) {
      if (event.getEntity() == mc.field_1724) {
         if (!this.control && this.interactConfig.getValue() == FreecamModule.Interact.CAMERA) {
            event.setPosition(RotationUtil.getRotationVector(this.pitch, this.yaw));
         }

      }
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         if (!this.control && (Boolean)this.rotateConfig.getValue()) {
            float[] currentAngles = new float[]{this.yaw, this.pitch};
            class_243 eyePos = this.position;
            class_239 result = RayCastUtil.rayCast((double)mc.field_1761.method_2904(), eyePos, currentAngles);
            if (result.method_17783() == class_240.field_1332) {
               float[] newAngles = RotationUtil.getRotationsTo(mc.field_1724.method_33571(), result.method_17784());
               Managers.ROTATION.setRotation(new Rotation(1, newAngles[0], newAngles[1]));
            }
         }

      }
   }

   @EventListener
   public void onPerspective(PerspectiveEvent event) {
      event.cancel();
   }

   @EventListener
   public void onBob(BobViewEvent event) {
      if (this.control) {
         event.cancel();
      }

   }

   private float getMovementMultiplier(boolean positive, boolean negative) {
      if (positive == negative) {
         return 0.0F;
      } else {
         return positive ? 1.0F : -1.0F;
      }
   }

   private class_241 handleVanillaMotion(float speed, float forward, float strafe) {
      if (forward == 0.0F && strafe == 0.0F) {
         return class_241.field_1340;
      } else {
         if (forward != 0.0F && strafe != 0.0F) {
            forward *= (float)Math.sin(0.7853981633974483D);
            strafe *= (float)Math.cos(0.7853981633974483D);
         }

         return new class_241((float)((double)(forward * speed) * -Math.sin(Math.toRadians((double)this.yaw)) + (double)(strafe * speed) * Math.cos(Math.toRadians((double)this.yaw))), (float)((double)(forward * speed) * Math.cos(Math.toRadians((double)this.yaw)) - (double)(strafe * speed) * -Math.sin(Math.toRadians((double)this.yaw))));
      }
   }

   private void changeLookDirection(double cursorDeltaX, double cursorDeltaY) {
      float f = (float)cursorDeltaY * 0.15F;
      float g = (float)cursorDeltaX * 0.15F;
      this.pitch += f;
      this.yaw += g;
      this.pitch = class_3532.method_15363(this.pitch, -90.0F, 90.0F);
   }

   public class_243 getCameraPosition() {
      return this.position;
   }

   public float[] getCameraRotations() {
      return new float[]{this.yaw, this.pitch};
   }

   public static enum Interact {
      PLAYER,
      CAMERA;

      // $FF: synthetic method
      private static FreecamModule.Interact[] $values() {
         return new FreecamModule.Interact[]{PLAYER, CAMERA};
      }
   }

   public class FreecamKeyboardInput extends class_743 {
      private final class_315 options;

      public FreecamKeyboardInput(class_315 options) {
         super(options);
         this.options = options;
      }

      public void method_3129(boolean slowDown, float slowDownFactor) {
         if (FreecamModule.this.control) {
            super.method_3129(slowDown, slowDownFactor);
         } else {
            this.unset();
            float speed = (Float)FreecamModule.this.speedConfig.getValue() / 10.0F;
            float fakeMovementForward = FreecamModule.this.getMovementMultiplier(this.options.field_1894.method_1434(), this.options.field_1881.method_1434());
            float fakeMovementSideways = FreecamModule.this.getMovementMultiplier(this.options.field_1913.method_1434(), this.options.field_1849.method_1434());
            class_241 dir = FreecamModule.this.handleVanillaMotion(speed, fakeMovementForward, fakeMovementSideways);
            float y = 0.0F;
            if (this.options.field_1903.method_1434()) {
               y += speed;
            } else if (this.options.field_1832.method_1434()) {
               y -= speed;
            }

            FreecamModule.this.lastPosition = FreecamModule.this.position;
            FreecamModule.this.position = FreecamModule.this.position.method_1031((double)dir.field_1343, (double)y, (double)dir.field_1342);
         }

      }

      private void unset() {
         this.field_3910 = false;
         this.field_3909 = false;
         this.field_3908 = false;
         this.field_3906 = false;
         this.field_3905 = 0.0F;
         this.field_3907 = 0.0F;
         this.field_3904 = false;
         this.field_3903 = false;
      }
   }
}
