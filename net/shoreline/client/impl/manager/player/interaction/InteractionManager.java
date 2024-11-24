package net.shoreline.client.impl.manager.player.interaction;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_3965;
import net.shoreline.client.Shoreline;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.Globals;
import net.shoreline.client.util.player.RotationUtil;
import net.shoreline.client.util.world.SneakBlocks;

public final class InteractionManager implements Globals {
   public InteractionManager() {
      Shoreline.EVENT_HANDLER.subscribe(this);
   }

   public boolean placeBlock(class_2338 pos, int slot, boolean grim, boolean strictDirection, boolean clientSwing, RotationCallback rotationCallback) {
      class_2350 direction = this.getInteractDirection(pos, grim, strictDirection);
      if (Modules.BLOCK_INTERACT.isEnabled() && direction == null && !strictDirection) {
         direction = class_2350.field_11036;
      }

      if (direction == null) {
         return false;
      } else {
         class_2338 neighbor = pos.method_10093(direction.method_10153());
         return this.placeBlock(neighbor, direction, slot, clientSwing, rotationCallback);
      }
   }

   public boolean placeBlock(class_2338 pos, class_2350 direction, int slot, boolean clientSwing, RotationCallback rotationCallback) {
      class_243 hitVec = pos.method_46558().method_1019((new class_243(direction.method_23955())).method_1021(0.5D));
      return this.placeBlock(new class_3965(hitVec, direction, pos, false), slot, clientSwing, rotationCallback);
   }

   public boolean placeBlock(class_3965 hitResult, int slot, boolean clientSwing, RotationCallback rotationCallback) {
      boolean isSpoofing = slot != Managers.INVENTORY.getServerSlot();
      if (isSpoofing) {
         Managers.INVENTORY.setSlot(slot);
      }

      boolean isRotating = rotationCallback != null;
      if (isRotating) {
         float[] angles = RotationUtil.getRotationsTo(mc.field_1724.method_33571(), hitResult.method_17784());
         rotationCallback.handleRotation(true, angles);
      }

      boolean result = this.placeBlockImmediately(hitResult, clientSwing);
      if (isRotating) {
         float[] angles = RotationUtil.getRotationsTo(mc.field_1724.method_33571(), hitResult.method_17784());
         rotationCallback.handleRotation(false, angles);
      }

      if (isSpoofing) {
         Managers.INVENTORY.syncToClient();
      }

      return result;
   }

   public boolean placeBlockImmediately(class_3965 result, boolean clientSwing) {
      class_2680 state = mc.field_1687.method_8320(result.method_17777());
      boolean shouldSneak = SneakBlocks.isSneakBlock(state) && !mc.field_1724.method_5715();
      if (shouldSneak) {
      }

      class_1269 actionResult = this.placeBlockInternally(result);
      if (actionResult.method_23665() && actionResult.method_23666()) {
         if (clientSwing) {
            mc.field_1724.method_6104(class_1268.field_5808);
         } else {
            Managers.NETWORK.sendPacket(new class_2879(class_1268.field_5808));
         }
      }

      if (shouldSneak) {
      }

      return actionResult.method_23665();
   }

