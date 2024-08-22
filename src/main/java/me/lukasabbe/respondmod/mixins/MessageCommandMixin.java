package me.lukasabbe.respondmod.mixins;

import me.lukasabbe.respondmod.RespondMod;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(MessageCommand.class)
public class MessageCommandMixin {
    @Inject(method = "execute", at=@At("HEAD"))
    private static void execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, SignedMessage message, CallbackInfo ci) {
        RespondMod.latestSend.put(targets.iterator().next().getUuid(), source.getPlayer().getUuid());
    }
}
