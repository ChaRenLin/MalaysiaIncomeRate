
# Malaysia Income API

This API allows users to create, update, and retrieve user information, as well as fetch external salary data.

## Prerequisites

- Java 17 or later
- Spring Boot
- Maven (or Gradle) for dependency management
- Postman for API testing

## Project Setup

### 1. Clone the repository

```bash
git clone <repository_url>
```

### 2. Build the project

If you're using **Maven**, run the following command to build the project:

```bash
mvn clean install
```

If you're using **Gradle**, run the following command:

```bash
./gradlew build
```

### 3. Run the Application

To run the application, use the following command:

```bash
mvn spring-boot:run
```

This will start the Spring Boot application, and it will be accessible at `http://localhost:8080`.

---

## Endpoints

### 1. **Create User**

- **Method**: `POST`
- **URL**: `/api/users`
- **Description**: Creates a new user with the provided information.
- **Request Body** (JSON):
  ```json
  {
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 30,
    "salary": 5000,
    "icNumber": "123456789012"
  }
  ```
- **Response** (JSON):
  ```json
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 30,
    "salary": 5000,
    "icNumber": "123456789012"
  }
  ```

### 2. **Update User by IC Number**

- **Method**: `PUT`
- **URL**: `/api/users/update/{icNumber}`
- **Description**: Updates the information of an existing user based on the IC Number.
- **Request Body** (JSON):
  ```json
  {
    "name": "Johnathan Doe",
    "email": "johnathan.doe@example.com",
    "age": 31,
    "salary": 5500
  }
  ```
- **Response** (JSON):
  ```json
  {
    "id": 1,
    "name": "Johnathan Doe",
    "email": "johnathan.doe@example.com",
    "age": 31,
    "salary": 5500,
    "icNumber": "123456789012"
  }
  ```

### 3. **Get Users (Paginated)**

- **Method**: `GET`
- **URL**: `/api/users?page={pageNumber}`
- **Description**: Retrieves a paginated list of users.
- **Query Parameter**: 
  - `page`: The page number (default: 0).
- **Response** (JSON):
  ```json
  {
    "content": [
      {
        "id": 1,
        "name": "Johnathan Doe",
        "email": "johnathan.doe@example.com",
        "age": 31,
        "salary": 5500,
        "icNumber": "123456789012"
      }
    ],
    "totalPages": 1,
    "totalElements": 1,
    "size": 20,
    "number": 0
  }
  ```

### 4. **Get External Salary Data**

- **Method**: `GET`
- **URL**: `/api/users/external`
- **Description**: Fetches external salary data from an external API.
- **Response** (String): The external salary data in a string format.

---

## Postman Collection

You can test the API using the Postman collection. Import the following collection to Postman to get started quickly.

1. Download the collection file: [Malaysia_Income_API_Collection.postman_collection](<provide_link_to_file>).
2. Open Postman and click **Import**.
3. Select **File** and upload the collection.

The collection includes the following requests:

- **Create User**: `POST /api/users`
- **Update User by IC Number**: `PUT /api/users/update/{icNumber}`
- **Get Users (Paginated)**: `GET /api/users?page={pageNumber}`
- **Get External Salary Data**: `GET /api/users/external`

---

## How to Test API Using Postman

1. **Import the Postman Collection**:
   - Click on **Import** in Postman and select the `Malaysia_Income_API_Collection.postman_collection` file.

2. **Testing the Create User Endpoint**:
   - Open the **Create User** request.
   - Set the method to `POST`.
   - Enter the user details in the request body.
   - Send the request and observe the response.

3. **Testing the Update User Endpoint**:
   - Open the **Update User by IC Number** request.
   - Set the method to `PUT`.
   - Replace `{icNumber}` with an existing IC number.
   - Enter the new details in the request body.
   - Send the request and observe the response.

4. **Testing the Get Users Endpoint**:
   - Open the **Get Users** request.
   - Set the method to `GET`.
   - Modify the `page` parameter to fetch different pages of users.
   - Send the request and observe the response.

5. **Testing the Get External Salary Data Endpoint**:
   - Open the **Get External Salary Data** request.
   - Set the method to `GET`.
   - Send the request and observe the response.

---

## Troubleshooting

- **Error**: "IC Number already exists"
  - **Cause**: The IC number already exists in the database.
  - **Solution**: Use a unique IC number for each user.

- **Error**: "User not found"
  - **Cause**: The IC number provided does not exist in the system.
  - **Solution**: Ensure the correct IC number is used.

- **Error**: "Failed to fetch external salary data"
  - **Cause**: There was an issue with the external API.
  - **Solution**: Check the external API service or ensure the endpoint is active.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