   private class_1269 placeBlockInternally(class_3965 hitResult) {
      return mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, hitResult);
   }

   public class_1269 placeBlockPacket(class_3965 hitResult) {
      Managers.NETWORK.sendSequencedPacket((id) -> {
         return new class_2885(class_1268.field_5808, hitResult, id);
      });
      return class_1269.field_5812;
   }

   public class_2350 getInteractDirection(class_2338 blockPos, boolean grim, boolean strictDirection) {
      Set<class_2350> validDirections = grim ? this.getPlaceDirectionsGrim(mc.field_1724.method_33571(), blockPos) : this.getPlaceDirectionsNCP(mc.field_1724.method_33571(), blockPos.method_46558());
      class_2350 interactDirection = null;
      class_2350[] var6 = class_2350.values();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         class_2350 direction = var6[var8];
         class_2680 state = mc.field_1687.method_8320(blockPos.method_10093(direction));
         if (!state.method_26215() && state.method_26227().method_15769() && (!strictDirection || validDirections.contains(direction.method_10153()))) {
            interactDirection = direction;
            break;
         }
      }

      return interactDirection == null ? null : interactDirection.method_10153();
   }

   public class_2350 getPlaceDirectionNCP(class_2338 blockPos, boolean visible) {
      class_243 eyePos = new class_243(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)mc.field_1724.method_5751(), mc.field_1724.method_23321());
      if ((double)blockPos.method_10263() == eyePos.method_10216() && (double)blockPos.method_10264() == eyePos.method_10214() && (double)blockPos.method_10260() == eyePos.method_10215()) {
         return class_2350.field_11033;
      } else {
         Set<class_2350> ncpDirections = this.getPlaceDirectionsNCP(eyePos, blockPos.method_46558());
         Iterator var5 = ncpDirections.iterator();

         class_2350 dir;
         do {
            if (!var5.hasNext()) {
               return class_2350.field_11036;
            }

            dir = (class_2350)var5.next();
         } while(visible && !mc.field_1687.method_22347(blockPos.method_10093(dir)));

         return dir;
      }
   }

   public Set<class_2350> getPlaceDirectionsNCP(class_243 eyePos, class_243 blockPos) {
      return this.getPlaceDirectionsNCP(eyePos.field_1352, eyePos.field_1351, eyePos.field_1350, blockPos.field_1352, blockPos.field_1351, blockPos.field_1350);
   }

   public class_2350 getPlaceDirectionGrim(class_2338 blockPos) {
      Set<class_2350> directions = this.getPlaceDirectionsGrim(mc.field_1724.method_19538(), blockPos);
      return (class_2350)directions.stream().findAny().orElse(class_2350.field_11036);
   }

   public Set<class_2350> getPlaceDirectionsGrim(class_243 eyePos, class_2338 blockPos) {
      return this.getPlaceDirectionsGrim(eyePos.field_1352, eyePos.field_1351, eyePos.field_1350, blockPos);
   }

   public Set<class_2350> getPlaceDirectionsGrim(double x, double y, double z, class_2338 pos) {
      Set<class_2350> dirs = new HashSet(6);
      class_238 combined = this.getCombinedBox(pos);
      class_238 eyePositions = (new class_238(x, y + 0.4D, z, x, y + 1.62D, z)).method_1014(2.0E-4D);
      if (eyePositions.field_1321 <= combined.field_1321) {
         dirs.add(class_2350.field_11043);
      }

      if (eyePositions.field_1324 >= combined.field_1324) {
         dirs.add(class_2350.field_11035);
      }

      if (eyePositions.field_1320 >= combined.field_1320) {
         dirs.add(class_2350.field_11034);
      }

      if (eyePositions.field_1323 <= combined.field_1323) {
         dirs.add(class_2350.field_11039);
      }

      if (eyePositions.field_1325 >= combined.field_1325) {
         dirs.add(class_2350.field_11036);
      }

      if (eyePositions.field_1322 <= combined.field_1322) {
         dirs.add(class_2350.field_11033);
      }

      return dirs;
   }

   private class_238 getCombinedBox(class_2338 pos) {
      class_265 shape = mc.field_1687.method_8320(pos).method_26220(mc.field_1687, pos).method_1096((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260());
      class_238 combined = new class_238(pos);

      double maxZ;
      double minX;
      double minY;
      double minZ;
      double maxX;
      double maxY;
      for(Iterator var4 = shape.method_1090().iterator(); var4.hasNext(); combined = new class_238(minX, minY, minZ, maxX, maxY, maxZ)) {
         class_238 box = (class_238)var4.next();
         minX = Math.max(box.field_1323, combined.field_1323);
         minY = Math.max(box.field_1322, combined.field_1322);
         minZ = Math.max(box.field_1321, combined.field_1321);
         maxX = Math.min(box.field_1320, combined.field_1320);
         maxY = Math.min(box.field_1325, combined.field_1325);
         maxZ = Math.min(box.field_1324, combined.field_1324);
      }

      return combined;
   }

   private boolean isIntersected(class_238 bb, class_238 other) {
      return other.field_1320 - 9.999999974752427E-7D > bb.field_1323 && other.field_1323 + 9.999999974752427E-7D < bb.field_1320 && other.field_1325 - 9.999999974752427E-7D > bb.field_1322 && other.field_1322 + 9.999999974752427E-7D < bb.field_1325 && other.field_1324 - 9.999999974752427E-7D > bb.field_1321 && other.field_1321 + 9.999999974752427E-7D < bb.field_1324;
   }

   public Set<class_2350> getPlaceDirectionsNCP(double x, double y, double z, double dx, double dy, double dz) {
      double xdiff = x - dx;
      double ydiff = y - dy;
      double zdiff = z - dz;
      Set<class_2350> dirs = new HashSet(6);
      if (ydiff > 0.5D) {
         dirs.add(class_2350.field_11036);
      } else if (ydiff < -0.5D) {
         dirs.add(class_2350.field_11033);
      } else {
         dirs.add(class_2350.field_11036);
         dirs.add(class_2350.field_11033);
      }

      if (xdiff > 0.5D) {
         dirs.add(class_2350.field_11034);
      } else if (xdiff < -0.5D) {
         dirs.add(class_2350.field_11039);
      } else {
         dirs.add(class_2350.field_11034);
         dirs.add(class_2350.field_11039);
      }

      if (zdiff > 0.5D) {
         dirs.add(class_2350.field_11035);
      } else if (zdiff < -0.5D) {
         dirs.add(class_2350.field_11043);
      } else {
         dirs.add(class_2350.field_11035);
         dirs.add(class_2350.field_11043);
      }

      return dirs;
   }

   public boolean isInEyeRange(class_2338 pos) {
      return (double)pos.method_10264() > mc.field_1724.method_23318() + (double)mc.field_1724.method_5751();
   }
}
