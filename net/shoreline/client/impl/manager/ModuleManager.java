package net.shoreline.client.impl.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.shoreline.client.Shoreline;
import net.shoreline.client.ShorelineMod;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.impl.module.client.BaritoneModule;
import net.shoreline.client.impl.module.client.CapesModule;
import net.shoreline.client.impl.module.client.ClickGuiModule;
import net.shoreline.client.impl.module.client.ColorsModule;
import net.shoreline.client.impl.module.client.HUDModule;
import net.shoreline.client.impl.module.client.RotationsModule;
import net.shoreline.client.impl.module.client.ServerModule;
import net.shoreline.client.impl.module.combat.AuraModule;
import net.shoreline.client.impl.module.combat.AutoArmorModule;
import net.shoreline.client.impl.module.combat.AutoBowReleaseModule;
import net.shoreline.client.impl.module.combat.AutoCrystalModule;
import net.shoreline.client.impl.module.combat.AutoLogModule;
import net.shoreline.client.impl.module.combat.AutoTotemModule;
import net.shoreline.client.impl.module.combat.AutoTrapModule;
import net.shoreline.client.impl.module.combat.AutoWebModule;
import net.shoreline.client.impl.module.combat.AutoXPModule;
import net.shoreline.client.impl.module.combat.BlockLagModule;
import net.shoreline.client.impl.module.combat.BowAimModule;
import net.shoreline.client.impl.module.combat.ClickCrystalModule;
import net.shoreline.client.impl.module.combat.CriticalsModule;
import net.shoreline.client.impl.module.combat.HoleFillModule;
import net.shoreline.client.impl.module.combat.NoHitDelayModule;
import net.shoreline.client.impl.module.combat.ReplenishModule;
import net.shoreline.client.impl.module.combat.SelfBowModule;
import net.shoreline.client.impl.module.combat.SelfTrapModule;
import net.shoreline.client.impl.module.combat.SurroundModule;
import net.shoreline.client.impl.module.combat.TriggerModule;
import net.shoreline.client.impl.module.exploit.AntiHungerModule;
import net.shoreline.client.impl.module.exploit.ChorusControlModule;
import net.shoreline.client.impl.module.exploit.ClientSpoofModule;
import net.shoreline.client.impl.module.exploit.CrasherModule;
import net.shoreline.client.impl.module.exploit.DisablerModule;
import net.shoreline.client.impl.module.exploit.ExtendedFireworkModule;
import net.shoreline.client.impl.module.exploit.FakeLatencyModule;
import net.shoreline.client.impl.module.exploit.FastLatencyModule;
import net.shoreline.client.impl.module.exploit.FastProjectileModule;
import net.shoreline.client.impl.module.exploit.PacketCancelerModule;
import net.shoreline.client.impl.module.exploit.PacketFlyModule;
import net.shoreline.client.impl.module.exploit.PhaseModule;
import net.shoreline.client.impl.module.exploit.PortalGodModeModule;
import net.shoreline.client.impl.module.exploit.ReachModule;
import net.shoreline.client.impl.module.misc.AntiAimModule;
import net.shoreline.client.impl.module.misc.AntiSpamModule;
import net.shoreline.client.impl.module.misc.AutoAcceptModule;
import net.shoreline.client.impl.module.misc.AutoEatModule;
import net.shoreline.client.impl.module.misc.AutoFishModule;
import net.shoreline.client.impl.module.misc.AutoReconnectModule;
import net.shoreline.client.impl.module.misc.AutoRespawnModule;
import net.shoreline.client.impl.module.misc.BeaconSelectorModule;
import net.shoreline.client.impl.module.misc.BetterChatModule;
import net.shoreline.client.impl.module.misc.ChatNotifierModule;
import net.shoreline.client.impl.module.misc.ChestSwapModule;
import net.shoreline.client.impl.module.misc.FakePlayerModule;
import net.shoreline.client.impl.module.misc.InvCleanerModule;
import net.shoreline.client.impl.module.misc.MiddleClickModule;
import net.shoreline.client.impl.module.misc.NoPacketKickModule;
import net.shoreline.client.impl.module.misc.NoSoundLagModule;
import net.shoreline.client.impl.module.misc.PacketLoggerModule;
import net.shoreline.client.impl.module.misc.TimerModule;
import net.shoreline.client.impl.module.misc.TrueDurabilityModule;
import net.shoreline.client.impl.module.misc.UnfocusedFPSModule;
import net.shoreline.client.impl.module.misc.XCarryModule;
import net.shoreline.client.impl.module.movement.AntiLevitationModule;
import net.shoreline.client.impl.module.movement.AutoWalkModule;
import net.shoreline.client.impl.module.movement.ElytraFlyModule;
import net.shoreline.client.impl.module.movement.EntityControlModule;
import net.shoreline.client.impl.module.movement.EntitySpeedModule;
import net.shoreline.client.impl.module.movement.FakeLagModule;
import net.shoreline.client.impl.module.movement.FastFallModule;
import net.shoreline.client.impl.module.movement.FlightModule;
import net.shoreline.client.impl.module.movement.IceSpeedModule;
import net.shoreline.client.impl.module.movement.JesusModule;
import net.shoreline.client.impl.module.movement.LongJumpModule;
import net.shoreline.client.impl.module.movement.NoFallModule;
import net.shoreline.client.impl.module.movement.NoJumpDelayModule;
import net.shoreline.client.impl.module.movement.NoSlowModule;
import net.shoreline.client.impl.module.movement.ParkourModule;
import net.shoreline.client.impl.module.movement.SpeedModule;
import net.shoreline.client.impl.module.movement.SprintModule;
import net.shoreline.client.impl.module.movement.StepModule;
import net.shoreline.client.impl.module.movement.TickShiftModule;
import net.shoreline.client.impl.module.movement.TridentFlyModule;
import net.shoreline.client.impl.module.movement.VelocityModule;
import net.shoreline.client.impl.module.movement.YawModule;
import net.shoreline.client.impl.module.render.BlockHighlightModule;
import net.shoreline.client.impl.module.render.BreakHighlightModule;
import net.shoreline.client.impl.module.render.ChamsModule;
import net.shoreline.client.impl.module.render.ESPModule;
import net.shoreline.client.impl.module.render.ExtraTabModule;
import net.shoreline.client.impl.module.render.FreecamModule;
import net.shoreline.client.impl.module.render.FullbrightModule;
import net.shoreline.client.impl.module.render.HoleESPModule;
import net.shoreline.client.impl.module.render.NameProtectModule;
import net.shoreline.client.impl.module.render.NametagsModule;
import net.shoreline.client.impl.module.render.NoRenderModule;
import net.shoreline.client.impl.module.render.NoRotateModule;
import net.shoreline.client.impl.module.render.NoWeatherModule;
import net.shoreline.client.impl.module.render.ParticlesModule;
import net.shoreline.client.impl.module.render.PhaseESPModule;
import net.shoreline.client.impl.module.render.SkeletonModule;
import net.shoreline.client.impl.module.render.SkyboxModule;
import net.shoreline.client.impl.module.render.TooltipsModule;
import net.shoreline.client.impl.module.render.TracersModule;
import net.shoreline.client.impl.module.render.TrueSightModule;
import net.shoreline.client.impl.module.render.ViewClipModule;
import net.shoreline.client.impl.module.render.ViewModelModule;
import net.shoreline.client.impl.module.world.AntiInteractModule;
import net.shoreline.client.impl.module.world.AutoMineModule;
import net.shoreline.client.impl.module.world.AutoToolModule;
import net.shoreline.client.impl.module.world.AvoidModule;
import net.shoreline.client.impl.module.world.BlockInteractModule;
import net.shoreline.client.impl.module.world.FastDropModule;
import net.shoreline.client.impl.module.world.FastPlaceModule;
import net.shoreline.client.impl.module.world.MultitaskModule;
import net.shoreline.client.impl.module.world.NoGlitchBlocksModule;
import net.shoreline.client.impl.module.world.ScaffoldModule;
import net.shoreline.client.impl.module.world.SpeedmineModule;

