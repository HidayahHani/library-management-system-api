# library-management-system-api

## Getting Started

### Prerequisites

- ⁠Java 17 or higher
- ⁠Maven 3.6 or higher

### How to Run the Server Locally

1.⁠ ⁠*Clone the repository*

```⁠bash
 git clone https://github.com/HidayahHani/library-management-system-api.git
 cd library-management-system-api
```

⁠
2.⁠ ⁠*Build the project*

```⁠bash
 mvn clean install
```

3.⁠ ⁠*Run the application*

```⁠bash
 mvn spring-boot:run
```

⁠
4.⁠ ⁠*Verify the server is running*

The server will start on ⁠ http://localhost:9090 ⁠

You can verify by accessing the books endpoint:

```⁠bash
curl http://localhost:9090/books
```

⁠
5.⁠ ⁠*Access H2 Database Console (Optional)*

If H2 console is enabled, you can access it at:

⁠  http://localhost:9090/h2-console

You can test the endpoints using any REST client such as:

- Postman
- cURL

## API Endpoints

1. Register a book

   ### POST /book

   Register a new book in the system.
   Books with the same ISBN are allowed, but they must have the same title and author, and each will receive a unique ID.

   ## Request Body (JSON):

   ```json
   {
     "author": "John Green",
     "isbn": "978-0-2154-9807-4",
     "title": "The Fault In Our Stars"
   }
   ```

   ✅ Example successful response:

   ```json
   {
     "author": "John Green",
     "borrowed": false,
     "borrower": null,
     "id": 3,
     "isbn": "978-0-2154-9807-4",
     "title": "The fault in our stars"
   }
   ```

   ❌ Example failed response:

   ```json
   {
      "message": "Books with the same ISBN must have the same title and author"
   }
   ```

1. Get All Books:

   ### GET /books

   Return a list of all registered books in the system.

   #### This request does not have a body

   Example response:

   ```json
   [
     {
       "author": "John Green",
       "borrowed": true,
       "borrower": {
         "email": "emilycoop@gmail.com",
         "id": 1,
         "name": "Emily Cooper"
       },
       "id": 1,
       "isbn": "978-0-2154-9807-4",
       "title": "The fault in our stars"
     },
     {
       "author": "Emily Carter",
       "borrowed": false,
       "borrower": null,
       "id": 2,
       "isbn": "978-0-7410-0987-3",
       "title": "Beyond The Horizon"
     },
     {
       "author": "John Green",
       "borrowed": false,
       "borrower": null,
       "id": 3,
       "isbn": "978-0-2154-9807-4",
       "title": "The fault in our stars"
     }
   ]
   ```

1. Add a borrower

   ### POST /borrower

   Register a new borrower into the system. All fields are required.

   Request Body (JSON):

   ```json
   {
     "name": "Sam Smith",
     "email": "samsmith@gmail.com"
   }
   ```

   ✅ Example Successful Response:

   ```json
   {
     "email": "jennylopez@gmail.com",
     "id": 2,
     "name": "Jennifer Lopez"
   }
   ```

   ❌ Example failed response:

   ```json
   {
     "name": "Name is required",
     "email": "Email is required"
   }
   ```

1. Borrow a book

   ### POST /borrowBook?bookId={bookId}&borrowerId={borrowerId}

   #### example: borrowBook?bookId=3&borrowerId=1

   #### This request does not have a body

   Marks a book as borrowed by a borrower.
   A book cannot be borrowed if it is already borrowed.

   ✅Example successful response:

   ```json
   {
     "author": "Emily Carter",
     "borrowed": true,
     "borrower": {
       "email": "jennylopez@gmail.com",
       "id": 2,
       "name": "Jennifer Lopez"
     },
     "id": 2,
     "isbn": "978-0-7410-0987-3",
     "title": "Beyond The Horizon"
   }
   ```

   ❌ Example failed response:

   ```json
   {
     "message": "Book is already borrowed"
   }
   ```

1. Return a book

   ### POST /returnBook?bookId={bookId}

   #### example: /returnBook?bookId=1

   #### This request does not have a body

   Upon successful return, the borrowed status of the book will return to false

   ✅example successful response:

   ```json
   {
     "author": "Emily Carter",
     "borrowed": false,
     "borrower": null,
     "id": 2,
     "isbn": "978-0-7410-0987-3",
     "title": "Beyond The Horizon"
   }
   ```

   ❌ example failed to return:

   ```json
   {
     "message": "Book is not borrowed"
   }
   ```

## Assumptions

### 1. book and borrower records cannot be updated or deleted

The task did not mention any UPDATE or DELETE operations.
Therefore, only the following are supported.

1. Register a book
2. List of all books
3. Add a Borrower
4. Borrow a book
5. Return a book
   No edit or delete functionality is implemented

### 2. Books and Borrowers are added one at a time

The task did not specify batch insert operations.
Therefore:

- only one book can be added per request
- Only one borrower can be added per request

### 3. Borrower Listing endpoint is not required

The task only mentioned "List all books". It did not require "Listing all borrower".
So no /borrowers endpoint to view all the borrowers

### 4. Borrowers can borrow multiple books with no limit

There was no requirement stating a limit on how many books one borrower may borrow. Therefore, a borrower may borrow multipl books simultaneously.

### 5. Basic validation rules are assumed

The task did not define validation rules, so these were assumed:

- Book title, author, and isbn must not be empty
- Borrower name and email must not be empty
  Only minimal validation is enforced

### 6. The requirements did not mention a database engine.

H2 in-memory was chosen because:

- it requires no installation
- Lightweight
- Automatically reset on restart ensure every test run starts with a clean database
- Fully compatible with Spring Boot + JPA
