package main.util;

import java.lang.reflect.Array;
import java.util.Random;

public class ArrayUtils {
	private static Random r = new Random(System.nanoTime());
	
	@SuppressWarnings("unchecked")
	public static <T> T[] filterZeros (T[] arr, Class<T> clazz) {
		if (arr == null || containsOnlyNull(arr)) {
			return null;
		}
		int index = 0;
		T[] new_arr = (T[]) Array.newInstance(clazz, arr.length);
		
		
		for (T t : arr) {
			if (t != null) {
				new_arr[index++] = arr[index];
			}
		}
		
		return new_arr;
	}
	
	
	public static <T> T randomObject (T[] arr) {
		if (arr == null || containsOnlyNull(arr)) {
			return null;
		}
		T o;
		while ((o = arr[r.nextInt(arr.length)]) == null);
		return o;
		
	}

	public static boolean containsOnlyNull (Object[] arr) {
		if (arr == null) {
			return true;
		}
		
		int nulls = 0;
		for (Object a : arr) {
			if (a == null) {
				nulls++;
			}
		}
		
		if (nulls == arr.length) {
			return true;
		}
		return false;
	}
	
	public static boolean isnotin (Object[] array, Object obj) {
		for (Object o : array) {
			if (o != null && o.equals(obj)) {
				return false;
			}
		}
		return true;
	}
}
