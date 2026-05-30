package com.lightning.mod;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

public class ChunkCacheManager {
    private static final Path CACHE_DIR = Paths.get(System.getProperty("user.dir"), "lightning_cache");

    public static void initializeCache() {
        try {
            if (Files.exists(CACHE_DIR)) {
                clearCache();
            }
            Files.createDirectories(CACHE_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearCache() {
        if (!Files.exists(CACHE_DIR)) return;
        try {
            Files.walk(CACHE_DIR)
                 .sorted(Comparator.reverseOrder())
                 .map(Path::toFile)
                 .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveChunkToDisk(int x, int z, byte[] chunkData) {
        new Thread(() -> {
            try {
                Path chunkFile = CACHE_DIR.resolve("c_" + x + "_" + z + ".tmp");
                Files.write(chunkFile, chunkData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}