package net.shoreline.client.api.module;

import net.shoreline.client.impl.manager.player.rotation.Rotation;
import net.shoreline.client.init.Managers;

public class RotationModule extends ToggleModule {
   private final int rotationPriority;

   public RotationModule(String name, String desc, ModuleCategory category) {
      super(name, desc, category);
      this.rotationPriority = 100;
   }

   public RotationModule(String name, String desc, ModuleCategory category, int rotationPriority) {
      super(name, desc, category);
      this.rotationPriority = rotationPriority;
   }

   protected void setRotation(float yaw, float pitch) {
      Managers.ROTATION.setRotation(new Rotation(this.getRotationPriority(), yaw, pitch));
   }

   protected void setRotationSilent(float yaw, float pitch) {
      Managers.ROTATION.setRotationSilent(yaw, pitch, true);
   }

   protected void setRotationClient(float yaw, float pitch) {
      Managers.ROTATION.setRotationClient(yaw, pitch);
   }

   protected boolean isRotationBlocked() {
      return Managers.ROTATION.isRotationBlocked(this.getRotationPriority());
   }

   protected int getRotationPriority() {
      return this.rotationPriority;
   }
}
