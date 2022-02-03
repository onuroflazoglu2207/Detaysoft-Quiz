package com.quiz.detaysoftnew.repository;

import java.util.ArrayList;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.quiz.detaysoftnew.model.TodoModel;

public interface TodoRepository extends JpaRepository<TodoModel, Long> {

	@Query(value = "call todosGetByValues(:who,:title,:explan,:success,:date,:and_or)", nativeQuery = true)
	ArrayList<TodoModel> getByValues(@Param("who") String who, @Param("title") String title,
			@Param("explan") String explan, @Param("success") Boolean success, @Param("date") String date,
			@Param("and_or") boolean and_or);

	@Modifying
	@Transactional
	@Query(value = "call todosDeleteByWho(:who)", nativeQuery = true)
	void deleteByWho(@Param("who") String who);

}
