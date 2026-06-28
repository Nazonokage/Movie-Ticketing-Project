# PrType_mvi (Movie Ticketing System)

A **2nd year (2023)** Java Swing desktop project 📽️🎟️ for registering movie tickets, storing ticket records in a **MySQL** database, viewing/editing/deleting records, and generating/printing a formatted ticket receipt.

---

## Flow chart (high-level) 🧭

```mermaid
graph TD
  A[Launch App] --> B[Login (admin/user demo)]
  B --> C[Ticket Registration]

  C -->|Show| D[Ticket Data Table]
  C -->|Register| E[Insert into MySQL (registrazione)]
  C -->|Register and Print| E --> F[Open Receipt Window]

  D -->|Delete| G[DELETE from MySQL by tkt_id]
  D -->|Edit| H[Edit Ticket Dialog]
  H -->|Save| I[UPDATE registrazione by tkt_id]
  H -->|Print| E2[Open Receipt Window]

  F --> J[Print Receipt (PrinterJob)]
  J --> K[Back to Registration]
```

---

## What this project is about

This application provides a simple “movie ticket” workflow:

1. **Login** (hardcoded demo accounts)
2. **Ticket Registration**
   - Enter customer name, movie title, price, seat number, and ticket priority.
   - Save the ticket to the database.
   - Optionally open a **printable ticket** window right after registration.
3. **Ticket Data Screen**
   - Shows all tickets from the database in a table.
   - Allows **Delete** and **Edit** on each ticket.
   - Shows **total income** summary.
4. **Edit Ticket**
   - Update fields and save changes back to the database.
   - Optionally **print** the updated ticket.
5. **Printing**
   - The ticket window calculates discount (based on priority) and VAT, then prints the panel with a receipt-like layout.

---

## UI Screens / Main Classes

### 1) Login + App entry
- **`src/App.java`**
  - Creates the login window.
  - Accepts two demo credentials:
    - `admin / admin`
    - `user / user`
  - After successful login, it opens `Tickteting_main`.

### 2) Ticket registration
- **`src/Tickteting_main.java`**
  - Main Swing frame containing:
    - Form fields: customer name, movie title, price, seat number, priority (Regular/VIP/Student/Senior/PWD/Child)
    - Buttons:
      - **Show** → opens ticket table (data screen)
      - **Register** → inserts record into DB
      - **Register and Print** → inserts record and opens a printable ticket (`printa`)
  - Database insert is done with a **PreparedStatement**.

### 3) Ticket table / data viewer
- **`src/DataDisplay.java`** (`JDialog`)
  - Loads all rows from MySQL table `registrazione` and shows them in a `JTable`.
  - Adds “Delete” and “Edit” buttons per row.
  - **Delete**:
    - Deletes record from `registrazione` by `tkt_id`
    - Removes the row from the table
  - **Edit**:
    - Opens `modifica` dialog with the selected record data.
  - Calculates totals (sum of ticket prices + total records).

### 4) Edit dialog
- **`src/modifica.java`** (`JDialog`)
  - Lets you change:
    - customer name, movie title, price, seat number, priority
  - **Save** updates the database record (`UPDATE registrazione ... WHERE tkt_id=?`).
  - **Print** opens a new ticket receipt window (`printa`) and prints it.

### 5) Receipt / ticket printing
- **`src/printa.java`** (`JFrame`)
  - Builds a ticket receipt UI (logo + formatted text panel) including:
    - seat number, movie, customer name
    - original price
    - discount based on priority
    - VAT (11%)
    - total (discounted price + VAT)
    - ticket ID and timestamp
  - **Printing** is implemented by printing the panel (`printPanel`) using `PrinterJob` and a custom `Printable`.
  - Discount rules implemented in code:
    - `VIP` → 30% discount (discountPercentage = 0.7)
    - `Student`, `Senior`, `PWD`, `Child` → 85% discount (discountPercentage = 0.15)
    - `Regular` → no discount

---

## Database (MySQL)

The schema is referenced from the SQL file in this project (`sql code 2 .txt`, “name the database as `tiketa`”).

### Database name
- `tiketa`

### Table: `registrazione`
From `sql code 2 .txt`:
- `tkt_id` (auto increment primary key)
- `C_nme` (customer name)
- `movie` (movie title)
- `prc` (price)
- `seat` (seat number)
- `prty` (priority)
- `RegDateTime` (DATETIME)

> Note: Code uses `C_nme`, `prc`, `prty` column names exactly.

---

## How to run

### 1) Requirements
- Java (Swing UI)
- MySQL Server running locally
- MySQL user with access to database `tiketa`

### 2) Setup MySQL
1. Create database:
   - `tiketa`
2. Run the provided SQL that creates `registrazione`.

### 3) Configure DB connection in code
The JDBC connection strings are hardcoded in the Java files.
- `DB_URL` used:
  - generally: `jdbc:mysql://localhost:3306/tiketa`
- user:
  - `root`
- password:
  - empty string `""`

Make sure your MySQL credentials match (or update the code).

### 4) Build & run
- Run `src/App.java` as the entry point.
- After login, use the registration form to insert tickets.

---

## Notes / Known quirks in the current code ⚠️
- `Tickteting_main.java` contains two different database URL usages (one references `busticketing`, another `tiketa`). In practice, your MySQL must match what each button is using.
- The UI uses a combination of `JFrame` and `JDialog` and some dialogs recreate/reload data.

---

## Suggested repository name 🚀

If you pick **`ticketa-ticketing-system`**, a good short description to place in GitHub is:

**“Movie ticketing desktop system (Java Swing + MySQL) that registers tickets, displays them in a table, supports edit/delete, and prints receipts with discounts and VAT.”**

Other name ideas:
- `movie-ticketing-system-swing`
- `java-movie-ticketing-mysql`
- `swing-ticketing-app`

---

## File map
- `src/App.java` – login window, opens ticketing UI
- `src/Tickteting_main.java` – ticket registration + DB insert + open data screen
- `src/DataDisplay.java` – shows tickets, delete/edit
- `src/modifica.java` – edit ticket and save/update DB, print option
- `src/printa.java` – ticket receipt UI + discount/VAT + print receipt
- `src/ConsolePrinter.java` – unrelated example printer code
- `src/PdfDocument.java` – placeholder interface

