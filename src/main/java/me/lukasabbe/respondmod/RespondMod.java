package me.lukasabbe.respondmod;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RespondMod implements DedicatedServerModInitializer {
    public static Map<UUID,UUID> latestSend = new HashMap<>();
    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register(Commands::rCommand);
    }
}
