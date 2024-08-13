package me.lukasabbe.respondmod.mixins;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.lukasabbe.respondmod.RespondMod;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(MessageCommand.class)
public class MessageCommandMixin {
    @Inject(method = "execute", at=@At("HEAD"))
    private static void execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Text message, CallbackInfoReturnable<Integer> cir) {
        try{
            RespondMod.latestSend.put(targets.iterator().next().getUuid(), source.getPlayer().getUuid());
        }catch (CommandSyntaxException ignored){}
    }
}
