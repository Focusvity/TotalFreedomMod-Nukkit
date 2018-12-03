package me.focusvity.totalfreedommod.rank;

import cn.nukkit.utils.TextFormat;
import lombok.Getter;

public enum Title implements Displayable
{

    MASTER_BUILDER("a", "Master Builder", "MB", TextFormat.DARK_AQUA),
    EXECUTIVE("an", "Executive", "Exec", TextFormat.RED),
    DEVELOPER("a", "Developer", "Dev", TextFormat.DARK_PURPLE),
    OWNER("the", "Owner", "Owner", TextFormat.BLUE);

    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final TextFormat color;

    Title(String determiner, String name, String tag, TextFormat color)
    {
        this.determiner = determiner;
        this.name = name;
        this.color = color;
        this.tag = TextFormat.DARK_GRAY + "[" + color + tag + TextFormat.DARK_GRAY + "]" + color;
    }

    public String getLoginMessage()
    {
        return determiner + " " + color + TextFormat.ITALIC + name;
    }
}
