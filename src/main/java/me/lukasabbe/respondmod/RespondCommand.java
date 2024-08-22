package me.lukasabbe.respondmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lukasabbe.respondmod.mixins.MessageCommandInvoker;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.NoSuchElementException;

public class RespondCommand {
    public void rCommand(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager
                .literal("r")
                .then(CommandManager
                        .argument("respond", MessageArgumentType.message())
                        .executes(this::run)));
    }

    private int run(CommandContext<ServerCommandSource> ctx) {
        final ServerCommandSource source = ctx.getSource();
        if(!source.isExecutedByPlayer()) {
            source.sendError(Text.literal("You can't respond as the console"));
            return 0;
        }
        ServerPlayerEntity player = source.getPlayer();
        if(!RespondMod.latestSend.containsKey(player.getUuid())) {
            source.sendError(Text.literal("You have no one to respond to"));
            return 0;
        }
        try{
            ServerPlayerEntity receiver = player
                    .getServerWorld()
                    .getPlayers(t -> t.getUuid() == RespondMod.latestSend.get(player.getUuid()))
                    .getFirst();
            MessageArgumentType.getSignedMessage(ctx,"respond",signedMessage ->
                    MessageCommandInvoker.execute(source, Collections.singleton(receiver),signedMessage));
            return 1;
        }catch (NoSuchElementException ignore){
            source.sendError(Text.literal("The player is not online"));
            return 0;
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}