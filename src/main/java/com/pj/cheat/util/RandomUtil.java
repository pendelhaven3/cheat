package com.pj.cheat.util;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;

public class RandomUtil {

	public static <T> T getRandomElement(List<T> list) {
		return list.get(RandomUtils.nextInt(0, list.size()));
	}
	
}