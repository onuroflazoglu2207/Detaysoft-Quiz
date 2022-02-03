package com.quiz.detaysoftnew.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quiz.detaysoftnew.model.UserModel;
import com.quiz.detaysoftnew.repository.TodoRepository;
import com.quiz.detaysoftnew.repository.UserRepository;

@RestController
@RequestMapping(value = "/users")
public class UserController extends GlobalController<UserModel> {

	@Autowired
	private TodoRepository todoRepository;
	@Autowired
	private UserRepository userRepository;

	public UserController() {
		super(GlobalController.userRepository);
	}

	@Override
	public Object[] getByValues(Map<String, String> map, boolean and_or) {
		return (Object[]) isHaveValue(map, new MapAdapter() {
			public Object actionPerformed(Object object) {
				return new Object[] { mapNotSet.actionPerformed(UserModel.values[0]) };
			}
		}, new MapAdapter() {
			public Object actionPerformed(Object object) {
				try {
					String[] values = valueMaker(UserModel.values, map);
					return userRepository.getByValues(values[1], values[2], values[3], values[4], and_or).toArray();
				} catch (Exception e) {
					return new Object[] { mapMaker(false, errorInValues) };
				}
			}
		}, UserModel.values[0]);
	}

	@Override
	public Object insert(@RequestBody Map<String, String> map) {
		return isHaveValue(map, new MapAdapter() {
			public Object actionPerformed(Object identity) {
				return isFindIdentity(identity.toString(), new MapAdapter() {
					public Object actionPerformed(Object object) {
						return mapMaker(true, textIsHaveDatabase(((UserModel) object).getIdentity().toString()));
					}
				}, new MapAdapter() {
					public Object actionPerformed(Object object) {
						try {
							String[] values = valueMaker(UserModel.values, map);
							userRepository.save(
									new UserModel(Long.valueOf(values[0]), values[1], values[2], values[3], values[4]));
							return mapMaker(true, newUserCreated);
						} catch (Exception e) {
							return mapMaker(false, notFoundOrUnaccept);
						}
					}
				}, mapUnacceptable);
			}
		}, mapNotHave, identityText);
	}

	@Override
	public Object deleteAll() {
		userRepository.deleteAll();
		todoRepository.deleteAll();
		return mapMaker(true, textDeleteFromDatabase(allData));
	}

	@Override
	public Object delete(@PathVariable String identity) {
		return isFindIdentity(identity, new MapAdapter() {
			public Object actionPerformed(Object object) {
				String who = identity;
				System.out.println(who);
				todoRepository.deleteByWho(who);
				userRepository.deleteById(Long.valueOf(identity));
				return mapMaker(true, textDeleteFromDatabase(identity));
			}
		}, mapNotFound, mapUnacceptable);
	}

	@Override
	public Object update(@PathVariable String identity, @RequestBody Map<String, String> map) {
		return isFindIdentity(identity, new MapAdapter() {
			public Object actionPerformed(Object object) {
				Object identityIsHave = isHaveValue(map, new MapAdapter() {
					public Object actionPerformed(Object object) {
						return identity.equals(object.toString()) ? null : object;
					}
				}, new MapAdapter() {
					public Object actionPerformed(Object object) {
						return null;
					}
				}, identityText);
				if (identityIsHave != null)
					return mapNotSet.actionPerformed(differentIdentity);
				Object newIdentityModel = isHaveValue(map, new MapAdapter() {
					public Object actionPerformed(Object newIdentity) {
						return isFindIdentity(newIdentity.toString(), new MapAdapter() {
							public Object actionPerformed(Object object) {
								if (identity.equals(newIdentity.toString()))
									return null;
								return mapMaker(false, textIsHaveDatabase(newIdentity.toString()));
							}
						}, new MapAdapter() {
							public Object actionPerformed(Object object) {
								return newIdentity;
							}
						}, mapUnacceptable);
					}
				}, new MapAdapter() {
					public Object actionPerformed(Object object) {
						return null;
					}
				}, newIdentityText);
				try {
					UserModel model = (UserModel) object;
					String[] values = valueMaker(UserModel.values, map);
					UserModel temp = new UserModel(null, values[1], values[2], values[3], values[4]);
					if (newIdentityModel != null)
						if (newIdentityModel instanceof String)
							temp.setIdentity(Long.valueOf(newIdentityModel.toString()));
						else
							return newIdentityModel;
					else
						temp.setIdentity(model.getIdentity());
					if (temp.getName() == null)
						temp.setName(model.getName());
					if (temp.getPassword() == null)
						temp.setPassword(model.getPassword());
					userRepository.save(temp);
					if (newIdentityModel != null)
						userRepository.deleteById(Long.valueOf(identity.toString()));
					return mapMaker(true, userUpdated);
				} catch (Exception e) {
					return mapMaker(false, errorInValues);
				}
			}
		}, mapNotFound, mapUnacceptable);
	}

}
