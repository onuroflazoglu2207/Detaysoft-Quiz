package com.quiz.detaysoftnew.controller;

import java.util.ArrayList;
import java.util.Map;

public interface ControllerInterface {

	public Iterable<Object> getAll();

	public ArrayList<Object> getByIdentity(Iterable<Map<String, String>> maps);

	public Object getByIdentity(Map<String, String> map);

	public Object getByIdentity(String identity);

	public Object[] getByValuesAnd(Map<String, String> map);

	public Object[] getByValuesOr(Map<String, String> map);

	public Object[] getByValues(Map<String, String> map, boolean and_or);

	public ArrayList<Object> insert(Iterable<Map<String, String>> maps);

	public Object insert(Map<String, String> map);

	public Object deleteAll();

	public ArrayList<Object> delete(Iterable<Map<String, String>> maps);

	public Object delete(Map<String, String> map);

	public Object delete(String identity);

	public ArrayList<Object> update(Iterable<Map<String, String>> maps);

	public Object update(Map<String, String> map);

	public Object update(String identity, Map<String, String> map);

	public Object size();
}
