package com.wahfl2.bobbert.compat;

public class LoadedMods {
    public static boolean ANGELICA;

    static {
        try {
            Class.forName("com.gtnewhorizons.angelica.loading.AngelicaTweaker");
            ANGELICA = true;
        } catch (NoClassDefFoundError | ClassNotFoundException ignored) {
        }
    }
}
