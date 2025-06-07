package cs211.project.services;

import cs211.project.models.TeamSchedule;
import cs211.project.models.collections.TeamScheduleCollection;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TeamScheduleListFileDatasource implements Datasource<TeamScheduleCollection> {
    private String directoryName;
    private String fileName;

    public TeamScheduleListFileDatasource(String directoryName, String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    // ตรวจสอบว่ามีไฟล์ให้อ่านหรือไม่ ถ้าไม่มีให้สร้างไฟล์เปล่า
    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
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
    public TeamScheduleCollection readData(){
        TeamScheduleCollection eventDetails = new TeamScheduleCollection();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการอ่านไฟล์
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
        TeamSchedule.nextTeamScheduleId = 1;
        try {
            // ใช้ while loop เพื่ออ่านข้อมูลรอบละบรรทัด
            while ( (line = buffer.readLine()) != null ){
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;

                // แยกสตริงด้วย ,
                String[] data = line.split(",");

                // อ่านข้อมูลตาม index แล้วจัดการประเภทของข้อมูลให้เหมาะสม
                String nameAct_teamSchedule = data[0].trim();
                String detail = data[1].trim();
                String status = data[2].trim();
                String eventID = data[3].trim();
                String TeamID = data[4].trim();
                String idRecord = data[5].trim();
                // เพิ่มข้อมูลลงใน list
                eventDetails.addNewTeamSchedule(idRecord,nameAct_teamSchedule,detail,status,eventID,TeamID);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return eventDetails;
    }

    @Override
    public void writeData(TeamScheduleCollection data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการเขียนไฟล์
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
            // สร้าง csv ของ Eventdetail และเขียนลงในไฟล์ทีละบรรทัด
            for (TeamSchedule teamSchedule : data.getTeamSchedules()) {
                String line = teamSchedule.getNameAct_teamSchedule() + "," + teamSchedule.getDetail() + ","  + teamSchedule.getStatus() + "," + teamSchedule.getEventId() + "," + teamSchedule.getTeamId() + "," + teamSchedule.getIdRecord();
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
