package net.shoreline.client.init;

import net.shoreline.client.impl.manager.ModuleManager;
import net.shoreline.client.impl.manager.anticheat.AntiCheatManager;
import net.shoreline.client.impl.manager.client.AccountManager;
import net.shoreline.client.impl.manager.client.CapeManager;
import net.shoreline.client.impl.manager.client.CommandManager;
import net.shoreline.client.impl.manager.client.MacroManager;
import net.shoreline.client.impl.manager.client.SocialManager;
import net.shoreline.client.impl.manager.combat.TotemManager;
import net.shoreline.client.impl.manager.combat.hole.HoleManager;
import net.shoreline.client.impl.manager.network.NetworkManager;
import net.shoreline.client.impl.manager.player.InventoryManager;
import net.shoreline.client.impl.manager.player.MovementManager;
import net.shoreline.client.impl.manager.player.PositionManager;
import net.shoreline.client.impl.manager.player.interaction.InteractionManager;
import net.shoreline.client.impl.manager.player.rotation.RotationManager;
import net.shoreline.client.impl.manager.world.WaypointManager;
import net.shoreline.client.impl.manager.world.sound.SoundManager;
import net.shoreline.client.impl.manager.world.tick.TickManager;

public class Managers {
   public static NetworkManager NETWORK;
   public static ModuleManager MODULE;
   public static MacroManager MACRO;
   public static CommandManager COMMAND;
   public static SocialManager SOCIAL;
   public static WaypointManager WAYPOINT;
   public static AccountManager ACCOUNT;
   public static TickManager TICK;
   public static InventoryManager INVENTORY;
   public static PositionManager POSITION;
   public static RotationManager ROTATION;
   public static AntiCheatManager ANTICHEAT;
   public static MovementManager MOVEMENT;
   public static HoleManager HOLE;
   public static TotemManager TOTEM;
   public static InteractionManager INTERACT;
   public static SoundManager SOUND;
   public static CapeManager CAPES;
   private static boolean initialized;

   public static void init() {
      if (!isInitialized()) {
         NETWORK = new NetworkManager();
         MODULE = new ModuleManager();
         MACRO = new MacroManager();
         SOCIAL = new SocialManager();
         WAYPOINT = new WaypointManager();
         ACCOUNT = new AccountManager();
         TICK = new TickManager();
         INVENTORY = new InventoryManager();
         POSITION = new PositionManager();
         ROTATION = new RotationManager();
         ANTICHEAT = new AntiCheatManager();
         MOVEMENT = new MovementManager();
         HOLE = new HoleManager();
         TOTEM = new TotemManager();
         INTERACT = new InteractionManager();
         COMMAND = new CommandManager();
         SOUND = new SoundManager();
         initialized = true;
      }

   }

   public static void postInit() {
      if (isInitialized()) {
         MODULE.postInit();
         MACRO.postInit();
         ACCOUNT.postInit();
         CAPES = new CapeManager();
      }

   }

   public static boolean isInitialized() {
      return initialized;
   }
}
