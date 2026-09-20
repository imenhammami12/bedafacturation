# Invoicing Desktop App

A JavaFX desktop application to create, manage and export invoices. It connects to a MySQL database and automatically calculates FODEC, VAT and the final amount to pay, following the Tunisian invoicing rules.

## Features

- Secure login screen (admin accounts stored in MySQL)
- Full invoice management (CRUD): add, list, edit and delete invoices
- Automatic calculations:
  - Total = unit price x quantity
  - FODEC = 1 % of the total
  - VAT = 19 % of (total + FODEC)
  - Total incl. tax = total + FODEC + VAT
  - Net to pay = total incl. tax + 1 DT (fiscal stamp)
- Professional PDF export of the selected invoice (header, client block, items table, totals, signature area) generated with Apache PDFBox

## Tech stack

| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| UI | JavaFX 17 (FXML + CSS) |
| Database | MySQL / MariaDB (JDBC, Connector/J 8.2.0) |
| PDF | Apache PDFBox 2.0.3 |
| IDE | IntelliJ IDEA / Eclipse |

## Project structure

```
src/
  application/   Main class (MainFacture)
  controllers/   JavaFX controllers (login, list, add, edit)
  crud/          Database access (SQL queries)
  DAO/           MySQL connection (singleton)
  entities/      Facture model
  pdf/           PDF invoice generator (FacturePdf)
  vue/           FXML views and images
  css/           Stylesheets
```

## Getting started

### Prerequisites

- JDK 17
- JavaFX SDK 17 (https://gluonhq.com/products/javafx)
- MySQL or XAMPP (MariaDB)
- Libraries (add them to the project classpath):
  - `mysql-connector-j-8.2.0.jar` (included in this repository)
  - `pdfbox-2.0.3.jar`, `fontbox-2.0.3.jar`, `commons-logging-1.2.jar`

### 1. Create the database

Start MySQL, then run this script (phpMyAdmin > SQL tab):

```sql
CREATE DATABASE facture CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE facture;

CREATE TABLE admin (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(50),
  login VARCHAR(50),
  mp VARCHAR(50)
);

CREATE TABLE facture (
  id INT AUTO_INCREMENT PRIMARY KEY,
  date DATE,
  codeclient INT,
  designation VARCHAR(255),
  quantite INT,
  pu FLOAT, pt FLOAT, total FLOAT, fodec FLOAT,
  tva FLOAT, totalttc FLOAT, netAPayer FLOAT
);

-- Demo account
INSERT INTO admin (nom, login, mp) VALUES ('Admin', 'admin', 'admin');
```

The connection settings are in `src/DAO/ConnectionSingleton.java` (default: `localhost:3306`, user `root`, empty password).

### 2. Configure the project

1. Open the folder in IntelliJ IDEA and create a module from the existing sources (`src` as source root).
2. Set the project SDK to JDK 17.
3. Add the JavaFX `lib` folder and the 4 jar files listed above as dependencies.

### 3. Run

Run the class `application.MainFacture` with these VM options (adapt the path):

```
--module-path "C:\javafx-sdk-17\lib" --add-modules javafx.controls,javafx.fxml
```

Log in with the demo account: `admin` / `admin`.

## PDF export

Select an invoice in the table and click the PDF button. The file `facture_<id>.pdf` is created in the working directory of the application and opens automatically.

To customize the company name, address and colors, edit the constants at the top of `src/pdf/FacturePdf.java`.

## Author

Imen Hammami - internship project, 2024.
