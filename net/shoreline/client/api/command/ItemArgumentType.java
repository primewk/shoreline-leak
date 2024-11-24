package net.shoreline.client.api.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_1792;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public class ItemArgumentType implements ArgumentType<class_1792> {
   public static ItemArgumentType item() {
      return new ItemArgumentType();
   }

   public static class_1792 getItem(CommandContext<?> context, String name) {
      return (class_1792)context.getArgument(name, class_1792.class);
   }

   public class_1792 parse(StringReader reader) throws CommandSyntaxException {
      String string = reader.readString();
      class_1792 item = (class_1792)class_7923.field_41178.method_10223(new class_2960("minecraft", string));
      if (item == null) {
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, (Object)null);
      } else {
         return item;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      Iterator var3 = class_7923.field_41178.iterator();

      while(var3.hasNext()) {
         class_1792 item = (class_1792)var3.next();
         builder.suggest(class_7923.field_41178.method_10221(item).method_12832());
      }

      return builder.buildFuture();
   }
}
