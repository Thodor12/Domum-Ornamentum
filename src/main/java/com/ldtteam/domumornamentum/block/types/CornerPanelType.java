package com.ldtteam.domumornamentum.block.types;

import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum CornerPanelType implements StringRepresentable
{
    INNER("inner"),
    OUTER("outer");

    private final String serializationName;

    CornerPanelType(final String serializationName)
    {
        this.serializationName = serializationName;
    }

    @Override
    @NotNull
    public String getSerializedName()
    {
        return serializationName;
    }

    public String getTranslationKeySuffix()
    {
        return getSerializedName().replace("_", ".");
    }

    public String getDefaultEnglishTranslation()
    {
        final String[] parts = getSerializedName().split("_");
        return Arrays.stream(parts)
                 .map(StringUtils::capitalize)
                 .collect(Collectors.joining(" "));
    }
}
