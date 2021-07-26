package net.tnemc.core.common.menu.design.icon;

import net.tnemc.core.common.menu.consumables.IconClickListener;

import java.util.function.Consumer;

public interface IconType {

  String name();

  Consumer<IconClickListener> onClick();
}