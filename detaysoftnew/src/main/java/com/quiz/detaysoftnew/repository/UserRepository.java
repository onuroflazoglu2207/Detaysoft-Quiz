package com.quiz.detaysoftnew.repository;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.quiz.detaysoftnew.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {

	@Query(value = "call usersGetByValues(:name,:phone,:email,:password,:and_or)", nativeQuery = true)
	ArrayList<UserModel> getByValues(@Param("name") String name, @Param("phone") String phone,
			@Param("email") String email, @Param("password") String password, @Param("and_or") boolean and_or);

}
