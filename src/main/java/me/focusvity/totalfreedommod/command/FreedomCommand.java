package me.focusvity.totalfreedommod.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.*;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.TotalFreedomMod;
import me.focusvity.totalfreedommod.rank.Rank;

import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class FreedomCommand implements CommandExecutor
{

    protected static CommandMap cmap;
    protected final String commandName;
    protected final String description;
    protected final String usage;
    protected final String[] aliases;
    protected final Rank rank;
    protected final SourceType source;

    public FreedomCommand(String commandName, String description, String usage, String[] aliases, Rank rank, SourceType source)
    {
        this.commandName = commandName;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.rank = rank;
        this.source = source;
    }

    public void register()
    {
        ReflectCommand cmd = new ReflectCommand(commandName);

        if (description != null)
        {
            cmd.setDescription(description);
        }

        if (usage != null)
        {
            cmd.setUsage(usage);
        }

        if (aliases != null)
        {
            cmd.setAliases(aliases);
        }

        if (!getCommandMap().register("", cmd))
        {
            unregisterNukkitCommand((PluginCommand) TotalFreedomMod.plugin.getServer().getPluginCommand(cmd.getName()));
            getCommandMap().register("", cmd);
        }

        cmd.setExecutor(this);
    }

    @Override
    public abstract boolean onCommand(CommandSender sender, Command cmd, String string, String[] args);

    private Object getPrivateField(Object object, String field) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

    private void unregisterNukkitCommand(PluginCommand command)
    {
        try
        {
            Object result = getPrivateField(TotalFreedomMod.plugin.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");

            @SuppressWarnings("unchecked")
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            knownCommands.remove(command.getName());

            for (String knownAlias : command.getAliases())
            {
                knownCommands.remove(knownAlias);
            }
        }
        catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException ex)
        {
            TotalFreedomMod.plugin.getLogger().alert("", ex);
        }
    }

    final CommandMap getCommandMap()
    {
        if (cmap == null)
        {
            try
            {
                final Field f = Server.getInstance().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Server.getInstance());
                return getCommandMap();
            }
            catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex)
            {
                TotalFreedomMod.plugin.getLogger().critical("", ex);
            }
        }
        else if (cmap != null)
        {
            return cmap;
        }
        return getCommandMap();
    }

    public final class ReflectCommand extends Command
    {
        private FreedomCommand cmd = null;

        protected ReflectCommand(String command)
        {
            super(command);
        }

        public void setExecutor(FreedomCommand cmd)
        {
            this.cmd = cmd;
        }

        @Override
        public boolean execute(CommandSender sender, String string, String[] args)
        {
            if (cmd != null)
            {
                if (sender instanceof Player && source == SourceType.CONSOLE)
                {
                    sender.sendMessage(TextFormat.RED + "You must be on console to be able to execute this command!");
                    return true;
                }

                if (!(sender instanceof Player) && source == SourceType.PLAYER)
                {
                    sender.sendMessage(TextFormat.RED + "You must be in-game to be able to execute this command!");
                    return true;
                }

                if (!Rank.getRank(sender).isAtLeast(rank))
                {
                    sender.sendMessage(TextFormat.RED + "You must be at least " + rank.getName() + " to be able to execute this command!");
                    return true;
                }

                if (!cmd.onCommand(sender, this, string, args))
                {
                    sender.sendMessage(TextFormat.WHITE + usage.replace("/<command>", commandName));
                    return false;
                }
            }

            return false;
        }
    }
}
