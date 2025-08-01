[![Modrinth Downloads](https://img.shields.io/modrinth/dt/inventory-overhaul?style=for-the-badge&logo=modrinth&color=1bd96a)](https://modrinth.com/project/inventory-overhaul/versions)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1317181?style=for-the-badge&logo=curseforge&color=f16436)](https://www.curseforge.com/minecraft/mc-mods/inventory-overhaul/files/all?showAlphaFiles=show)
[![GitHub Downloads](https://img.shields.io/github/downloads/McMelonTV/InventoryOverhaul/total?style=for-the-badge&logo=github&color=f0f6fc)](https://github.com/McMelonTV/InventoryOverhaul/releases/latest)

# Inventory Overhaul

A mod that aims to improve inventory management by changing the core aspects of the player's inventory and hotbar.

By default, this mod is set to behave exactly like vanilla and not change anything, however you can modify the behavior
with gamerules and through the mod's config.

### Showcase

https://github.com/user-attachments/assets/f60c17a2-a35b-450a-85a8-b9eb9b5d94fc

### Features

- Customizable hotbar size through server-side gamerules
    - This currently uses the rest of the inventory as additional hotbar slots when the total slot size of the hotbar is
      set to more than the default 9
- Customizable screen position and scale of the hotbar through a client-side config
- Multiple settings for hotbar scrolling configurable through a client-side config
    - You can currently choose the scrolling direction (Row/Column) and a scrolling mode (Split/Continuous)
    - The continuous scrolling mode acts like vanilla hotbar scrolling with it going through the whole hotbar while the
      split scrolling mode locks scrolling to the active scrolling direction while making it possible to scroll the
      other direction while holding a modifier key which is configurable through the Key Binds settings menu

### Usage

- #### Setting the hotbar size
    - You can set the hotbar size values through the Game Rules menu under the More tab in the world creation screen, or
      through the /gamerule command
    - Commands:
        - To set the horizontal size (rows): `/gamerule hotbarSizeX <number: row count>`
        - To set the vertical size (columns): `/gamerule hotbarSizeY <number: column count>`
- #### Client-side configuration
    - You can change all the client-side configuration values through the Inventory Settings menu under Controls in the
      game Settings
    - You can set the modifier keybind through the Key Binds menu under Controls in the game Settings

### Installation

1. Set up your preferred mod loader (Thanks to [Architectury](https://github.com/architectury), this mod currently
   supports both Fabric and NeoForge, including derivatives like Quilt)
2. Download the latest release of [Architectury API](https://modrinth.com/mod/architectury-api/versions) and the mod
   from [GitHub Releases](https://github.com/McMelonTV/InventoryOverhaul/releases/latest), [Modrinth](https://modrinth.com/project/inventory-overhaul/versions),
   or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/inventory-overhaul/files/all?showAlphaFiles=show) for your Minecraft version as well as [Fabric API](https://modrinth.com/mod/fabric-api) if you are using Fabric/Quilt
3. Add the mod and API jars file to your `.minecraft/mods` folder
4. Set up the gamerules and client configuration to your liking
5. Have fun!

If you are trying to use a Minecraft version that is not yet supported by the mod:

- If the Minecraft version is older than the current latest supported: Open an issue, and I'll consider backporting the
  mod to that version.
- If the Minecraft version is newer than the current latest supported: I will try to update the mod to the latest
  Minecraft version as soon as possible

### Planned Features

- Completely decouple the hotbar from the rest of the inventory
- Make the inventory size customizable
- Custom GUIs and Menu Widgets for the hotbar and inventory
- More robust screen positioning configuration, possibly with a drag and drop editor
- APIs for easy integration with other mods
- More to come...

### Known bugs

- The experience, health, armor, etc. bars, the offhand slot, and the selected hotbar item tooltip are displayed in the
  vanilla position, sometimes under the hotbar, depending on the configured hotbar position
- The hotbar is sometimes weirdly offset by a pixel or two; this especially noticeable when the client config values are
  set to the defaults
- You cannot currently set the hotbar position offset and padding through the in-game config screen
- Found another bug? Please report it in
  the [Issues tab on GitHub](https://github.com/McMelonTV/InventoryOverhaul/issues)
