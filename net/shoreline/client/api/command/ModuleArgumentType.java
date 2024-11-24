package net.shoreline.client.api.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_2172;
import net.shoreline.client.api.config.ConfigContainer;
import net.shoreline.client.api.module.Module;
import net.shoreline.client.init.Managers;

public class ModuleArgumentType implements ArgumentType<Module> {
   public static ModuleArgumentType module() {
      return new ModuleArgumentType();
   }

   public static Module getModule(CommandContext<?> context, String name) {
      return (Module)context.getArgument(name, Module.class);
   }

   public Module parse(StringReader reader) throws CommandSyntaxException {
      String string = reader.readString();
      String id = String.format("%s-module", string.toLowerCase());
      Module module = Managers.MODULE.getModule(id);
      if (module == null) {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, (Object)null);
      } else {
         return module;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return class_2172.method_9264(Managers.MODULE.getModules().stream().map(ConfigContainer::getName), builder);
   }

   public Collection<String> getExamples() {
      return Managers.MODULE.getModules().stream().map(ConfigContainer::getName).limit(10L).toList();
   }
}
