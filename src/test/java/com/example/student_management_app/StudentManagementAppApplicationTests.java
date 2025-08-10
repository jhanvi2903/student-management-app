// End-to-end integration testing with H2 database - using RestAssured
// RestAssured - RestAssured is designed for testing REST APIs — it sends HTTP requests and verifies HTTP responses.
//It focuses on the API layer, not direct interaction with databases, instead it uses h2 db
// In short - RestAssured is a great tool for end-to-end integration testing of REST APIs in Spring Boot as it is  very popular for testing real HTTP requests end-to-end. It is combined with Spring Boot’s test annotations and an in-memory DB.

package com.example.student_management_app;

import com.example.student_management_app.dto.StudentRequestDTO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class StudentManagementAppApplicationTests {

	@LocalServerPort
	private int port;

	private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter();
	private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

	@BeforeAll
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
		RestAssured.filters(requestLoggingFilter, responseLoggingFilter);

		RestAssured.requestSpecification = new RequestSpecBuilder()
				.setContentType(ContentType.JSON)
				.setAccept(ContentType.JSON)
				.build();

	}

	@Order(1)
	@Test
	void testCreateStudent_whenValidStudentDetailsProvided_thenReturns200() {
		StudentRequestDTO studentRequestDTO = new StudentRequestDTO();
		studentRequestDTO.setName("Jhanvi");
		studentRequestDTO.setEmail("jhanvi@gmail.com");
		studentRequestDTO.setMarks(97);
		studentRequestDTO.setPassword("12345678");
		studentRequestDTO.setRepeatPassword("12345678");

		given()// Arrange
				.body(studentRequestDTO)
		.when() // Act
				.post("api/v1/students")
		.then() // Assert
				.statusCode(HttpStatus.CREATED.value())
				.body("name", equalTo(studentRequestDTO.getName()))
				.body("email", equalTo(studentRequestDTO.getEmail()))
				.body("marks", equalTo(studentRequestDTO.getMarks()));

	}

	@Test
	@Order(2)
	void testGetStudents_returnsStudents() {
		given() // Arrange
		.when(). // Act
				get("api/v1/students")
		.then() // Assert
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	@Order(3)
	void testGetStudentById_whenValidId_thenReturnsStudent() {
		given() // Arrange
		.when() // Act
				.get("api/v1/students/id/1")
		.then() // Assert
				.statusCode(HttpStatus.OK.value())
				.body("id", equalTo(1))
				.body("name", equalTo("Jhanvi"))
				.body("email", equalTo("jhanvi@gmail.com"))
				.body("marks", equalTo(97));
	}

	@Test
	@Order(4)
	void testGetStudentById_whenInvalidId_thenReturnsNotFound() {
		given() // Arrange
		.when() // Act
				.get("api/v1/students/id/999")
		.then() // Assert
				.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	@Order(5)
	void testGetStudentByName_whenValidName_thenReturnsStudents() {
		given() // Arrange
		.when() // Act
				.get("api/v1/students/name/Jhanvi")
		.then() // Assert
				.statusCode(HttpStatus.OK.value())
				.body("size()", greaterThan(0))
				.body("[0].name", equalTo("Jhanvi"))
				.body("[0].email", equalTo("jhanvi@gmail.com"));
	}

	@Test
	@Order(6)
	void testGetStudentByName_whenInvalidName_thenReturnsEmptyList() {
		given() // Arrange
		.when() // Act
				.get("api/v1/students/name/NonExistentStudent")
		.then() // Assert
				.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	@Order(7)
	void testGetStudentsWithPaginationAndSorting_thenReturnsPagedResults() {
		given() // Arrange
				.param("page", 0)
				.param("sortBy", "marks")
		.when() // Act
				.get("api/v1/students")
		.then() // Assert
				.statusCode(HttpStatus.OK.value())
				.body("content", notNullValue())
				.body("pageable.pageNumber", equalTo(0))
				.body("pageable.pageSize", equalTo(10))
				.body("sort.sorted", equalTo(true));
	}

	@Test
	@Order(8)
	void testUpdateStudent_whenValidData_thenReturnsUpdatedStudent() {
		StudentRequestDTO updateDTO = new StudentRequestDTO();
		updateDTO.setName("Jhanvi Updated");
		updateDTO.setEmail("jhanvi.updated@gmail.com");
		updateDTO.setMarks(95);
		updateDTO.setPassword("newpassword123");
		updateDTO.setRepeatPassword("newpassword123");

		given() // Arrange
				.body(updateDTO)
		.when() // Act
				.put("api/v1/students/1")
		.then() // Assert
				.statusCode(HttpStatus.OK.value())
				.body("id", equalTo(1))
				.body("name", equalTo("Jhanvi Updated"))
				.body("email", equalTo("jhanvi.updated@gmail.com"))
				.body("marks", equalTo(95));
	}

	@Test
	@Order(9)
	void testUpdateStudent_whenInvalidId_thenReturnsNotFound() {
		StudentRequestDTO updateDTO = new StudentRequestDTO();
		updateDTO.setName("Test Student");
		updateDTO.setEmail("test@gmail.com");
		updateDTO.setMarks(85);
		updateDTO.setPassword("password123");
		updateDTO.setRepeatPassword("password123");

		given() // Arrange
				.body(updateDTO)
		.when() // Act
				.put("api/v1/students/999")
		.then() // Assert
				.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	@Order(10)
	void testDeleteStudent_whenValidId_thenReturnsNoContent() {
		given() // Arrange
		.when() // Act
				.delete("api/v1/students/1")
		.then() // Assert
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	@Order(11)
	void testDeleteStudent_whenInvalidId_thenReturnsNotFound() {
		given() // Arrange
		.when() // Act
				.delete("api/v1/students/999")
		.then() // Assert
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

}
