package net.ltxprogrammer.changed.world.features.structures;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.init.ChangedFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Set;

/*
//Some Oblate Spheroid code I need to replace into the main function
int[][][] oblateSpheroidShell(int h, int w) {
    int[][][] matrix = new int[w][h][w];
    double centerX = w / 2.0;
    double centerY = h / 2.0;
    double centerZ = w / 2.0;
    double w2 = (w / 2.0) * (w / 2.0);
    double h2 = (h / 2.0) * (h / 2.0);

    for (int x = 0; x < w; x++) {
        for (int y = 0; y < h; y++) {
            for (int z = 0; z < w; z++) {
                if ((((x - centerX) * (x - centerX)) / w2) + (((y - centerY) * (y - centerY)) / h2) + (((z - centerZ) * (z - centerZ)) / w2) == 1) {
                    matrix[x][y][z] = 1;
                }
            }
        }
    }
    return matrix;
}
*/

public class Beehive extends Feature<NoneFeatureConfiguration> {
    public static Beehive FEATURE = null;
    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> CONFIGURED_FEATURE = null;
    public static Holder<PlacedFeature> PLACED_FEATURE = null;

    public static Feature<?> feature() {
        if (FEATURE == null) {
            FEATURE = new Beehive();
            CONFIGURED_FEATURE = FeatureUtils.register(Changed.modResourceStr("decayed_lab_dark_latex_dragon"), FEATURE, FeatureConfiguration.NONE);
            PLACED_FEATURE = PlacementUtils.register(Changed.modResourceStr("decayed_lab_dark_latex_dragon"), CONFIGURED_FEATURE, List.of());
        }
        return FEATURE;
    }

    public static Holder<PlacedFeature> placedFeature() {
        return PLACED_FEATURE;
    }

    public static final Set<ResourceLocation> GENERATE_BIOMES = Set.of(new ResourceLocation("plains"), new ResourceLocation("meadow"));

    private final Set<ResourceKey<Level>> generate_dimensions = Set.of(Level.OVERWORLD);

    private StructureTemplate template = null;

    public Beehive() {
        super(NoneFeatureConfiguration.CODEC);

    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!generate_dimensions.contains(context.level().getLevel().dimension()))
            return false;

        if (template == null)
            template = context.level().getLevel().getStructureManager().getOrCreate(Changed.modResource("decayed_lab_dld"));

        if (template == null)
            return false;

        boolean anyPlaced = false;
        if ((context.random().nextInt(1000000) + 1) <= ChangedFeatures.LAB_RARITY) {
            int count = context.random().nextInt(1) + 1;
            for (int a = 0; a < count; a++) {
                int i = context.origin().getX() + context.random().nextInt(16);
                int k = context.origin().getZ() + context.random().nextInt(16);

                int j = context.level().getHeight(Heightmap.Types.OCEAN_FLOOR_WG, i, k) - 1;

                BlockPos spawnTo = new BlockPos(i + 0, j + -1, k + 0);

                if (spawnTo.getY() < context.level().getSeaLevel())
                    continue;

                if (template.placeInWorld(context.level(), spawnTo, spawnTo,
                        new StructurePlaceSettings().setMirror(Mirror.values()[context.random().nextInt(2)])
                                .setRotation(Rotation.values()[context.random().nextInt(3)]).setRandom(context.random())
                                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).setIgnoreEntities(false),
                        context.random(), 2)) {

                    anyPlaced = true;
                }
            }
        }

        return anyPlaced;
    }
}
