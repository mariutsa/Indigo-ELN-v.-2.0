package com.epam.indigoeln.core.repository.experiment;

import com.epam.indigoeln.core.model.Experiment;
import com.epam.indigoeln.core.model.ExperimentShort;
import com.epam.indigoeln.core.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;

public interface ExperimentRepository extends MongoRepository<Experiment, String> {

    Collection<Experiment> findByAuthor(User author);

    Long countByTemplateId(String templateId);

    @Query(value="{ 'project' : ?0 }", fields="{ 'id' : 1, 'title' : 1, 'experimentNumber' : 1, " +
            "'templateId' : 1, 'project' : 1}")
    Collection<ExperimentShort> findExperimentsByProject(String project);

    @Query("{'fileIds': ?0}") //TODO try notebooks DBref $id
    Experiment findByFileId(String fileId);
}