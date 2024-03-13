package librarymanagementsystem; 

//-------------------------importing different classes-----------------------------
import java.util.ArrayList;  //for using of arraylist   
import java.util.Scanner;    //to take input from the user
import java.io.*;            //used for file handling

//------------------------default class of Book is created which implements filehandling by using Serializable---------------
class Book implements Serializable {
    int book_ID;
    String title;
    String author;
    String genre;
    Boolean isAvailable;
    
    //------------------------------constructor for class Book----------------------------------------------
    
    public Book(int book_ID, String title, String author, String genre, boolean isAvailable) {
        this.book_ID = book_ID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = isAvailable;
    }
}
//--------------------creating a default class User which implements file handling by using Serializable keyword------------------
class User implements Serializable{
    int user_ID;
    String name;
    String contact;
    ArrayList<Book> borrowed_books = new ArrayList<Book>();
}

//-------------creating a default library class which implements file handling using Serializable keyword-------------------
class Library implements Serializable{
    ArrayList<Book> books = new ArrayList<Book>();
    ArrayList<User> users = new ArrayList<User>();

//-------------creating public fuction of adding book(using public so it is accesible throughout the program)---------------------------
    public void addBook(Book b) {
        books.add(b);
    }

//-------------creating public function of adding user(using public so it is accesible throughout the program) ------------------------
    public void addUser(User u) {
        users.add(u);
    }

//-------------creating public function of checking availability of books(using public so it is accesible throughout the program) ------------------------
    public void BookAvailability(Book b, User u) {
        if (books.contains(b) && b.isAvailable) {
            b.isAvailable = false;    //if the book is available for borrow it will let the user borrow the book and mark the book as unavailable 
            u.borrowed_books.add(b);  //add the book in the list of borrowed books 
            System.out.println("Book is available for borrowing");
        } else {
            System.out.println("Book is not available for checkout or borrow.");
        }
    }
//-------------creating public function of returning of book(using public so it is accesible throughout the program) ------------------------
    public void returnBook(Book b, User u) {
        if (u.borrowed_books.contains(b)) {
            b.isAvailable = true; //when the user will return the borrowed book the availability will become true
            u.borrowed_books.remove(b); //it will remove the book from borrowed books
            System.out.println("Book has been returned successfully.");
        } else {
            System.out.println("Book is not borrowed by this user.");
        }
    }
//------------public function of seraching the book by title(using public so it is accesible throughout the program) ------------------------
    public ArrayList<Book> searchBookByTitle(String t) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(t)) { //equalsIgnorCase will help to ignore the uppercase and lowercase format during search
                foundBooks.add(book);       //add the book in the foundbooks list
            }
        }
        return foundBooks;
    }
//------------public function of seraching the book by author(using public so it is accesible throughout the program) ------------------------
    public ArrayList<Book> searchBookByAuthor(String a){
        ArrayList<Book> foundBooks = new ArrayList<>();
        for(Book book: books){
            if (book.author.equalsIgnoreCase(a)) {     //equalsIgnorCase will help to ignore the uppercase and lowercase format during search
                foundBooks.add(book);                  //add the book in the foundbooks list
            }
        }
        return foundBooks;
    } 
  //---------------------file handling: this function will save data in our file for later use---------------
 public void saveData() {
        File file = new File("library_data.ser"); // Change the file name to "library_data.ser"
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(this);
            out.close();
            System.out.println("Library data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving library data: " + e.getMessage());
        }
    }

 //--------------------file handling: this function reloads data that was previously saved -----------------------
    public static Library loadData() {
        Library library = null;
        File file = new File("library_data.ser"); // Change the file name to "library_data.ser"
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            library = (Library) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading library data: " + e.getMessage());
        }
        return library != null ? library : new Library();
    }
}

//-----------------creating a public class ------------------------------------- 
public class LibraryManagementSystem {
    static Library library = new Library();   //using static keyword so we don't have to access it in other methods by passing it in parameters
    static Scanner scanner = new Scanner(System.in);

//---------------the following functions will prompt the user to imput the details-----------------------
//---------------error handling is done by using try and catch statements--------------------------------
    public static void addBook() {
        try{
        System.out.print("Enter Book ID: ");
        int book_ID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Book Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Book Genre: ");
        String genre = scanner.nextLine();
        Book book = new Book(book_ID, title, author, genre, true);
        library.addBook(book);
        System.out.println("Book added successfully!");
        }
        catch(Exception e){
            System.out.println("invalid input");
            scanner.nextLine();
        }
    }

