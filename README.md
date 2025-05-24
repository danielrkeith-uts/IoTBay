# IoTBay
41025 Introduction to Software Development
`https://github.com/danielrkeith-uts/IoTBay`

## Setting up local files
1. Create a `database.db` file inside the `database` directory
2. Run the following `SQL` scripts located in `database/queries` on the `database.db` file:
    1. `create_tables.sql`
    2. `insert_entries.sql`
3. Copy `local-files/DB.java.txt` into `iotbay/src/main/java/model/dao` and rename it to `DB.java`
4. Go to the `database.db` file and right-click on it to `Copy Path`
5. Paste the path inside the `DB.java` file, replacing the bit that says `<ABSOLUTE_DB_PATH>`
    - This will be an absolute path, i.e. specific to your device
    - If on windows, replace all double-backslashes with a forward slash.

## Running project locally
1. Create new apache-tomcat server
2. Add deployment from the `iotbay/target/iotbay` directory *(You may need to do step 4-5 first to create the folder, and then redo the steps once the server starts)*
   - Type is exploded
   - Don't edit optional deployment parameters
3. Start server
4. Navigate to the `iotbay` directory (i.e., the folder containing `pom.xml`)
5. Run `mvn clean compile package`
6. Right-click on the server and select `Publish Server (full)`
7. Navigate to the relevant url (likely `localhost:8080/iotbay`)

## Notes on pre-supplied data and navigating the web application
- The register page is navigated to using a link located on the login page.
- The staff password is `staff123`. This is used when registering as staff.
- The admin password is `admin123`. This is used when registering as system admin.
- Two users can be logged in by default. *(Please don't change the account details of these customers, as this can cause tests to fail)*
  - John Smith (Customer)
    - Email: `john.smith@gmail.com`
    - Password: `johnsPassword`
  - Gregory Stafferson (Staff)
    - Email: `gregory.stafferson@iotbay.com`
    - Password: `!@#$%^&*()`
