# ⚡ Archmage

A Minecraft Forge mod built around mastering the six elements.

## Elements
| Element | Strong Against | Weak To |
|---------|---------------|---------|
| ⚡ Lightning | Water/Ice | Earth |
| ❄️ Water/Ice | Fire | Lightning |
| 🔥 Fire | Earth | Water/Ice |
| 🪨 Earth | Lightning | Fire |
| ✨ Holy | Void | Void |
| 🌑 Void | Holy | Holy |

## Mastery Tiers
1. **Apprentice** (0–500 XP) — Basic spells
2. **Adept** (500–2000 XP) — Advanced spells + elemental armor
3. **Mage** (2000–5000 XP) — Passive buffs unlock
4. **Archmage** (5000–10000 XP) — Legendary gear access
5. **Elemental Lord** (10000+ XP) — Full mastery, legendary spells

## Unlocking Holy & Void
Players must:
1. Defeat all **4 elemental bosses**
2. Discover a **hidden Ascension Altar** in the Mystic Grove biome

## Project Structure
```
src/main/java/com/archmage/
├── Archmage.java          # Mod entry point
├── elements/              # ElementType enum + damage calc
├── mastery/               # MasteryData, MasteryTier, MasterySystem
├── spells/                # SpellBase + SpellRegistry
├── items/                 # ElementalStaff + all gear
├── entities/              # Mobs (Will-o-Wisp etc.)
├── bosses/                # ElementalBoss base + 6 boss classes
├── world/                 # Biomes + structures
└── client/                # UI overlays, HUD
```

## Setup
```bash
# Clone and set up (like npm install)
git clone https://github.com/YOUR_USERNAME/archmage.git
cd archmage
./gradlew genIntellijRuns
./gradlew build
```

## Tech Stack
- Minecraft Forge 1.20.1
- Java 17
- Gradle (build tool)
