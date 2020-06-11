package me.focusvity.totalfreedommod.command;

import cn.nukkit.command.CommandMap;
import me.focusvity.totalfreedommod.TotalFreedomMod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.CodeSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CommandLoader
{

    private static CommandMap cmap;

    public CommandLoader()
    {
        registerCommands();
    }

    public static void registerCommands()
    {
        try
        {
            Pattern pattern = Pattern.compile("me/focusvity/totalfreedommod/command/(Command_[^\\$]+)\\.class");
            CodeSource cs = TotalFreedomMod.class.getProtectionDomain().getCodeSource();

            if (cs != null)
            {
                ZipInputStream zip = new ZipInputStream(cs.getLocation().openStream());
                ZipEntry zipEntry;
                while ((zipEntry = zip.getNextEntry()) != null)
                {
                    String entry = zipEntry.getName();
                    Matcher matcher = pattern.matcher(entry);
                    if (matcher.find())
                    {
                        try
                        {
                            Class<?> commandClass = Class.forName("me.focusvity.totalfreedommod.command." + matcher.group(1));
                            if (commandClass.isAnnotationPresent(CommandParameters.class))
                            {
                                FreedomCommand command = (FreedomCommand) commandClass.getConstructor().newInstance();
                                command.register();
                            }
                        }
                        catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
                        {
                            TotalFreedomMod.plugin.getLogger().critical("", ex);
                        }
                    }
                }
            }
        }
        catch (IOException ex)
        {
            TotalFreedomMod.plugin.getLogger().critical("", ex);
        }
    }
}
