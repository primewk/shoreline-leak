package net.shoreline.client.api.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_640;
import net.shoreline.client.util.Globals;

public class PlayerArgumentType implements ArgumentType<String>, Globals {
   public static PlayerArgumentType player() {
      return new PlayerArgumentType();
   }

   public static String getPlayer(CommandContext<?> context, String name) {
      return (String)context.getArgument(name, String.class);
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      return reader.readString();
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      Collection<class_640> playerListEntries = mc.field_1724.field_3944.method_2880();
      Iterator var4 = playerListEntries.iterator();

      while(var4.hasNext()) {
         class_640 playerListEntry = (class_640)var4.next();
         builder.suggest(playerListEntry.method_2966().getName());
      }

      return builder.buildFuture();
   }
}
