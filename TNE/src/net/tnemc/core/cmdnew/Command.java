package net.tnemc.core.cmdnew;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 10/9/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

  String name();

  String[] aliases() default {};

  boolean console() default false;

  boolean developer() default false;

  String author() default "creatorfromhell";

  String permission() default "";

  String help() default "Command help coming soon.";
}