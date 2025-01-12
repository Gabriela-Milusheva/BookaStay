<!-- PROJECT LOGO -->
<br />
 
<p align="center">
  <a href="https://github.com/Gabriela-Milusheva/BookaStay/blob/main/">
    <img src="images/bookastay_logo.png" alt="BookaStay Logo" width="30%" height="30%">
  </a>
</p>

<h1 align="center">BookaStay: Room Booking System</h1>

<p align="center">
  BookaStay is a web application designed for the reservation of hotel rooms and accommodations. It provides a user-friendly platform that allows users to effortlessly book their vacation stays.
</p>

<!-- ABOUT THE PROJECT -->
---

<h2 align="center">🌱 About The Project</h2>

- 👨🏻‍💼 Габриела Милушева, Фак.№ 471222058 (Users) <br />
- 🏨 Данаил Димитров, Фак.№ 471224118 (Hotels and Rooms) <br />
- 🗓️ Ина Михайлова, Фак.№ 471222010 (Reservations)


<!-- GETTING STARTED -->
---

<h2 align="center">🚀 Getting Started</h2>

### 🧩 Prerequisites

Before running this project, make sure you have the following tools and software installed and set up on your system:

1. **[Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html)** (version X.X or higher)
    To verify the version, run the following command:
   ```sh
   java -version
   
3. [Maven](https://maven.apache.org/install.html)
   ```sh
   mvn -version

4. [PostgreSQL](https://www.postgresql.org/download/) - the database used for this project
   ```sh
   psql --version
   ```

   You will need to set up a database and configure it in the application.properties file of the Spring Boot project. 

5. [Postman](https://www.postman.com/downloads/) (optional)- for testing the backend API endpoints

---

### 👣 Steps

To get a local copy up and running, follow these steps:

1. **Clone the repository**
   <br />
   <br />
   Clone the project to your local machine using Git:
   ```sh
   git clone https://github.com/Gabriela-Milusheva/BookaStay.git
   ```
   
3. **Start the back-end server**
   <br />
   <br />
   Open your terminal and go to the backend directory:
   ```sh
   cd BookaStay/bookastay
   ```
   Compile the project and download all dependencies:
    ```sh
    mvn clean install
    ```
   Ensure the project is functioning as expected by running the test suite:
   ```sh
    mvn test
    ```
   Start the server:
   ```sh
   mvn spring-boot:run
   ```
   
5. **Restart the server if needed**:
   <br />
   <br />
   If the server stops or you need to restart it, you can stop the running process and restart it:
   ```sh
   mvn spring-boot:run
   ```
   ---
   

