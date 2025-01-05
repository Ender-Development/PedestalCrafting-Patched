
// Auto generated groovyscript example file
// MODS_LOADED: pedestalcrafting

log.info 'mod \'pedestalcrafting\' detected, running script'

// Pedestal Crafting:
// Converts a number of 2+ inputs into a single output item. The recipe can be customized with various particles.

mods.pedestalcrafting.pedestal_crafting.removeByInput(item('minecraft:redstone_block'))
mods.pedestalcrafting.pedestal_crafting.removeByOutput(item('minecraft:stick'))
// mods.pedestalcrafting.pedestal_crafting.removeAll()

mods.pedestalcrafting.pedestal_crafting.recipeBuilder()
    .center(ore('oreIron'))
    .input(ore('stickWood'),ore('plankWood'),ore('logWood'),item('minecraft:stick'))
    .output(item('minecraft:diamond'))
    .ticks(100)
    .register()

mods.pedestalcrafting.pedestal_crafting.recipeBuilder()
    .center(ore('oreGold'))
    .input(item('minecraft:chest'),item('minecraft:piston'))
    .output(item('minecraft:emerald'))
    .ticks(100)
    .addCraftingParticle('fireworkSpark', 10)
    .register()

mods.pedestalcrafting.pedestal_crafting.recipeBuilder()
    .center(ore('oreDiamond'))
    .input(item('minecraft:hopper'),item('minecraft:chest'))
    .output(item('minecraft:stone'))
    .ticks(100)
    .addCraftingParticle('bubble', 10)
    .addPostCraftCoreParticle('suspended', 10)
    .register()

mods.pedestalcrafting.pedestal_crafting.recipeBuilder()
    .center(ore('oreRedstone'))
    .input(item('minecraft:cobblestone'), ore('ingotGold'))
    .output(item('minecraft:redstone'))
    .ticks(100)
    .addCraftingParticle('instantSpell', 10)
    .addPostCraftCoreParticle('dripLava', 10)
    .addPostCraftPedestalParticle('portal', 10)
    .register()


