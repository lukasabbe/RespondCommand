package me.lukasabbe.respondmod.mixins;

import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(MessageCommand.class)
public interface MessageCommandInvoker {
    @Invoker("execute")
    static int execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, MessageArgumentType.SignedMessage signedMessage){
        return 0;
    }
}