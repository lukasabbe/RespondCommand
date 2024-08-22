package me.lukasabbe.respondmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.NoSuchElementException;

public class RespondCommand {
    public void rCommand(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        dispatcher.register(CommandManager
                .literal("r")
                .then(CommandManager
                        .argument("respond", MessageArgumentType.message())
                        .executes(this::run)));
    }

    private int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        final ServerCommandSource source = ctx.getSource();
        ServerPlayerEntity player = source.getPlayer();
        if(!RespondMod.latestSend.containsKey(player.getUuid())) {
            source.sendError(Texts.toText(() -> "You have no one to respond to"));
            return 0;
        }
        try{
            ServerPlayerEntity receiver = player
                    .getServerWorld()
                    .getPlayers(t -> t.getUuid() == RespondMod.latestSend.get(player.getUuid()))
                    .getFirst();
            Text respond = MessageArgumentType.getMessage(ctx,"respond");
            receiver.sendSystemMessage((new TranslatableText("commands.message.display.incoming", receiver.getDisplayName(), respond)).formatted(Formatting.GRAY,Formatting.ITALIC),player.getUuid());
            source.sendFeedback((new TranslatableText("commands.message.display.outgoing", receiver.getDisplayName(), respond)).formatted(Formatting.GRAY, Formatting.ITALIC), false);
            return 1;
        }catch (NoSuchElementException ignore){
            source.sendError(Texts.toText(()->"The player is not online"));
            return 0;
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}