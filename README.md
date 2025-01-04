## Pedestal Crafting - Patched

This is a fork of [Pedestal Crafting](https://www.curseforge.com/minecraft/mc-mods/pedestal-crafting) by [Axieum](https://www.curseforge.com/members/axieum). It provides a few bug fixes and improvements to the original mod as it hasn't been updated since 2018-06-06.

<a href="https://www.akliz.net/enderman"><img src="https://github.com/Ender-Development/PatchouliBooks/raw/master/banner.png" align="center"/></a>

## Changes
### Bugfixes
- [Crash when loading on a server](https://github.com/axieum/PedestalCrafting/issues/22)
- [Adding multiple particle effects to the core/pedestals after crafting only shows one](https://github.com/axieum/PedestalCrafting/issues/5)
- [Grass Pedestal has grey texture](https://github.com/axieum/PedestalCrafting/issues/3)

### Improvements
- [Add support for ingredient transformations](https://github.com/axieum/PedestalCrafting/issues/4)
- [Locking recipes to only accept certain pedestal variants](https://github.com/axieum/PedestalCrafting/issues/2)

### More Localization
- Indonesian, Turkish, Hindi, Dutch, German, Swedish, Arabic, Italian

## Description

Pedestals can be crafted, and placed within a small area around a Pedestal Core. Items to be infused will be placed on the pedestals, while the item to be transformed will be placed on the core. Pedestals are safe for use as decorations, while the Pedestal Core is not.

## Compatibility

Features 100% support for all: WAILA/HWYLA, The One Probe, Just Enough Items and Craft Tweaker.

## Modpacks

Are you a modpack creator, or looking to manipulate the recipes? Read ahead!

This mod is intended for modpack creators, as it provides a fancy mean of crafting. It has full recipe tweaking through the use the commonly known CraftTweaker mod and the syntax is as follows:

```
mods.pedestalcrafting.Pedestal.addRecipe(output, ticks, coreItem, inputItems[]);

mods.pedestalcrafting.Pedestal.addRecipe(output, ticks, coreItem, inputItems[], particlesCrafting[], particlesPostCraftCore[], particlesPostCraftPedestal[]);
```

By using the second statement, you are able to override the particles that are spawned during each crafting phase. Particles Crafting are the particles spawned at the core, for the full duration of the crafting process, while particles post crafting core, are the particles that are spawned on the core after a successful craft, and finally the particles post crafting pedestal are the particles spawned on the pedestals after a successful craft as the items are removed.

An example script that adds a standard recipe, using default particles is as follows (will produce, a bedrock block, that takes 5 seconds; placing a granite block on the core, and surrounding with a sand block, iron ingot, gold ingot and an obsidian block):

```
mods.pedestalcrafting.Pedestal.addRecipe(<minecraft:bedrock>, 100, <minecraft:stone:1>, [
    <minecraft:sand>,
    <ore:ingotIron>,
    <ore:ingotGold>,
    <minecraft:obsidian>
]);
```

Another example, that features overriding the particles, is as follows (particle name of "endRod" with a count of "2" will spawn at the core during crafting; particle with name "portal" and count of "50" will spawn at the core after crafting; particle with name "smoke" and count "25" and also a chance of "flame" with count "5" will spawn at the pedestals after crafting):

```
mods.pedestalcrafting.Pedestal.addRecipe(<minecraft:apple>, 100, <minecraft:diamond>, [
    <ore:ingotIron>,
    <ore:ingotIron>,
    <ore:ingotIron>,
    <ore:ingotIron>,
    <ore:ingotIron>
], [["endRod", "2"]], [["portal", "50"]], [["smoke", "25"], ["flame", "5"]]);
```

Note, not specifying a particle count will default to 3. For valid particle names, see: https://minecraft.gamepedia.com/Particles

This mod was heavily influenced by ExtendedCrafting by BlakeBr0, and many references are made to his source code in this mod.

## [Ender-Development](https://github.com/Ender-Development)

Our Team currently includes:
- `_MasterEnderman_` - Project-Manager, Developer
- `Klebestreifen` - Developer

You can contact us on our [Discord](https://discord.gg/JF7x2vG).

## Contributing
Feel free to contribute to the project. We are always happy about pull requests.
If you want to help us, you can find potential tasks in the [issue tracker](https://github.com/Ender-Development/PatchouliBooks/issues).
Of course, you can also create new issues if you find a bug or have a suggestion for a new feature.
Should you have any questions, feel free to ask us on [Discord](https://discord.gg/JF7x2vG).

## Partnership with Akliz

> It's a pleasure to be partnered with Akliz. Besides being a fantastic server provider, which makes it incredibly easy to set up a server of your choice, they help me to push myself and the quality of my projects to the next level. Furthermore, you can click on the banner below to get a discount. :')

<a href="https://www.akliz.net/enderman"><img src="https://github.com/MasterEnderman/Zerblands-Remastered/raw/master/Akliz_Partner.png" align="center"/></a>

If you aren't located in the [US](https://www.akliz.net/enderman), Akliz now offers servers in:

- [Europe](https://www.akliz.net/enderman-eu)
- [Oceania](https://www.akliz.net/enderman-oce)