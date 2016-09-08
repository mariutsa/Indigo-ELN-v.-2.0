package com.epam.indigoeln.core.repository.search.entity;

import com.epam.indigoeln.core.repository.search.AggregationUtils;
import com.epam.indigoeln.web.rest.dto.search.request.SearchCriterion;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.from;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class ComponentSearchAggregationBuilder {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_THERAPEUTIC_AREA = "therapeuticArea";
    public static final String FIELD_CODE_AND_NAME = "projectCode";
    public static final String FIELD_YIELD = "batchYield";
    public static final String FIELD_PURITY = "purity";
    public static final String FIELD_COMPOUND_ID = "compoundId";
    public static final String FIELD_CHEMICAL_NAME = "chemicalName";
    public static final String FIELD_STRUCTURE = "structure";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_BATCHES = "batches";

    public static final String FIELD_STRUCTURE_STRUCTURE_ID = FIELD_STRUCTURE + ".structureId";

    public static final String FIELD_CONTENT_DESCRIPTION = FIELD_CONTENT + ".description";
    public static final String FIELD_CONTENT_TITLE = FIELD_CONTENT + ".title";
    public static final String FIELD_CONTENT_BATCHES = FIELD_CONTENT + ".batches";

    public static final String FIELD_BATCHES_CHEMICAL_NAME = FIELD_BATCHES + ".chemicalName";
    public static final String FIELD_BATCHES_COMPOUND_ID = FIELD_BATCHES + ".compoundId";
    public static final String FIELD_BATCHES_PURITY = FIELD_BATCHES + ".purity";
    public static final String FIELD_BATCHES_YIELD = FIELD_BATCHES + ".yield";
    public static final String FIELD_BATCHES_CODE_AND_NAME_NAME = FIELD_BATCHES + ".codeAndName.name";
    public static final String FIELD_BATCHES_THERAPEUTIC_AREA_NAME = FIELD_BATCHES + ".therapeuticArea.name";
    public static final String FIELD_BATCHES_STRUCTURE = FIELD_BATCHES + ".structure";

    public static final String COMPONENT_NAME_EXPERIMENT_DESCRIPTION = "experimentDescription";
    public static final String COMPONENT_NAME_PRODUCT_BATCH_SUMMARY = "productBatchSummary";
    public static final String COMPONENT_NAME_CONCEPT_DETAILS = "conceptDetails";
    public static final String CONDITION_CONTAINS = "contains";
    public static final String CONDITION_EQUALS = "=";
    public static final String CONDITION_IN = "in";
    public static List<String> BATCH_FIELDS = Arrays.asList(FIELD_THERAPEUTIC_AREA, FIELD_CODE_AND_NAME, FIELD_YIELD,
            FIELD_PURITY, FIELD_COMPOUND_ID, FIELD_CHEMICAL_NAME);
    protected List<Aggregation> aggregations;

    public ComponentSearchAggregationBuilder() {
        aggregations = new ArrayList<>();
    }

    public ComponentSearchAggregationBuilder withBingoIds(StructureSearchType type, List<Integer> bingoIds) {
        switch (type) {
            case Product:
                aggregations.add(getBatchesAggregationByBingoIds(bingoIds));
                break;
            case Reaction:
        }
        return this;
    }

    public ComponentSearchAggregationBuilder withQuerySearch(String querySearch) {
        aggregations.add(getBatchesAggregationByQuerySearch(querySearch));
        aggregations.add(getDescriptionAggregation(querySearch));
        aggregations.add(getConceptAggregation(querySearch));
        return this;
    }

    public ComponentSearchAggregationBuilder withAdvancedCriteria(List<SearchCriterion> criteria) {
        getBatchesAggregationByCriteria(criteria).ifPresent(aggregations::add);
        getDescriptionAggregation(criteria).ifPresent(aggregations::add);
        getConceptAggregation(criteria).ifPresent(aggregations::add);
        return this;
    }

    public Optional<Collection<Aggregation>> build() {
        return Optional.ofNullable(aggregations.isEmpty() ? null : aggregations);
    }

    private Aggregation getDescriptionAggregation(String querySearch) {
        return getDescriptionAggregation(new SearchCriterion(FIELD_DESCRIPTION, FIELD_DESCRIPTION, CONDITION_CONTAINS, querySearch));
    }

    private Optional<Aggregation> getDescriptionAggregation(List<SearchCriterion> criteria) {
        if (criteria.isEmpty()) {
            return Optional.empty();
        }
        return criteria.stream().filter(c -> FIELD_DESCRIPTION.equals(c.getField())).findAny().map(this::getDescriptionAggregation);
    }

    private Aggregation getBatchesAggregationByBingoIds(List<Integer> bingoIds) {
        Collection<SearchCriterion> searchCriteria = new ArrayList<>();
        searchCriteria.add(new SearchCriterion(FIELD_STRUCTURE_STRUCTURE_ID, FIELD_STRUCTURE_STRUCTURE_ID, CONDITION_IN, bingoIds));
        return getBatchesAggregation(searchCriteria, false);
    }

    private Aggregation getBatchesAggregationByQuerySearch(String querySearch) {
        Collection<SearchCriterion> searchCriteria = new ArrayList<>();
        Stream.of(FIELD_THERAPEUTIC_AREA, FIELD_CODE_AND_NAME, FIELD_COMPOUND_ID, FIELD_CHEMICAL_NAME).map(
                f -> new SearchCriterion(f, f, CONDITION_CONTAINS, querySearch)
        ).forEach(searchCriteria::add);
        Stream.of(FIELD_YIELD, FIELD_PURITY).map(
                f -> new SearchCriterion(f, f, CONDITION_EQUALS, querySearch)
        ).forEach(searchCriteria::add);
        return getBatchesAggregation(searchCriteria, false);
    }

    private Optional<Aggregation> getBatchesAggregationByCriteria(List<SearchCriterion> criteria) {
        if (criteria.isEmpty()) {
            return Optional.empty();
        }
        final List<SearchCriterion> batchCriteria = criteria.stream().filter(c -> BATCH_FIELDS.contains(c.getField())).collect(Collectors.toList());
        return Optional.ofNullable(batchCriteria.isEmpty() ? null : getBatchesAggregation(batchCriteria, true));

    }

    private Aggregation getConceptAggregation(String querySearch) {
        return getConceptAggregation(new SearchCriterion(FIELD_NAME, FIELD_NAME, CONDITION_CONTAINS, querySearch));
    }

    private Optional<Aggregation> getConceptAggregation(List<SearchCriterion> criteria) {
        if (criteria.isEmpty()) {
            return Optional.empty();
        }
        return criteria.stream().filter(c -> FIELD_NAME.equals(c.getField())).findAny().map(this::getConceptAggregation);
    }

    private Aggregation getDescriptionAggregation(SearchCriterion criterion) {
        List<AggregationOperation> result = new ArrayList<>();
        result.add(project(FIELD_NAME, FIELD_CONTENT));
        result.add(match(where(FIELD_NAME).is(COMPONENT_NAME_EXPERIMENT_DESCRIPTION)));
        result.add(project(from(field(FIELD_DESCRIPTION, FIELD_CONTENT_DESCRIPTION))));
        result.add(match(AggregationUtils.createCriterion(criterion)));
        return newAggregation(result);
    }

    private Aggregation getBatchesAggregation(Collection<SearchCriterion> criteria, boolean and) {
        List<AggregationOperation> result = new ArrayList<>();
        result.add(project(FIELD_NAME, FIELD_CONTENT));
        result.add(match(where(FIELD_NAME).is(COMPONENT_NAME_PRODUCT_BATCH_SUMMARY)));
        result.add(project(from(field(FIELD_BATCHES, FIELD_CONTENT_BATCHES))));
        result.add(unwind(FIELD_BATCHES));
        result.add(project(from(
                field(FIELD_THERAPEUTIC_AREA, FIELD_BATCHES_THERAPEUTIC_AREA_NAME),
                field(FIELD_CODE_AND_NAME, FIELD_BATCHES_CODE_AND_NAME_NAME),
                field(FIELD_YIELD, FIELD_BATCHES_YIELD),
                field(FIELD_STRUCTURE, FIELD_BATCHES_STRUCTURE),
                field(FIELD_PURITY, FIELD_BATCHES_PURITY),
                field(FIELD_COMPOUND_ID, FIELD_BATCHES_COMPOUND_ID),
                field(FIELD_CHEMICAL_NAME, FIELD_BATCHES_CHEMICAL_NAME)
        )));

        Criteria matchCriteria;
        if (and) {
            matchCriteria = new Criteria().andOperator(AggregationUtils.createCriteria(criteria).toArray(new Criteria[criteria.size()]));
        } else {
            matchCriteria = new Criteria().orOperator(AggregationUtils.createCriteria(criteria).toArray(new Criteria[criteria.size()]));
        }
        result.add(match(matchCriteria));

        return newAggregation(result);

    }

    private Aggregation getConceptAggregation(SearchCriterion criterion) {
        List<AggregationOperation> result = new ArrayList<>();
        result.add(project(FIELD_NAME, FIELD_CONTENT));
        result.add(match(where(FIELD_NAME).is(COMPONENT_NAME_CONCEPT_DETAILS)));
        result.add(project(from(field(FIELD_NAME, FIELD_CONTENT_TITLE))));

        result.add(match(AggregationUtils.createCriterion(criterion)));

        return newAggregation(result);

    }

}
