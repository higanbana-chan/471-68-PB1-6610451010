package cs211.project.services;

import cs211.project.models.Event;
import cs211.project.models.collections.EventCollection;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EventListFileDatasource implements Datasource<EventCollection>{
    private String directoryName;
    private String fileName;

    public EventListFileDatasource(String directoryName, String fileName){
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
    public EventCollection readData(){
        EventCollection events = new EventCollection();
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
                String id = data[0].trim();
                String name = data[1].trim();
                String poster = data[2].trim();
                String detail = data[3].trim();
                String start = data[4].trim();
                String end = data[5].trim();
                String type = data[6].trim();
                String location = data[7].trim();
                String organizer = data[8].trim();
                int currParticipants = Integer.parseInt(data[9].trim());
                int maxParticipants = Integer.parseInt(data[10].trim());
                // เพิ่มข้อมูลลงใน list
                events.addEvent(id, name, poster, detail, start, end, type, location, organizer, currParticipants, maxParticipants);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return events;
    }

    @Override
    public void writeData(EventCollection data) {
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
            for (Event event : data.getEvents()) {
                String line = event.getId() +
                        "," + event.getName() +
                        "," + event.getPoster() +
                        "," + event.getDetail() +
                        "," + event.getStart() +
                        "," + event.getEnd() +
                        "," + event.getType() +
                        "," + event.getLocation() +
                        "," + event.getOrganizer() +
                        "," + event.getCurrParticipants() +
                        "," + event.getMaxParticipants();
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

