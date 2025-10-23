package com.wahfl2.bobbert.mixin;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.server.integrated.IntegratedServer;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.wahfl2.bobbert.BobbertConfig;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    @Redirect(
        method = "tick",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/client/settings/GameSettings;renderDistanceChunks:I"))
    private int getOverrideViewDistance(GameSettings instance) {
        int overwrite = BobbertConfig.viewDistanceOverwrite;
        if (overwrite != 0) {
            return overwrite;
        }
        return instance.renderDistanceChunks;
    }
}
