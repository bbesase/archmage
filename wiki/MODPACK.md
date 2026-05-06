# Archmage Modpack — Mod List & Integration Notes

Minecraft 1.20.1 | Forge

---

## Spell & Magic System

| Mod | Role | Archmage Integration |
|-----|------|----------------------|
| [Iron's Spells 'n Spellbooks](https://www.curseforge.com/minecraft/mc-mods/irons-spells-n-spellbooks) | Core casting engine — mana, spellbooks, cast animations | Archmage spells extend `AbstractSpell`; mastery system hooks into cast events via Forge event bus |
| [Spell Engine](https://www.curseforge.com/minecraft/mc-mods/spell-engine) | Shared spell API used by Iron's Spells | Transitive dependency, no direct integration needed |
| [To Magic'n Extras](https://www.curseforge.com/minecraft/mc-mods/to-magic-n-extras) | Extra spells/content for Iron's Spells | Compatible out of the box; no conflicts expected |

---

## RPG & Progression

| Mod | Role | Archmage Integration |
|-----|------|----------------------|
| [Origins](https://www.curseforge.com/minecraft/mc-mods/origins) | Player origin/class selection at game start | Potential: tie element selection to Origins choice (e.g. Pyromancer origin = Fire start) |
| [Apotheosis](https://www.curseforge.com/minecraft/mc-mods/apotheosis) | Enchanting overhaul, gems, bosses | Archmage legendary gear should be designed around Apotheosis gem sockets |
| [PlayerEx](https://www.curseforge.com/minecraft/mc-mods/playerex) | RPG attribute system (strength, dex, etc.) | Archmage mastery tiers could scale off PlayerEx attributes or grant attribute bonuses on tier-up |
| [Armor Runes](https://www.curseforge.com/minecraft/mc-mods/armor-runes) | Runic enchantments for armor | Elemental runes fit the Archmage theme — no code changes needed, works naturally |
| [Curios API](https://www.curseforge.com/minecraft/mc-mods/curios) | Extra equipment slots (rings, amulets, etc.) | **Required** — Archmage rings/amulets must use Curios slots, not vanilla inventory |

---

## World Generation

| Mod | Role | Archmage Integration |
|-----|------|----------------------|
| [Terralith](https://www.curseforge.com/minecraft/mc-mods/terralith) | Biome overhaul | Archmage's Mystic Grove biome must be configured to spawn alongside Terralith biomes |
| [When Dungeons Arise](https://www.curseforge.com/minecraft/mc-mods/when-dungeons-arise) | Large dungeon structures | Good loot table candidates for elemental spell scrolls |
| [Yung's Better Dungeons](https://www.curseforge.com/minecraft/mc-mods/yungs-better-dungeons) | Vanilla dungeon overhaul | Same as above — add Archmage loot injection via loot table JSON |

---

## Combat

| Mod | Role | Archmage Integration |
|-----|------|----------------------|
| [Better Combat](https://www.curseforge.com/minecraft/mc-mods/better-combat) | Weapon attack animations and combos | Archmage melee weapons (elemental swords/axes) need Better Combat weapon config JSONs |
| [Malice and Mischief](https://www.curseforge.com/minecraft/mc-mods/malice-and-mischief) | Extra bosses, enemies, accessories | Adds boss variety that complements Archmage's boss progression; no direct integration needed |

---

## Mobs & Content

| Mod | Role | Archmage Integration |
|-----|------|----------------------|
| [AzureLib](https://www.curseforge.com/minecraft/mc-mods/azurelib) | Animation engine (successor to GeckoLib) | **Required** — Archmage's custom mobs (Will-o-Wisp, Void Wraith, etc.) use AzureLib for animations |

---

## UI & Quality of Life

| Mod | Role | Archmage Integration |
|-----|------|----------------------|
| [JEI](https://www.curseforge.com/minecraft/mc-mods/jei) | Recipe viewer | Archmage recipes auto-appear in JEI — add JEI plugin later for mastery unlock display |
| [Jade](https://www.curseforge.com/minecraft/mc-mods/jade) | Block/entity tooltips on hover | Add Jade plugin to show element type and mastery level on Archmage mobs |
| [Waystones](https://www.curseforge.com/minecraft/mc-mods/waystones) | Fast travel | No integration needed — just QoL for large exploration world |
| [Balm](https://www.curseforge.com/minecraft/mc-mods/balm) | Waystones dependency | Transitive, no action needed |
| [FTB Quests](https://www.curseforge.com/minecraft/mc-mods/ftb-quests) | Quest/progression guide system | **High value** — build an Archmage quest line that walks players through element mastery, boss order, and prestige unlock |
| [Xaero's Minimap / World Map](https://www.curseforge.com/minecraft/mc-mods/xaeros-minimap) | Minimap and world map | No integration needed |

---

## Performance

| Mod | Role |
|-----|------|
| [Embeddium](https://www.curseforge.com/minecraft/mc-mods/embeddium) | Sodium-equivalent rendering optimization (Forge port) |
| [FerriteCore](https://www.curseforge.com/minecraft/mc-mods/ferritecore) | Memory usage reduction |
| [ModernFix](https://www.curseforge.com/minecraft/mc-mods/modernfix) | Bug fixes and load time improvements |
| [ImmediatelyFast](https://www.curseforge.com/minecraft/mc-mods/immediatelyfast) | Render batch optimization |

---

## Integration Priority

These are the integrations that need code written in Archmage (in priority order):

1. **Iron's Spells** — extend `AbstractSpell` for all Archmage spells *(blocks spell development)*
2. **Curios API** — register Archmage accessory slots *(blocks item development)*
3. **AzureLib** — set up animation rig for first custom mob *(blocks mob development)*
4. **Better Combat** — add weapon config JSONs for elemental melee weapons
5. **FTB Quests** — build quest line *(do last, once content exists)*
6. **JEI / Jade** — add plugins for mastery display *(polish pass)*
