package nl.dykam.dev.autoregen;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoRegenPlugin extends JavaPlugin implements Listener {
    private WorldGuardPlugin worldGuard;
    private HashMap<UUID, Boolean> bypasses;
    private Map<String, RegenGroup> regenGroups;
    private Random random;

    public static final StringFlag AUTOREGEN = new StringFlag("autoregen");
    public static final Pattern TIME_PATTERN = Pattern.compile("(\\d+)(s(?:ec(?:ond)?)?|m(:?in(?:ute)?)?|h(?:our)?|d(?:ay)?)?", Pattern.CASE_INSENSITIVE);

    @Override
    public void onEnable() {
        super.onEnable();
        regenGroups = new HashMap<>();
        bypasses = new HashMap<>();
        random = new Random();
        WGCustomFlagsPlugin customFlagsPlugin = setupCustomFlags();

        customFlagsPlugin.addCustomFlag(AUTOREGEN);

        worldGuard = WorldGuardPlugin.inst();
        saveDefaultConfig();
        loadConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void loadConfig() {

    }

    private TimeRules parseTimeRules(ConfigurationSection regen) {
        String string = regen.getString("type", "INSTANT");
        TimeRuleType type = TimeRuleType.DELAY;
        try {
            type = TimeRuleType.valueOf(string);
        } catch (IllegalArgumentException ignored) {}
        return new TimeRules(parseThreshold(regen.getString("threshold", "0")), parseTime(regen.getString("time", "0")), type);
    }

    private Threshold parseThreshold(String threshold) {
        if(threshold.endsWith("%"))
            return new Threshold(Float.parseFloat(threshold.substring(0, threshold.length() - 1)), Threshold.Type.PERCENTAGE);
        else
            return new Threshold(Integer.parseInt(threshold), Threshold.Type.ABSOLUTE);
    }

    private long parseTime(String time) {
        Matcher matcher = TIME_PATTERN.matcher(time);
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        while (matcher.find()) {
            String unit = matcher.group(2) == null ? "s" : matcher.group(2);
            Integer amount = Integer.parseInt(matcher.group(1));
            switch (unit.toLowerCase()) {
                case "s":case "sec":case "second":
                    calendar.add(Calendar.SECOND, amount);
                    break;
                case "m":case "min":case "minute":
                    calendar.add(Calendar.MINUTE, amount);
                    break;
                case "h":case "hour":
                    calendar.add(Calendar.HOUR, amount);
                    break;
                case "d": case "day":
                    calendar.add(Calendar.MONTH, amount);
                    break;
            }
        }
        return calendar.getTimeInMillis() - now.getTimeInMillis();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Can only be executed by a player");
            return true;
        }
        Player player = (Player) sender;
        testBypass(player);
        bypasses.put(player.getUniqueId(), !bypasses.get(player.getUniqueId()));
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onBlockBreak(final BlockBreakEvent event) {
        if(testBypass(event.getPlayer()))
            return;

        RegenGroup regenGroup = getRegenGroup(event.getBlock().getLocation());
        if(regenGroup == null)
            return;
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        bypasses.remove(event.getPlayer().getUniqueId());
    }

    private boolean testBypass(Player player) {
        if(!bypasses.containsKey(player.getUniqueId())) {
            boolean value = player.hasPermission("autoregen.bypass");
            bypasses.put(player.getUniqueId(), value);
            return value;
        }
        return bypasses.get(player.getUniqueId());
    }

    WGCustomFlagsPlugin setupCustomFlags() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WGCustomFlags");
        return plugin instanceof WGCustomFlagsPlugin ? (WGCustomFlagsPlugin) plugin : null;
    }

    private RegenGroup getRegenGroup(Location location) {
        ApplicableRegionSet applicableRegions = worldGuard.getRegionManager(location.getWorld()).getApplicableRegions(location);
        String flag = applicableRegions.getFlag(AUTOREGEN);
        if(flag == null)
            return null;

        RegenGroup regenGroup = regenGroups.get(flag);

        if(regenGroup == null) {
            StringBuilder sb = new StringBuilder();
            for (ProtectedRegion applicableRegion : applicableRegions) {
                sb.append(applicableRegion.getId()).append(" ");
            }
            sb.setLength(sb.length() - 1);
            getLogger().severe(String.format("One of the regions \"%s\" contains unknown value \"%s\" for flag \"%s\"", sb, flag, AUTOREGEN.getName()));
        }
        return regenGroup;
    }
}
