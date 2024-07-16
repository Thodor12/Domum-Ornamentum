package com.ldtteam.domumornamentum.datagen.panel;

import com.ldtteam.domumornamentum.block.ModBlocks;
import com.ldtteam.domumornamentum.block.decorative.CornerPanelBlock;
import com.ldtteam.domumornamentum.block.types.CornerPanelShapeType;
import com.ldtteam.domumornamentum.block.types.CornerPanelType;
import com.ldtteam.domumornamentum.datagen.MateriallyTexturedModelBuilder;
import com.ldtteam.domumornamentum.datagen.utils.ModelBuilderUtils;
import com.ldtteam.domumornamentum.util.Constants;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import static com.ldtteam.domumornamentum.block.decorative.CornerPanelBlock.*;
import static net.minecraft.world.item.ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;

public class CornerPanelBlockStateProvider extends BlockStateProvider
{
    public CornerPanelBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen.getPackOutput(), Constants.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        final CornerPanelBlock cornerPanel = ModBlocks.getInstance().getCornerPanel();
        final MultiPartBlockStateBuilder builder = getMultipartBuilder(cornerPanel);
        for (Direction facingValue : FACING.getPossibleValues())
        {
            for (CornerPanelType typeValue : TYPE.getPossibleValues())
            {
                for (CornerPanelShapeType shapeTypeValue : SHAPE_TYPE.getPossibleValues())
                {
                    final var partBuilder = builder.part();

                    final BlockModelBuilder model = models()
                                                      .withExistingParent("block/corner_panel/corner_panel_" + typeValue.getSerializedName(),
                                                        modLoc("block/corner_panel/corner_panel_%s_spec".formatted(typeValue.getSerializedName())))
                                                      .customLoader(MateriallyTexturedModelBuilder::new)
                                                      .end();

                    partBuilder.modelFile(model)
                      .rotationX(getRotationX(shapeTypeValue))
                      .rotationY(getRotationY(facingValue, shapeTypeValue))
                      .uvLock(true)
                      .addModel()
                      .condition(FACING, facingValue)
                      .condition(TYPE, typeValue)
                      .condition(SHAPE_TYPE, shapeTypeValue)
                      .end();
                }
            }
        }

        final ItemModelBuilder itemSpecModelBuilder = itemModels().withExistingParent("corner_panel_spec", mcLoc("block/thin_block"));
        itemSpecModelBuilder.transforms().transform(THIRD_PERSON_RIGHT_HAND).rightRotation(0, 90, 0);
        CornerPanelType[] values = CornerPanelType.values();
        for (int i = 0; i < values.length; i++)
        {
            CornerPanelType value = values[i];
            final ItemModelBuilder.OverrideBuilder overrideBuilder = itemSpecModelBuilder.override();
            overrideBuilder.predicate(new ResourceLocation(Constants.CORNER_PANEL_MODEL_OVERRIDE), i);
            overrideBuilder.model(itemModels().getExistingFile(modLoc("block/corner_panel/corner_panel_%s_spec".formatted(value.getSerializedName()))));
            overrideBuilder.end();
        }

        final ItemModelBuilder itemModelBuilder = itemModels()
                                                    .getBuilder(cornerPanel.getRegistryName().getPath())
                                                    .parent(itemSpecModelBuilder)
                                                    .customLoader(MateriallyTexturedModelBuilder::new)
                                                    .end();
        ModelBuilderUtils.applyDefaultItemTransforms(itemModelBuilder);
    }

    @NotNull
    @Override
    public String getName()
    {
        return "Corner Panel BlockStates Provider";
    }

    public int getRotationX(final CornerPanelShapeType shapeType)
    {
        return switch (shapeType)
        {
            case UPPER -> 90;
            case LOWER -> 270;
            case CENTER -> 0;
        };
    }

    public int getRotationY(final Direction direction, final CornerPanelShapeType shapeType)
    {
        if (shapeType.equals(CornerPanelShapeType.CENTER))
        {
            return direction.get2DDataValue() * 90;
        }
        else
        {
            return Rotation.CLOCKWISE_90.rotate(direction).get2DDataValue() * 90;
        }
    }
}
