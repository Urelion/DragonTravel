package eu.phiwa.dragontravel.core.objects;

import eu.phiwa.dragontravel.core.DragonTravelMain;
import eu.phiwa.dragontravel.nms.IRyeDragon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("DT-StatDragon")
public class StationaryDragon implements ConfigurationSerializable {

    private String displayName;
    private String owner;
    private String name;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;

    private IRyeDragon dragon;


    public StationaryDragon(String name, Map<String, Object> data) {
        this.x = (double) data.get("x");
        this.y = (double) data.get("y");
        this.z = (double) data.get("z");
        this.yaw = (double) data.get("yaw");
        this.pitch = (double) data.get("pitch");
        this.worldName = (String) data.get("world");
        this.name = name;
        if (data.containsKey("displayname")) {
            this.displayName = (String) data.get("displayname");
        } else {
            this.displayName = (String) data.get("name");
        }
        if (data.containsKey("owner")) {
            this.owner = (String) data.get("owner");
        } else {
            this.owner = new String("admin");
        }

        this.dragon = createDragon(false);
        DragonTravelMain.listofStatDragons.put(name.toLowerCase(), this);
    }

    public StationaryDragon(Player player, String name, String displayName, Location loc, boolean isNew) {
        this.owner = player.getUniqueId().toString();
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.yaw = loc.getYaw();
        this.pitch = loc.getPitch();
        this.worldName = loc.getWorld().getName();
        this.name = name.toLowerCase();
        this.displayName = displayName;

        this.dragon = createDragon(isNew);
        DragonTravelMain.listofStatDragons.put(name.toLowerCase(), this);
        player.sendMessage(DragonTravelMain.getInstance().getMessagesHandler().getMessage("Messages.General.Successful.AddedStatDragon"));
    }

    /**
     * Creates a stationary dragon
     */
    public IRyeDragon createDragon(boolean isNew) {
        final IRyeDragon dragon = DragonTravelMain.getInstance().getNmsHandler().getRyeDragon(toLocation());
        dragon.fixWings();
        dragon.setCustomName(ChatColor.translateAlternateColorCodes('&', displayName));
        dragon.setCustomNameVisible(true);
        if (isNew)
            DragonTravelMain.getInstance().getDbStatDragonsHandler().createStatDragon(name, this);
        return dragon;
    }

    public void removeDragon(boolean isPermanent) {
        String name = null;
        for (Map.Entry<String, StationaryDragon> entry : DragonTravelMain.listofStatDragons.entrySet()) {
            if (entry.getValue().equals(this)) {
                name = entry.getKey();
                break;
            }
        }
        if (name == null) {
            return;
        }
        DragonTravelMain.listofStatDragons.remove(name);
        dragon.getEntity().remove();
        if (isPermanent)
            DragonTravelMain.getInstance().getDbStatDragonsHandler().deleteStatDragon(name);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        sb.append("--- StatDragon ---").append('\n');
        sb.append("Name: " + name).append('\n');
        sb.append("Display Name: " + displayName).append('\n');
        sb.append("Owner: " + owner).append('\n');
        sb.append("X: " + x).append('\n');
        sb.append("Y: " + y).append('\n');
        sb.append("Z: " + z).append('\n');
        sb.append("Yaw: " + yaw).append('\n');
        sb.append("Pitch: " + pitch).append('\n');
        sb.append("World: " + worldName).append('\n');
        sb.append("---------------").append('\n');
        return sb.toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("x", x);
        ret.put("y", y);
        ret.put("z", z);
        ret.put("yaw", yaw);
        ret.put("pitch", pitch);
        ret.put("world", worldName);
        ret.put("owner", owner);
        ret.put("displayname", displayName);
        return ret;
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z, (float) yaw, (float) pitch);
    }

    public IRyeDragon getDragon() {
        return dragon;
    }

    public void setDragon(IRyeDragon dragon) {
        this.dragon = dragon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }
}
