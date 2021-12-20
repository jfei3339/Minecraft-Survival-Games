package me.Jon.SurvivalGames;

import net.md_5.bungee.api.ChatColor;

/*
 * Class representing string functions for the server chat.
 */
public class StringFunctions {
	
	/**
	 * Surrounds a word with a certain colored brackets. A stylistic aspect.
	 * 
	 * @param word: the string to surround
	 * @param color: the color that the word should be.
	 * @return the string with num surrounded
	 */
	public static String surround(String word, String color) {
		return ChatColor.DARK_GRAY + "[" + color + word + ChatColor.DARK_GRAY + "] ";
	}

}
