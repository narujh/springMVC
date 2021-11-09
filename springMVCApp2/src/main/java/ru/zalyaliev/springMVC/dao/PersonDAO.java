package ru.zalyaliev.springMVC.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ru.zalyaliev.springMVC.models.Person;

@Component
public class PersonDAO {
	private static final String URL = "jdbc:postgresql://localhost:5432/first_db";	
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "postgres";
	
	private static Connection connection;
	
	//Производит подключение к драйверу и базе данных
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Передает в форму все строки из таблицы Person
	public List<Person> index() {
		List<Person> people = new ArrayList<>();
		
		try {
			Statement statement = connection.createStatement();
			String SQL = "SELECT * FROM Person";
			ResultSet resultSet = statement.executeQuery(SQL);
			
			while (resultSet.next()) {
				Person person = new Person();
				
				person.setId(resultSet.getInt("id"));
				person.setName(resultSet.getString("name"));
				person.setEmail(resultSet.getString("email"));
				person.setAge(resultSet.getInt("age"));
				
				people.add(person);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return people;
	}
	
	//Делает запрос к базе данных, после чего возвращает строки с подходящим id и передает в форму
	//первую строку из полученного ответа от базы данных
	public Person show(int id) {
		Person person = null;
		try {
			PreparedStatement preparedStatement = 
					connection.prepareStatement("SELECT * FROM Person WHERE id=?");
			
			preparedStatement.setInt(1, id);
			
			ResultSet resultSet =  preparedStatement.executeQuery();
			resultSet.next();
			
			person = new Person();
			
			person.setId(id);
			person.setName(resultSet.getString("name"));
			person.setEmail(resultSet.getString("email"));
			person.setAge(resultSet.getInt("age"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return person;
	}
	
	//Записывает строки (человека) данными из формы в таблицу Person
	public void save(Person person) {
		try {
			PreparedStatement preparedStatement = 
					connection.prepareStatement("INSERT INTO Person VALUES(1, ?, ?, ?)"); 
			
			preparedStatement.setString(1, person.getName());
			preparedStatement.setInt(2, person.getAge());
			preparedStatement.setString(3, person.getEmail());
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Обновляет данные (человека) в таблице Person по id
	public void update (int id, Person updatedPerson) {		
		try {
			PreparedStatement preparedStatement =
					connection.prepareStatement("UPDATE Person SET name=?, age=?, email=? WHERE id=?");
			
			preparedStatement.setString(1, updatedPerson.getName());
			preparedStatement.setInt(2, updatedPerson.getAge());
			preparedStatement.setString(3, updatedPerson.getEmail());
			preparedStatement.setInt(4, id);
			
			preparedStatement.executeUpdate();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Удаляет строки (людей) в таблице Person по id
	public void delete(int id) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Person WHERE id=?");
			
			preparedStatement.setInt(1, id);
			
			preparedStatement.executeUpdate();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		people.removeIf(p -> p.getId() == id);
	}
}
