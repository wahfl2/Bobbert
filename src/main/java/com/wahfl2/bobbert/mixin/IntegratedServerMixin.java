package com.wahfl2.bobbert.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.integrated.IntegratedServer;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.wahfl2.bobbert.BobbertConfig;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    @ModifyExpressionValue(
        method = "tick",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/client/settings/GameSettings;renderDistanceChunks:I"))
    private int getOverrideViewDistance(int original) {
        int overwrite = BobbertConfig.viewDistanceOverwrite;
        if (overwrite != 0 && BobbertConfig.enabled) {
            return overwrite;
        }
        return original;
    }
}
