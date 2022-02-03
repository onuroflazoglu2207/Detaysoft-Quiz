package com.quiz.detaysoftnew.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.quiz.detaysoftnew.model.TodoModel;
import com.quiz.detaysoftnew.repository.TodoRepository;
import com.quiz.detaysoftnew.repository.UserRepository;

public abstract class GlobalController<T> implements ControllerInterface {

	public static final String dateNow = "now";
	public static final String identityText = "identity";
	public static final String newIdentityText = "newIdentity";

	public static TodoRepository todoRepository;
	public static UserRepository userRepository;
	@Autowired
	private final JpaRepository<T, Long> repository;

	public GlobalController(JpaRepository<T, Long> repository) {
		this.repository = repository;
	}

	@Override
	@GetMapping("/getAll")
	@SuppressWarnings("unchecked")
	public Iterable<Object> getAll() {
		return (Iterable<Object>) repository.findAll();
	}

	@Override
	@GetMapping("/getByIdentitys")
	public ArrayList<Object> getByIdentity(@RequestBody Iterable<Map<String, String>> maps) {
		return stateRepeater(maps, new MapAdapter() {
			public Object actionPerformed(Map<String, String> map) {
				return getByIdentity(map);
			}
		});
	}

	@Override
	@GetMapping("/getByIdentity")
	public Object getByIdentity(@RequestBody Map<String, String> map) {
		return isHaveValue(map, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return getByIdentity(object.toString());
			}
		}, mapNotHave, identityText);

	}

	@Override
	@GetMapping("/getByIdentity/{identity}")
	public Object getByIdentity(@PathVariable String identity) {
		return isFindIdentity(identity, new MapAdapter(), mapNotFound, mapUnacceptable);
	}

	@Override
	@GetMapping("/getByValuesAnd")
	public Object[] getByValuesAnd(@RequestBody Map<String, String> map) {
		return getByValues(map, true);
	}

	@Override
	@GetMapping("/getByValuesOr")
	public Object[] getByValuesOr(@RequestBody Map<String, String> map) {
		return getByValues(map, false);
	}

	@Override
	public abstract Object[] getByValues(Map<String, String> map, boolean and_or);

	@Override
	@PutMapping("/inserts")
	public ArrayList<Object> insert(@RequestBody Iterable<Map<String, String>> maps) {
		return stateRepeater(maps, new MapAdapter() {
			public Object actionPerformed(Map<String, String> map) {
				return insert(map);
			}
		});
	}

	@Override
	@PutMapping("/insert")
	public abstract Object insert(Map<String, String> map);

	@Override
	@DeleteMapping("/deleteAll")
	public Object deleteAll() {
		repository.deleteAll();
		return mapMaker(false, textDeleteFromDatabase(allData));
	}

	@Override
	@DeleteMapping("/deletes")
	public ArrayList<Object> delete(@RequestBody Iterable<Map<String, String>> maps) {
		return stateRepeater(maps, new MapAdapter() {
			public Object actionPerformed(Map<String, String> map) {
				return delete(map);
			}
		});
	}

	@Override
	@DeleteMapping("/delete")
	public Object delete(@RequestBody Map<String, String> map) {
		return isHaveValue(map, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return delete(object.toString());
			}
		}, mapNotHave, identityText);
	}

	@Override
	@DeleteMapping("/delete/{identity}")
	public abstract Object delete(String identity);

	@Override
	@PutMapping("updates")
	public ArrayList<Object> update(@RequestBody Iterable<Map<String, String>> maps) {
		return stateRepeater(maps, new MapAdapter() {
			public Object actionPerformed(Map<String, String> map) {
				return update(map);
			}
		});
	}

	@Override
	@PutMapping("update")
	public Object update(@RequestBody Map<String, String> map) {
		return isHaveValue(map, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return update(object.toString(), map);
			}
		}, mapNotHave, identityText);
	}

	@Override
	@PutMapping("update/{identity}")
	public abstract Object update(String identity, Map<String, String> map);

	@Override
	@GetMapping("/size")
	public Object size() {
		return mapMaker(true, "size : " + String.valueOf(repository.findAll().size()));
	}

	public ArrayList<Object> stateRepeater(Iterable<Map<String, String>> maps, MapAdapter listener) {
		ArrayList<Object> objects = new ArrayList<Object>();
		for (Map<String, String> map : maps)
			objects.add(listener.actionPerformed(map));
		return objects;
	}

	public Object isHaveValue(Map<String, String> map, MapAdapter have, MapAdapter notHave, String value) {
		if (map == null)
			return notHave.actionPerformed(value);
		String str = map.get(value);
		if (str != null)
			return have.actionPerformed(str);
		else
			return notHave.actionPerformed(value);
	}

	public Object isFindIdentity(String identity, MapAdapter found, MapAdapter notFound, MapAdapter unaccept) {
		if (identity == null || (identity.length() > String.valueOf(Long.MAX_VALUE).length()
				|| !identity.chars().allMatch(Character::isDigit)))
			return unaccept.actionPerformed(identity);
		Optional<T> optional = repository.findById(Long.valueOf(identity));
		if (optional != null && !optional.isEmpty())
			return found.actionPerformed(optional.get());
		else
			return notFound.actionPerformed(identity);
	}

	public static String[] valueMaker(String[] values, Map<String, String> map) {
		String[] array = new String[values.length];
		for (int i = 0; i < values.length; i++)
			array[i] = map.get(values[i]);
		return array;
	}

	public static Map<String, String> mapMaker(boolean message, String value) {
		Map<String, String> hashmap = new HashMap<String, String>();
		hashmap.put(message ? "message" : "error", value);
		return hashmap;
	}

	public static TodoModel createTodoModel(String who, String title, String explan, String success, String date) {
		return new TodoModel(who, title, explan, success == null ? Boolean.FALSE : Boolean.valueOf(success),
				date == null ? null
						: date.equals(dateNow) ? LocalDateTime.now()
								: LocalDateTime.parse(date, DateTimeFormatter.ofPattern(TodoModel.dateFormat)));
	}

	public static final String allData = "All data";
	public static final String differentIdentity = "different Identity";

	public static final String userUpdated = "User is updated!";
	public static final String newUserCreated = "New user is created!";
	public static final String errorInValues = "Have an error in values!";
	public static final String notFoundOrUnaccept = "Not found or unacceptable value!";

	public static final String textNotFound(String value) {
		return value + " is not found!";
	}

	public static final String textNotHave(String value) {
		return "Not have " + value + " value!";
	}

	public static final String textUnacceptable(String value) {
		return value + " is unacceptable value!";
	}

	public static final String textNotSet(String value) {
		return "You cannot set " + value + " value!";
	}

	public static final String textIsHaveDatabase(String value) {
		return value + " is have in database!";
	}

	public static final String textDeleteFromDatabase(String value) {
		return value + " is deleted from database!";
	}

	public static final MapAdapter mapNotFound = new MapAdapter() {
		public Object actionPerformed(Object object) {
			return mapMaker(false, textNotFound(object.toString()));
		}
	};

	public static final MapAdapter mapNotHave = new MapAdapter() {
		public Object actionPerformed(Object object) {
			return mapMaker(false, textNotHave(object.toString()));
		}
	};

	public static final MapAdapter mapUnacceptable = new MapAdapter() {
		public Object actionPerformed(Object object) {
			return mapMaker(false, textUnacceptable(object.toString()));
		}
	};

	public static final MapAdapter mapNotSet = new MapAdapter() {
		public Object actionPerformed(Object object) {
			return mapMaker(false, textNotSet(object.toString()));
		}
	};

}