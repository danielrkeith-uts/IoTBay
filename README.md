# IoTBay
41025 Introduction to Software Development
`https://github.com/danielrkeith-uts/IoTBay`

## Running project locally
1. Create new apache-tomcat server
2. Add deployment from the `iotbay/src/main/webapp` directory
   - Type is exploded
   - Don't edit optional deployment parameters
3. Start server
4. Navigate to the `iotbay` directory (i.e., the folder containing `pom.xml`)
5. Run `mvn package`
6. Navigate to the relevant url (likely `localhost:8080/webapp`)
