package me.focusvity.totalfreedommod.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.admin.AdminList;
import me.focusvity.totalfreedommod.rank.Rank;

@CommandParameters(name = "cmdspy", description = "Toggles command spy", aliases = "commandspy", rank = Rank.SUPER_ADMIN, source = SourceType.PLAYER)
public class Command_cmdspy extends FreedomCommand
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length > 0)
        {
            return false;
        }

        boolean toggle = AdminList.getAdmin((Player) sender).isCommandSpyEnabled();
        sender.sendMessage(TextFormat.GRAY + "Command spy: " + (toggle ? TextFormat.RED + "Disabled" : TextFormat.GREEN + "Enabled"));
        AdminList.getAdmin((Player) sender).setCommandSpyEnabled(!toggle);
        return true;
    }
}
