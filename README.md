# Dropbox-Equivalent Service

## Project Overview

The Dropbox-Equivalent Service is a simplified file management system that mimics the functionality of Dropbox. Users can upload, retrieve, update, delete, and list files via a set of RESTful APIs. The system also maintains metadata associated with each file, including file name, creation timestamp, and size.

## Technologies Used

- **Backend Framework:** Spring Boot
- **Database:** MySQL
- **File Storage:** Local Storage
- **Build Tool:** Gradle
- **Docker:** For database management

## Local Setup

To run this project locally, follow these steps:

### 1. Clone the Repository

```bash
git clone https://github.com/ashutosh-aanand/Dropbox-backend.git
cd Dropbox-backend
```

### 2. Start the MySQL Database

1. Ensure Docker is running on your system.
2. Navigate to the `local-environment` directory.

   ```bash
   cd local-environment
   ```

3. Start the MySQL container using Docker Compose.

   ```bash
   docker-compose up -d
   ```

### 3. Configure the Application

Update the `src/main/resources/application.yml` file with your MySQL database credentials:

```properties
spring.datasource.url: jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username: your_username
spring.datasource.password: your_password
spring.jpa.hibernate.ddl-auto: update
```

### 4. Build and Run the Application

Navigate back to the root of the project directory:

```bash
cd ..
```

Build and run the application using Gradle:

```bash
./gradlew bootRun
```

The service should now be running at `http://localhost:8080`.

## Project Controllers Overview

The project includes the following controllers to handle file operations:

1. **FileController** - Manages file-related endpoints.
   - **Upload File:** `POST /files/upload`
      - **Description:** Allows users to upload files along with metadata.
      - **Input:** File binary data, file name, metadata (if any).
      - **Output:** A unique file identifier.

   - **Read File:** `GET /files/{fileId}`
      - **Description:** Retrieves a file based on its unique identifier.
      - **Input:** Unique file identifier.
      - **Output:** File binary data.

   - **Update File:** `PUT /files/{fileId}`
      - **Description:** Updates an existing file or its metadata.
      - **Input:** New file binary data or new metadata.
      - **Output:** Updated metadata or a success message.

   - **Delete File:** `DELETE /files/{fileId}`
      - **Description:** Deletes a file based on its unique identifier.
      - **Input:** Unique file identifier.
      - **Output:** A success or failure message.

   - **List Files:** `GET /files`
      - **Description:** Lists all available files and their metadata.
      - **Input:** None.
      - **Output:** A list of file metadata objects, including file IDs, names, createdAt timestamps, etc.
