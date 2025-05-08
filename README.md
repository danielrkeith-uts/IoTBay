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
2. Navigate to the `iotbay` directory (i.e., the folder containing `pom.xml`)
3. Run `mvn clean compile package`
4. Add deployment from the `iotbay/target/iotbay` directory
   - Type is exploded
   - Don't edit optional deployment parameters
5. Start server
6. Navigate to the relevant url (likely `localhost:8080/iotbay`)
