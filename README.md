# 🧱 EnderChestSee

A powerful, modern Minecraft Paper plugin for version 1.21.4 that allows viewing and editing any online player's **Ender Chest**!
Supports permission-based editing, MiniMessage-formatted messages, and full inventory sync.

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.4-green)
![Platform](https://img.shields.io/badge/Platform-Paper-blue)
![Java](https://img.shields.io/badge/Java-17%2B-orange)

---

## ✨ Features

| Feature                             | Description                                                                 |
|-------------------------------------|-----------------------------------------------------------------------------|
| 🔍 `/endersee <player>`             | View and edit any online/offline player's Ender Chest                       |
| 🚫 Permission-based control         | Prevent players from editing without permission                             |
| 🧾 `messages.yml` with MiniMessage | All plugin messages fully customizable with placeholders                     |
| 💾 Auto data saving                 | Changes persist even if the target player is offline or doesn’t open EC     |
| 🔁 `/endersee reload`              | Hot-reload messages without restarting the server                           |

---

## 🧰 Commands

| Command                  | Description                                      | Permission             |
|--------------------------|--------------------------------------------------|------------------------|
| `/endersee <player>`     | Opens the player's Ender Chest                   | `endersee.use`         |
| `/endersee reload`       | Reloads the `messages.yml` configuration         | `endersee.reload`      |

---

## 🔐 Permissions

| Node              | Description                                              |
|-------------------|----------------------------------------------------------|
| `endersee.use`    | Allows using `/endersee` to view a player's Ender Chest  |
| `endersee.reload` | Allows reloading the plugin’s messages                   |
| `endersee.modify` | Allows modification of a player's enderchest             |

---

## ⚙️ Configuration

The plugin creates a `messages.yml` file on first run.  
All messages support [MiniMessage](https://docs.advntr.dev/minimessage/format.html) and custom placeholders like `<player>` and `<prefix>`.

```yaml
prefix: "<gray>[<green>EnderSee</green>]</gray> "
only-players: "<white>Only players can use this command</white>"
no-permission: "<red>No permission.</red>"
invalid-usage: "<red>Usage: /endersee <player> </red>"
player-not-found: "<red>They player wasn't found</red>"
open-endchest: "<green>Opening enderchest of <player> </green>"
enderchest-gui: "<dark_gray><player>'s Enderchest</dark_gray>"
reload-complete: "<green>Reloaded Successfully</green>"
```

# 📜 License

This plugin is open source under the [MIT License](https://opensource.org/license/mit).

# 👤 Author
Made with ☕ and ❤️ by [@Gura1](https://github.com/Guradev)
