
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  14 : 15  @  14 : 16

~ 	public static PropertyEnum<BlockPlanks.EnumType> VARIANT;

> INSERT  12 : 16  @  12

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockPlanks.EnumType>create("variant", BlockPlanks.EnumType.class);
+ 	}
+ 

> CHANGE  4 : 5  @  4 : 5

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  21 : 24  @  21 : 23

~ 			BlockPlanks.EnumType[] types = BlockPlanks.EnumType.META_LOOKUP;
~ 			for (int i = 0; i < types.length; ++i) {
~ 				list.add(new ItemStack(item, 1, types[i].getMetadata()));

> EOF
