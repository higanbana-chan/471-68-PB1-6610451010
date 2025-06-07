package cs211.project.services;

import cs211.project.models.EventSchedule;
import cs211.project.models.collections.EventScheduleList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EventsDetailListFileDatasource implements Datasource<EventScheduleList> {
    private String directoryName;
    private String fileName;

    public EventsDetailListFileDatasource(String directoryName, String fileName){
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
    public EventScheduleList readData(){
        EventScheduleList eventDetails = new EventScheduleList();
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
        try {
            // ใช้ while loop เพื่ออ่านข้อมูลรอบละบรรทัด
            while ( (line = buffer.readLine()) != null ){
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;

                // แยกสตริงด้วย ,
                String[] data = line.split(",");

                // อ่านข้อมูลตาม index แล้วจัดการประเภทของข้อมูลให้เหมาะสม
                String time = data[0].trim();
                String nameAct = data[1].trim();
                String detail = data[2].trim();
                String eventID = data[3].trim();
                // เพิ่มข้อมูลลงใน list
                eventDetails.addNewEventsDetail(time,nameAct,detail,eventID);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return eventDetails;
    }

    @Override
    public void writeData(EventScheduleList data) {
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
            for (EventSchedule eventSchedule : data.getEventDetails()) {
                String line = eventSchedule.getTime() + "," + eventSchedule.getNameAct() + "," + eventSchedule.getDetail() + "," + eventSchedule.getEventId();
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
