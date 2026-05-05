# PZM Client — Fabric PvP Utility Mod
**For Minecraft 1.21.1 | Fabric Loader 0.16.5+**
*By PZM Studio*

---

## Features

### 🎮 Core Toggles (General tab)
| Feature | Description |
|---|---|
| **Auto-Sprint** | Auto-sprints whenever you move forward with enough hunger |
| **Kill Counter** | Tracks kills, deaths, and K/D ratio on-screen |
| **CPS Counter** | Live clicks-per-second display with optional peak CPS |
| **Reach Display** | Shows your real-time distance to the entity you're looking at |
| **Armor HUD** | Armor icons + durability % with color-coded warnings |
| **Combo Counter** | Hit streak counter with bounce animation |
| **Custom Crosshair** | Full crosshair override (see Crosshair tab) |
| **Sound Changer** | Custom hit & kill sounds (see Sounds tab) |

### 🔊 Sounds Tab
- Hit sounds: `ORB`, `CLICK`, `POP`, `HURT`
- Kill sounds: `THUNDER`, `LEVELUP`, `ANVIL`, `EXPLOSION`
- Volume and pitch sliders

### 🖥️ HUD Tab
- Toggle K/D ratio, death counter, peak CPS, armor durability warnings

### ✚ Crosshair Tab
- Styles: `DEFAULT`, `PLUS`, `DOT`, `CROSS`, `CIRCLE`, `DYNAMIC`
- `DYNAMIC` — gap expands with movement speed
- Size, gap, thickness sliders
- Outline toggle
- **Rainbow Mode** 🌈

### 🎁 Extras / Surprises
| Feature | Description |
|---|---|
| 💥 Blood Particles | Burst of damage particles on hit |
| ⚡ Screen Flash on Kill | Red flash when you get a kill |
| 🔥 Killstreak Announcements | Double Kill → Triple → Quadra → PENTA → GODLIKE → BEYOND GODLIKE |
| 👑 Bossbar Killstreak | Displays your current kill streak in the boss bar |
| 🟢 MATRIX MODE | Falling green code rains down your screen. Yes, really. |

---

## Controls
- **Right Shift** → Open PZM Client GUI

---

## Setup / Build

### Requirements
- JDK 21
- Git

### Steps
```bash
git clone <this-repo>
cd pzmclient
./gradlew build
```
Compiled jar will be at `build/libs/pzmclient-1.0.0.jar`

Drop into your `.minecraft/mods/` folder alongside:
- [Fabric Loader 0.16.5+](https://fabricmc.net/use/installer/)
- [Fabric API 0.102.0+1.21.1](https://modrinth.com/mod/fabric-api)

### Optional
- [ModMenu](https://modrinth.com/mod/modmenu) — adds PZM Client to the mods list GUI

---

## Config
Auto-saved to `.minecraft/config/pzmclient.json` whenever you close the GUI.

---

## Project Structure
```
src/main/java/dev/pzm/pzmclient/
├── PzmClient.java              # Entrypoint, keybind, tick/HUD registration
├── config/
│   └── ModConfig.java          # All settings, load/save
├── features/
│   ├── AutoSprint.java
│   ├── BloodParticles.java
│   ├── KillstreakAnnouncer.java
│   └── MatrixMode.java
├── gui/
│   └── PzmScreen.java          # Tabbed GUI (Right Shift)
├── hud/
│   ├── ArmorHud.java
│   ├── ComboCounter.java
│   ├── CpsCounter.java
│   ├── CrosshairRenderer.java
│   ├── KillCounter.java
│   └── ReachDisplay.java
├── mixin/
│   ├── ClientPlayerInteractionManagerMixin.java  # Hit detection
│   ├── GameRendererMixin.java
│   ├── InGameHudMixin.java                       # Crosshair cancel, flash, matrix
│   ├── LivingEntityMixin.java                    # Kill/death detection
│   └── ScreenFlash.java
└── sound/
    └── SoundManager.java
```
