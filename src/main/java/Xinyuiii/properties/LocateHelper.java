package Xinyuiii.properties;

import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mccore.util.pos.CPos;
import com.seedfinding.mccore.version.MCVersion;
import org.jspecify.annotations.Nullable;

public class LocateHelper {
    @Nullable
    public static CPos getInRegion(long structureSeed, int regionX, int regionZ, ChunkRand rand, MCVersion version) {
        rand.setRegionSeed(structureSeed, regionX, regionZ, 30084232, version);

        int chunkX = regionX * 27 + rand.nextInt(23);
        int chunkZ = regionX * 27 + rand.nextInt(23);

        CPos cPos = new CPos(chunkX, chunkZ);

        if (version.isOlderThan(MCVersion.v1_17)) {
            rand.setRegionSeed(structureSeed, chunkX, chunkZ, 30084232, version);
            rand.advance(2);
            int determine = rand.nextInt(5);
            return determine >= 2 ? cPos : null;
        } else {
            rand.setCarverSeed(structureSeed, chunkX, chunkZ, version);
            int determine = rand.nextInt(5);
            return determine >= 2 ? cPos : null;
        }
    }

}
