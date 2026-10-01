import Xinyuiii.enumType.BastionType;
import Xinyuiii.properties.BastionGenerator;
import Xinyuiii.properties.LocateHelper;
import Xinyuiii.reecriture.NewItems;
import com.seedfinding.mcbiome.source.NetherBiomeSource;
import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mccore.rand.seed.WorldSeed;
import com.seedfinding.mccore.util.data.Pair;
import com.seedfinding.mccore.util.pos.BPos;
import com.seedfinding.mccore.util.pos.CPos;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcfeature.loot.item.ItemStack;
import com.seedfinding.mcfeature.loot.item.Items;
import com.seedfinding.mcfeature.structure.BastionRemnant;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Test26_3 {
    public static final MCVersion VERSION = MCVersion.v1_21;


    @Test
    public void testLoot() {
        ChunkRand rand = new ChunkRand();
        BastionGenerator bastionGenerator = new BastionGenerator(VERSION);
        long seed = 22;

        CPos cPos = LocateHelper.getInRegion(seed, 0, 0, rand, VERSION);
        Assertions.assertNotNull(cPos);

        bastionGenerator.generate(seed, cPos);

        List<Pair<BPos, List<ItemStack>>> loot = bastionGenerator.generateLoot();

        List<ItemStack> chest = null;

        for (Pair<BPos, List<ItemStack>> pair : loot) {
            BPos bPos = pair.getFirst();
            if (bPos.equals(new BPos(69, 36, 111))) {
                chest = pair.getSecond();
                break;
            }
        }

        Assertions.assertNotNull(chest);

        int gilded_blackstone = 9;
        int gold_block = 4;
        int iron_ingot = 3;
        int magma_cream = 8;

        for (ItemStack stack : chest) {
            int count = stack.getCount();
            if (stack.getItem().equalsName(Items.GILDED_BLACKSTONE)) gilded_blackstone -= count;
            if (stack.getItem().equalsName(Items.GOLD_BLOCK)) gold_block -= count;
            if (stack.getItem().equalsName(Items.IRON_INGOT)) iron_ingot -= count;
            if (stack.getItem().equalsName(Items.MAGMA_CREAM)) magma_cream -= count;
        }

        Assertions.assertEquals(0, gilded_blackstone);
        Assertions.assertEquals(0, gold_block);
        Assertions.assertEquals(0, iron_ingot);
        Assertions.assertEquals(0, magma_cream);
    }


    @Test
    public void run() {
        ChunkRand rand = new ChunkRand();
        BastionRemnant bastionRemnant = new BastionRemnant(VERSION);
        BastionGenerator bastionGenerator = new BastionGenerator(VERSION);

        int finalSeed = -1;
        BPos finalChestPos = BPos.ORIGIN;

        for (int seed = 0; seed < 10000; seed++) {
            long structureSeed = WorldSeed.toStructureSeed(seed);

            CPos cPos = LocateHelper.getInRegion(structureSeed, 0, 0, rand, VERSION);
            if (cPos == null) continue;

            bastionGenerator.generate(seed, cPos);

            BastionType type = bastionGenerator.getType();
            if (type != BastionType.TREASURE) continue;

            List<Pair<BPos, List<ItemStack>>> loot = bastionGenerator.generateLoot();
            BPos bPos = getSpearChestPos(loot);
            if (bPos == null) continue;

            NetherBiomeSource biomeSource = new NetherBiomeSource(VERSION, seed);
            if (!bastionRemnant.canSpawn(cPos, biomeSource)) continue;

            finalSeed = seed;
            finalChestPos = bPos;
            break;
        }

        Assertions.assertEquals(13, finalSeed);
        Assertions.assertEquals(new BPos(219, 36, 35), finalChestPos);
    }

    @Nullable
    private static BPos getSpearChestPos(List<Pair<BPos, List<ItemStack>>> loot) {
        for (Pair<BPos, List<ItemStack>> pair : loot) {
            BPos pos = pair.getFirst();
            if (pos.getY() > 50) continue;// only bottom to test
            List<ItemStack> stacks = pair.getSecond();
            for (ItemStack stack : stacks) {
                if (stack.getItem().equalsName(NewItems.DIAMOND_SPEAR)) {
                    return pos;
                }
            }
        }
        return null;
    }
}
