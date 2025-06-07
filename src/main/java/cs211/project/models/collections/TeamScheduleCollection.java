package cs211.project.models.collections;

import cs211.project.models.EventSchedule;
import cs211.project.models.TeamSchedule;

import java.util.ArrayList;

public class TeamScheduleCollection {
    private ArrayList<TeamSchedule> teamSchedules;

    public TeamScheduleCollection(){ teamSchedules = new ArrayList<>(); }

    public void addNewTeamSchedule(String idRecord, String nameAct_teamSchedule, String detail, String status, String eventId, String teamId){
        idRecord = idRecord.trim();
        nameAct_teamSchedule = nameAct_teamSchedule.trim();
        status = status.trim();
        detail = detail.trim();
        eventId = eventId.trim();
        teamId = teamId.trim();
        teamSchedules.add(new TeamSchedule(idRecord, nameAct_teamSchedule, detail,status , eventId, teamId));
    }

/*
    public void addNewTeamSchedule(String idRecord, String nameAct_teamSchedule, String detail, String eventId, String teamId){
        idRecord = idRecord.trim();
        nameAct_teamSchedule = nameAct_teamSchedule.trim();
        detail = detail.trim();
        eventId = eventId.trim();
        teamId = teamId.trim();
        teamSchedules.add(new TeamSchedule(idRecord, nameAct_teamSchedule, detail, eventId, teamId));
    }

 */
    public TeamSchedule findTeamScheduleById(String idRecord) {
        for (TeamSchedule teamSchedule : teamSchedules) {
            if (teamSchedule.getIdRecord().equals(idRecord)) {
                return teamSchedule;
            }
        }
        return null;
    }


    public void setNameActTeamScheduleById(String idRecord,String nameAct_teamSchedule){
        TeamSchedule exist = findTeamScheduleById(idRecord);
        if (exist != null){
            exist.setNameAct_teamSchedule(nameAct_teamSchedule);
        }
    }

    public void setDetailById(String idRecord,String detail){
        TeamSchedule exist = findTeamScheduleById(idRecord);
        if (exist != null){
            exist.setDetail(detail);
        }
    }

    public void setStatusById(String idRecord,String status){
        TeamSchedule exist = findTeamScheduleById(idRecord);
        if (exist != null){
            exist.setStatus(status);
        }
    }

    public ArrayList<TeamSchedule> getTeamSchedules() {return teamSchedules;}
}
