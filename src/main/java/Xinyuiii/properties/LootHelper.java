package Xinyuiii.properties;

import Xinyuiii.enumType.BastionType;
import Xinyuiii.reecriture.BastionPools.BastionStructureLoot;
import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mccore.util.data.Pair;
import com.seedfinding.mccore.util.pos.BPos;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcfeature.loot.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LootHelper {
    private static int getTreasureBottomGoldBlocks(BastionGenerator generator) {
        if (generator.getType() != BastionType.TREASURE) {
            return 0;
        }
        String bottom = null;
        for (BastionGenerator.Piece piece : generator.getPieces()) {
            if (piece.name.contains("treasure/bases/centers/center")) {
                bottom = piece.name;
                break;
            }
        }
        assert bottom != null;
        if (bottom.endsWith("0")) return 19;
        if (bottom.endsWith("1")) return 12;
        if (bottom.endsWith("2")) return 16;
        if (bottom.endsWith("3")) return 13;
        return 0;
    }

    public static int getFixedGoldBlocks(BastionGenerator generator) {
        return switch (generator.getType()) {
            case HOUSING -> 17;//9bottom, 3middle, 4leftTower, 1leftTowerMiddle
            case STABLES -> 0;
            case TREASURE -> 4 + getTreasureBottomGoldBlocks(generator);//2bridge, 2chestRoom, bottom
            case BRIDGE -> 16;//16chalice
        };
    }

    public static List<BPos> getVariableGoldBlocks(BastionGenerator generator) {
        List<Pair<BPos, Float>> goldPositions = new ArrayList<>();
        ChunkRand rand = new ChunkRand();
        for (BastionGenerator.Piece piece : generator.getPieces()) {
            if (!BastionStructureLoot.STRUCTURE_VARIABLE_GOLD_OFFSETS.containsKey(piece.name)) continue;
            for (BPos offset : BastionStructureLoot.STRUCTURE_VARIABLE_GOLD_OFFSETS.get(piece.name).getFirst()) {
                goldPositions.add(new Pair<>(piece.pos.add(piece.getTransformedPos(offset, piece.rotation)),
                        BastionStructureLoot.STRUCTURE_VARIABLE_GOLD_OFFSETS.get(piece.name).getSecond()));
            }
        }
        return goldPositions.stream().map(pos -> {
            rand.setPositionSeed(pos.getFirst(), MCVersion.latest());// dummy version
            return rand.nextFloat() < pos.getSecond() ? null : pos.getFirst();
        }).filter(Objects::nonNull).toList();
    }

    public static int getTotalGoldIngots(BastionGenerator generator, List<Pair<BPos, List<ItemStack>>> loot) {
        int chestGold = 0;
        for (Pair<BPos, List<ItemStack>> chest : loot) {
            for (ItemStack stack : chest.getSecond()) {
                if (stack.getItem().getName().equals("gold_block")) chestGold += stack.getCount() * 9;
                if (stack.getItem().getName().equals("gold_ingot")) chestGold += stack.getCount();
            }
        }
        return chestGold + getFixedGoldBlocks(generator) * 9 + getVariableGoldBlocks(generator).size() * 9;
    }

    public static int[] getStableInfo(BastionGenerator generator) {
        if (generator.getType() != BastionType.STABLES) {
            return null;
        }
        int tripleRampart = 0;
        int goodBottom = 0;
        for (BastionGenerator.Piece piece : generator.getPieces()) {
            if (piece.name.equals("hoglin_stable/ramparts/ramparts_1")) tripleRampart++;
            if (piece.name.equals("hoglin_stable/walls/side_wall_1")) goodBottom++;
        }
        return new int[]{tripleRampart, goodBottom};
    }
}
