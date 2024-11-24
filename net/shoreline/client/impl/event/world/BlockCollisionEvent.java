package net.shoreline.client.impl.event.world;

import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

@Cancelable
public class BlockCollisionEvent extends Event {
   private final class_2338 pos;
   private final class_2680 state;
   private class_265 voxelShape;

   public BlockCollisionEvent(class_265 voxelShape, class_2338 pos, class_2680 state) {
      this.pos = pos;
      this.state = state;
      this.voxelShape = voxelShape;
   }

   public class_2338 getPos() {
      return this.pos;
   }

   public class_2680 getState() {
      return this.state;
   }

   public class_2248 getBlock() {
      return this.state.method_26204();
   }

   public class_265 getVoxelShape() {
      return this.voxelShape;
   }

   public void setVoxelShape(class_265 voxelShape) {
      this.voxelShape = voxelShape;
   }
}
