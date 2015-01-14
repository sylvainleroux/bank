package com.sleroux.accountchecker.banquepopulaire.util.extractors;

public interface ContentExtractor<T> {

	boolean extract(String _line);

	T getValue();

	String getName();
}
