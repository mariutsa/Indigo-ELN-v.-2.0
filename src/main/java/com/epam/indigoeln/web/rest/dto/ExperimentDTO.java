package com.epam.indigoeln.web.rest.dto;

import com.epam.indigoeln.core.model.Experiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for Experiment
 */
public class ExperimentDTO implements Serializable {

    private static final long serialVersionUID = -305591958439648518L;

    private String id;
    private String title;
    private String project;
    private String experimentNumber;
    private String templateId;
    private List<ComponentDTO> components = new ArrayList<>();

    public ExperimentDTO() {
    }

    public ExperimentDTO(Experiment experiment) {
        this.id =experiment.getId();
        this.title =experiment.getTitle();
        this.project =experiment.getProject();
        this.experimentNumber =experiment.getExperimentNumber();
        this.templateId =experiment.getTemplateId();
        this.components = experiment.getComponents() != null ?
            experiment.getComponents().stream().map(ComponentDTO::new).collect(Collectors.toList()) : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getExperimentNumber() {
        return experimentNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getProject() {
        return project;
    }

    public List<ComponentDTO> getComponents() {
        return components;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setExperimentNumber(String experimentNumber) {
        this.experimentNumber = experimentNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setComponents(List<ComponentDTO> components) {
        this.components = components != null ? components : new ArrayList<>();
    }
}
