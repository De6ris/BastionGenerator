package Xinyuiii.properties;

import Xinyuiii.reecriture.BastionPools.BastionStructureLoot;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcfeature.loot.LootTable;

import java.util.List;

public class VersionDifference {
    public static List<LootTable> getLootTable(MCVersion version, String pieceName) {
        if (version.isBetween(MCVersion.v1_16, MCVersion.v1_16_1)) {
            return BastionStructureLoot.STRUCTURE_LOOT_1_16_0.get(pieceName);
        } else if (version.isBetween(MCVersion.v1_16_2, MCVersion.v1_19_4)) {
            return BastionStructureLoot.STRUCTURE_LOOT_1_16_2.get(pieceName);
//        } else if (version.isBetween(MCVersion.v1_20, MCVersion.v1_21_9)) {// TODO
        } else if (version.isBetween(MCVersion.v1_20, MCVersion.v1_20_6)) {
            return BastionStructureLoot.STRUCTURE_LOOT_1_20_0.get(pieceName);
        } else {// 1.21.10
            return BastionStructureLoot.STRUCTURE_LOOT_1_21_10.get(pieceName);
        }
    }

    public static int getSalt(MCVersion version) {
        if (version.isBetween(MCVersion.v1_16, MCVersion.v1_18_2)) {
            return 40012;
        } else if (version.isBetween(MCVersion.v1_19, MCVersion.v1_19_2)) {
            return 40013;
//        } else if (version.isBetween(MCVersion.v1_19_3, MCVersion.v26.2)) {// TODO
        } else if (version.isBetween(MCVersion.v1_19_3, MCVersion.v1_20_6)) {
            return 40000;
        } else {// 26.3
            return 40018;
        }
    }
}
