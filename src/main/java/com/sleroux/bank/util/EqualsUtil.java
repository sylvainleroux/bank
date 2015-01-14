package com.sleroux.bank.util;

import java.math.BigDecimal;

public final class EqualsUtil {

	static public boolean areEqual(boolean aThis, boolean aThat) {
		return aThis == aThat;
	}

	static public boolean areEqual(char aThis, char aThat) {
		// System.out.println("char");
		return aThis == aThat;
	}

	static public boolean areEqual(long aThis, long aThat) {
		/*
		 * Implementation Note Note that byte, short, and int are handled by
		 * this method, through implicit conversion.
		 */
		// System.out.println("long");
		return aThis == aThat;
	}

	static public boolean areEqual(float aThis, float aThat) {
		// System.out.println("float");
		return Float.floatToIntBits(aThis) == Float.floatToIntBits(aThat);
	}

	static public boolean areEqual(double aThis, double aThat) {
		// System.out.println("double");
		return Double.doubleToLongBits(aThis) == Double.doubleToLongBits(aThat);
	}

	static public boolean areEqual(BigDecimal aThis, BigDecimal aThat) {
		return aThis.setScale(2).equals(aThat.setScale(2));
	}

	/**
	 * Possibly-null object field.
	 * 
	 * Includes type-safe enumerations and collections, but does not include
	 * arrays. See class comment.
	 */
	static public boolean areEqual(Object aThis, Object aThat) {
		// System.out.println("Object");
		return aThis == null ? aThat == null : aThis.equals(aThat);
	}
}
