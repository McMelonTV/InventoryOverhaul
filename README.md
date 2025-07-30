# Inventory Overhaul

A mod that aims to improve inventory management by changing the core aspects of the player's inventory and hotbar.

By default, this mod is set to behave exactly like vanilla and not change anything, however you can modify the behavior
with gamerules and through the mod's config.

### Showcase

[Showcase Video](https://raw.githubusercontent.com/McMelonTV/InventoryOverhaul/refs/heads/main/assets/showcase.mp4)

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
2. Download the latest release of the mod
   from [GitHub Releases](https://github.com/McMelonTV/InventoryOverhaul/releases/latest), [Modrinth](tba),
   or [CurseForge](tba) for your Minecraft version
3. Add the mod jar file to your `.minecraft/mods` folder
4. Set up the gamerules and client configuration to your liking
5. Have fun!

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
- Found another bug? Please report it in
  the [Issues tab on GitHub](https://github.com/McMelonTV/InventoryOverhaul/issues)