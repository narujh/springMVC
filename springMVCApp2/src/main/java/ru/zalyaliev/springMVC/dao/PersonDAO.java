package ru.zalyaliev.springMVC.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ru.zalyaliev.springMVC.models.Person;

@Component
public class PersonDAO {
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public PersonDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//Передает в форму все строки из таблицы Person
	public List<Person> index() {
		return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
	}
	
	//Делает запрос к базе данных, после чего возвращает строки с подходящим id и передает в форму
	//первую строку из полученного ответа от базы данных
	public Person show(int id) {
		return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id},
				new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
	}
	
	//Записывает строки (человека) данными из формы в таблицу Person
	public void save(Person person) {
		jdbcTemplate.update("INSERT INTO Person VALUES(1, ?, ?, ?)", person.getName(),
				person.getAge(), person.getEmail());
	}
	
	//Обновляет данные (человека) в таблице Person по id
	public void update (int id, Person updatedPerson) {
		jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=? WHERE id=?", updatedPerson.getName(),
				updatedPerson.getAge(), updatedPerson.getEmail(), updatedPerson.getId());
	}
	
	//Удаляет строки (людей) в таблице Person по id
	public void delete(int id) {
		jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
	}
}
