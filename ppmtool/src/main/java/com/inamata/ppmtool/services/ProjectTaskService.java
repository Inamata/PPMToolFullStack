package com.inamata.ppmtool.services;

import com.inamata.ppmtool.domain.Backlog;
import com.inamata.ppmtool.domain.ProjectTask;
import com.inamata.ppmtool.repositories.BacklogRepository;
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

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask ){
        //EXCEPTIONS: Project not found


        //PTs to be added to specific project, project !=null (BL exist)
        //set backlog to projecttask (set relationship)
        //sequence like IDPRO-1 IDPRO-2
        //update the BL Sequence
        //INITIAL priority  low, medium, high ->groups task by priority
        //status if null update to something

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
    }

    public Iterable<ProjectTask>findBacklogByID(String id){
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

}
