package com.inamata.ppmtool.services;

import com.inamata.ppmtool.domain.Backlog;
import com.inamata.ppmtool.domain.Project;
import com.inamata.ppmtool.domain.ProjectTask;
import com.inamata.ppmtool.exceptions.ProjectIdException;
import com.inamata.ppmtool.exceptions.ProjectNotFoundException;
import com.inamata.ppmtool.repositories.BacklogRepository;
import com.inamata.ppmtool.repositories.ProjectRepository;
import com.inamata.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask ){

        try{
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;

            backlog.setPTSequence(backlogSequence);

            projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);
            if(projectTask.getPriority()==null){ //In future check if 0
                projectTask.setPriority(3);
            }

            if(projectTask.getStatus()==""|| projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
        }catch(NullPointerException e){
            throw new ProjectNotFoundException("Project not Found!");
        }

    }

    public Iterable<ProjectTask>findBacklogByID(String id){

        Project project = projectRepository.findByProjectIdentifier(id);
        if(project==null){
            throw new ProjectNotFoundException("Project with ID '"+id+"' does not exist!");
        }else{
            return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
        }
    }

    public ProjectTask findPTbyProjectSequence(String backlog_id, String pt_id){
        //make sure we are searching on the right backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog==null){
            throw new ProjectNotFoundException("Project with ID:"+backlog_id+" does not exist");
        }
        // make sure task exist
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask==null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project '"+backlog_id+"'");
        }
        return projectTask;

    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id) {
        ProjectTask projectTask = findPTbyProjectSequence(backlog_id, pt_id);
        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTbyProjectSequence(backlog_id, pt_id);
        projectTaskRepository.delete(projectTask);
    }
}
