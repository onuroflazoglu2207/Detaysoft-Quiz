package com.quiz.detaysoftnew.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quiz.detaysoftnew.model.TodoModel;
import com.quiz.detaysoftnew.repository.TodoRepository;

@RestController
@RequestMapping(value = "/todos")
public class TodoController extends GlobalController<TodoModel> {

	@Autowired
	private UserController userController;
	@Autowired
	private TodoRepository todoRepository;

	public TodoController() {
		super(GlobalController.todoRepository);
	}

	@Override
	public Object[] getByValues(Map<String, String> map, boolean and_or) {
		return (Object[]) isHaveValue(map, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return new Object[] { mapNotSet.actionPerformed(TodoModel.values[0]) };
			}
		}, new MapAdapter() {
			public Object actionPerformed(Object object) {
				try {
					String[] values = valueMaker(TodoModel.values, map);
					String success = map.get(values[4]);
					return todoRepository.getByValues(values[1], values[2], values[3],
							success == null ? null : Boolean.valueOf(success), values[5], and_or).toArray();
				} catch (Exception e) {
					return new Object[] { mapMaker(false, errorInValues) };
				}
			}
		}, TodoModel.values[0]);
	}

	@GetMapping("/getByWhos")
	public Object getByWho(@RequestBody Iterable<Map<String, String>> maps) {
		return stateRepeater(maps, new MapAdapter() {
			public Object actionPerformed(Map<String, String> map) {
				return getByWho(map);
			}
		});
	}

	@GetMapping("/getByWho")
	public Object getByWho(@RequestBody Map<String, String> map) {
		return isHaveValue(map, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return getByWho(object.toString());
			}
		}, mapNotHave, TodoModel.values[1]);
	}

	@GetMapping("/getByWho/{who}")
	public Object getByWho(@PathVariable String who) {
		return userController.isFindIdentity(who, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return todoRepository.getByValues(who, null, null, null, null, false).toArray();
			}
		}, mapNotFound, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return mapUnacceptable.actionPerformed(TodoModel.values[1]);
			}
		});
	}

	@Override
	public Object insert(@RequestBody Map<String, String> map) {
		return isHaveValue(map, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return mapNotSet.actionPerformed(identityText);
			}
		}, new MapAdapter() {
			public Object actionPerformed(Object object) {
				try {
					String[] values = valueMaker(TodoModel.values, map);
					return userController.isFindIdentity(values[1], new MapAdapter() {
						public Object actionPerformed(Object object) {
							return todoRepository
									.save(createTodoModel(values[1], values[2], values[3], values[4], values[5]));
						}
					}, mapNotFound, new MapAdapter() {
						public Object actionPerformed(Object object) {
							return mapNotHave.actionPerformed(TodoModel.values[1]);
						}
					});
				} catch (Exception e) {
					return mapMaker(false, notFoundOrUnaccept);
				}
			}
		}, identityText);
	}

	@Override
	public Object delete(@PathVariable String identity) {
		Object object = getByIdentity(identity);
		if (object instanceof TodoModel) {
			todoRepository.deleteById(Long.valueOf(identity));
			return mapMaker(false, textDeleteFromDatabase(identity));
		} else
			return object;
	}

	@DeleteMapping("/deleteByWhos")
	public Object deleteByWho(@RequestBody Iterable<Map<String, String>> maps) {
		return stateRepeater(maps, new MapAdapter() {
			public Object actionPerformed(Map<String, String> map) {
				return deleteByWho(map);
			}
		});
	}

	@DeleteMapping("/deleteByWho")
	public Object deleteByWho(@RequestBody Map<String, String> map) {
		return isHaveValue(map, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return deleteByWho(object.toString());
			}
		}, mapNotHave, TodoModel.values[1]);
	}

	@DeleteMapping("/deleteByWho/{who}")
	public Object deleteByWho(@PathVariable String who) {
		return userController.isFindIdentity(who, new MapAdapter() {
			public Object actionPerformed(Object object) {
				todoRepository.deleteByWho(who);
				return mapMaker(true, textDeleteFromDatabase(who));
			}
		}, mapNotFound, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return mapUnacceptable.actionPerformed(TodoModel.values[1]);
			}
		});
	}

	@Override
	public Object update(@PathVariable String identity, @RequestBody Map<String, String> map) {
		return isHaveValue(map, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return mapNotSet.actionPerformed(newIdentityText);
			}
		}, new MapAdapter() {
			public Object actionPerformed(Object object) {
				String identityInRequest = map.get(identityText);
				if (identityInRequest != null && !identityInRequest.equals(identity))
					return mapNotSet.actionPerformed(differentIdentity);
				return isFindIdentity(identity, new MapAdapter() {
					public Object actionPerformed(Object object) {
						try {
							TodoModel model = (TodoModel) object;
							String[] values = valueMaker(TodoModel.values, map);
							TodoModel temp = createTodoModel(values[1], values[2], values[3], values[4], values[5]);
							if (temp.getWho() != null)
								model.setWho(temp.getWho());
							model.setTitle(temp.getTitle());
							model.setExplan(temp.getExplan());
							if (temp.getSuccess() != null)
								model.setSuccess(temp.getSuccess());
							model.setDate(temp.getDate());
							return todoRepository.save(model);
						} catch (Exception e) {
							return mapMaker(false, errorInValues);
						}
					}
				}, mapNotFound, mapUnacceptable);
			}
		}, newIdentityText);
	}
}