package net.lugo.lightoverlay.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.lugo.lightoverlay.OverlayHandler;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ModeArgumentType implements ArgumentType<OverlayHandler.Mode> {
    private static final DynamicCommandExceptionType INVALID_MODE = new DynamicCommandExceptionType(
            value -> Component.translatable("text.light-overlay.command.mode.invalid", value)
    );

    private ModeArgumentType() {
    }

    public static ModeArgumentType mode() {
        return new ModeArgumentType();
    }

    public static OverlayHandler.Mode getMode(CommandContext<?> context, String name) {
        return context.getArgument(name, OverlayHandler.Mode.class);
    }

    @Override
    public OverlayHandler.Mode parse(StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();
        String input = reader.readUnquotedString();
        try {
            return OverlayHandler.Mode.valueOf(input.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            reader.setCursor(start);
            throw INVALID_MODE.createWithContext(reader, input);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (OverlayHandler.Mode mode : OverlayHandler.Mode.values()) {
            if (mode.name().toLowerCase(Locale.ROOT).startsWith(builder.getRemaining().toLowerCase(Locale.ROOT))) {
                builder.suggest(mode.name().toLowerCase(Locale.ROOT));
            }
        }
        return builder.buildFuture();
    }
}