public class ModuleManager {
   private final Map<String, Module> modules = Collections.synchronizedMap(new LinkedHashMap());

   public ModuleManager() {
      this.register(new ServerModule(), new CapesModule(), new ClickGuiModule(), new ColorsModule(), new HUDModule(), new RotationsModule(), new AuraModule(), new AutoArmorModule(), new AutoBowReleaseModule(), new AutoCrystalModule(), new AutoLogModule(), new AutoTotemModule(), new AutoTrapModule(), new AutoWebModule(), new AutoXPModule(), new BlockLagModule(), new BowAimModule(), new ClickCrystalModule(), new CriticalsModule(), new HoleFillModule(), new NoHitDelayModule(), new ReplenishModule(), new SelfBowModule(), new SelfTrapModule(), new SurroundModule(), new TriggerModule(), new AntiHungerModule(), new ChorusControlModule(), new ClientSpoofModule(), new CrasherModule(), new DisablerModule(), new ExtendedFireworkModule(), new FakeLatencyModule(), new FastLatencyModule(), new FastProjectileModule(), new PacketCancelerModule(), new PacketFlyModule(), new PhaseModule(), new PortalGodModeModule(), new ReachModule(), new AntiAimModule(), new AntiSpamModule(), new AutoAcceptModule(), new AutoEatModule(), new AutoFishModule(), new AutoReconnectModule(), new AutoRespawnModule(), new BeaconSelectorModule(), new BetterChatModule(), new ChatNotifierModule(), new ChestSwapModule(), new FakePlayerModule(), new InvCleanerModule(), new MiddleClickModule(), new NoPacketKickModule(), new NoSoundLagModule(), new PacketLoggerModule(), new TimerModule(), new TrueDurabilityModule(), new UnfocusedFPSModule(), new XCarryModule(), new AntiLevitationModule(), new AutoWalkModule(), new ElytraFlyModule(), new EntityControlModule(), new EntitySpeedModule(), new FakeLagModule(), new FastFallModule(), new FlightModule(), new IceSpeedModule(), new JesusModule(), new LongJumpModule(), new NoFallModule(), new NoJumpDelayModule(), new NoSlowModule(), new ParkourModule(), new SpeedModule(), new SprintModule(), new StepModule(), new TickShiftModule(), new TridentFlyModule(), new VelocityModule(), new YawModule(), new BlockHighlightModule(), new BreakHighlightModule(), new ChamsModule(), new ESPModule(), new ExtraTabModule(), new FreecamModule(), new FullbrightModule(), new HoleESPModule(), new NameProtectModule(), new NametagsModule(), new NoRenderModule(), new NoRotateModule(), new NoWeatherModule(), new ParticlesModule(), new PhaseESPModule(), new SkeletonModule(), new SkyboxModule(), new TooltipsModule(), new TracersModule(), new TrueSightModule(), new ViewClipModule(), new ViewModelModule(), new AntiInteractModule(), new AutoMineModule(), new AutoToolModule(), new AvoidModule(), new BlockInteractModule(), new FastDropModule(), new FastPlaceModule(), new MultitaskModule(), new NoGlitchBlocksModule(), new ScaffoldModule(), new SpeedmineModule());
      if (ShorelineMod.isBaritonePresent()) {
         this.register((Module)(new BaritoneModule()));
      }

      Shoreline.info("Registered {} modules!", this.modules.size());
   }

   public void postInit() {
   }

   private void register(Module... modules) {
      Module[] var2 = modules;
      int var3 = modules.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Module module = var2[var4];
         this.register(module);
      }

   }

   private void register(Module module) {
      this.modules.put(module.getId(), module);
   }

   public Module getModule(String id) {
      return (Module)this.modules.get(id);
   }

   public List<Module> getModules() {
      return new ArrayList(this.modules.values());
   }
}
