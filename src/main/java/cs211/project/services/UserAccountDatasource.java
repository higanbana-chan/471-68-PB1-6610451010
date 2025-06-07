package cs211.project.services;

import cs211.project.models.UserAccount;
import cs211.project.models.collections.AccountCollection;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserAccountDatasource implements Datasource<AccountCollection> {
    private String directoryName;
    private String fileName;
    private String filePath;

    public UserAccountDatasource(String directoryName, String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        this.filePath = directoryName + File.separator + fileName;
        checkFileIsExisted();
    }

    // ตรวจสอบว่ามีไฟล์ให้อ่านหรือไม่ ถ้าไม่มีให้สร้างไฟล์เปล่า
    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public AccountCollection readData() {
        AccountCollection accounts = new AccountCollection();
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String firstName = data[0].trim();
                String lastName = data[1].trim();
                String username = data[2].trim();
                String password = data[3].trim();
                String email = data[4].trim();
                String type = data[5].trim();
                String date = data[6].trim();
                String logged = data[7].trim();
                String imgPath = data[8].trim();

                UserAccount account = new UserAccount(username, password, email, firstName, lastName, type, date, logged, imgPath);
                accounts.addAccount(account);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return accounts;
    }

    @Override
    public void writeData(AccountCollection data) {
        File file = new File(filePath);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);

        try {
            for (UserAccount account : data.getUserAccounts()) {
                String line = account.getFirstName() + "," +
                        account.getLastName() + "," +
                        account.getUsername() + "," +
                        account.getPassword() + "," +
                        account.getEmail() + "," +
                        account.getAccountType() + "," +
                        account.getRegisteredDate() + "," +
                        account.getLastLoggedIn() + "," +
                        account.getImgPath();
                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
