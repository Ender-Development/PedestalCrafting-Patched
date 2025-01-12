
// Auto generated groovyscript example file
// MODS_LOADED: pedestalcrafting

log.info 'mod \'pedestalcrafting\' detected, running script'

// Pedestal Crafting:
// Converts a center item into a single output item. Additional inputs can be placed on pedestals around the core. The
// crafting time as well as the particles that show during or after finishing a recipe can be customized.

mods.pedestalcrafting.pedestal_crafting.removeByCenter(item('minecraft:wool'))
mods.pedestalcrafting.pedestal_crafting.removeByInput(item('minecraft:redstone_block'))
mods.pedestalcrafting.pedestal_crafting.removeByOutput(item('minecraft:stick'))
// mods.pedestalcrafting.pedestal_crafting.removeAll()

mods.pedestalcrafting.pedestal_crafting.recipeBuilder()
    .center(item('minecraft:lava_bucket'))
    .output(item('minecraft:obsidian'))
    .ticks(100)
    .register()

mods.pedestalcrafting.pedestal_crafting.recipeBuilder()
    .center(ore('plankWood'))
    .input(ore('stickWood'),item('minecraft:water_bucket'),ore('logWood'))
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


