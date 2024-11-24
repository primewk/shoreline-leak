package net.shoreline.client.impl.manager.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2172;
import net.minecraft.class_408;
import net.minecraft.class_634;
import net.minecraft.class_637;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.command.Command;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.impl.command.BindCommand;
import net.shoreline.client.impl.command.ConfigCommand;
import net.shoreline.client.impl.command.DisableAllCommand;
import net.shoreline.client.impl.command.DrawnCommand;
import net.shoreline.client.impl.command.FriendCommand;
import net.shoreline.client.impl.command.HClipCommand;
import net.shoreline.client.impl.command.HelpCommand;
import net.shoreline.client.impl.command.HideAllCommand;
import net.shoreline.client.impl.command.ModuleCommand;
import net.shoreline.client.impl.command.ModulesCommand;
import net.shoreline.client.impl.command.NbtCommand;
import net.shoreline.client.impl.command.OpenFolderCommand;
import net.shoreline.client.impl.command.PrefixCommand;
import net.shoreline.client.impl.command.ReloadSoundCommand;
import net.shoreline.client.impl.command.ResetCommand;
import net.shoreline.client.impl.command.ToggleCommand;
import net.shoreline.client.impl.command.VClipCommand;
import net.shoreline.client.impl.command.VanishCommand;
import net.shoreline.client.impl.event.gui.chat.ChatMessageEvent;
import net.shoreline.client.impl.event.keyboard.KeyboardInputEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.Globals;

public class CommandManager implements Globals {
   private final List<Command> commands = new ArrayList();
   private String prefix = ".";
   private int prefixKey = 46;
   private final CommandDispatcher<class_2172> dispatcher = new CommandDispatcher();
   private final class_2172 source;

   public CommandManager() {
      this.source = new class_637((class_634)null, mc);
      Shoreline.EVENT_HANDLER.subscribe(this);
      this.register(new BindCommand(), new ConfigCommand(), new DisableAllCommand(), new DrawnCommand(), new FriendCommand(), new HClipCommand(), new HelpCommand(), new HideAllCommand(), new ModulesCommand(), new NbtCommand(), new OpenFolderCommand(), new PrefixCommand(), new ResetCommand(), new ReloadSoundCommand(), new ToggleCommand(), new VanishCommand(), new VClipCommand());
      Iterator var1 = Managers.MODULE.getModules().iterator();

      while(var1.hasNext()) {
         Module module = (Module)var1.next();
         this.register((Command)(new ModuleCommand(module)));
      }

      Shoreline.info("Registered {} commands!", this.commands.size());
      var1 = this.commands.iterator();

      while(var1.hasNext()) {
         Command command = (Command)var1.next();
         command.buildCommand(command.getCommandBuilder());
         this.dispatcher.register(command.getCommandBuilder());
      }

   }

   @EventListener
   public void onChatMessage(ChatMessageEvent.Client event) {
      String text = event.getMessage().trim();
      if (text.startsWith(this.prefix)) {
         String literal = text.substring(1);
         event.cancel();
         mc.field_1705.method_1743().method_1803(text);

         try {
            this.dispatcher.execute(this.dispatcher.parse(literal, this.source));
         } catch (Exception var5) {
         }
      }

   }

   @EventListener
   public void onKeyboardInput(KeyboardInputEvent event) {
      if (event.getAction() == 1 && event.getKeycode() == this.prefixKey && mc.field_1755 == null) {
         event.cancel();
         mc.method_1507(new class_408(""));
      }

   }

   private LiteralArgumentBuilder<Object> redirectBuilder(String alias, LiteralCommandNode<?> destination) {
      LiteralArgumentBuilder<Object> literalArgumentBuilder = (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)LiteralArgumentBuilder.literal(alias.toLowerCase()).requires(destination.getRequirement())).forward(destination.getRedirect(), destination.getRedirectModifier(), destination.isFork())).executes(destination.getCommand());
      Iterator var4 = destination.getChildren().iterator();

      while(var4.hasNext()) {
         CommandNode<?> child = (CommandNode)var4.next();
         literalArgumentBuilder.then(child);
      }

      return literalArgumentBuilder;
   }

   private void register(Command... commands) {
      Command[] var2 = commands;
      int var3 = commands.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Command command = var2[var4];
         this.register(command);
      }

   }

   private void register(Command command) {
      this.commands.add(command);
   }

   public List<Command> getCommands() {
      return this.commands;
   }

   public Command getCommand(String name) {
      Iterator var2 = this.commands.iterator();

      Command command;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         command = (Command)var2.next();
      } while(!command.getName().equalsIgnoreCase(name));

      return command;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setPrefix(String prefix, int prefixKey) {
      this.prefix = prefix;
      this.prefixKey = prefixKey;
   }

   public CommandDispatcher<class_2172> getDispatcher() {
      return this.dispatcher;
   }

   public class_2172 getSource() {
      return this.source;
   }
}