    public static void addUser() {
        try{
        System.out.print("Enter User ID: ");
        int user_ID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter User Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter User Contact: ");
        String contact = scanner.nextLine();
        if(contact.length()<11 || contact.length()>12){
            System.out.println("invalid contact number.Enter a 11 digit number");
        }
        else{
        User user = new User();
        user.user_ID = user_ID;
        user.name = name;
        user.contact = contact;
        library.addUser(user);
        System.out.println("User added successfully!");
        }
        }
        catch(Exception e){
            System.out.println("Invalid input");
            scanner.nextLine();
            
        }
    }

    public static void displayBooks() {
        try{
        System.out.println("List of Books:");
        for (Book book : library.books) {
            System.out.println("ID: " + book.book_ID + ", Title: " + book.title + ", Author: " + book.author + ", Genre: " + book.genre + ", Availability: " + (book.isAvailable ? "Available" : "Not Available"));
        }
        }
        catch(Exception e){
        System.out.println("invalid!");
        }
        
    }

    public static void BookAvailability() {
        System.out.print("Enter Book ID to check availability: ");
        int book_ID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter User ID: ");
        int user_ID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Book book = null;
        User user = null;
        for (Book b : library.books) {
            if (b.book_ID == book_ID) {
                book = b;
                break;
            }
        }
        for (User u : library.users) {
            if (u.user_ID == user_ID) {
                user = u;
                break;
            }
        }
        if (book != null && user != null) {
            library.BookAvailability(book, user);
        } else {
            System.out.println("Invalid Book ID or User ID.");
        }
    }

    public static void returnBook() {
        System.out.print("Enter Book ID to return: ");
        int book_ID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter User ID: ");
        int user_ID = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Book book = null;
        User user = null;
        for (Book b : library.books) {
            if (b.book_ID == book_ID) {
                book = b;
                break;
            }
        }
        for (User u : library.users) {
            if (u.user_ID == user_ID) {
                user = u;
                break;
            }
        }
        if (book != null && user != null) {
            library.returnBook(book, user);
        } else {
            System.out.println("Invalid Book ID or User ID.");
        }
    }

    public static void searchBookByTitle() {
        try{
        System.out.print("Enter Title to search: ");
        String title = scanner.nextLine();
        ArrayList<Book> foundBooks = library.searchBookByTitle(title);
        if (foundBooks.isEmpty()) {
            System.out.println("No books found with the given title.");
        } else {
            System.out.println("Books found with the given title:");
            for (Book book : foundBooks) {
                System.out.println("ID: " + book.book_ID + ", Title: " + book.title + ", Author: " + book.author + ", Genre: " + book.genre + ", Availability: " + (book.isAvailable ? "Available" : "Not Available"));
            }
        }
        }
        catch(Exception e){
            System.out.println("Invalid ");
        }
        
    }

    public static void searchBookByAuthor() {
        try{
        System.out.print("Enter Author to search: ");
        String author = scanner.nextLine();
        ArrayList<Book> foundBooks = library.searchBookByAuthor(author);
        if (foundBooks.isEmpty()) {
            System.out.println("No books found with the given author.");
        } else {
            System.out.println("Books found with the given author:");
            for (Book book : foundBooks) {
                System.out.println("ID: " + book.book_ID + ", Title: " + book.title + ", Author: " + book.author + ", Genre: " + book.genre + ", Availability: " + (book.isAvailable ? "Available" : "Not Available"));
            }
        }
        }
        catch(Exception e){
        System.out.println("Invalid input");
        scanner.nextLine();
        }
    }
    

   
    public static void main(String[] args) {
         // Get the directory of the Java file
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Directory of the Java file: " + currentDirectory);
        
        // Get the directory of the program
        String programDirectory = System.getProperty("user.dir");
        System.out.println("Directory of the program: " + programDirectory);
       library = Library.loadData();
       System.out.println("Welcome to the Library Management System");
        while(true){
        
        System.out.println("You can choose from the following");
        System.out.println("1. Add Books");
        System.out.println("2. Add users ");
        System.out.println("3. Display book information");
        System.out.println("4. Borrow or checking out books");
        System.out.println("5. returning books");
        System.out.println("6. Searching for book by its title ");
        System.out.println("7. Searching for book by its author");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
          scanner.nextLine(); // for consuming a line 
           
//------------------giving user a choice to perform function of its own choice------------------------------
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    displayBooks();
                    break;
                case 4:
                    BookAvailability();
                    break;
                case 5:
                    returnBook();
                    break;
                case 6:
                    searchBookByTitle();
                    break;
                case 7:
                    searchBookByAuthor();
                    break;
                case 8:
                    System.out.println("Exiting. Thank you!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            library.saveData();
        }
    }
    
}
